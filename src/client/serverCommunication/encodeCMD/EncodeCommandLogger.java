package client.serverCommunication.encodeCMD;

import client.serverCommunication.ClassType;
import client.serverCommunication.Format;

public class EncodeCommandLogger {
    public static String turnOffSavingChat() {
        return Format.encode(ClassType.LOGGER, "turnOffSavingChat");
    }

    public static String turnOnSavingChat() {
        return Format.encode(ClassType.LOGGER, "turnOnSavingChat");
    }

    public static String getSavingChatStatus() {
        return Format.encode(ClassType.LOGGER, "getSavingChatStatus");
    }
}
