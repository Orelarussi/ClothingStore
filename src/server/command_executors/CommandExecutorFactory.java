package server.command_executors;

import java.util.HashMap;

public class CommandExecutorFactory {
    private static HashMap<ServiceType,IExecute> commandExecutorDictionary = new HashMap<ServiceType,IExecute>();
    static {
        commandExecutorDictionary.put(ServiceType.ADMIN,new AdminManagerCommandExecutor());
    }
    public static IExecute getCommandExecutor(ServiceType serviceType){
        return commandExecutorDictionary.get(serviceType);
    }
}
