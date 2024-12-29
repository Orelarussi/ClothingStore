package logger;

import models.Employee;
import models.Purchase;
import models.purchase_plan.PurchasePlan;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * A utility class responsible for logging various activities within the application.
 */
public class Logger {

    private static final String basicPath = "src/Store/Server/Logger/";
    private static final String LOG_FILE_PATH = basicPath + "log.txt";
    private static String saveChatConversation;
    private static database.PropertiesHandler propertiesHandler;

    /**
     * Initializes the logger with configurations.
     */
    public static void initLogger() {
        propertiesHandler = new database.PropertiesHandler(basicPath + "Preferences.properties");
        saveChatConversation = propertiesHandler.getProperty("SAVE_CHAT_CONVERSATIONS");
        System.out.println("Logger is live");
    }

    /**
     * Logs the registration of an employee.
     *
     * @param emp The registered employee.
     */
    public static void registerEmployee(models.Employee emp) {
        log("Employee Registered: " + emp.getId());
    }

    /**
     * Logs the registration of a customer.
     *
     * @param customer The registered customer.
     */
    public static void registerCustomer(models.customer.Customer customer) {
        log("Customer Registered: " + customer.getId());
    }

    /**
     * Logs a purchase event.
     *
     * @param purchase The purchase instance.
     */
    public static void logPurchase(Purchase purchase) {
        log("Customer " + purchase.getCustomerID()+ " purchased " + purchase.getPurchaseID());
    }

     //TODO: handle chat
    /**
     * Handles the saving of chat conversations.
     *
     * @param fileName The file name for storing the conversation.
     * @param conversation The conversation content.
     */
    public static void saveChat(String fileName, String conversation) {
        if (saveChatConversation == "Yes") {
            // Save the entire chat to separate file

        }
        // String logMessage = "[" + LocalDateTime.now() + "] " + sender + " to " + receiver + ": " + message;
        // log(logMessage);
    }

    /**
     * Logs the initiation of a chat.
     */
    public static void logChatStarted(Employee sender, Employee receiver) {
        log("Chat started from: " + sender.getId() + " To -> "+ receiver.getId());
    }

    /**
     * Turns off the saving of chat conversations.
     */
    public static void turnOffSavingChat() {
        saveChatConversation = "No";
        propertiesHandler.setProperty("SAVE_CHAT_CONVERSATIONS", "No");
    }

    /**
     * Turns on the saving of chat conversations.
     */
    public static void turnOnSavingChat() {
        saveChatConversation = "Yes";
        propertiesHandler.setProperty("SAVE_CHAT_CONVERSATIONS", "Yes");
    }

    /**
     * Retrieves the current configuration for saving chat conversations.
     *
     * @return The status of saving chat conversations.
     */
    public static String getSavingChatStatus() {
        return saveChatConversation;
    }

    /**
     * Logs a given message with a timestamp to the log file.
     *
     * @param message The message to be logged.
     */
    private static void log(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE_PATH, true))) {
            writer.write("[" + LocalDateTime.now() + "] " + message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
