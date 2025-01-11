package server;

import client.serverCommunication.Format;
import client.serverCommunication.decodeCMD.DecodeExecuteCommand;
import com.google.gson.JsonObject;
import server.command_executors.*;
import server.database.ChatSession;
import server.database.SocketData;
import server.models.Employee;
import server.models.User;
import server.services.ChatManager;
import server.services.LoginResult;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;


public class ClientHandler extends Thread {
    public static final Map<Integer, SocketData> connections = new HashMap<>();
    private Integer userId;
    private LoginResult loginResult = LoginResult.FAILURE;
    private SocketData socketData;
    public ClientHandler(Socket socket) {
        this.socketData = new SocketData(socket);
    }

    @Override
    public void run() {
        try {
            String request;
            while ((request = socketData.getInputStream().readLine()) != null) {
                // generic handler for all cases
                ServiceType serviceType = ServerDecoder.getServiceType(request);
                MethodType methodType = ServerDecoder.getMethodType(request);
                IExecute commandExecutor = CommandExecutorFactory.getCommandExecutor(serviceType);
                String response = commandExecutor.execute(userId,loginResult,request);
                if(methodType == MethodType.LOGIN){
                    handleLoginResponse(response);
                }

                socketData.getOutputStream().println(response);
            }
          } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                socketData.getOutputStream().close();
                socketData.getSocket().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            synchronized (connections) {
                if(userId != null){
                    connections.remove(connections.get(userId));
                }
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
        if (result!= LoginResult.FAILURE){
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

    }
}