package server.models.chat;

import java.time.LocalDateTime;

public class Message {
    private final int senderId; // ID of the sender
    private final String content; // Message content
    private final LocalDateTime timestamp; // Time the message was sent

    public Message(int senderId, String content,LocalDateTime timestamp ) {
        this.senderId = senderId;
        this.content = content;
        this.timestamp = timestamp;
    }

    // Getters
    public int getSenderId() {
        return senderId;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "From: " +senderId+", at"+timestamp+":\n"
               +content;
    }
}
