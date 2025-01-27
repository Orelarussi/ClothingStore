package client.handlers;

import com.google.gson.JsonObject;
import server.command_executors.MethodType;
import server.command_executors.ServiceType;
import server.models.Person;
import java.util.HashSet;
import java.util.Set;

public class SessionHandler extends BaseHandler{
    private static SessionHandler instance;

    public static SessionHandler getInstance() {
        if (instance == null) {
            instance = new SessionHandler();
        }
        return instance;
    }

    private SessionHandler() {super(ServiceType.SESSION);}

    public String login(int id, String password){
        JsonObject data = new JsonObject();
        data.addProperty("id",id);
        data.addProperty("password",password);
        return super.encodeRequest(MethodType.LOGIN,data);
    }

    public String logout(Integer id) {
        JsonObject data = new JsonObject();
        data.addProperty("id", id);
        return super.encodeRequest(MethodType.LOGOUT,data);
    }
}
