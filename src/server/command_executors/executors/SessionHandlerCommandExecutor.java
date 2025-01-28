package server.command_executors.executors;

import com.google.gson.JsonObject;
import server.command_executors.IExecute;
import server.command_executors.MethodType;
import server.command_executors.ServerDecoder;
import server.services.LoginResult;
import server.services.SessionManager;

import java.io.IOException;

public class SessionHandlerCommandExecutor implements IExecute {
    @Override
    public String execute(Integer userId, LoginResult loginResult, String request) {
        SessionManager manager = SessionManager.getInstance();
        MethodType method = ServerDecoder.getMethodType(request);
        JsonObject data = ServerDecoder.getData(request);
        JsonObject response = new JsonObject();
        int id;
        switch (method) {
            case LOGIN:
                id = data.get("id").getAsInt();
                String password = data.get("password").getAsString();
                LoginResult result = manager.login(id, password);
                response.addProperty("id", id);
                response.addProperty("result", result.toJsonString());
                break;
            case LOGOUT:
                id = data.get("id").getAsInt();
                try {
                    manager.logout(id);
                    response.addProperty("result", "success");
                } catch (IOException e) {
                    response.addProperty("error", e.getMessage());
                }
                break;
        }
        return response.toString();
    }
}
