package de.chat2u.cucumber.steps;

import cucumber.api.java.de.Dann;
import cucumber.api.java.de.Gegebensei;
import cucumber.api.java.de.Wenn;
import de.chat2u.ChatServer;
import de.chat2u.cucumber.selenium.OfflineChatContainer;
import de.chat2u.persistence.users.DataBase;
import de.chat2u.cucumber.selenium.OfflineDataBase;
import de.chat2u.model.User;
import org.junit.Assert;

/**
 * Created LogoutSteps in PACKAGE_NAME
 * by ARSTULKE on 15.11.2016.
 */
public class LogoutSteps {

    @Gegebensei("^der angemeldete Teilnehmer \"([^\"]*)\" mit dem Passwort \"([^\"]*)\".$")
    public void derAngemeldeteTeilnehmerMitDemPasswort(String username, String password) throws Throwable {
        DataBase userRepository = new OfflineDataBase();
        userRepository.addUser(new User(username), password);
        ChatServer.initialize(userRepository, new OfflineChatContainer());

        ChatServer.login(username, password, null);
    }

    @Wenn("^\"([^\"]*)\" sich abmeldet,$")
    public void sichAbmeldet(String username) throws Throwable {
        ChatServer.logout(username);
    }

    @Dann("^sehe ich \"([^\"]*)\" nicht mehr in der Liste der Teilnehmer, die gerade Online sind.$")
    public void seheIchNichtMehrInDerListeDerTeilnehmerDieGeradeOnlineSind(String username) throws Throwable {
        Assert.assertFalse(ChatServer.getOnlineUsers().getUsernameList().contains(username));
    }
}
