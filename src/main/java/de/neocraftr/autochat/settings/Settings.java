package de.neocraftr.autochat.settings;

import com.google.gson.JsonObject;
import de.neocraftr.autochat.AutoChat;
import net.labymod.settings.elements.*;
import net.labymod.utils.Material;

import java.util.ArrayList;
import java.util.List;

public class Settings {

    private ArrayList<String> messages = new ArrayList<>();
    private int interval = 60;
    private boolean allowDuplicate = false;
    private boolean varyInterval = false;

    public void loadConfig() {
        if(getConfig().has("messages")) {
            setMessages(getAutoChat().getGson().fromJson(getConfig().get("messages"), ArrayList.class));
        }
        if(getConfig().has("interval")) {
            setInterval(getConfig().get("interval").getAsInt());
        }
        if(getConfig().has("allowDuplicate")) {
            setAllowDuplicate(getConfig().get("allowDuplicate").getAsBoolean());
        }
        if(getConfig().has("varyInterval")) {
            setVaryInterval(getConfig().get("varyInterval").getAsBoolean());
        }
    }

    public void fillSettings(List<SettingsElement> settings) {
        final BooleanElement allowDuplicateBtn = new BooleanElement("Doppelte Nachrichten erlauben", new ControlElement.IconData(Material.LEVER), allowDuplicate -> {
            setAllowDuplicate(allowDuplicate);
            getConfig().addProperty("allowDuplicate", allowDuplicate);
            saveConfig();
        }, isAllowDuplicate());
        settings.add(allowDuplicateBtn);

        final BooleanElement varyIntervalBtn = new BooleanElement("Interval +/- 20%", new ControlElement.IconData(Material.WATCH), varyInterval -> {
            setVaryInterval(varyInterval);
            getConfig().addProperty("varyInterval", isVaryInterval());
            saveConfig();
        }, isVaryInterval());
        settings.add(varyIntervalBtn);

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
            getConfig().add("messages", getAutoChat().getGson().toJsonTree(messages));
            saveConfig();
        });
        settings.add(messagesSetting);

        settings.add(new HeaderElement("§7Übersicht Befehle: .autochat help"));
    }

    private JsonObject getConfig() {
        return AutoChat.getAutoChat().getConfig();
    }

    private void saveConfig() {
        AutoChat.getAutoChat().saveConfig();
    }

    private AutoChat getAutoChat() {
        return AutoChat.getAutoChat();
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

    public boolean isAllowDuplicate() {
        return allowDuplicate;
    }
    public void setAllowDuplicate(boolean allowDuplicate) {
        this.allowDuplicate = allowDuplicate;
    }

    public boolean isVaryInterval() {
        return varyInterval;
    }
    public void setVaryInterval(boolean varyInterval) {
        this.varyInterval = varyInterval;
    }
}
