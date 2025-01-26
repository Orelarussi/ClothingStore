package client.handlers;

import com.google.gson.JsonObject;
import server.command_executors.MethodType;
import server.command_executors.ServiceType;

import java.time.LocalDateTime;

public class ChatHandler extends BaseHandler{
    private ChatHandler() {
        super(ServiceType.CHAT);
    }
    private static ChatHandler instance = new ChatHandler();
    public static ChatHandler getInstance() {
        return instance;
    }

    public String waitingForChatRequest(int selectedBranchId, int employeeId){
        JsonObject data = new JsonObject();
        data.addProperty("selectedBranchId",selectedBranchId);
        data.addProperty("employeeId",employeeId);
        return super.encodeRequest(MethodType.WAITING_FOR_CHAT_REQUEST,data);
    }

    public String removeFromWaitingList(Integer id, int branchId) {
        JsonObject data = new JsonObject();
        data.addProperty("BranchId",branchId);
        data.addProperty("myID",id);
        return super.encodeRequest(MethodType.REMOVE_FROM_WAITING_LIST,data);
    }

    public String removeFromAvailableList(int employeeId) {
        JsonObject data = new JsonObject();
        data.addProperty("employeeId", employeeId);
        return super.encodeRequest(MethodType.REMOVE_FROM_AVAILABLE_LIST, data);
    }

    public String availableForChat(Integer employeeId) {
        JsonObject data = new JsonObject();
        data.addProperty("employeeId", employeeId);
        return super.encodeRequest(MethodType.AVAILABLE_FOR_CHAT_REQUEST, data);
    }

    public String getOptionalChat(Integer employeeId) {
        JsonObject data = new JsonObject();
        data.addProperty("employeeId", employeeId);
        return super.encodeRequest(MethodType.GET_OPTIONAL_CHAT, data);
    }

    public String joinExistChat(int chatId, Integer employeeId) {
        JsonObject data = new JsonObject();
        data.addProperty("employeeId", employeeId);
        data.addProperty("chatId", chatId);
        return super.encodeRequest(MethodType.JOIN_EXIST_CHAT, data);
    }

    public String startChatMessage(int chatId, Integer employeeId) {
        JsonObject data = new JsonObject();
        data.addProperty("employeeId", employeeId);
        data.addProperty("chatId", chatId);
        return super.encodeRequest(MethodType.START_CHAT_MESSAGE, data);
    }

    public String closeChat(int chatId) {
        JsonObject data = new JsonObject();
        data.addProperty("chatId", chatId);
        return super.encodeRequest(MethodType.CLOSE_CHAT, data);
    }

    public String sendMessage(int chatId, Integer employeeId, String content, LocalDateTime timestamp) {
        JsonObject data = new JsonObject();
        data.addProperty("employeeId", employeeId);
        data.addProperty("chatId", chatId);
        data.addProperty("timestamp", timestamp.toString());
        data.addProperty("content", content);


        return super.encodeRequest(MethodType.SEND_MESSAGE, data);
    }
}
