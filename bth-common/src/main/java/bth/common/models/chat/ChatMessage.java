package bth.common.models.chat;

import lombok.Data;

import java.util.Date;

@Data
public class ChatMessage {
    private String chatId;
    private String messageId;
    private String senderId;
    private String recipientId;
    private String senderName;
    private String recipientName;
    private String text;
    private Date timestamp;
    private boolean read = false;
}
