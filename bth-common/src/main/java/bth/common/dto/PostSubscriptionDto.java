package bth.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PostSubscriptionDto {
    private String id;
    private String userId;
    private String email;
    private String subscribedUserId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean enabled;
}
