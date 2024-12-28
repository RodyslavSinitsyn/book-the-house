package bth.postservice.entity;

import bth.models.dto.BookingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "posts")
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(nullable = false)
    private String title;
    private String imageUrl;
    @Column(nullable = false)
    private BookingStatus status;
    @Embedded
    private PostDetails details;
    @Embedded
    private PostLocation location;

    @Embeddable
    @Data
    public static class PostDetails {
        private String description;
        private LocalDate availableFrom;
        private LocalDate availableTo;
        @Column(nullable = false)
        private BigDecimal price;
    }

    @Embeddable
    @Data
    public static class PostLocation {
        @Column(nullable = false)
        private String country;
        @Column(nullable = false)
        private String city;
        @Column(nullable = false)
        private String street;
        private String houseNumber;
    }
}
