package bth.common.models.chat;

import lombok.Data;

@Data
public class Chat {
    private String id;
    private String recipient;
    private String lastMessage;
    private int unreadCount;
    private boolean hide = false;
}
