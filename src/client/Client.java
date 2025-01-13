package client;

import client.handlers.AdminHandler;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import server.command_executors.ServerDecoder;
import server.services.LoginResult;

import java.io.*;
import java.net.Socket;

public class Client {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 12345;
    private static final Gson gson = new Gson();
    private static AdminHandler adminHandler = new AdminHandler();
    private static LoginResult loginResult= LoginResult.FAILURE;
    private static Integer id;
    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT)){
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Connected to server at " + SERVER_HOST + ":" + SERVER_PORT);

            while (loginResult==LoginResult.FAILURE){
                // id
                System.out.print("Username id: ");
                int id = Integer.parseInt(consoleInput.readLine());
                // password
                System.out.print("Password: ");
                String password = consoleInput.readLine();
                String request = adminHandler.login(id,password);
                //send the request to the server
                out.println(request);
                JsonObject loginResponse = ServerDecoder.convertToJsonObject(in.readLine());
                loginResult = LoginResult.valueOf(loginResponse.get("result").getAsString());
                if (loginResult==LoginResult.FAILURE){
                    System.out.println("User id or password are incorrect, please try again");
                }
            }
            if (loginResult == LoginResult.ADMIN) {
                startAdminMenu(in, out, consoleInput);
            }else {

            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private static void startAdminMenu(BufferedReader in, PrintWriter out, BufferedReader consoleInput) throws IOException{
            boolean connectionAlive = true;
            while (connectionAlive) {
                // Build JSON request
                JsonObject requestJson = new JsonObject();
                System.out.print("Enter action (add_employee, exit): ");
                String action = consoleInput.readLine();
                switch (action.toLowerCase()){

                }

                // Send JSON request
//                out.println(gson.toJson(requestJson));

                // Read JSON response
//                String response = in.readLine();
//                JsonObject responseJson = gson.fromJson(response, JsonObject.class);
//                System.out.println("Server response: " + responseJson);
            }
    }
}
