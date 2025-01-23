package server.command_executors;

import com.google.gson.JsonObject;
import server.services.ChatManager;
import server.services.LoginResult;

public class ChatManagerCommandExecutor implements IExecute{
    private ChatManager chatManager;

    @Override
    public String execute(Integer userId,LoginResult loginResult, String request) {
        this.chatManager = ChatManager.getInstance();
        MethodType method = ServerDecoder.getMethodType(request);
        JsonObject data = ServerDecoder.getData(request);
        JsonObject response = new JsonObject();
        int employeeId;
        switch (method){
            case WAITING_FOR_CHAT_REQUEST:
                int selectedBranchId = data.get("selectedBranchId").getAsInt();
                employeeId = data.get("employeeId").getAsInt();
                Integer chatId = this.chatManager.waitingForChatRequest(selectedBranchId,employeeId);
                response.addProperty("chatId",chatId);
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
        }
        return response.toString();

    }
}
