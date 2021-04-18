package de.neocraftr.autochat;

import com.google.gson.Gson;
import de.neocraftr.autochat.events.ChatReceiveListener;
import de.neocraftr.autochat.events.ChatSendListener;
import de.neocraftr.autochat.events.TickListener;
import de.neocraftr.autochat.settings.Settings;
import net.labymod.api.LabyModAddon;
import net.labymod.core.LabyModCore;
import net.labymod.settings.elements.*;

import java.util.*;

public class AutoChat extends LabyModAddon {

    public static String PREFIX = "§8[§2AutoChat§8] §7";

    private static AutoChat autoChat;
    private Gson gson;
    private Settings settings;
    private boolean active = false;
    private long nextSendMessage = 0;
    private int lastMessage = -1;

    @Override
    public void onEnable() {
        setAutoChat(this);
        setGson(new Gson());
        setSettings(new Settings());
        getApi().getEventManager().register(new ChatSendListener());
        getApi().getEventManager().register(new ChatReceiveListener());
        getApi().registerForgeListener(new TickListener());
    }

    @Override
    public void loadConfig() {
        getSettings().loadConfig();
    }

    @Override
    protected void fillSettings(List<SettingsElement> settings) {
        getSettings().fillSettings(settings);
    }

    public String colorize(String msg) {
        char[] b = msg.toCharArray();
        for (int i = 0; i < b.length - 1; i++) {
            if (b[i] == '&' && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i+1]) > -1) {
                b[i] = '§';
                b[i+1] = Character.toLowerCase(b[i+1]);
            }
        }
        return new String(b);
    }

    public void sendRandomMessage() {
        int numMessages = getSettings().getMessages().size();
        if(numMessages == 1) {
            setLastMessage(0);
            LabyModCore.getMinecraft().getPlayer().sendChatMessage(getSettings().getMessages().get(0));
        } else if (numMessages > 1) {
            int random = new Random().nextInt(numMessages);
            if (random == getLastMessage() && !getSettings().isAllowDuplicate()) {
                random++;
                if(random > numMessages - 1) random = 0;
            }
            setLastMessage(random);
            LabyModCore.getMinecraft().getPlayer().sendChatMessage(getSettings().getMessages().get(random));
        }
    }

    public static AutoChat getAutoChat() {
        return autoChat;
    }
    public static void setAutoChat(AutoChat autoChat) {
        AutoChat.autoChat = autoChat;
    }

    public Gson getGson() {
        return gson;
    }
    public void setGson(Gson gson) {
        this.gson = gson;
    }

    public Settings getSettings() {
        return settings;
    }
    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }

    public long getNextSendMessage() {
        return nextSendMessage;
    }
    public void setNextSendMessage(long nextSendMessage) {
        this.nextSendMessage = nextSendMessage;
    }

    public int getLastMessage() {
        return lastMessage;
    }
    public void setLastMessage(int lastMessage) {
        this.lastMessage = lastMessage;
    }
}
