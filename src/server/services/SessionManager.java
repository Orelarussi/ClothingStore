package server.services;

import server.database.SocketData;
import server.logger.Logger;

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
        if (isLoggedIn(userID)) {
            LoginResult result = LoginResult.FAILURE;
            String message = "You are already logged in. please logout first from the other session.";
            result.setMessage(message);
            Logger.log(message);
            return result;
        }
        return AdminManager.getInstance().login(userID,password);
    }

    public synchronized void logout(int id) throws IOException {
        SocketData data = connections.remove(id);
        if(data != null){
            // disconnect user previous connection
            data.close();
            String s = "Removed connection from client socket";
            Logger.log(s);
            System.out.println(s);
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
