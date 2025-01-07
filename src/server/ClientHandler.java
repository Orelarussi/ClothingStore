package server;

import client.serverCommunication.Format;
import client.serverCommunication.decodeCMD.DecodeExecuteCommand;
import server.database.ChatSession;
import server.database.SocketData;
import server.models.Employee;
import server.models.User;
import server.services.ChatManager;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;


public class ClientHandler extends Thread {
    private final Map<User, SocketData> connections;
    private SocketData socketData;
    public ClientHandler(Socket socket, Map<User, SocketData> connections) {
        this.socketData = new SocketData(socket);
        this.connections = connections;
    }

    @Override
    public void run() {
        try {

            String inputString;
            boolean notLoggedIn = true;
            // adminManager.login(314965553,"bla bla");
            // Handle client input request
            while ((inputString = socketData.getInputStream().readLine()) != null) {
                if (notLoggedIn) {
                    String loginResponse = DecodeExecuteCommand.decode_and_execute(inputString);
                    if (Format.getMethod(inputString).equals("Login")) {
                        switch (Format.getType(loginResponse)) {
                            case EXCEPTION:
                                socketData.getOutputStream().println(loginResponse);
                                break;
                            case EMPTY:
                                break;
                            default:
                                notLoggedIn = false;
                                Employee emp = new Employee(loginResponse);
                                synchronized (connections) {
                                    connections.put(emp, socketData);
                                }
                                socketData.getOutputStream().println(loginResponse);
                                break;
                        }
                    }
                } else {
                    System.out.println("input string: " + inputString);
                    String res = DecodeExecuteCommand.decode_and_execute(inputString);
                    System.out.println("output string: " + res);
                    // System.out.println("SERVER: SocketData Response: " + socketData.getClientAddress());
                    socketData.getOutputStream().println(res);
                }
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
                connections.remove(Server.getUserBySocketData(socketData));
            }
            final ChatManager chatManager = ChatManager.getInstance();
            if (chatManager.getChattingEmployees().containsKey(socketData)) {
                ChatSession chat = chatManager.getChatSessionBySocketData(socketData);
                chat.removeListener(socketData);
                chatManager.getAvailableEmployees().remove(socketData);
            } else {
                synchronized (chatManager) {
                    chatManager.getAvailableEmployees().remove(socketData);

                }
            }
        }
    }
}