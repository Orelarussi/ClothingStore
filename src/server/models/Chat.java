package server.models;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    private final int employee1ID;
    private final int employee2ID;
    private final List<Message> messages;

    public Chat(int employee1ID, int employee2ID) {
        this.employee1ID = employee1ID;
        this.employee2ID = employee2ID;
        this.messages = new ArrayList<>();
    }

    // Add a message to the chat
    public void addMessage(Employee sender, String messageContent) {
        Message message = new Message(sender, messageContent);
        messages.add(message);
    }

    // Display the chat
    public void displayChat() {
        System.out.println("Chat between " + employee1ID + " and " + employee2ID + ":");
        for (Message message : messages) {
            System.out.println(message);
        }
    }

    // Getters
    public int getEmployee1ID() {
        return employee1ID;
    }

    public int getEmployee2ID() {
        return employee2ID;
    }

    public List<Message> getMessages() {
        return messages;
    }
}
