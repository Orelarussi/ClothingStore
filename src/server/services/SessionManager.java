package server.services;

import server.database.SocketData;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SessionManager {
    private static SessionManager instance;
    private Map<Integer, SocketData> connections = new HashMap<>();

    private SessionManager() {

    }

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public synchronized LoginResult login(int userID, String password) {
        if (connections.get(userID) != null) {
            LoginResult result = LoginResult.FAILURE;
            result.setMessage("You are already logged in. please logout first from the other session.");
            return result;
        }
        return AdminManager.getInstance().login(userID,password);
    }

    public synchronized void logout(int id) throws IOException {
        SocketData data = connections.remove(id);
        if(data != null){
            // disconnect user previous connection
            data.close();
            System.out.println("Removed connection from client socket");
        }
    }

    public boolean isLoggedIn(int id) {
        return connections.get(id) != null;
    }

    public Map<Integer, SocketData> getConnections() {
        return connections;
    }

    public synchronized void addConnection(int id, SocketData socketData) {
        connections.put(id, socketData);
    }
}
