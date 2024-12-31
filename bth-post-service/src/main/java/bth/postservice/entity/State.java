package bth.postservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "states")
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "wikiDataId")
public class State {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @Column(name = "country_code", nullable = false, length = 2)
    private String countryCode;

    @Column(name = "fips_code", length = 255)
    private String fipsCode;

    @Column(name = "iso2", length = 255)
    private String iso2;

    @Column(length = 191)
    private String type;

    @Column(precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(precision = 11, scale = 8)
    private BigDecimal longitude;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private short flag;

    @Column(name = "wiki_data_id", length = 255)
    private String wikiDataId;
}
