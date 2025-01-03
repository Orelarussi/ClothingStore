package server.models;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    private Employee employee1;
    private Employee employee2;
    private List<Message> messages;

    public Chat(Employee employee1, Employee employee2) {
        this.employee1 = employee1;
        this.employee2 = employee2;
        this.messages = new ArrayList<>();
    }

    // Add a message to the chat
    public void addMessage(Employee sender, String messageContent) {
        Message message = new Message(sender, messageContent);
        messages.add(message);
    }

    // Display the chat
    public void displayChat() {
        System.out.println("Chat between " + employee1.getFirstName() + " and " + employee2.getFirstName() + ":");
        for (Message message : messages) {
            System.out.println(message);
        }
    }

    // Getters
    public Employee getEmployee1() {
        return employee1;
    }

    public Employee getEmployee2() {
        return employee2;
    }

    public List<Message> getMessages() {
        return messages;
    }
}
