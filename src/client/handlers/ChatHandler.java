package client.handlers;

import com.google.gson.JsonObject;
import server.command_executors.MethodType;
import server.command_executors.ServiceType;

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

    public String availableForChat(Integer id) {
        return "";
    }
}
