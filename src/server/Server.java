package server;

import server.logger.Logger;
import server.utils.JsonUtils;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    public static final int PORT = 12345;

    public static void main(String[] args) {
        System.out.println("--> Server is running...");
        Logger.initLogger();
        JsonUtils.load();

        // server wait to client connection then wrap the handler using thread
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (!serverSocket.isClosed()) {
                new ClientHandler(serverSocket.accept()).run();
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("--> Server is shutting down...");
            JsonUtils.save();
        }
    }
}