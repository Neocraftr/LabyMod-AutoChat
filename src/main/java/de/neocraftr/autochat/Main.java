package de.neocraftr.autochat;

import com.google.gson.Gson;
import net.labymod.api.LabyModAddon;
import net.labymod.core.LabyModCore;
import net.labymod.core.MinecraftAdapter;
import net.labymod.settings.elements.SettingsElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main extends LabyModAddon {

    public static Main instance;
    public static MinecraftAdapter mc;
    public static String prefix = "§8[§2AutoChat§8] §7";
    public static Gson gson;
    public static Random random;
    public static boolean active = false;
    public static boolean saveMessage = false;
    public static ArrayList<String> messages = null;
    public static int interval = 0;
    public static int counter = 0;
    public static int lastMessage = -1;

    @Override
    public void onEnable() {
        instance = this;
        gson = new Gson();
        random = new Random();
        getApi().getEventManager().register(new ChatListener());
        getApi().registerForgeListener(new Events());
        mc = LabyModCore.getMinecraft();
    }

    @Override
    public void loadConfig() {
        if(!getConfig().has("messages")) {
            getConfig().add("messages", gson.toJsonTree(new ArrayList<String>()));
            saveConfig();
        }
        if(!getConfig().has("interval")) {
            getConfig().addProperty("interval", 60);
            saveConfig();
        }

        messages = gson.fromJson(getConfig().get("messages"), ArrayList.class);
        interval = getConfig().get("interval").getAsInt();
    }

    @Override
    protected void fillSettings(List<SettingsElement> list) {}

    public void saveSettings() {
        getConfig().add("messages", gson.toJsonTree(messages));
        getConfig().addProperty("interval", interval);
        saveConfig();
    }

    public String colorize(String msg) {
        return msg.replace("&", "§");
    }

    public void printHelp() {
        String helpText = "";
        helpText += "\n§7---------------------- §2AutoChat §7----------------------";
        helpText += "\n§eBefehle:";
        helpText += "\n§a#autochat help §7- §aZeigt alle verfügbaren Befehle an.";
        helpText += "\n§a#autochat on/off §7- §aSchaltet Autochat an oder aus.";
        helpText += "\n§a#autochat info/i §7- §aZeigt die aktuellen Einstellungen an.";
        helpText += "\n§a#autochat add §7- §aFüget eine Nachricht zur Listen hinzu.";
        helpText += "\n§a#autochat remove <Nummer> §7- §aEntfernt eine Nachricht von der Liste.";
        helpText += "\n§eAnstatt §a#autochat §ekann auch der Befehl §a#ac §everwendet werden. Die Nachrichten werden nach zufälliger Reihenfolge gesenset. Es wird keine Nachricht 2-mal hintereinander gesendet.";
        helpText += "\n§7----------------------------------------------------";
        getApi().displayMessageInChat(helpText);
    }
}
