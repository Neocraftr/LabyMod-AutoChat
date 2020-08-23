package de.neocraftr.autochat.events;

import de.neocraftr.autochat.AutoChat;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TickListener {

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        if(e.phase == TickEvent.Phase.START) {
            if (getAutoChat().isActive()) {
                if(System.currentTimeMillis() > getAutoChat().getNextSendMessage()) {
                    getAutoChat().setNextSendMessage(System.currentTimeMillis() + getAutoChat().getInterval() * 1000);
                    getAutoChat().sendRandomMessage();
                }
            }
            if (getAutoChat().isActive() && !getAutoChat().getApi().isIngame()) {
                getAutoChat().setActive(false);
            }
        }
    }

    private AutoChat getAutoChat() {
        return AutoChat.getAutoChat();
    }
}
