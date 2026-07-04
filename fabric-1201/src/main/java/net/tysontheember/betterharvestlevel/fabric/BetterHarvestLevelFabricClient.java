package net.tysontheember.betterharvestlevel.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.tysontheember.betterharvestlevel.ClientRules;

public final class BetterHarvestLevelFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(BetterHarvestLevelFabric.RULES_CHANNEL,
                (client, handler, buf, responseSender) -> {
                    String json = buf.readUtf(1048576);
                    client.execute(() -> ClientRules.accept(json));
                });
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> ClientRules.clear());
    }
}
