package server.command_executors;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import server.ClientHandler;
import server.database.SocketData;
import server.models.chat.ChatSession;
import server.models.chat.Message;
import server.services.AdminManager;
import server.services.ChatManager;
import server.services.LoginResult;
import server.services.SessionManager;

import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;

public class ChatManagerCommandExecutor implements IExecute {
    private ChatManager chatManager;

    @Override
    public String execute(Integer userId, LoginResult loginResult, String request) {
        this.chatManager = ChatManager.getInstance();
        MethodType method = ServerDecoder.getMethodType(request);
        JsonObject data = ServerDecoder.getData(request);
        JsonObject response = new JsonObject();
        int employeeId;
        Integer chatId;
        switch (method) {
            case WAITING_FOR_CHAT_REQUEST:
                int selectedBranchId = data.get("selectedBranchId").getAsInt();
                employeeId = data.get("employeeId").getAsInt();
                chatId = this.chatManager.waitingForChatRequest(selectedBranchId, employeeId);
                response.addProperty("chatId", chatId);
                if (chatId != null) {
                    // שליחת תגובה גם לעובד השני
                    int otherEmployeeId = this.chatManager.getOtherEmployeeIdInChat(chatId, employeeId);
                    sendResponseToEmployee(otherEmployeeId, response); // פונקציה לשליחת התגובה
                }
                break;
            case AVAILABLE_FOR_CHAT_REQUEST:
                employeeId = data.get("employeeId").getAsInt();
                chatId = this.chatManager.availableForChatRequest(employeeId);
                response.addProperty("chatId", chatId);
                if (chatId != null) {
                    // שליחת תגובה גם לעובד השני
                    int otherEmployeeId = this.chatManager.getOtherEmployeeIdInChat(chatId, employeeId);
                    sendResponseToEmployee(otherEmployeeId, response); // פונקציה לשליחת התגובה
                }
                break;
            case REMOVE_FROM_WAITING_LIST:
                employeeId = data.get("employeeId").getAsInt();
                int branchId = data.get("branchId").getAsInt();
                chatManager.removeFromWaitingList(branchId, employeeId);
                response.addProperty("result", "removed");
                break;
            case REMOVE_FROM_AVAILABLE_LIST:
                employeeId = data.get("employeeId").getAsInt();
                chatManager.removeFromAvailableList(employeeId);
                response.addProperty("result", "removed");
                break;
            case GET_OPTIONAL_CHAT:
                employeeId = data.get("employeeId").getAsInt();
                List<ChatSession> chatSessions = this.chatManager.getOptionalChatToJoin(employeeId);
                JsonArray chatSessionsArray = new JsonArray();
                for (ChatSession chatSession : chatSessions) {
                    JsonObject chatSessionJson = new JsonObject();
                    chatSessionJson.addProperty("chatId", chatSession.getChatId());
                    chatSessionJson.addProperty("description", chatSession.toString());
                    chatSessionsArray.add(chatSessionJson);
                }
                response.add("chatSessionsArray", chatSessionsArray);
                break;
            case JOIN_EXIST_CHAT:
                employeeId = data.get("employeeId").getAsInt();
                chatId = data.get("chatId").getAsInt();
                Boolean success = this.chatManager.joinChatAsManager(employeeId, chatId);
                response.addProperty("success", success);
                break;
            case START_CHAT_MESSAGE://כותרת השיחה
                employeeId = data.get("employeeId").getAsInt();
                chatId = data.get("chatId").getAsInt();
                String chatTitle = this.chatManager.startChatMessage(employeeId, chatId);
                response.addProperty("message", chatTitle);
                break;
            case CLOSE_CHAT:
                chatId = data.get("chatId").getAsInt();
                chatManager.closeChat(chatId);
                break;
            case SEND_MESSAGE:
                int senderId = data.get("employeeId").getAsInt();
                chatId = data.get("chatId").getAsInt();
                String timeString = data.get("timestamp").getAsString();
                String content = data.get("content").getAsString();
                LocalDateTime timestamp = LocalDateTime.parse(timeString);
                String senderName = AdminManager.getInstance().getEmployeeNameById(senderId);
                Message message = new Message(senderId, content, timestamp,senderName);
                data.addProperty("senderName", senderName);

                List<Integer> employeeToSend = chatManager.sendMessage(message, chatId);
                try {
                    for (int id : employeeToSend) {
                        sendResponseToEmployee(id, data); // פונקציה לשליחת התגובה
                    }
                    response.addProperty("success", true);
                    response.addProperty("senderID",senderId);
                    response.addProperty("content",content);
                } catch (IllegalStateException e) {
                    System.err.println("Failed to send response to employee " + userId + ": " + e.getMessage());
                    response.addProperty("success", false);
                    response.addProperty("error", e.getMessage());
                }
        }
        return response.toString();
    }

    protected void sendResponseToEmployee(int employeeId, JsonObject response) throws IllegalStateException {
        // Get the connection for the employee
        SocketData socketData = SessionManager.getInstance().getConnections().get(employeeId);
        if (socketData == null) {
            throw new IllegalStateException("Employee " + employeeId + " is not currently connected.");
        }
        PrintWriter out = socketData.getOutputStream();
        out.println(response.toString());
    }
}
