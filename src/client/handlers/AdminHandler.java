package client.handlers;

import com.google.gson.JsonObject;
import server.command_executors.MethodType;
import server.command_executors.ServiceType;

public class AdminHandler extends HandlerBase {
    public AdminHandler() {
        super(ServiceType.ADMIN);
    }
    public String login(int id, String password){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id",id);
        jsonObject.addProperty("password",password);
        return super.encodeRequest(MethodType.LOGIN,jsonObject);
    }

}
