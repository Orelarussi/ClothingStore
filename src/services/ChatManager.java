package services;

import models.Chat;
import models.Employee;
import models.Message;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ChatManager {
    private Queue<Employee> availableEmployees;
    private List<Chat> activeChats;

    public ChatManager() {
        this.availableEmployees = new LinkedList<>();
        this.activeChats = new ArrayList<>();
    }

    // Add an available employee
    public void addAvailableEmployee(Employee employee) {
        availableEmployees.add(employee);
        System.out.println(employee.getFirstName() + " is now available for chat.");
    }

    // Start a chat between two employees
    public void startChat(Employee employee1, Employee employee2) {
        if (!availableEmployees.contains(employee1) || !availableEmployees.contains(employee2)) {
            System.out.println("One or both employees are not available for chat.");
            return;
        }

        Chat chat = new Chat(employee1, employee2);
        activeChats.add(chat);

        // Remove employees from available list
        availableEmployees.remove(employee1);
        availableEmployees.remove(employee2);

        System.out.println("Chat started between " + employee1.getFirstName() + " and " + employee2.getFirstName());
    }

    // Employee sends a message in an active chat
    public void sendMessage(Employee employee, String messageContent) {
        for (Chat chat : activeChats) {
            if (chat.getEmployee1() == employee || chat.getEmployee2() == employee) {
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
            if (chat.getEmployee1() == employee || chat.getEmployee2() == employee) {
                activeChats.remove(chat);
                availableEmployees.add(employee);
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

}