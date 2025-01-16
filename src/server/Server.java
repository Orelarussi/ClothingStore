package server;

import server.logger.Logger;
import server.utils.JsonUtils;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    public static final int PORT = 12345;

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) {
        System.out.println("--> Server is running...");
        JsonUtils.load();

        Logger.initLogger();

        // server wait to client connection then wrap the handler using thread
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                //TODO: Getting Employee When Client-Login Here
                ClientHandler clientHandler = new ClientHandler(serverSocket.accept());
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("--> Server is shutting down...");
        }
    }


}