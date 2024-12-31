package bth.postservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "subregions")
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "wikiDataId")
public class Subregion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "translations", columnDefinition = "TEXT")
    private String translations;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "flag", nullable = false)
    private Short flag = 1;

    @Column(name = "wiki_data_id", length = 255)
    private String wikiDataId;
}
