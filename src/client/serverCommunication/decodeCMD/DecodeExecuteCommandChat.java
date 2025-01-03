package client.serverCommunication.decodeCMD;

import client.serverCommunication.Format;
import database.ChatSession;
import database.SocketData;
import models.Employee;
import server.Server;
import server.services.ChatManager;

import java.util.Set;

public class DecodeExecuteCommandChat {
    public static String execute(String command) {
        ChatManager chatManager = ChatManager.getInstance();
        models.Employee emp;
        database.SocketData socketData;
        String branch;
        ChatSession chat;
        String response = Format.encodeSuccessMessage();

        switch (Format.getMethod(command)) {
            case "clientOnline":
                emp = new Employee(Format.getFirstParam(command));
                socketData = Server.getSocketDataByEmployee(emp);
                chatManager.getAvailableEmployees().put(socketData, emp);
                break;
            case "clientOffline":
                emp = new Employee(Format.getFirstParam(command));
                socketData = Server.getSocketDataByEmployee(emp);
                chatManager.getAvailableEmployees().remove(socketData);
                break;
            case "sendMessage":
                emp = new Employee(Format.getFirstParam(command));
                socketData = Server.getSocketDataByEmployee(emp);
                String message = Format.getSecondParam(command);
                chat = chatManager.getChatSessionBySocketData(socketData);
                chat.broadcast(emp, message, socketData.getOutputStream());
                break;
            case "getAvailableBranches":
                branch = Format.getFirstParam(command);
                Set<String> branches = chatManager.getAvailableBranches(branch);

                if (branches.size() == 0)
                    response = Format.encodeEmpty("");
                else
                    response = Format.encodeAvailableBranches(branches);

                break;
            case "getAvailableChats":
                branch = Format.getFirstParam(command);
                Set<ChatSession> availableChats = chatManager.getAvailableChats(branch);

                if (availableChats.size() == 0)
                    response = Format.encodeEmpty("");
                else
                    response = Format.encodeAvailableChats(availableChats);
                break;
            case "requestChatWithBranch":                   // Employee #1 and #2 starting a new chat between them
                emp = new Employee(Format.getFirstParam(command));
                branch = Format.getSecondParam(command);

                if (chatManager.isAvailableEmployeesForChat(branch)) {
                    SocketData otherEmpSocketData = chatManager.getFirstAvailableEmployeeByBranch(branch);

                    if (otherEmpSocketData != null) {
                        chat = new ChatSession(emp, Server.getEmployeeBySocketData(otherEmpSocketData));
                        chat.addListener(otherEmpSocketData, Server.getEmployeeBySocketData(otherEmpSocketData));
                        chat.addListener(Server.getSocketDataByEmployee(emp), emp);

                        chatManager.getChattingEmployees().put(otherEmpSocketData, chat);
                        chatManager.getChattingEmployees().put(Server.getSocketDataByEmployee(emp), chat);

                        chatManager.getAvailableEmployees().remove(otherEmpSocketData);
                        chatManager.getAvailableEmployees().remove(Server.getSocketDataByEmployee(emp));

                        otherEmpSocketData.getOutputStream().println("CHAT@@@setCurrentChat###" + chat.getSessionID());
                        response = "CHAT@@@setCurrentChat###" + chat.getSessionID();
                    }
                } else {
                    chatManager.addEmployeeToWaitingList(emp, branch);
                    response = "CHAT@@@waitingList###" + branch;
                }
                break;
            case "leaveChat":
                emp = new Employee(Format.getFirstParam(command));
                socketData = Server.getSocketDataByEmployee(emp);
                chat = chatManager.getChatSessionBySocketData(socketData);
                chat.removeListener(socketData);
                response = "CHAT@@@abortCurrentChat###";
                break;
            case "joinChatSession":
                int sessionID = Integer.parseInt(Format.getSecondParam(command));
                emp = new Employee(Format.getFirstParam(command));
                socketData = Server.getSocketDataByEmployee(emp);
                chat = ChatManager.getInstance().getChatSessionByID(sessionID);

                chat.addListener(socketData, emp);
                chatManager.getChattingEmployees().put(socketData, chat);

                response = "CHAT@@@setCurrentChat###" + chat.getSessionID();
                break;
            default:
                break;
        }

        return response;
    }
}
