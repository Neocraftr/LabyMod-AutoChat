package de.neocraftr.autochat;
import net.labymod.api.events.MessageSendEvent;

public class ChatListener implements MessageSendEvent {
    @Override
    public boolean onSend(String msg) {
        if(Main.saveMessage) {
            Main.saveMessage = false;
            if(!msg.equalsIgnoreCase("cancel")) {
                Main.messages.add(msg);
                Main.instance.saveSettings();
                Main.instance.getApi().displayMessageInChat(Main.prefix+"§aNachricht hinzugefügt: §7"+Main.instance.colorize(msg));
            } else {
                Main.instance.getApi().displayMessageInChat(Main.prefix+"§cHinzufügen abgebrochen.");
            }
            return true;
        }

        String[] args = msg.split(" ");
        if(args.length >= 1) {
            if(args[0].equalsIgnoreCase(".autochat") || args[0].equalsIgnoreCase(".ac")) {
                if(args.length >= 2) {
                    if(args[1].equalsIgnoreCase("on")) {
                        Main.counter = Main.interval*20;
                        Main.active = true;
                        Main.instance.getApi().displayMessageInChat(Main.prefix+"§aAutochat aktiviert.");
                    } else if(args[1].equalsIgnoreCase("off")) {
                        Main.active = false;
                        Main.instance.getApi().displayMessageInChat(Main.prefix+"§cAutochat deaktiviert.");
                    } else if(args[1].equalsIgnoreCase("add")) {
                        Main.saveMessage = true;
                        Main.instance.getApi().displayMessageInChat(Main.prefix+"§aSchreibe die Nachricht, die hinzugefügt werden soll, in den Chat. §cSchreibe \"cancel\" zum Abbrechen.");
                    } else if(args[1].equalsIgnoreCase("remove")) {
                        if(args.length == 3) {
                            try {
                                String message = Main.messages.remove(Integer.parseInt(args[2]));
                                Main.instance.saveSettings();
                                Main.instance.getApi().displayMessageInChat(Main.prefix+"§aNachricht entfernt: §7"+Main.instance.colorize(message));
                            } catch (NumberFormatException e) {
                                Main.instance.getApi().displayMessageInChat(Main.prefix+"§cBitte gib eine gültige Zahl an.");
                            } catch (IndexOutOfBoundsException e) {
                                Main.instance.getApi().displayMessageInChat(Main.prefix+"§cDiese Nummer befindet sich nicht in der Liste.");
                            }
                        } else Main.instance.getApi().displayMessageInChat(Main.prefix+"§cVerwendung: .autochat remove <Nummer>");
                    } else if(args[1].equalsIgnoreCase("info") || args[1].equalsIgnoreCase("i")) {
                        String infoText = "";
                        infoText += "\n§7---------------------- §2AutoChat §7----------------------";
                        infoText += "\n§eNachrichten:";
                        for(int i=0; i<Main.messages.size(); i++) {
                            infoText += "\n§8- §a§l"+i+" §7"+Main.instance.colorize(Main.messages.get(i));
                        }
                        if(Main.messages.size() == 0) infoText += "\n§cKeine Nachrichten vorhanden.";
                        infoText += "\n§eNachrichteninterval: §a"+Main.interval+" Sekunden";
                        infoText += "\n§eAktiviert: "+(Main.active ? "§aJa" : "§cNein");
                        infoText += "\n§7----------------------------------------------------";
                        Main.instance.getApi().displayMessageInChat(infoText);
                    } else if(args[1].equalsIgnoreCase("interval")) {
                        if(args.length == 3) {
                            try {
                                Main.interval = Integer.parseInt(args[2]);
                                Main.instance.saveSettings();
                                Main.instance.getApi().displayMessageInChat(Main.prefix+"§aNachrichteninterval auf §e"+Main.interval+" Sekunden §agesetzt.");
                            } catch(NumberFormatException e) {
                                Main.instance.getApi().displayMessageInChat(Main.prefix+"§cBitte gib eine gültige Zahl an.");
                            }
                        } else Main.instance.getApi().displayMessageInChat(Main.prefix+"§cVerwendung: .autochat interval <Nachrichteninterval in Sekunden>");
                    } else Main.instance.printHelp();
                } else Main.instance.printHelp();
                return true;
            }
        }
        return false;
    }
}
