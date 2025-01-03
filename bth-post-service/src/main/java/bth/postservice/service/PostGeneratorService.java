package bth.postservice.service;

import bth.common.dto.BookingStatus;
import bth.postservice.entity.Post;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class PostGeneratorService {

    private static final Faker FAKER = new Faker(Locale.ENGLISH);

    private final LocationGeneratorService locationGeneratorService;

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public Post generate() {
        var post = new Post();

        post.setTitle(FAKER.book().title());
        post.setImageUrl(FAKER.internet().image());
        post.setStatus(FAKER.bool().bool() ? BookingStatus.AVAILABLE : BookingStatus.BOOKED);

        var details = new Post.PostDetails();
        details.setDescription(FAKER.weather().description());
        details.setAvailableFrom(LocalDate.now().plusDays(FAKER.number().numberBetween(1, 15)));
        details.setAvailableTo(LocalDate.now().plusDays(FAKER.number().numberBetween(16, 31)));
        details.setPrice(BigDecimal.valueOf(FAKER.number().randomDouble(2, 100, 10_000)));

        var randomCity = locationGeneratorService.generateCityLocationByCountry("Ukraine");
        var location = new Post.PostLocation();
        location.setCity(randomCity);
        location.setLocationPoint(
                randomCity.getLatitude().doubleValue(),
                randomCity.getLongitude().doubleValue());
        location.setStreet(FAKER.address().streetName());
        location.setHouseNumber(FAKER.address().buildingNumber());

        post.setLocation(location);
        post.setDetails(details);

        post.setUserId(FAKER.name().fullName());

        return post;
    }
}
