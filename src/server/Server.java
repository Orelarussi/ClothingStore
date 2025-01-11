package server;

import server.database.SocketData;
import server.logger.Logger;
import server.models.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

public class Server {
    public static final int PORT = 12345;

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) {
        System.out.println("--> Server is running...");
        Logger.initLogger();

        // server wait to client connection then wrap the handler using thread
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                //TODO: Getting Employee When Client-Login Here
                new ClientHandler(serverSocket.accept()).start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("--> Server is shutting down...");
        }
    }
}