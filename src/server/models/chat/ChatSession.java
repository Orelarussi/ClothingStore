package server.models.chat;

import server.models.Employee;
import server.services.AdminManager;

import java.util.ArrayList;
import java.util.List;

public class ChatSession {
    private final int chatSessionId;
    private final int employee1Id;
    private final int employee2Id;
    private final List<Message> messages; // List of messages in the chat
    private Integer shiftManagerId; // Optional shift manager
    AdminManager adminManager = AdminManager.getInstance();


    public ChatSession(int employee1Id, int employee2Id,int chatSessionId) {
        this.employee1Id = employee1Id;
        this.employee2Id = employee2Id;
        this.chatSessionId = chatSessionId;
        this.messages = new ArrayList<>();
        this.shiftManagerId= null;
    }

    // Getters
    public int getEmployee1Id() {
        return employee1Id;
    }

    public int getEmployee2Id() {
        return employee2Id;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public Integer getShiftManagerId() {
        return shiftManagerId;
    }


    // Setters for shift managers
    public Boolean setShiftManagerID(Integer shiftManagerId) {
        Employee manager= adminManager.findEmployeeById(shiftManagerId);
        Employee employee1 = adminManager.findEmployeeById(employee1Id);
        Employee employee2 = adminManager.findEmployeeById(employee2Id);
        if (this.shiftManagerId == null && (employee1.getBranchID() == manager.getBranchID()|| employee2.getBranchID()==manager.getBranchID()) ){
            this.shiftManagerId = shiftManagerId;
            return true;
        }
        else return false;
    }


        // Add a message to the chat
    public void addMessage(Message message) {
        messages.add(message);
    }

    public boolean isRelevantForShiftManager(int branchId) {
        AdminManager adminManager = AdminManager.getInstance();
        int branch1 = adminManager.findEmployeeById(employee1Id).getBranchID();
        int branch2 = adminManager.findEmployeeById(employee2Id).getBranchID();

        return (shiftManagerId == null) && (branch1 == branchId || branch2 == branchId);
    }

    @Override
    public String toString() {

        // Get names of employees
        String employee1Name = adminManager.getEmployeeNameById(employee1Id);
        String employee2Name = adminManager.getEmployeeNameById(employee2Id);
            if (shiftManagerId==null) {
                return "Chat between " + employee1Name + " " + employee1Id + " and " + employee2Name + " " + employee2Id;
            }
        String shiftManagerName = adminManager.getEmployeeNameById(shiftManagerId);
        return "Chat between " + employee1Name + " " + employee1Id + ", " + employee2Name + " " + employee2Id+ " and " + shiftManagerName + " " + shiftManagerId;


    }

    public int getChatId() {
        return chatSessionId;
    }

    public List<Integer> getOtherParticipants(int senderId) {
        List<Integer> otherParticipants = new ArrayList<>();
        if(senderId!=employee1Id){
            otherParticipants.add(employee1Id);
        }if(senderId!=employee2Id){
            otherParticipants.add(employee2Id);
        }if( shiftManagerId!=null && senderId!=shiftManagerId) {
            otherParticipants.add(shiftManagerId);
        }
        return otherParticipants;
    }
}
