package net.tysontheember.depthtier.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.tysontheember.depthtier.ClientRules;
import net.tysontheember.depthtier.Constants;
import net.tysontheember.depthtier.TierTooltip;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public final class BHLClientEvents {
    @SubscribeEvent
    static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        ClientRules.clear();
    }

    @SubscribeEvent
    static void onItemTooltip(ItemTooltipEvent event) {
        TierTooltip.append(event.getItemStack(), event.getToolTip());
    }
}
