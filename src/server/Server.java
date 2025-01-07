package server;

import server.database.SocketData;
import server.logger.Logger;
import server.models.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private static final int PORT = 12345;
    private static final Map<User, SocketData> connections = new HashMap<>();

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) {
        System.out.println("--> Server is running...");
        Logger.initLogger();

        // server wait to client connection then wrap the handler using thread
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                //TODO: Getting Employee When Client-Login Here
                new ClientHandler(serverSocket.accept(), connections).start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("--> Server is shutting down...");
        }
    }

    public static SocketData getSocketDataByUser(User user) {
        for (Map.Entry<User, SocketData> entry : connections.entrySet()) {
            User temp = entry.getKey();
            if (temp.getId() == user.getId())
                return entry.getValue();
        }
        return null;
    }

    public static User getUserBySocketData(SocketData socketData) {
        for (Map.Entry<User, SocketData> entry : connections.entrySet()) {
            SocketData temp = entry.getValue();

            if (temp.equals(socketData))
                return entry.getKey();
        }
        return null;
    }

    public static Map<User, SocketData> getConnections() {
        return connections;
    }
}