package server.command_executors;

import com.google.gson.JsonObject;
import server.services.AdminManager;
import server.services.LoginResult;

public class AdminManagerCommandExecutor implements IExecute{
    private AdminManager adminManager;

    @Override
    public String execute(Integer userId,LoginResult loginResult, String request) {
        this.adminManager = AdminManager.getInstance();
        MethodType method = ServerDecoder.getMethodType(request);
        JsonObject data = ServerDecoder.getData(request);
        switch (method){
            case LOGIN:
                JsonObject response = new JsonObject();
                Integer id = data.get("id").getAsInt();
                String password = data.get("password").getAsString();
                LoginResult result = adminManager.login(id,password);
                response.addProperty ("id", id);
                response.addProperty("result", result.toString());
                return response.toString();
            default: return "";
        }
    }
}
