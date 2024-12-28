package bth.postservice.service;

import bth.models.dto.BookingStatus;
import bth.postservice.entity.Post;
import com.github.javafaker.Faker;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Locale;

@Service
public class PostGeneratorService {

    public static final SecureRandom RANDOM = new SecureRandom();
    private static final Faker FAKER = new Faker(Locale.ENGLISH, RANDOM);

    public Post generate() {
        var post = new Post();

        post.setTitle(FAKER.lorem().sentence(4));
        post.setImageUrl(FAKER.internet().image());
        post.setStatus(RANDOM.nextBoolean() ? BookingStatus.AVAILABLE : BookingStatus.BOOKED);

        var details = new Post.PostDetails();
        details.setDescription(FAKER.lorem().sentence());
        details.setAvailableFrom(LocalDate.now().plusDays(FAKER.number().numberBetween(1, 15)));
        details.setAvailableTo(LocalDate.now().plusDays(FAKER.number().numberBetween(16, 31)));
        details.setPrice(BigDecimal.valueOf(FAKER.number().randomDouble(2, 100, 10_000)));

        var location = new Post.PostLocation();
        location.setCountry(FAKER.country().name());
        location.setCity(FAKER.address().city());
        location.setStreet(FAKER.address().streetName());
        location.setHouseNumber(FAKER.address().buildingNumber());

        post.setLocation(location);
        post.setDetails(details);

        return post;
    }
}
