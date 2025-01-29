package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.models.chat.ChatSession;
import server.services.ChatManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ChatManagerTest {

    private ChatManager chatManager;

    @BeforeEach
    public void setUp() {
        chatManager = ChatManager.getInstance();
    }

    @Test
    public void testCreateChat() {
        int employee1Id = 1;
        int employee2Id = 2;
        int chatId = chatManager.createChat(employee1Id, employee2Id);

        assertTrue(chatId > 0, "Chat ID should be greater than 0.");
    }

    @Test
    public void testCloseChat() {
        int employee1Id = 1;
        int employee2Id = 2;
        int chatId = chatManager.createChat(employee1Id, employee2Id);

        chatManager.closeChat(chatId);

        // Validate that the chat session is removed
        assertThrows(NullPointerException.class, () -> chatManager.getOtherEmployeeIdInChat(chatId, employee1Id));
    }
}
