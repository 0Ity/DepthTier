package net.tysontheember.betterharvestlevels.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.tysontheember.betterharvestlevels.ClientRules;
import net.tysontheember.betterharvestlevels.network.RulesPayload;

public final class BetterHarvestLevelsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(RulesPayload.TYPE,
                (payload, context) -> context.client().execute(() -> ClientRules.accept(payload.json())));
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> ClientRules.clear());
    }
}
