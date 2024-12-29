package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import database.ChatSession;
import database.SocketData;
import logger.Logger;
import models.Employee;
import services.ChatManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {
    private static final int PORT = 12345;

    private static Map<Employee, SocketData> connections = new HashMap<>();
    private static ChatManager chatManager = ChatManager.getInstance();


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

    public static SocketData getSocketDataByEmployee(Employee emp) {
        for(Map.Entry<Employee, SocketData> entry : connections.entrySet()) {
            Employee temp = entry.getKey();
            if(temp.getId() == emp.getId())
                return entry.getValue();
        }
        return null;
    }

    private static void handleClient(Socket clientSocket) {
      try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
           PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String request;
           while ((request = in.readLine()) != null) {
                System.out.println("Received: " + request);
                Gson gson = new Gson();
                // Parse JSON request
                JsonObject requestJson = gson.fromJson(request, JsonObject.class);
                String action = requestJson.get("action").getAsString();
                                // Process request based on action
                JsonObject responseJson = processRequest(action, requestJson);

                // Send response as JSON
                System.out.println(gson.toJson(responseJson));

                if ("exit".equalsIgnoreCase(action)) {
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error handling client: " + e.getMessage());
        }
    }

    public static Employee getEmployeeBySocketData(SocketData socketData) {
        for(Map.Entry<Employee, SocketData> entry : connections.entrySet()) {
            SocketData temp = entry.getValue();

            if(temp.equals(socketData))
                return entry.getKey();
        }
        return null;
    }
    public static Map<Employee, SocketData> getConnections() {
        return connections;
    }

    private static JsonObject processRequest(String action, JsonObject requestJson) {
        JsonObject response = new JsonObject();

        switch (action) {
            case "ping":
                response.addProperty("status", "success");
                response.addProperty("message", "Pong!");
                break;
            case "add_employee":
                String employeeName = requestJson.get("name").getAsString();
                response.addProperty("status", "success");
                response.addProperty("message", "Employee " + employeeName + " added successfully.");
                break;
            default:
                response.addProperty("status", "error");
                response.addProperty("message", "Unknown action: " + action);
        }

        return response;
    }

}