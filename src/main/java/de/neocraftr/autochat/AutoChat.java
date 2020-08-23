package de.neocraftr.autochat;

import com.google.gson.Gson;
import de.neocraftr.autochat.events.ChatReceiveListener;
import de.neocraftr.autochat.events.ChatSendListener;
import de.neocraftr.autochat.events.TickListener;
import de.neocraftr.autochat.settings.ArraySettingsElement;
import net.labymod.api.LabyModAddon;
import net.labymod.core.LabyModCore;
import net.labymod.settings.elements.*;
import net.labymod.utils.Material;

import java.util.*;

public class AutoChat extends LabyModAddon {

    public static String PREFIX = "§8[§2AutoChat§8] §7";

    private static AutoChat autoChat;
    private Gson gson;
    private boolean active = false;
    private ArrayList<String> messages = new ArrayList<>();
    private int interval = 60;
    private long nextSendMessage = 0;
    private int lastMessage = -1;

    @Override
    public void onEnable() {
        setAutoChat(this);
        setGson(new Gson());
        getApi().getEventManager().register(new ChatSendListener());
        getApi().getEventManager().register(new ChatReceiveListener());
        getApi().registerForgeListener(new TickListener());
    }

    @Override
    public void loadConfig() {
        if(getConfig().has("messages")) {
            setMessages(getGson().fromJson(getConfig().get("messages"), ArrayList.class));
        }
        if(getConfig().has("interval")) {
            setInterval(getConfig().get("interval").getAsInt());
        }
    }

    @Override
    protected void fillSettings(List<SettingsElement> settings) {
        final NumberElement intervalSetting = new NumberElement("Nachrichten Interval",
                new ControlElement.IconData(Material.WATCH), getInterval());
        intervalSetting.setMinValue(1);
        intervalSetting.addCallback(interval -> {
            setInterval(interval);
            getConfig().addProperty("interval", interval);
            saveConfig();
        });
        settings.add(intervalSetting);

        final ArraySettingsElement messagesSetting = new ArraySettingsElement("Nachrichten",
                new ControlElement.IconData(Material.BOOK_AND_QUILL), getMessages(), messages -> {
                    setMessages(messages);
                    getConfig().add("messages", getGson().toJsonTree(messages));
                    saveConfig();
                });
        settings.add(messagesSetting);

        settings.add(new HeaderElement("§7Übersicht Befehle: .autochat help"));
    }

    public String colorize(String msg) {
        return msg.replace("&", "§");
    }

    public void sendRandomMessage() {
        int numMessages = getMessages().size();
        if(numMessages == 1) {
            setLastMessage(0);
            LabyModCore.getMinecraft().getPlayer().sendChatMessage(getMessages().get(0));
        } else if (numMessages > 1) {
            int random = new Random().nextInt(numMessages - 1);
            if (random == getLastMessage()) {
                random++;
                if(random > numMessages - 1) random = 0;
            }
            setLastMessage(random);
            LabyModCore.getMinecraft().getPlayer().sendChatMessage(getMessages().get(random));
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

    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }

    public ArrayList<String> getMessages() {
        return messages;
    }
    public void setMessages(ArrayList<String> messages) {
        this.messages = messages;
    }

    public int getInterval() {
        return interval;
    }
    public void setInterval(int interval) {
        this.interval = interval;
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
