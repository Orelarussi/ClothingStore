package server.command_executors;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ServerDecoder {

    public static final String SERVICE_KEY = "service";
    public static final String METHOD_KEY = "method";
    public static final String DATA_KEY = "data";
    public static ServiceType getServiceType(String input){
        String service = convertToJsonObject(input).get(SERVICE_KEY).getAsString();
        return ServiceType.valueOf(service.toUpperCase());
    }
    public static MethodType getMethodType(String input){
        String method = convertToJsonObject(input).get(METHOD_KEY).getAsString();
        return MethodType.valueOf(method.toUpperCase());
    }

    public static JsonObject getData(String input){
        return convertToJsonObject(input).get(DATA_KEY).getAsJsonObject();
    }

    public static JsonObject convertToJsonObject(String jsonString){
        JsonElement jsonElement = JsonParser.parseString(jsonString);
        return jsonElement.getAsJsonObject();
    }
    public static String unAuthResponse(ServiceType serviceType,MethodType methodType){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(SERVICE_KEY,serviceType.toString());
        jsonObject.addProperty(METHOD_KEY,methodType.toString());
        jsonObject.addProperty("message","Method not premiered, please login with the right permissions!");
        return jsonObject.toString();
    }
}
