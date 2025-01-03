package client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import server.models.Admin;
import server.models.Employee;
import server.models.User;

import java.io.*;
import java.net.Socket;

public class Client {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 12345;
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT)){
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Connected to server at " + SERVER_HOST + ":" + SERVER_PORT);
            User user = login(in,out,consoleInput);
            System.out.println(user);
            startClientListener(in,out,consoleInput);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private static User login(BufferedReader in, PrintWriter out, BufferedReader consoleInput) throws IOException{
        JsonObject requestJson = new JsonObject();
        requestJson.addProperty("action","login");

        System.out.print("Username id: ");
        int id = Integer.parseInt(consoleInput.readLine());
        requestJson.addProperty("id",id);

        System.out.print("Password: ");
        String password = consoleInput.readLine();
        requestJson.addProperty("password", password);
        // Send JSON request to server
        out.println(requestJson);
        String response = in.readLine();

        JsonObject responseJson = gson.fromJson(response, JsonObject.class);
        if (response.contains("error")){
            System.out.println(responseJson.get("error"));
            return null;
        }

        String usertype = responseJson.get("usertype").getAsString();
        String jsonUser = responseJson.get("user").getAsString();

        User user = switch (usertype) {
            case "employee" -> gson.fromJson(jsonUser, Employee.class);
            case "admin" -> gson.fromJson(jsonUser, Admin.class);
            default -> null;
        };
        return user;
    }

    private static void startClientListener(BufferedReader in, PrintWriter out, BufferedReader consoleInput) throws IOException{
            boolean connectionAlive = true;
            while (connectionAlive) {
                // Build JSON request
                JsonObject requestJson = new JsonObject();
                System.out.print("Enter action (add_employee, exit): ");
                String action = consoleInput.readLine();
                requestJson.addProperty("action", action);
                switch (action.toLowerCase()){
                    case "add_employee":
                        System.out.print("Enter employee name: ");
                        String name = consoleInput.readLine();
                        requestJson.addProperty("name", name);
                        // TODO: finish build new employee req
                        break;
                    case "exit":
                        connectionAlive = false;
                        break;
                    default:
                        System.out.print("Action not found please try again");
                        break;
                }

                // Send JSON request
                out.println(gson.toJson(requestJson));

                // Read JSON response
                String response = in.readLine();
                JsonObject responseJson = gson.fromJson(response, JsonObject.class);
                System.out.println("Server response: " + responseJson);
            }
    }
}
