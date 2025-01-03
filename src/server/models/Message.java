package server.models;

public class Message {
    private Employee sender;
    private String content;

    public Message(Employee sender, String content) {
        this.sender = sender;
        this.content = content;
    }

    public Employee getSender() {
        return sender;
    }

    public void setSender(Employee sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return sender.getFirstName() + ": " + content;
    }
}
