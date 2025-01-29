package server;

import com.google.gson.JsonObject;
import server.command_executors.*;
import server.database.SocketData;
import server.services.LoginResult;
import server.services.SessionManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;


public class ClientHandler extends Thread {
    private Integer userId;
    private SocketData socketData;

    public ClientHandler(Socket socket) throws IOException {
        this.socketData = new SocketData(socket);
    }

    @Override
    public void run() {
        String request;
        ServiceType serviceType;
        MethodType methodType;
        IExecute commandExecutor;
        String response;
        BufferedReader inputStream = socketData.getInputStream();
        PrintWriter outputStream = socketData.getOutputStream();
        LoginResult loginResult = LoginResult.FAILURE;
        try {
            while ((request = inputStream.readLine()) != null) {
                // generic handler for all cases
                serviceType = ServerDecoder.getServiceType(request);
                methodType = ServerDecoder.getMethodType(request);

                commandExecutor = CommandExecutorFactory.getCommandExecutor(serviceType);
                response = commandExecutor.execute(userId, loginResult, request);
                if (methodType == MethodType.LOGIN) {
                    handleLoginResponse(response);
                }

                outputStream.println(response);//send response to client
            }
          } catch (SocketException e){
           // socketData.getSocket().get
            System.out.println("Socket Closed from client");
          } catch (IOException e) {
            error(e, outputStream);
        } finally {
            if (userId!= null) {
                try {
                    SessionManager.getInstance().logout(userId);
                } catch (IOException e) {
                    error(e, outputStream);
                }
            }
        }
    }

    private static void error(Exception e, PrintWriter outputStream) {
        System.out.println(e.getMessage());
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("error", e.getMessage());
        outputStream.println(jsonObject);
    }

    private void handleLoginResponse(String response){
        JsonObject responseJsonObject = ServerDecoder.convertToJsonObject(response);
        int id = responseJsonObject.get("id").getAsInt();
        String loginRes = responseJsonObject.get("result").getAsString();

        LoginResult result = LoginResult.fromJson(loginRes);
        if (result == LoginResult.FAILURE) {
            System.out.println(result.getMessage());
            return;
        }
        SessionManager.getInstance().addConnection(id,socketData);
        userId = id;
    }
}