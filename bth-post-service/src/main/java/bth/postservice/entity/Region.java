package bth.postservice.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "regions")
@Getter
@Setter
@EqualsAndHashCode(of = "wikiDataId")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "translations", columnDefinition = "TEXT")
    private String translations;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "flag", nullable = false)
    private Short flag = 1;

    @Column(name = "wiki_data_id", length = 255)
    private String wikiDataId;
}
