package bth.postservice.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "post_subscriptions")
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
public class PostSubscription implements HasStringId {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @Column
    private String userId;

    @Column(nullable = false, length = 255)
    private String email;

    @JoinColumn(nullable = false)
    private String subscribedUserId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private boolean enabled;

    @Override
    public String getId() {
        return id != null ? id.toString() : null;
    }
}

