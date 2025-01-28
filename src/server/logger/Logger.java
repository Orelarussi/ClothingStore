package server.logger;

import server.models.Employee;
import server.models.Purchase;
import server.models.customer.Customer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * A utility class responsible for logging various activities within the application.
 */
public class Logger {

    private static final String basicPath = "src/server/logger/";
    private static final String LOG_FILE_PATH = basicPath + "log.txt";

    /**
     * Initializes the server.logger with configurations.
     */
    public static void initLogger() {
        System.out.println("Logger is live");
        log("Server started");
    }

    /**
     * Logs the registration of an employee.
     *
     * @param emp The registered employee.
     */
    public static void addEmployee(Employee emp) {
        log("Employee added: " + emp.getId());
    }

    /**
     * Logs the registration of a customer.
     *
     * @param customer The registered customer.
     */
    public static void registerCustomer(Customer customer) {
        log("Customer added: " + customer.getId());
    }

    /**
     * Logs a purchase event.
     *
     * @param purchase The purchase instance.
     */
    public static void logPurchase(Purchase purchase) {
        log("Customer " + purchase.getCustomerID()+ " purchased " + purchase.getPurchaseID());
    }

    /**
     * Logs the initiation of a chat.
     */
    public static void logChatStarted(int sender, int receiver) {
        log("Chat started from: " + sender + " To -> "+ receiver);
    }


    public static void deleteEmployee(Employee employee) {
        log("Employee deleted: "+employee.getId());
    }

    /**
     * Logs a given message with a timestamp to the log file.
     *
     * @param message The message to be logged.
     */
    public static void log(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE_PATH, true))) {
            writer.write("[" + LocalDateTime.now() + "] " + message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
