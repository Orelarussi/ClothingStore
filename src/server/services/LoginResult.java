package server.services;

import com.google.gson.*;
import java.lang.reflect.Type;

public enum LoginResult {
    // Indicates admin login
    ADMIN,
    // Indicates employee login
    EMPLOYEE,
    // Indicates login failure
    FAILURE;

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String toJsonString() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LoginResult.class, new LoginResultSerializer())
                .create();
        return gson.toJson(this, LoginResult.class);
    }

    public static LoginResult fromJson(String s) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LoginResult.class, new LoginResultDeserializer())
                .create();
        return gson.fromJson(s, LoginResult.class);
    }

    // Custom serializer for LoginResult
    private static class LoginResultSerializer implements JsonSerializer<LoginResult> {
        @Override
        public JsonElement serialize(LoginResult src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("type", src.name());
            jsonObject.addProperty("message", src.getMessage());
            return jsonObject;
        }
    }

    // Custom deserializer for LoginResult
    private static class LoginResultDeserializer implements JsonDeserializer<LoginResult> {
        @Override
        public LoginResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            String type = jsonObject.get("type").getAsString();
            LoginResult result = LoginResult.valueOf(type);
            if (jsonObject.has("message")) {
                result.setMessage(jsonObject.get("message").getAsString());
            }
            return result;
        }
    }
}
