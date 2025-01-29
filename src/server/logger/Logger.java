package server.logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    private static final String basicPath = "src/server/logger/";
    private static final String LOG_FILE_PATH = basicPath + "log.txt";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm") ;

    public static void log(String message) {
        writeToFile(message);
    }

    private static void writeToFile(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Logger.LOG_FILE_PATH, true))) {
            String line = "[" + LocalDateTime.now().format(DATE_TIME_FORMATTER) + "] " + message + "\n";
            writer.write(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}