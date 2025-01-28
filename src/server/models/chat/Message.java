package server.models.chat;

import com.google.gson.JsonObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {
    private final int senderId; // ID of the sender
    private String senderName = "";
    private final String content; // Message content
    private final LocalDateTime timestamp; // Time the message was sent

    public Message(int senderId, String content, LocalDateTime timestamp, String senderName) {
        this.senderId = senderId;
        this.content = content;
        this.timestamp = timestamp;
        this.senderName= senderName;
    }
    // Getters
    public String getSenderName() {
        return senderName;
    }

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

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("employeeId", senderId);
        jsonObject.addProperty("senderName", senderName);
        jsonObject.addProperty("content", content);
        jsonObject.addProperty("timestamp", timestamp.toString());
        return jsonObject;
    }
}
