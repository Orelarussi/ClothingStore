package server.services;

import server.logger.Logger;
import server.models.Employee;
import server.models.chat.ChatSession;
import server.models.chat.Message;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ChatManager {
    private final Map<Integer, Queue<Integer>> availableEmployeesByBranch; // Available employees by branch
    private final Map<Integer, Queue<Integer>> waitingEmployeesByBranch; // Waiting employees by branch
    private final Map<Integer, Object> branchLocks; // <branch,Object>Locks for branches
    private final Map<Integer, ChatSession> activeChatSessions;
    private static int nextChatSessionId = 1;
    private static ChatManager instance;

    private ChatManager() {
        availableEmployeesByBranch = new ConcurrentHashMap<Integer, Queue<Integer>>();
        waitingEmployeesByBranch = new ConcurrentHashMap<>();
        branchLocks = new ConcurrentHashMap<>();
        activeChatSessions = new ConcurrentHashMap<>();

        for (int branchId = 1; branchId <= 4; branchId++) {
            availableEmployeesByBranch.put(branchId, new ConcurrentLinkedQueue<>());
            waitingEmployeesByBranch.put(branchId, new ConcurrentLinkedQueue<>());
            branchLocks.put(branchId, new Object());
        }
    }

    public static synchronized ChatManager getInstance() {
        if (instance == null) {
            instance = new ChatManager();
        }
        return instance;
    }

    private synchronized Object getBranchLock(int branchId) {
        return branchLocks.get(branchId);
    }

    public Boolean joinChatAsManager(int managerId, int targetChatId) {
        ChatSession chatSession = activeChatSessions.get(targetChatId);
        if (chatSession == null) {
            System.out.println("The chat the shift manager tried to join is no longer available. chat target" + targetChatId);
            return false;
        }
        int shiftManagerBranchId = getBranchIdByEmployeeId(managerId);
        if (chatSession.isRelevantForShiftManager(shiftManagerBranchId)) {
            chatSession.setShiftManagerID(managerId);
            Logger.log("Shift manager with ID " + managerId + " joined chat with ID " + targetChatId+"\n", Logger.LogType.CHAT);
            System.out.println("Manager with ID " + managerId + " joined chat with ID " + targetChatId);
            return true;
        }
        return false;
    }

    public List<ChatSession> getOptionalChatToJoin(int managerId) {
        int shiftManagerBranchId = getBranchIdByEmployeeId(managerId);
        return activeChatSessions.values().stream()
                .filter(chatSession -> chatSession.isRelevantForShiftManager(shiftManagerBranchId)).toList();
    }

    public Integer waitingForChatRequest(int selectedBranchId, int employeeId) {
        synchronized (getBranchLock(selectedBranchId)) {
            Queue<Integer> availableQueue = availableEmployeesByBranch.get(selectedBranchId);
            if (availableQueue.isEmpty()) {
                addWaitingEmployee(selectedBranchId, employeeId);
                return null;
            }
            int availableEmployeeID = availableQueue.poll();
            return createChat(employeeId, availableEmployeeID);
        }

    }

    public Integer availableForChatRequest(int employeeId) {
        int branchId = getBranchIdByEmployeeId(employeeId);
        synchronized (getBranchLock(branchId)) {
            Queue<Integer> waitingQueue = waitingEmployeesByBranch.get(branchId);
            if (waitingQueue.isEmpty()) {
                addAvailableEmployee(branchId, employeeId);
                return null;
            }
            int waitingEmployeeID = waitingQueue.poll();
            return createChat(waitingEmployeeID, employeeId);
        }
    }

    public int createChat(int employee1Id, int employee2Id) {
        int chatSessionId = getNewChatSessionId();
        ChatSession chatSession = new ChatSession(employee1Id, employee2Id, chatSessionId);
        activeChatSessions.put(chatSessionId, chatSession);
        Logger.log(chatSession.toString()+"opened. chat ID: "+chatSessionId+"\n", Logger.LogType.CHAT);
        System.out.println(chatSession.toString()+". chat ID: "+chatSessionId);
        return chatSessionId;
    }

    public void closeChat(int chatSessionId) {
        //update the log
        ChatSession chatSession= activeChatSessions.get(chatSessionId);
        String logMessage = "Chat " + chatSessionId + " closed successfully. Messages:\n";
        for(Message message :chatSession.getMessages()){
            logMessage+=message.toString()+"\n";
        }
        Logger.log(logMessage, Logger.LogType.CHAT);
        System.out.println("Chat " + chatSessionId + " closed successfully.");
        //close
        activeChatSessions.remove(chatSessionId);//The chat object will be eligible for garbage collection if no other references exist
    }




    //the synchronized is not! inside the add func ,need to surround.
    public void addWaitingEmployee(int branchId, int employeeId) {
        Queue<Integer> waitingQueue = waitingEmployeesByBranch.get(branchId);
        waitingQueue.add(employeeId);

    }

    public void addAvailableEmployee(int branchId, int employeeId) {
        Queue<Integer> availableQueue = availableEmployeesByBranch.get(branchId);
        availableQueue.add(employeeId);
    }

    //the synchronized is inside the remove fuc
    public void removeFromWaitingList(int branchId, int employeeId) {
        synchronized (getBranchLock(branchId)) {
            Queue<Integer> waitingQueue = waitingEmployeesByBranch.get(branchId);
            waitingQueue.remove(employeeId);
        }
    }

    public void removeFromAvailableList(int employeeId) {
        int branchId = getBranchIdByEmployeeId(employeeId);
        synchronized (getBranchLock(branchId)) {
            Queue<Integer> availableQueue = availableEmployeesByBranch.get(branchId);
            availableQueue.remove(employeeId);
        }
    }

    //help functions:
    private int getBranchIdByEmployeeId(int employeeId) {
        AdminManager adminManager = AdminManager.getInstance();
        Employee employee = adminManager.findEmployeeById(employeeId);
        return employee.getBranchID();
    }

    private synchronized int getNewChatSessionId() {
        return nextChatSessionId++;
    }

    public int getOtherEmployeeIdInChat(Integer chatId, int employeeId) {
        ChatSession chatSession = activeChatSessions.get(chatId);
        return (chatSession.getEmployee1Id() == employeeId)
                ? chatSession.getEmployee2Id()
                : chatSession.getEmployee1Id();
    }

    public String startChatMessage(int employeeId, Integer chatId) {
        String chatSessionMessage = activeChatSessions.get(chatId).toString();
        return chatSessionMessage;
    }

    public List<Integer> sendMessage(Message message, int chatId) {
        ChatSession chatSession = activeChatSessions.get(chatId);
        chatSession.addMessage(message);
        return chatSession.getOtherParticipants(message.getSenderId());
    }
}