package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 12345;
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // Handle client in a new thread
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String request;
            while ((request = in.readLine()) != null) {
                System.out.println("Received: " + request);

                // Parse JSON request
                JsonObject requestJson = gson.fromJson(request, JsonObject.class);
                String action = requestJson.get("action").getAsString();

                // Process request based on action
                JsonObject responseJson = processRequest(action, requestJson);

                // Send response as JSON
                out.println(gson.toJson(responseJson));

                if ("exit".equalsIgnoreCase(action)) {
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error handling client: " + e.getMessage());
        }
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
