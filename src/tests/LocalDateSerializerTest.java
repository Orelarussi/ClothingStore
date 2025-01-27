package tests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import server.services.LocalDateAdapter;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocalDateSerializerTest {
    @Test
    public void testLocalDateSerialization() {
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
        LocalDate date = LocalDate.of(2023, 12, 25);

        String json = gson.toJson(date);
        assertEquals("\"2023-12-25\"", json);
    }
}
