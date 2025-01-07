package bth.postservice.entity;

import bth.common.dto.BookingStatus;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "posts")
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
@NamedEntityGraph(
        name = "Post.single",
        attributeNodes = {
                @NamedAttributeNode(value = "location", subgraph = "location-subgraph")
        },
        subgraphs = {
                @NamedSubgraph(name = "location-subgraph", attributeNodes = {
                        @NamedAttributeNode(value = "city", subgraph = "city-subgraph")
                }),
                @NamedSubgraph(name = "city-subgraph", attributeNodes = {
                        @NamedAttributeNode("state"),
                        @NamedAttributeNode("country")
                })
        }
)
public class Post implements HasStringId {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String title;
    private String imageUrl;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    @Embedded
    private PostDetails details;
    @Embedded
    private PostLocation location;
    @Column(nullable = false)
    private String userId;

    @Override
    public String getId() {
        return id != null ? id.toString() : null;
    }

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
        public static final int SRID = 4326;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "city_id", nullable = false)
        private City city;
        @Column(nullable = false, columnDefinition = "geometry(Point,4326)")
        private Point locationPoint;
        @Column(nullable = false)
        private String street;
        private String houseNumber;

        // TODO: check https://habr.com/ru/companies/domclick/articles/558876/
        public void setLocationPoint(double latitude, double longitude) {
            final var geometryFactory = new GeometryFactory(new PrecisionModel(), SRID);
            locationPoint = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        }
    }
}
