package net.tysontheember.betterharvestlevel.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.tysontheember.betterharvestlevel.BHLManager;
import net.tysontheember.betterharvestlevel.ClientRules;
import net.tysontheember.betterharvestlevel.Constants;
import net.tysontheember.betterharvestlevel.network.RulesPayload;
import net.tysontheember.betterharvestlevel.platform.Services;

@Mod(Constants.MOD_ID)
public final class BetterHarvestLevelNeoForge {
    public BetterHarvestLevelNeoForge(IEventBus modBus) {
        BHLManager.init(Services.PLATFORM.getConfigDir());
        modBus.addListener(BetterHarvestLevelNeoForge::onRegisterPayloads);
    }

    private static void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
        event.registrar("1").optional().playToClient(RulesPayload.TYPE, RulesPayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() -> ClientRules.accept(payload.json())));
    }
}
