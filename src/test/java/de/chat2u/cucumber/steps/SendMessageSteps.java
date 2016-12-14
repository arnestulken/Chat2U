package de.chat2u.cucumber.steps;

import cucumber.api.java.de.Dann;
import cucumber.api.java.de.Gegebensei;
import cucumber.api.java.de.Wenn;
import de.chat2u.ChatServer;
import de.chat2u.authentication.AuthenticationService;
import de.chat2u.authentication.UserRepository;
import de.chat2u.cucumber.selenium.TestServer;
import de.chat2u.model.chats.Group;
import de.chat2u.model.users.AuthenticationUser;
import de.chat2u.model.Message;
import de.chat2u.utils.MessageBuilder;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

/**
 * Created SendMessageSteps in PACKAGE_NAME
 * by ARSTULKE on 15.11.2016.
 */
public class SendMessageSteps {
    private String message;

    @Gegebensei("^ein Chat mit den Teilnehmern \"([^\"]*)\" und \"([^\"]*)\"$")
    public void einChatMitDenTeilnehmernUnd(String user1, String user2) throws Throwable {
        UserRepository<AuthenticationUser> userRepository = new UserRepository<>();
        String password = "Test123";
        userRepository.addUser(new AuthenticationUser(user1, password));
        userRepository.addUser(new AuthenticationUser(user2, password));
        ChatServer.initialize(new AuthenticationService(userRepository));
        ChatServer.login(user1, password, TestServer.getMockSession());
        ChatServer.login(user2, password, TestServer.getMockSession());
    }

    @Wenn("^\"([^\"]*)\" die Nachricht \"([^\"]*)\" sendet$")
    public void dieNachrichtSendet(String username, String message) throws Throwable {
        this.message = MessageBuilder.buildTextMessage(new Message(username, message, ChatServer.LobbyID)).toString();
        ChatServer.sendTextMessageToChat(username, message, ChatServer.LobbyID);
    }

    @Dann("^soll diese im Chat angezeigt werden.$")
    public void sollDieseImChatAngezeigtWerden() throws Throwable {
        assertTrue(getStringHistory(ChatServer.LobbyID).contains(message));
        assertTrue(getStringHistory(ChatServer.LobbyID).contains(message));
    }

    private List<String> getStringHistory(String chatID) {
        Group chat = (Group) ChatServer.chats.getChatByID(chatID);
        return chat.getHistory().stream().map((msg) -> MessageBuilder.buildTextMessage(msg).toString()).collect(Collectors.toList());
    }
}
