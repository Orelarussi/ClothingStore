package server;

import server.database.SocketData;
import server.logger.Logger;
import server.utils.JsonUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {
    public static final int PORT = 12345;
    private boolean isRunning = true;

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    public void start() {
        System.out.println("--> Server is running...");
        Logger.initLogger();
        JsonUtils.load();

        // server wait to client connection then wrap the handler using thread
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (isRunning) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                clientHandler.start();
            }
        } catch (IOException | IllegalThreadStateException e) {
            e.printStackTrace();
        } finally {
            System.out.println("--> Server is shutting down...");
            JsonUtils.save();
        }
    }

    public void stop() {
        isRunning = false;
    }
}