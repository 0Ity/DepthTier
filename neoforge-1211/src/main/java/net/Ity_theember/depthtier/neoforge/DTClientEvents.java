package net.Ity_theember.depthtier.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.Ity_theember.depthtier.ClientRules;
import net.Ity_theember.depthtier.Constants;
import net.Ity_theember.depthtier.TierTooltip;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public final class DTClientEvents {
    @SubscribeEvent
    static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        ClientRules.clear();
    }

    @SubscribeEvent
    static void onItemTooltip(ItemTooltipEvent event) {
        TierTooltip.append(event.getItemStack(), event.getToolTip());
    }
}
