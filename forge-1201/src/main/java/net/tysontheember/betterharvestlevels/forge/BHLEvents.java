package net.tysontheember.betterharvestlevels.forge;

import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.minecraft.server.MinecraftServer;
import net.tysontheember.betterharvestlevels.ActiveRules;
import net.tysontheember.betterharvestlevels.Constants;
import net.tysontheember.betterharvestlevels.HarvestVerdict;
import net.tysontheember.betterharvestlevels.RulesAccess;
import net.tysontheember.betterharvestlevels.ServerRules;
import net.tysontheember.betterharvestlevels.command.BHLCommand;
import net.tysontheember.betterharvestlevels.forge.network.BHLNetwork;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public final class BHLEvents {
    @SubscribeEvent
    public static void onHarvestCheck(PlayerEvent.HarvestCheck event) {
        ActiveRules rules = RulesAccess.get(event.getEntity().level());
        if (rules == null) return;
        HarvestVerdict.evaluate(rules, event.getEntity().getMainHandItem(), event.getTargetBlock(), event.canHarvest())
                .ifPresent(event::setCanHarvest);
    }

    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        if (event.getPlayer() == null) {
            ServerRules.rebuild();
        } else {
            ServerRules.ensure();
        }
        event.getPlayers().forEach(BHLNetwork::sendTo);
    }

    @SubscribeEvent
    public static void onServerStopped(ServerStoppedEvent event) {
        ServerRules.clear();
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        BHLCommand.register(event.getDispatcher(), () -> {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server == null) return;
            server.getPlayerList().getPlayers().forEach(BHLNetwork::sendTo);
        });
    }
}
