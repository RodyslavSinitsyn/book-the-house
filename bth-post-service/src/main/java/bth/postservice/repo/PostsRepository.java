package bth.postservice.repo;

import bth.common.dto.filter.PostsFilterDto;
import bth.postservice.entity.Post;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostsRepository extends JpaRepository<Post, UUID>, JpaSpecificationExecutor<Post> {

    @Query("SELECT p FROM Post p ORDER BY ST_Distance(p.location.locationPoint, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)) ASC")
    List<Post> findAllPostsOrderedByDistance(@Param("longitude") double longitude,
                                             @Param("latitude") double latitude);

    default Page<Post> findFilteredPosts(PostsFilterDto filter, int page, int pageSize) {
        var specification = Specification
                .where(hasCountry(filter.getCountry()))
                .and(hasUser(filter.getUser()))
                .and(hasCity(filter.getCity()))
                .and(hasPriceInRange(filter.getPriceMin(), filter.getPriceMax()));
        return findAll(specification, PageRequest.of(page, pageSize));
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
