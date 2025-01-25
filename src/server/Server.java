package server;

import server.logger.Logger;
import server.services.BranchManager;
import server.utils.JsonUtils;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    public static final int PORT = 12345;
    private boolean isRunning = true;

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    public void start(){
        System.out.println("--> Server is running...");
        Logger.initLogger();
        JsonUtils.load();

        // server wait to client connection then wrap the handler using thread
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (isRunning) {
                new ClientHandler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("--> Server is shutting down...");
            JsonUtils.save();
        }
    }

    public void stop(){
        isRunning = false;
    }
}