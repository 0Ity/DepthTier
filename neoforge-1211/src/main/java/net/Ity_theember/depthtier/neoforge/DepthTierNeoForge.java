package net.Ity_theember.depthtier.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.Ity_theember.depthtier.DTManager;
import net.Ity_theember.depthtier.ClientRules;
import net.Ity_theember.depthtier.Constants;
import net.Ity_theember.depthtier.network.RulesPayload;
import net.Ity_theember.depthtier.platform.Services;

@Mod(Constants.MOD_ID)
public final class DepthTierNeoForge {
    public DepthTierNeoForge(IEventBus modBus) {
        DTManager.init(Services.PLATFORM.getConfigDir());
        modBus.addListener(DepthTierNeoForge::onRegisterPayloads);
    }

    private static void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
        event.registrar("1").optional().playToClient(RulesPayload.TYPE, RulesPayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() -> ClientRules.accept(payload.json())));
    }
}
