package net.tysontheember.betterharvestlevels.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.tysontheember.betterharvestlevels.BHLManager;
import net.tysontheember.betterharvestlevels.ClientRules;
import net.tysontheember.betterharvestlevels.Constants;
import net.tysontheember.betterharvestlevels.network.RulesPayload;
import net.tysontheember.betterharvestlevels.platform.Services;

@Mod(Constants.MOD_ID)
public final class BetterHarvestLevelsNeoForge {
    public BetterHarvestLevelsNeoForge(IEventBus modBus) {
        BHLManager.init(Services.PLATFORM.getConfigDir());
        modBus.addListener(BetterHarvestLevelsNeoForge::onRegisterPayloads);
    }

    private static void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
        event.registrar("1").optional().playToClient(RulesPayload.TYPE, RulesPayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() -> ClientRules.accept(payload.json())));
    }
}
