package de.neocraftr.autochat.events;
import de.neocraftr.autochat.AutoChat;
import net.labymod.api.events.MessageSendEvent;

import java.util.StringJoiner;

public class ChatSendListener implements MessageSendEvent {

    @Override
    public boolean onSend(String msg) {
        String[] args = msg.split(" ");
        if(args.length >= 1) {
            if(args[0].equalsIgnoreCase(".autochat") || args[0].equalsIgnoreCase(".ac")) {
                if(args.length >= 2) {
                    if(args[1].equalsIgnoreCase("on")) {
                        if(getAutoChat().getSettings().getMessages().size() > 0) {
                            getAutoChat().setNextSendMessage(0);
                            getAutoChat().setActive(true);
                            getAutoChat().getApi().displayMessageInChat(AutoChat.PREFIX+"§aAutochat aktiviert.");
                        } else {
                            getAutoChat().getApi().displayMessageInChat(AutoChat.PREFIX+"§cBitte füge mindestens eine Nachricht in den Einstellungen hinzu.");
                        }
                    } else if(args[1].equalsIgnoreCase("off")) {
                        getAutoChat().setActive(false);
                        getAutoChat().getApi().displayMessageInChat(AutoChat.PREFIX+"§cAutochat deaktiviert.");
                    } else if(args[1].equalsIgnoreCase("once")) {
                        if(getAutoChat().getSettings().getMessages().size() > 0) {
                            getAutoChat().sendRandomMessage();
                        } else {
                            getAutoChat().getApi().displayMessageInChat(AutoChat.PREFIX+"§cBitte füge mindestens eine Nachricht in den Einstellungen hinzu.");
                        }
                    } else if(args[1].equalsIgnoreCase("info") || args[1].equalsIgnoreCase("i")) {
                        StringJoiner joiner = new StringJoiner("\n");
                        joiner.add("§7---------------------- §2AutoChat §7----------------------");
                        joiner.add("§eNachrichten:");
                        for(int i = 0; i< getAutoChat().getSettings().getMessages().size(); i++) {
                            joiner.add("§8- §a§l"+i+" §7"+ getAutoChat().colorize(getAutoChat().getSettings().getMessages().get(i)));
                        }
                        if(getAutoChat().getSettings().getMessages().size() == 0) joiner.add("\n§cKeine Nachrichten vorhanden.");
                        joiner.add("§eNachrichteninterval: §a"+getAutoChat().getSettings().getInterval()+" Sekunden");
                        joiner.add("§eAktiviert: "+(getAutoChat().isActive() ? "§aJa" : "§cNein"));
                        joiner.add("§7----------------------------------------------------");
                        getAutoChat().getApi().displayMessageInChat(joiner.toString());
                    } else printHelp();
                } else printHelp();
                return true;
            }
        }
        return false;
    }

    private void printHelp() {
        StringJoiner joiner = new StringJoiner("\n");
        joiner.add("§7---------------------- §2AutoChat §7----------------------");
        joiner.add("§eVerfügbare Befehle:");
        joiner.add("§a.autochat help §7- §eZeigt alle verfügbaren Befehle an.");
        joiner.add("§a.autochat on/off §7- §eSchaltet Autochat an oder aus.");
        joiner.add("§a.autochat once §7- §eSendet eine einzelne zufällige Nachricht.");
        joiner.add("§a.autochat info/i §7- §eZeigt die aktuellen Einstellungen an.");
        joiner.add("§7----------------------------------------------------");
        getAutoChat().getApi().displayMessageInChat(joiner.toString());
    }

    private AutoChat getAutoChat() {
        return AutoChat.getAutoChat();
    }
}
