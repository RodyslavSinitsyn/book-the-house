package bth.common.message;

import java.io.Serializable;

public record PostCreatedMessage(
        String creatorId,
        String receiverEmail,
        String postTitle
) implements Serializable {
}
