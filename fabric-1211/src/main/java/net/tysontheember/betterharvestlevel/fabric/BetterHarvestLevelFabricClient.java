package net.tysontheember.betterharvestlevel.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.tysontheember.betterharvestlevel.ClientRules;
import net.tysontheember.betterharvestlevel.network.RulesPayload;

public final class BetterHarvestLevelFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(RulesPayload.TYPE,
                (payload, context) -> context.client().execute(() -> ClientRules.accept(payload.json())));
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> ClientRules.clear());
    }
}
