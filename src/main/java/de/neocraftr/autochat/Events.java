package de.neocraftr.autochat;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Events {

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        if(e.phase == TickEvent.Phase.START) {
            if (Main.active) {
                Main.counter++;
                if (Main.counter >= Main.interval * 20) {
                    Main.counter = 0;
                    int numMessages = Main.messages.size();
                    if(numMessages == 1) {
                        Main.lastMessage = 0;
                        Main.mc.getPlayer().sendChatMessage(Main.messages.get(0));
                    } else if (numMessages > 1) {
                        int random = Main.random.nextInt(numMessages - 1);
                        System.out.print(random+"   ");
                        if (random == Main.lastMessage) {
                            random++;
                            if(random > numMessages - 1) random = 0;
                        }
                        System.out.println(random);
                        Main.lastMessage = random;
                        Main.mc.getPlayer().sendChatMessage(Main.messages.get(random));
                    }
                }
            }
            if (!Main.instance.getApi().isIngame()) {
                if (Main.active) Main.active = false;
            }
        }
    }
}
