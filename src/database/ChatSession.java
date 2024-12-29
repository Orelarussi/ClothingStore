package database;

import logger.Logger;
import models.Employee;
import server.Server;
import services.ChatManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class ChatSession {
    private static int sessionCounter = 0;
    private int sessionID;
    private Map<SocketData, Employee> allListeners;
    private Employee creator;
    private Employee receiver;

    public ChatSession(Employee creator, Employee receiver) {
        this.allListeners = new HashMap<SocketData, Employee>();
        this.creator = creator;
        this.receiver = receiver;
        sessionID = sessionCounter++;
        Logger.logChatStarted(creator, receiver);
    }

    public int getSessionID() {
        return this.sessionID;
    }

    public Employee getCreatorEmployee() {
        return this.creator;
    }

    public Employee getReceiverEmployee() {
        return this.receiver;
    }

    public Employee getEmployeeBySocketData(SocketData socketData) {
        return allListeners.get(socketData);
    }

    public SocketData getSocketDataByEmployee(Employee emp) {
        for (Map.Entry<SocketData, Employee> entry : allListeners.entrySet()) {
            if (emp.getId() == entry.getValue().getId())
                return entry.getKey();
        }

        return null;
    }

    public void addListener(SocketData socketData, Employee emp) {
        allListeners.put(socketData, emp);
    }

    public void removeListener(SocketData socketData) {
        allListeners.remove(socketData);

        ChatManager.getInstance().getChattingEmployees().remove(socketData);
        ChatManager.getInstance().validateChatSession(this);
    }

    public void broadcast(Employee emp, String message, PrintWriter sender) {
        synchronized (allListeners) {
            for (Map.Entry<SocketData, Employee> entry : allListeners.entrySet()) {
                SocketData socketData = entry.getKey();
                if (socketData.getOutputStream() != sender) {
                    socketData.getOutputStream().println("CHAT@@@receiveMessage###" + emp.serializeToString() + "&&&" + message + "&&&");
                }
            }
        }
    }

    public void broadcast(String message) {
        synchronized (allListeners) {
            for (Map.Entry<SocketData, Employee> entry : allListeners.entrySet()) {
                SocketData socketData = entry.getKey();
                Employee emp = new Employee(121212, "Bon", "Koli", "024254555",
                        "pass", "holon", 23583454567l,
                        69846464l, Employee.Position.CASHIER);
                final String msg = "CHAT@@@receiveMessage###" + emp.serializeToString() + "&&&" + message + "&&&";
                socketData.getOutputStream().println(msg);
            }
        }
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
                    while ((message = socketData.getInputStream().readLine()) != null && !message.equals("")) {
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
