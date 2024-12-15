package client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.Socket;

public class Client {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 12345;
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to server at " + SERVER_HOST + ":" + SERVER_PORT);

            String userInput;
            while (true) {
                // Build JSON request
                JsonObject requestJson = new JsonObject();
                System.out.print("Enter action (ping, add_employee, exit): ");
                String action = consoleInput.readLine();
                requestJson.addProperty("action", action);

                if ("add_employee".equalsIgnoreCase(action)) {
                    System.out.print("Enter employee name: ");
                    String name = consoleInput.readLine();
                    requestJson.addProperty("name", name);
                }

                // Send JSON request
                out.println(gson.toJson(requestJson));

                // Exit if action is "exit"
                if ("exit".equalsIgnoreCase(action)) {
                    break;
                }

                // Read JSON response
                String response = in.readLine();
                JsonObject responseJson = gson.fromJson(response, JsonObject.class);
                System.out.println("Server response: " + responseJson);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
