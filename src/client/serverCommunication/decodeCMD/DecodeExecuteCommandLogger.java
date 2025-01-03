package client.serverCommunication.decodeCMD;

import client.serverCommunication.ClassType;
import client.serverCommunication.Format;
import server.logger.Logger;

import java.sql.SQLException;

public class DecodeExecuteCommandLogger {
    public static String execute(String command) throws SQLException {
        final String def = Format.encodeSuccessMessage();

        return switch (Format.getMethod(command)) {
            case "turnOffSavingChat" -> {
                // public static void turnOffSavingChat() {
                Logger.turnOffSavingChat();
                yield def;
            }
            case "turnOnSavingChat" -> {
                // public static void turnOnSavingChat() {
                Logger.turnOnSavingChat();
                yield def;
            }
            case "getSavingChatStatus" ->
                // public static String getSavingChatStatus() {
                    Format.encode(ClassType.LOGGER, "", Logger.getSavingChatStatus());
            default -> def;
        };
    }
}
