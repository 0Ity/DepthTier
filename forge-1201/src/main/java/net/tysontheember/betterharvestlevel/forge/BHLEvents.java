package net.tysontheember.betterharvestlevel.forge;

import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.minecraft.server.MinecraftServer;
import net.tysontheember.betterharvestlevel.ActiveRules;
import net.tysontheember.betterharvestlevel.Constants;
import net.tysontheember.betterharvestlevel.HarvestVerdict;
import net.tysontheember.betterharvestlevel.RulesAccess;
import net.tysontheember.betterharvestlevel.ServerRules;
import net.tysontheember.betterharvestlevel.command.BHLCommand;
import net.tysontheember.betterharvestlevel.forge.network.BHLNetwork;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public final class BHLEvents {
    @SubscribeEvent
    static void onHarvestCheck(PlayerEvent.HarvestCheck event) {
        ActiveRules rules = RulesAccess.get(event.getEntity().level());
        if (rules == null) return;
        HarvestVerdict.evaluate(rules, event.getEntity().getMainHandItem(), event.getTargetBlock(), event.canHarvest())
                .ifPresent(event::setCanHarvest);
    }

    @SubscribeEvent
    static void onDatapackSync(OnDatapackSyncEvent event) {
        if (event.getPlayer() == null) {
            ServerRules.rebuild();
        } else {
            ServerRules.ensure();
        }
        event.getPlayers().forEach(BHLNetwork::sendTo);
    }

    @SubscribeEvent
    static void onServerStopped(ServerStoppedEvent event) {
        ServerRules.clear();
    }

    @SubscribeEvent
    static void onRegisterCommands(RegisterCommandsEvent event) {
        BHLCommand.register(event.getDispatcher(), () -> {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server == null) return;
            server.getPlayerList().getPlayers().forEach(BHLNetwork::sendTo);
        });
    }
}
