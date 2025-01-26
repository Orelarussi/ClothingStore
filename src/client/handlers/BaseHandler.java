package client.handlers;

import com.google.gson.JsonObject;
import server.ClientHandler;
import server.command_executors.MethodType;
import server.command_executors.ServiceType;
import server.database.SocketData;

import java.io.PrintWriter;

public abstract class BaseHandler {
    private static final String SERVICE_KEY = "service";
    private static final String METHOD_KEY = "method";
    private static final String DATA_KEY = "data";
    private static ServiceType serviceType;

    public BaseHandler(ServiceType serviceType) {
        BaseHandler.serviceType = serviceType;
    }

    protected String encodeRequest(MethodType methodType, JsonObject data) {
            // Build the request string
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(SERVICE_KEY,serviceType.toString());
        jsonObject.addProperty(METHOD_KEY,methodType.toString());
        jsonObject.add(DATA_KEY,data);
        return jsonObject.toString();
    }

}
