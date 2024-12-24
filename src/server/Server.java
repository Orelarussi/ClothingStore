package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import database.SocketData;
import logger.Logger;
import models.Employee;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private static final int PORT = 12345;
    private static final Gson gson = new Gson();

    private static Map<Employee, SocketData> connections = new HashMap<>();


    public static void main(String[] args) {
        System.out.println("--> Server is running...");
        Logger.initLogger();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                //TODO: Getting Employee When Client-Login Here
                //Employee emp = new Employee("ישראל ישראלי", "0528921319", 123456789, 212444, "חולון", "1111", EmployeeTitle.CASHIER);
                new ClientHandler(serverSocket.accept(),connections).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private static void handleClient(Socket clientSocket) {
//        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
//
//            String request;
//            while ((request = in.readLine()) != null) {
//                System.out.println("Received: " + request);
//
//                // Parse JSON request
//                JsonObject requestJson = gson.fromJson(request, JsonObject.class);
//                String action = requestJson.get("action").getAsString();
//
//                // Process request based on action
//                JsonObject responseJson = processRequest(action, requestJson);
//
//                // Send response as JSON
//                out.println(gson.toJson(responseJson));
//
//                if ("exit".equalsIgnoreCase(action)) {
//                    break;
//                }
//            }
//        } catch (IOException e) {
//            System.out.println("Error handling client: " + e.getMessage());
//        }
//    }
//
//    private static JsonObject processRequest(String action, JsonObject requestJson) {
//        JsonObject response = new JsonObject();
//
//        switch (action) {
//            case "ping":
//                response.addProperty("status", "success");
//                response.addProperty("message", "Pong!");
//                break;
//            case "add_employee":
//                String employeeName = requestJson.get("name").getAsString();
//                response.addProperty("status", "success");
//                response.addProperty("message", "Employee " + employeeName + " added successfully.");
//                break;
//            default:
//                response.addProperty("status", "error");
//                response.addProperty("message", "Unknown action: " + action);
//        }
//
//        return response;
//    }
}
