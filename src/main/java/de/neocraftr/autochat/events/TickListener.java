package de.neocraftr.autochat.events;

import de.neocraftr.autochat.AutoChat;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Random;

public class TickListener {

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        if(e.phase == TickEvent.Phase.END) {
            if (getAutoChat().isActive()) {
                if(!getAutoChat().getApi().isIngame() || getAutoChat().getSettings().getMessages().size() == 0) {
                    getAutoChat().setActive(false);
                    return;
                }
                if(System.currentTimeMillis() > getAutoChat().getNextSendMessage()) {
                    int additionalTime = 0;
                    if(getAutoChat().getSettings().isVaryInterval()) {
                        Random rnd = new Random();
                        float randomPercent = rnd.nextInt(20) / 100F;
                        if(rnd.nextBoolean()) randomPercent *= -1;
                        additionalTime = (int) (getAutoChat().getSettings().getInterval() * randomPercent);
                    }
                    getAutoChat().setNextSendMessage(System.currentTimeMillis() + (getAutoChat().getSettings().getInterval() + additionalTime) * 1000);

                    getAutoChat().sendRandomMessage();
                }
            }
        }
    }

    private AutoChat getAutoChat() {
        return AutoChat.getAutoChat();
    }
}
