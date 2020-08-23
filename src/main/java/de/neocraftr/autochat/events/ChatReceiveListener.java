package de.neocraftr.autochat.events;

import de.neocraftr.autochat.AutoChat;
import net.labymod.api.events.MessageReceiveEvent;

public class ChatReceiveListener implements MessageReceiveEvent {

    @Override
    public boolean onReceive(String msgRaw, String msg) {
        if(msg.equals("[Switcher] Lade Daten herunter!")) {
            // Disable when changing CityBuild (GrieferGames only)
            getAutoChat().setActive(false);
        }
        return false;
    }

    private AutoChat getAutoChat() {
        return AutoChat.getAutoChat();
    }
}
