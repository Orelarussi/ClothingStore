package server.database;

import server.logger.Logger;
import server.models.Employee;
import server.models.Message;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Chat {
    private static int sessionCounter = 0;
    private int sessionID;
    private int creatorID;
    private int receiverID;
    private final List<Message> messages;

    public Chat(int creatorID, int receiverID) {
        this.creatorID = creatorID;
        this.receiverID = receiverID;
        this.messages = new ArrayList<>();

        sessionID = sessionCounter++;
        Logger.logChatStarted(creatorID, receiverID);
    }

    public int getSessionID() {
        return this.sessionID;
    }

    public int getCreatorID() {
        return this.creatorID;
    }

    public int getReceiverID() {
        return this.receiverID;
    }

    public void respondToClient(PrintWriter clientWriter, String response) {
        clientWriter.println(response);
    }

    public void listen(Employee emp, SocketData socketData) {
        new Thread(() -> {
            String message;
            try {
                while ((message = socketData.getInputStream().readLine()) != null && !message.isEmpty()) {
                    addMessage(emp, message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // Add a message to the chat
    public void addMessage(Employee sender, String messageContent) {
        Message message = new Message(sender, messageContent);
        messages.add(message);
    }

    // Display the chat
    public void displayChat() {
        System.out.println("Chat between " + creatorID + " and " + receiverID + ":");
        for (Message message : messages) {
            System.out.println(message);
        }
    }

    public List<Message> getMessages() {
        return messages;
    }
}
