package net.tysontheember.depthtier.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.tysontheember.depthtier.BHLManager;
import net.tysontheember.depthtier.ClientRules;
import net.tysontheember.depthtier.Constants;
import net.tysontheember.depthtier.network.RulesPayload;
import net.tysontheember.depthtier.platform.Services;

@Mod(Constants.MOD_ID)
public final class DepthTierNeoForge {
    public DepthTierNeoForge(IEventBus modBus) {
        BHLManager.init(Services.PLATFORM.getConfigDir());
        modBus.addListener(DepthTierNeoForge::onRegisterPayloads);
    }

    private static void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
        event.registrar("1").optional().playToClient(RulesPayload.TYPE, RulesPayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() -> ClientRules.accept(payload.json())));
    }
}
