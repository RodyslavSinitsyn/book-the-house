package bth.postservice.repo.jpa;

import bth.common.dto.filter.PostsFilterDto;
import bth.postservice.entity.Post;
import bth.postservice.projection.PostDistanceProjection;
import jakarta.persistence.criteria.JoinType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostsRepository extends JpaRepository<Post, UUID>, JpaSpecificationExecutor<Post> {

    @EntityGraph(value = "Post.single", type = EntityGraph.EntityGraphType.FETCH)
    Optional<Post> findById(@Param("id") UUID id);

    @Query("""
            SELECT p.id as id,
                   p.user.username as username,
                   p.user.friendlyName as friendlyName,
                   p.title as title,
                   p.details.description as description,
                   p.imageUrl as imageUrl,
                   p.details.price as price,
                   p.location.city.name as city,
                   p.location.city.country.name as country,
                   p.location.city.state.name as state,
                   p.status as status,
                   p.details.availableFrom as fromDate,
                   p.details.availableTo as toDate
            FROM Post p
            JOIN p.location l
            JOIN l.city c
            JOIN c.country
            JOIN c.state
            ORDER BY ST_Distance(l.locationPoint, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)) ASC
            """)
    List<PostDistanceProjection> findAllPostsOrderedByDistance(@Param("longitude") double longitude,
                                                               @Param("latitude") double latitude);

    default Page<Post> findFilteredPosts(PostsFilterDto filter,
                                         Collection<UUID> ids,
                                         int page,
                                         int pageSize) {
        var specification = Specification
                .where(hasIds(ids))
                .and(hasCountry(filter.getCountry()))
                .and(hasUser(filter.getUser()))
                .and(hasCity(filter.getCity()))
                .and(hasPriceInRange(filter.getPriceMin(), filter.getPriceMax()))
                .and((root, query, criteriaBuilder) -> {
                    var cityFetch = root.fetch("location", JoinType.INNER)
                            .fetch("city", JoinType.INNER);
                    cityFetch.fetch("country", JoinType.INNER);
                    cityFetch.fetch("state", JoinType.INNER);
                    query.distinct(true);
                    return null;
                });
        return findAll(specification, PageRequest.of(page, pageSize));
    }

    static Specification<Post> hasIds(Collection<UUID> ids) {
        return (root, query, criteriaBuilder) ->
                CollectionUtils.isEmpty(ids) ? null : root.get("id").in(ids);
    }

    static Specification<Post> hasUser(String userId) {
        return (root, query, criteriaBuilder) ->
                StringUtils.isEmpty(userId) ? null : criteriaBuilder.equal(root.get("userId"), userId);
    }

    static Specification<Post> hasCountry(String country) {
        return (root, query, criteriaBuilder) ->
                StringUtils.isEmpty(country) ? null : criteriaBuilder.equal(root.get("location").get("city").get("country").get("name"), country);
    }

    static Specification<Post> hasCity(String city) {
        return (root, query, criteriaBuilder) ->
                StringUtils.isEmpty(city) ? null : criteriaBuilder.equal(root.get("location").get("city").get("name"), city);
    }

    static Specification<Post> hasPriceInRange(Integer minPrice, Integer maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (minPrice == null && maxPrice == null) {
                return null;
            } else if (minPrice != null && maxPrice != null) {
                return criteriaBuilder.between(root.get("details").get("price"), minPrice, maxPrice);
            } else if (minPrice != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("details").get("price"), minPrice);
            } else {
                return criteriaBuilder.lessThanOrEqualTo(root.get("details").get("price"), maxPrice);
            }
        };
    }

}
