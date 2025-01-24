package server;

import com.google.gson.JsonObject;
import server.command_executors.*;
import server.database.SocketData;
import server.models.Employee;
import server.services.LoginResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;


public class ClientHandler extends Thread {
    private static final Map<Integer, SocketData> connections = new HashMap<>();
    private Integer userId;
    private LoginResult loginResult = LoginResult.FAILURE;
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

        try {
            while (true) {
                if ((request = inputStream.readLine()) == null) break;
                // generic handler for all cases
                serviceType = ServerDecoder.getServiceType(request);
                methodType = ServerDecoder.getMethodType(request);

                commandExecutor = CommandExecutorFactory.getCommandExecutor(serviceType);
                response = commandExecutor.execute(userId,loginResult,request);
                if(methodType == MethodType.LOGIN){
                    handleLoginResponse(response);
                }

                outputStream.println(response);//send response to client
            }
          } catch (SocketException e){
            System.out.println("Socket Closed from client");
          } catch (IOException e) {
            System.out.println(e.getMessage());
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("error", e.getMessage());
            outputStream.println(jsonObject);
        } finally {
            try {
                outputStream.println("{}");
                socketData.close();// closes socket, inputStream and outputStream
                synchronized (connections) {
                    if(userId != null){
                        SocketData removed = connections.remove(userId);
                        removed.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
//            final ChatManager chatManager = ChatManager.getInstance();
//            if (chatManager.getChattingEmployees().containsKey(socketData)) {
//                ChatSession chat = chatManager.getChatSessionBySocketData(socketData);
//                chat.removeListener(socketData);
//                chatManager.getAvailableEmployees().remove(socketData);
//            } else {
//                synchronized (chatManager) {
//                    chatManager.getAvailableEmployees().remove(socketData);
//
//                }
//            }
        }
    }
    private void handleLoginResponse(String response){
        JsonObject responseJsonObject = ServerDecoder.convertToJsonObject(response);
        int id = responseJsonObject.get("id").getAsInt();
        LoginResult result = LoginResult.valueOf(responseJsonObject.get("result").getAsString());
        if (result == LoginResult.FAILURE) {
            System.out.println("login failed ID or PASSWORD is incorrect ");
            return;
        }
        synchronized(connections){
            SocketData previousConnectionSocketData = connections.get(id);
            if(previousConnectionSocketData != null){
                // disconnect user previous connection
                try {
                    previousConnectionSocketData.getOutputStream().close();
                    previousConnectionSocketData.getSocket().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            userId = id;
            loginResult = result;
            connections.put(id,socketData);
        }
    }

    public static Map<Integer, SocketData> getConnections() {
        return connections;
    }
}