package server.command_executors;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.services.BranchManager;

import java.io.PrintWriter;

public class ServerDecoder {

    public static final String SERVICE_KEY = "service";
    public static final String METHOD_KEY = "method";
    public static final String DATA_KEY = "data";

    /**
     * Get the ServiceType from the JSON input
     */
    public static ServiceType getServiceType(String input) {
        String service = convertToJsonObject(input).get(SERVICE_KEY).getAsString();
        return ServiceType.valueOf(service.toUpperCase());
    }

    /**
     * Get the MethodType from the JSON input
     */
    public static MethodType getMethodType(String input) {
        String method = convertToJsonObject(input).get(METHOD_KEY).getAsString();
        return MethodType.valueOf(method.toUpperCase());
    }

    /**
     * Get the data object from the JSON input
     */
    public static JsonObject getData(String input) {
        return convertToJsonObject(input).get(DATA_KEY).getAsJsonObject();
    }

    /**
     * Convert a JSON string to JsonObject
     */
    public static JsonObject convertToJsonObject(String jsonString) {
        JsonElement jsonElement = JsonParser.parseString(jsonString);
        return jsonElement.getAsJsonObject();
    }
}
