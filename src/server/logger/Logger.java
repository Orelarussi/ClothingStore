package server.logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    private static final String basicPath = "src/server/logger/";

    private static final String EMPLOYEE_LOG_FILE_PATH = basicPath + "employee_log.txt";
    private static final String CUSTOMER_LOG_FILE_PATH = basicPath + "customer_log.txt";
    private static final String CHAT_LOG_FILE_PATH = basicPath + "chat_log.txt";
    private static final String SALE_LOG_FILE_PATH = basicPath + "sale_log.txt";

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public enum LogType {
        EMPLOYEE,
        CUSTOMER,
        CHAT,
        SALE
    }

    public static void log(String message, LogType logType) {
        String filePath;

        switch (logType) {
            case EMPLOYEE:
                filePath = EMPLOYEE_LOG_FILE_PATH;
                break;
            case CUSTOMER:
                filePath = CUSTOMER_LOG_FILE_PATH;
                break;
            case CHAT:
                filePath = CHAT_LOG_FILE_PATH;
                break;
            case SALE:
                filePath = SALE_LOG_FILE_PATH;
                break;
            default:
                throw new IllegalArgumentException("Invalid log type");
        }
        writeToFile(message, filePath);
    }

    private static void writeToFile(String message, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write("[" + LocalDateTime.now().format(DATE_TIME_FORMATTER) + "] " + message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}