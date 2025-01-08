package bth.postservice.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@EqualsAndHashCode(of = "username")
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NaturalId
    @Column(unique = true, nullable = false)
    private String username;
    private String friendlyName;
    private String email;
    @Column(nullable = false)
    private LocalDateTime createdAt;
}
