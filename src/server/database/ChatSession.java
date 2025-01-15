package server.database;

import server.logger.Logger;
import server.models.Employee;
import server.models.User;
import server.services.ChatManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class ChatSession {
    private static int sessionCounter = 0;
    private int sessionID;
    private Map<SocketData, User> allListeners;
    private User creator;
    private User receiver;

    public ChatSession(Employee creator, User receiver) {
        this.allListeners = new HashMap<SocketData, User>();
        this.creator = creator;
        this.receiver = receiver;
        sessionID = sessionCounter++;
        Logger.logChatStarted(creator, receiver);
    }

    public int getSessionID() {
        return this.sessionID;
    }

    public User getCreatorEmployee() {
        return this.creator;
    }

    public User getReceiverEmployee() {
        return this.receiver;
    }

    public User getEmployeeBySocketData(SocketData socketData) {
        return allListeners.get(socketData);
    }

    public SocketData getSocketDataByEmployee(Employee emp) {
        for (Map.Entry<SocketData, User> entry : allListeners.entrySet()) {
            if (emp.getId() == entry.getValue().getId())
                return entry.getKey();
        }

        return null;
    }

    public void addListener(SocketData socketData, User emp) {
        allListeners.put(socketData, emp);
    }

    public void removeListener(SocketData socketData) {
        allListeners.remove(socketData);

        ChatManager.getInstance().getChattingEmployees().remove(socketData);
        ChatManager.getInstance().validateChatSession(this);
    }

    public void broadcast(Employee emp, String message, PrintWriter sender) {
        synchronized (allListeners) {
            for (Map.Entry<SocketData, User> entry : allListeners.entrySet()) {
                SocketData socketData = entry.getKey();
                if (socketData.getOutputStream() != sender) {
                    socketData.getOutputStream().println("CHAT@@@receiveMessage###" + emp.serializeToString() + "&&&" + message + "&&&");
                }
            }
        }
    }

    public void broadcast(String message) {
//        synchronized (allListeners) {
//            for (Map.Entry<SocketData, User> entry : allListeners.entrySet()) {
//                SocketData socketData = entry.getKey();
//                Employee emp = new Employee(121212, "Bon", "Koli", "024254555",
//                        "pass", "holon", 23583454567L, Employee.Position.CASHIER);
//                final String msg = "CHAT@@@receiveMessage###" + emp.serializeToString() + "&&&" + message + "&&&";
//                socketData.getOutputStream().println(msg);
//            }
//        }
    }

    public void respondToClient(PrintWriter clientWriter, String response) {
        clientWriter.println(response);
    }

    public void listen(Employee emp, SocketData socketData) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String message;
                    while ((message = socketData.getInputStream().readLine()) != null && !message.isEmpty()) {
                        //String[] messageSplit = message.split(": ");
                        //appendMessage(messageSplit[0], messageSplit[1], false);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
