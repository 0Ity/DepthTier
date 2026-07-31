package net.tysontheember.betterharvestlevels.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.tysontheember.betterharvestlevels.BHLManager;
import net.tysontheember.betterharvestlevels.ServerRules;
import net.tysontheember.betterharvestlevels.command.BHLCommand;
import net.tysontheember.betterharvestlevels.network.RulesPayload;
import net.tysontheember.betterharvestlevels.platform.Services;

public final class BetterHarvestLevelsFabric implements ModInitializer {
    private static MinecraftServer server;

    @Override
    public void onInitialize() {
        BHLManager.init(Services.PLATFORM.getConfigDir());
        PayloadTypeRegistry.playS2C().register(RulesPayload.TYPE, RulesPayload.STREAM_CODEC);
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                BHLCommand.register(dispatcher, BetterHarvestLevelsFabric::resyncAll));
        ServerLifecycleEvents.SERVER_STARTING.register(s -> server = s);
        ServerLifecycleEvents.SERVER_STOPPED.register(s -> {
            server = null;
            ServerRules.clear();
        });
        ServerPlayConnectionEvents.JOIN.register((handler, sender, s) -> {
            ServerRules.ensure();
            sendTo(handler.player);
        });
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((s, resources, success) -> {
            ServerRules.rebuild();
            s.getPlayerList().getPlayers().forEach(BetterHarvestLevelsFabric::sendTo);
        });
    }

    private static void resyncAll() {
        if (server == null) return;
        server.getPlayerList().getPlayers().forEach(BetterHarvestLevelsFabric::sendTo);
    }

    private static void sendTo(ServerPlayer player) {
        if (ServerPlayNetworking.canSend(player, RulesPayload.TYPE)) {
            ServerPlayNetworking.send(player, new RulesPayload(ServerRules.json()));
        }
    }
}
