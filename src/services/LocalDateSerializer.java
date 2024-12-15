package services;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateSerializer implements JsonSerializer<LocalDate> {
    @Override
    public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
        // Convert LocalDate to a string in a specific format (e.g., "YYYY-MM-DD")
        return context.serialize(src.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }
}
