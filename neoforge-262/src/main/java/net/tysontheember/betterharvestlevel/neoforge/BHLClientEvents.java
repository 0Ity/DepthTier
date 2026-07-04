package net.tysontheember.betterharvestlevel.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.tysontheember.betterharvestlevel.ClientRules;
import net.tysontheember.betterharvestlevel.Constants;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public final class BHLClientEvents {
    @SubscribeEvent
    static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        ClientRules.clear();
    }
}
