package net.tysontheember.betterharvestlevel.neoforge;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.tysontheember.betterharvestlevel.ActiveRules;
import net.tysontheember.betterharvestlevel.Constants;
import net.tysontheember.betterharvestlevel.HarvestVerdict;
import net.tysontheember.betterharvestlevel.RulesAccess;
import net.tysontheember.betterharvestlevel.ServerRules;
import net.tysontheember.betterharvestlevel.command.BHLCommand;
import net.tysontheember.betterharvestlevel.network.RulesPayload;

@EventBusSubscriber(modid = Constants.MOD_ID)
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

        RulesPayload payload = new RulesPayload(ServerRules.json());
        if (event.getPlayer() == null) {
            PacketDistributor.sendToAllPlayers(payload);
        } else {
            PacketDistributor.sendToPlayer(event.getPlayer(), payload);
        }
    }

    @SubscribeEvent
    static void onServerStopped(ServerStoppedEvent event) {
        ServerRules.clear();
    }

    @SubscribeEvent
    static void onRegisterCommands(RegisterCommandsEvent event) {
        BHLCommand.register(event.getDispatcher(),
                () -> PacketDistributor.sendToAllPlayers(new RulesPayload(ServerRules.json())));
    }
}
