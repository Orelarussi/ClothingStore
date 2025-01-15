package client.serverCommunication.encodeCMD;

import client.serverCommunication.ClassType;
import client.serverCommunication.Format;
import server.models.Employee;

public class EncodeCommandChat {
    public static String clientOnline(Employee emp) {
        String res = Format.encode(ClassType.CHAT, "clientOnline", emp.serializeToString());
        return res;
    }
    public static String clientOffline(Employee emp) {
        String res = Format.encode(ClassType.CHAT, "clientOffline", emp.serializeToString());
        return res;
    }
    public static String sendMessage(Employee emp, String message) {
        String res = Format.encode(ClassType.CHAT, "sendMessage", emp.serializeToString(), message);
        return res;
    }
    public static String getAvailableBranches(String branch) {
        String res = Format.encode(ClassType.CHAT, "getAvailableBranches", branch);
        return res;
    }
    public static String getAvailableChats(String branch) {
        String res = Format.encode(ClassType.CHAT, "getAvailableChats", branch);
        return res;
    }
    public static String requestChatWithBranch(Employee emp, String branch) {
        String res = Format.encode(ClassType.CHAT, "requestChatWithBranch", emp.serializeToString(), branch);
        return res;
    }
    public static String leaveChat(Employee emp) {
        String res = Format.encode(ClassType.CHAT, "leaveChat", emp.serializeToString());
        return res;
    }
    public static String joinChatSession(Employee emp, int sessionID) {
        String res = Format.encode(ClassType.CHAT, "joinChatSession", emp.serializeToString(), Integer.toString(sessionID));
        return res;
    }
}
