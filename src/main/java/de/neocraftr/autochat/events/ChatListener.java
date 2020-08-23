package de.neocraftr.autochat.events;
import de.neocraftr.autochat.AutoChat;
import net.labymod.api.events.MessageSendEvent;

import java.util.StringJoiner;

public class ChatListener implements MessageSendEvent {

    @Override
    public boolean onSend(String msg) {
        String[] args = msg.split(" ");
        if(args.length >= 1) {
            if(args[0].equalsIgnoreCase(".autochat") || args[0].equalsIgnoreCase(".ac")) {
                if(args.length >= 2) {
                    if(args[1].equalsIgnoreCase("on")) {
                        getAutoChat().setNextSendMessage(0);
                        getAutoChat().setActive(true);
                        getAutoChat().getApi().displayMessageInChat(AutoChat.PREFIX+"§aAutochat aktiviert.");
                    } else if(args[1].equalsIgnoreCase("off")) {
                        getAutoChat().setActive(false);
                        getAutoChat().getApi().displayMessageInChat(AutoChat.PREFIX+"§cAutochat deaktiviert.");
                    } else if(args[1].equalsIgnoreCase("once")) {
                        getAutoChat().sendRandomMessage();
                    } else if(args[1].equalsIgnoreCase("info") || args[1].equalsIgnoreCase("i")) {
                        StringJoiner joiner = new StringJoiner("\n");
                        joiner.add("§7---------------------- §2AutoChat §7----------------------");
                        joiner.add("§eNachrichten:");
                        for(int i = 0; i< getAutoChat().getMessages().size(); i++) {
                            joiner.add("§8- §a§l"+i+" §7"+ getAutoChat().colorize(getAutoChat().getMessages().get(i)));
                        }
                        if(getAutoChat().getMessages().size() == 0) joiner.add("\n§cKeine Nachrichten vorhanden.");
                        joiner.add("§eNachrichteninterval: §a"+getAutoChat().getInterval()+" Sekunden");
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
        joiner.add("§eBefehle:");
        joiner.add("§a.autochat help §7- §aZeigt alle verfügbaren Befehle an.");
        joiner.add("§a.autochat on/off §7- §aSchaltet Autochat an oder aus.");
        joiner.add("§a.autochat once §7- §aSendet eine einzelne zufällige Nachricht.");
        joiner.add("§a.autochat info/i §7- §aZeigt die aktuellen Einstellungen an.");
        joiner.add("§eAnstatt §a.autochat §ekann auch der Befehl §a.ac §everwendet werden. Die Nachrichten werden nach zufälliger Reihenfolge gesenset. Es wird keine Nachricht 2-mal hintereinander gesendet.");
        joiner.add("§7----------------------------------------------------");
        getAutoChat().getApi().displayMessageInChat(joiner.toString());
    }

    private AutoChat getAutoChat() {
        return AutoChat.getAutoChat();
    }
}
