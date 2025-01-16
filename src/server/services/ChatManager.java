package server.services;

import server.ClientHandler;
import server.database.ChatSession;
import server.database.SocketData;
import server.models.Chat;
import server.models.Employee;
import server.models.Message;
import server.Server;
import server.models.User;

import java.util.*;

public class ChatManager {
    private static ChatManager instance;
    public static synchronized ChatManager getInstance() {
        if (instance == null) {
            instance = new ChatManager();
        }
        return instance;
    }

    private Map<SocketData, ChatSession> chattingEmployees;
    private Map<String, List<Employee>> waitingEmployees;
    private Map<SocketData, Employee> availableEmployees;
    private List<Chat> activeChats;

    private ChatManager() {
        this.availableEmployees = new HashMap<>();
        this.activeChats = new ArrayList<>();
        chattingEmployees = new HashMap<>();
        waitingEmployees = new HashMap<>();
    }



    public static Set<Integer> getAvailableBranches(int branch) {
        Set<Integer> branches = new HashSet<>();
        for (Map.Entry<Integer, SocketData> entry : ClientHandler.getConnections().entrySet()) {
            Employee emp = AdminManager.getInstance().findEmployeeById(entry.getKey());
            if (emp.getBranchID() != branch)
                branches.add(emp.getBranchID());
        }
        return branches;
    }


    // Add an available employee
    public void addAvailableEmployee(Employee employee) {
//        TODO put employee with socket
//        availableEmployees.add(employee);
        System.out.println(employee.getFirstName() + " is now available for chat.");
    }

    // Start a chat between two employees
    public void startChat(Employee employee1, Employee employee2) {
        if (!availableEmployees.containsKey(employee1) || !availableEmployees.containsKey(employee2)) {
            System.out.println("One or both employees are not available for chat.");
            return;
        }

        Chat chat = new Chat(employee1.getId(), employee2.getId());
        activeChats.add(chat);

        // Remove employees from available list
        availableEmployees.remove(employee1);
        availableEmployees.remove(employee2);

        System.out.println("Chat started between " + employee1.getFirstName() + " and " + employee2.getFirstName());
    }

    public Map<SocketData, Employee> getAvailableEmployees() {
        return availableEmployees;
    }

    // Employee sends a message in an active chat
    public void sendMessage(Employee employee, String messageContent) {
        for (Chat chat : activeChats) {
            if (chat.getEmployee1ID() == employee.getId() || chat.getEmployee2ID() == employee.getId()) {
                chat.addMessage(employee, messageContent);
                chat.displayChat();
                return;
            }
        }
        System.out.println("No active chat found for " + employee.getFirstName());
    }

    // When an employee is done with a chat, make them available again
    public void endChat(Employee employee) {
        for (Chat chat : activeChats) {
            if (chat.getEmployee1ID() == employee.getId()|| chat.getEmployee2ID() == employee.getId()) {
                activeChats.remove(chat);
//                availableEmployees.add(employee);
                System.out.println(employee.getFirstName() + " is now available for a new chat.");
                return;
            }
        }
        System.out.println("No active chat found for " + employee.getFirstName());
    }

    public void addMessage(Employee sender, Employee receiver, String content) {
        Message message = new Message(sender, content);
        System.out.println("Message added: " + message);
    }

    public Set<ChatSession> getAvailableChats(int branch) {
        Set<ChatSession> chats = new HashSet<>();
//        for (Map.Entry<SocketData, ChatSession> entry : chattingEmployees.entrySet()) {
//            //ChatSession chat = entry.getValue();
//            //Employee emp = chat.getCreatorEmployee();
//
//            User user = ClientHandler.getUserBySocketData(entry.getKey());
//            if (user instanceof Employee) {
//                Employee emp = (Employee) user;
//
//
//                if (emp.getBranchID() == branch){
//                    chats.add(entry.getValue());
//                }
//            }
//        }
        return chats;
    }

    public Map<SocketData, ChatSession> getChattingEmployees() {
        return chattingEmployees;
    }

    public ChatSession getChatSessionBySocketData(SocketData socketData) {
        return chattingEmployees.get(socketData);
    }

    public boolean isAvailableEmployeesForChat(int branch) {
        for (Map.Entry<SocketData, Employee> entry : availableEmployees.entrySet()) {
            Employee emp = entry.getValue();
            if (emp.getBranchID() == branch)
                return true;
        }

        return false;
    }

    public void addEmployeeToWaitingList(Employee emp, String branch) {
        boolean foundBranch = false;
        for (Map.Entry<String, List<Employee>> entry : waitingEmployees.entrySet()) {
            List<Employee> waitingForChat = entry.getValue();

            if (entry.getKey().equals(branch)) {
                List<Employee> employees = entry.getValue();
                employees.add(emp);
                waitingEmployees.put(branch, employees);
                foundBranch = true;
            }
        }

        if (!foundBranch) {
            List<Employee> employees = new ArrayList<Employee>();
            employees.add(emp);
            waitingEmployees.put(branch, employees);
        }
    }

    public void validateChatSession(ChatSession chat) {
        int chattingCount = 0;
        for (Map.Entry<SocketData, ChatSession> entry : chattingEmployees.entrySet()) {
            int tempSessionID = entry.getValue().getSessionID();
            int sessionID = chat.getSessionID();

            if (tempSessionID == sessionID)
                chattingCount++;
        }

        if (chattingCount < 2) { // Close chat because not enough employees for chat
            endChatSession(chat);
        }
    }

    public void endChatSession(ChatSession chat) {
        for (Map.Entry<SocketData, ChatSession> entry : chattingEmployees.entrySet()) {
            if (entry.getValue() == chat) {
                SocketData socketData = entry.getKey();
                chat.removeListener(socketData);
                chattingEmployees.remove(socketData);

                String command = "CHAT@@@abortCurrentChat###";
                socketData.getOutputStream().println(command);
            }
        }
    }

    public ChatSession getChatSessionByID(int sessionID) {
        for (Map.Entry<SocketData, ChatSession> entry : chattingEmployees.entrySet()) {
            ChatSession chat = entry.getValue();

            if (chat.getSessionID() == sessionID)
                return chat;
        }

        return null;
    }

    public SocketData getFirstAvailableEmployeeByBranch(int branch) {
        for (Map.Entry<SocketData, Employee> entry : availableEmployees.entrySet()) {
            Employee emp = entry.getValue();

            if (emp.getBranchID() == branch)
                return entry.getKey();

        }

        return null;
    }
}