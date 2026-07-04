package net.tysontheember.betterharvestlevel.neoforge;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.tysontheember.betterharvestlevel.ActiveRules;
import net.tysontheember.betterharvestlevel.Constants;
import net.tysontheember.betterharvestlevel.HarvestVerdict;
import net.tysontheember.betterharvestlevel.RulesAccess;
import net.tysontheember.betterharvestlevel.ServerRules;
import net.tysontheember.betterharvestlevel.command.BHLCommand;
import net.tysontheember.betterharvestlevel.network.RulesPayload;

import java.util.List;

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
            sendToPlayers(event.getPlayerList().getPlayers(), payload);
        } else {
            sendIfChanneled(event.getPlayer(), payload);
        }
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
            sendToPlayers(server.getPlayerList().getPlayers(), new RulesPayload(ServerRules.json()));
        });
    }

    private static void sendIfChanneled(ServerPlayer player, CustomPacketPayload payload) {
        if (player.connection.hasChannel(payload)) {
            PacketDistributor.sendToPlayer(player, payload);
        }
    }

    private static void sendToPlayers(List<ServerPlayer> players, CustomPacketPayload payload) {
        players.forEach(player -> sendIfChanneled(player, payload));
    }
}
