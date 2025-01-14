package client.serverCommunication.decodeCMD;

import client.serverCommunication.Format;

public class DecodeExecuteCommand {
    public static String decode_and_execute(String command) {
        String response = switch (Format.getType(command)) {
            case CHAT -> DecodeExecuteCommandChat.execute(command);
            case EMPLOYEE -> DecodeExecuteCommandEmployee.execute(command);
            case LOGGER -> DecodeExecuteCommandLogger.execute(command);
            case PURCHASE_HISTORY -> DecodeExecuteCommandPurchaseHistory.execute(command);
            default -> "";
        };
        if (response.isEmpty())
            response = Format.encodeEmpty("");
        return response;
    }
}
