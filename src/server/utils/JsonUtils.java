package server.utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class JsonUtils {

    private static final Gson gson = new Gson();

    // Generic method to read a JSON file and convert it to a list of objects
    public static <T> List<T> readJsonFile(String filePath, Class<T> clazz) {
        try (FileReader reader = new FileReader(filePath)) {
            Type listType = TypeToken.getParameterized(List.class, clazz).getType();
            return gson.fromJson(reader, listType);
        } catch (IOException e) {
            System.out.println("Error reading JSON file: " + e.getMessage());
            return null;
        }
    }

    // Generic method to write a list of objects to a JSON file
    public static <T> void writeJsonFile(String filePath, List<T> objects) {
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(objects, writer);
            System.out.println("JSON file updated successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to JSON file: " + e.getMessage());
        }
    }
}
