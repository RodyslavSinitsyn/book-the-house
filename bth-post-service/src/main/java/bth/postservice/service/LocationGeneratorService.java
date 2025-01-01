package bth.postservice.service;

import bth.postservice.entity.City;
import bth.postservice.entity.Country;
import bth.postservice.entity.Region;
import bth.postservice.entity.Subregion;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationGeneratorService {

    private final EntityManager entityManager;

    public City generateCityLocationByRegion(String name) {
        Region region;
        if (name != null) {
            region = entityManager.createQuery("FROM Region where name = :name", Region.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } else {
            region = entityManager.createQuery("FROM Region where id = :id", Region.class)
                    .setParameter("id", getRandomNumber(1, 6))
                    .getSingleResult();
        }
        return entityManager.createQuery("FROM City c where c.country.regionEntity.id = :id order by random()", City.class)
                .setParameter("id", region.getId())
                .setMaxResults(1)
                .getSingleResult();
    }

    public City generateCityLocationBySubregion(String name) {
        Subregion subregion;
        if (name != null) {
            subregion = entityManager.createQuery("FROM Subregion where name = :name", Subregion.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } else {
            subregion = entityManager.createQuery("FROM Subregion where id = :id", Subregion.class)
                    .setParameter("id", getRandomNumber(1, 22))
                    .setMaxResults(1)
                    .getSingleResult();
        }
        return entityManager.createQuery("FROM City c where c.country.subregionEntity.id = :id  order by random()", City.class)
                .setParameter("id", subregion.getId())
                .getSingleResult();
    }

    public City generateCityLocationByCountry(String name) {
        Country country;
        if (name != null) {
            country = entityManager.createQuery("FROM Country where name = :name", Country.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } else {
            country = entityManager.createQuery("FROM Country where id = :id", Country.class)
                    .setParameter("id", getRandomNumber(1, 250))
                    .getSingleResult();
        }
        return entityManager.createQuery("FROM City c where c.country.id = :id order by random()", City.class)
                .setParameter("id", country.getId())
                .setMaxResults(1)
                .getSingleResult();
    }

    private long getRandomNumber(int min, int max) {
        return (long) (Math.random() * (max - min) + min);
    }
}
