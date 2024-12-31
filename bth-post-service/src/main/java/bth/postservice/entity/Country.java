package bth.postservice.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "countries")
@Getter
@Setter
@EqualsAndHashCode(of = "wikiDataId")
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "iso3", length = 3)
    private String iso3;

    @Column(name = "numeric_code", length = 3)
    private String numericCode;

    @Column(name = "iso2", length = 2)
    private String iso2;

    @Column(name = "phonecode")
    private String phoneCode;

    @Column(name = "capital")
    private String capital;

    @Column(name = "currency")
    private String currency;

    @Column(name = "currency_name")
    private String currencyName;

    @Column(name = "currency_symbol")
    private String currencySymbol;

    @Column(name = "tld")
    private String tld;

    @Column(name = "native")
    private String nativeName;

    @Column(name = "region")
    private String region;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "region_id")
    private Region regionEntity;

    @Column(name = "subregion")
    private String subregion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subregion_id")
    private Subregion subregionEntity;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "timezones", columnDefinition = "TEXT")
    private String timezones;

    @Column(name = "translations", columnDefinition = "TEXT")
    private String translations;

    @Column(name = "latitude", precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 11, scale = 8)
    private BigDecimal longitude;

    @Column(name = "emoji", length = 191)
    private String emoji;

    @Transient
    @Column(name = "emojiU", length = 191)
    private String emojiU;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "flag", nullable = false)
    private Short flag = 1;

    @Column(name = "wiki_data_id")
    private String wikiDataId;
}
