package bth.postservice.service;

import bth.common.dto.BookingStatus;
import bth.postservice.entity.City;
import bth.postservice.entity.Post;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class PostGeneratorService {

    private static final Faker FAKER = new Faker(Locale.ENGLISH);

    private final LocationGeneratorService locationGeneratorService;

    private static final List<String> STATUS_WORDS = List.of("Cheap", "Standard", "Premium", "Luxury");
    private static final List<String> TYPE_WORDS = List.of("Hotel", "Motel", "Appartment", "House");
    private static final List<String> ADDITIONAL_WORDS = List.of(
            "Sea", "Ocean", "Mountain", "Lake", "City", "Forest", "Garden", "Beach");
    private static final List<String> DESCRIPTION_WORDS = List.of(
        "tv", "wifi", "microwave", "sauna", "conditioner", "balcony", "parking", "pool", "gym", "breakfast",
        "pets", "kitchen", "heating", "fireplace", "garden", "terrace", "elevator", "security", "spa", "bar"
    );

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public Post generate() {
        var post = new Post();

        post.setTitle(getRandomTitle());
        post.setImageUrl(FAKER.internet().image());
        post.setStatus(FAKER.bool().bool() ? BookingStatus.AVAILABLE : BookingStatus.BOOKED);

        var details = new Post.PostDetails();
        details.setDescription(getRandomDescription());
        details.setAvailableFrom(LocalDate.now().plusDays(FAKER.number().numberBetween(1, 15)));
        details.setAvailableTo(LocalDate.now().plusDays(FAKER.number().numberBetween(16, 31)));
        details.setPrice(BigDecimal.valueOf(FAKER.number().randomDouble(2, 100, 10_000)));

        var randomCity = getRandomCity();
        var location = new Post.PostLocation();
        location.setCity(randomCity);
        location.setLocationPoint(
                randomCity.getLatitude().doubleValue(),
                randomCity.getLongitude().doubleValue());
        location.setStreet(FAKER.address().streetName());
        location.setHouseNumber(FAKER.address().buildingNumber());

        post.setLocation(location);
        post.setDetails(details);

        return post;
    }

    private City getRandomCity() {
        var countries = List.of("Ukraine", "United States", "France", "Spain");
        var randomIdx = FAKER.number().numberBetween(0, countries.size());
        String countryName = null;
        if (randomIdx < countries.size()) {
            countryName = countries.get(randomIdx);
        }
        return locationGeneratorService.generateCityLocationByCountry(countryName);
    }

    private String getRandomTitle() {
        String status = STATUS_WORDS.get(FAKER.number().numberBetween(0, STATUS_WORDS.size()));
        String type = TYPE_WORDS.get(FAKER.number().numberBetween(0, TYPE_WORDS.size()));
        String name = FAKER.company().name(); // Using company name for variety
        String additional = ADDITIONAL_WORDS.get(FAKER.number().numberBetween(0, ADDITIONAL_WORDS.size()));
        return String.format("%s %s %s %s", status, type, name, additional);
    }

    private String getRandomDescription() {
        int descSize = FAKER.number().numberBetween(5, 8);
        List<String> shuffled = new ArrayList<>(DESCRIPTION_WORDS);
        Collections.shuffle(shuffled);
        return String.join(", ", shuffled.subList(0, descSize));
    }
}
