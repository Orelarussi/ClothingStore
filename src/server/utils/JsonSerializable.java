package server.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public abstract class JsonSerializable {
    protected static final Gson gson = new Gson();

    // No-arg constructor for Gson compatibility
    protected JsonSerializable() {}

    public JsonSerializable(String string){
    }
    // Serialize the object to a JSON string
    public String serializeToString() {
        return gson.toJson(this);
    }

    public JsonObject serializeToJson() {
        return gson.toJsonTree(this).getAsJsonObject();
    }

    // Abstract method to create an instance from JSON
    protected abstract void populateFromJson(String json);

    // Factory method to create an instance from JSON
//    public static <T extends JsonSerializable> T fromJson(String json, Class<T> tClass) {
//        try {
//            T instance = tClass.getDeclaredConstructor().newInstance();
//            instance.populateFromJson(json);
//            return instance;
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to deserialize JSON into " + tClass.getSimpleName(), e);
//        }
//    }

}

