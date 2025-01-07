package server.services;

public class ProductManager {
    private static ProductManager instance;

    public static synchronized ProductManager getInstance() {
        if (instance == null) {
            instance = new ProductManager();
        }
        return instance;
    }
}


