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
                    Main.instance.sendRandomMessage();
                }
            }
            if (!Main.instance.getApi().isIngame()) {
                if (Main.active) Main.active = false;
            }
        }
    }
}
