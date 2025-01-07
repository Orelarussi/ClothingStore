package client.serverCommunication.decodeCMD;

import client.serverCommunication.Format;

import java.sql.SQLException;

public class DecodeExecuteCommand {
    public static String decode_and_execute(String command) {
        String response = "";
        try {
            response = switch (Format.getType(command)) {
                case CHAT -> DecodeExecuteCommandChat.execute(command);
                case EMPLOYEE -> DecodeExecuteCommandEmployee.execute(command);
                case LOGGER -> DecodeExecuteCommandLogger.execute(command);
                case PURCHASE_HISTORY -> DecodeExecuteCommandPurchaseHistory.execute(command);
                default -> response;
            };
        } catch (SQLException e) {
            response = Format.encodeException("קיימת שגיאה מול מסד הנתונים, אנא נסה שוב מאוחר יותר");
            System.out.println(response + "\n" + e);
        }

        if (response == null || response.isEmpty())
            response = Format.encodeEmpty("");
        return response;
    }
}
