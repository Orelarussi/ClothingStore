package server.models.chat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {
    private final int senderId; // ID of the sender
    private String senderName = "";
    private final String content; // Message content
    private final LocalDateTime timestamp; // Time the message was sent

    public Message(int senderId, String content,LocalDateTime timestamp ) {
        this.senderId = senderId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
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
// Formatter to display only hours and minutes
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String formattedTime = timestamp.format(formatter);

        // Return a nicely formatted string
        return "[" + formattedTime + "] " + senderName + ": " + content;    }
}
