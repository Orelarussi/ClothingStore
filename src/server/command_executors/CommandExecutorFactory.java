package server.command_executors;

import java.util.HashMap;

public class CommandExecutorFactory {
    private static final HashMap<ServiceType, IExecute> commandExecutorDictionary = new HashMap<>();

    static {
        commandExecutorDictionary.put(ServiceType.ADMIN, new AdminManagerCommandExecutor());
        commandExecutorDictionary.put(ServiceType.CUSTOMER, new EmployeeManagerCommandExecutor());
    }

    public static IExecute getCommandExecutor(ServiceType serviceType) {
        return commandExecutorDictionary.get(serviceType);
    }
}
