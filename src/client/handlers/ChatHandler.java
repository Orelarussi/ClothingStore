package client.handlers;

import com.google.gson.JsonObject;
import server.command_executors.MethodType;
import server.command_executors.ServiceType;

public class ChatHandler extends BaseHandler{
    public ChatHandler() {
        super(ServiceType.CHAT);
    }

    public String openChat(int selectedBranchID,int myBranchID){
        JsonObject data = new JsonObject();
        data.addProperty("selectedBranchID",selectedBranchID);
        data.addProperty("myBranchID",myBranchID);
        return super.encodeRequest(MethodType.OPEN_CHAT,data);
    }
}
