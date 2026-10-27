package net.Ity_theember.depthtier.network;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.Ity_theember.depthtier.Constants;

public record RulesPayload(String json) implements CustomPacketPayload {
    public static final Type<RulesPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "rules"));
    public static final StreamCodec<io.netty.buffer.ByteBuf, RulesPayload> STREAM_CODEC =
            ByteBufCodecs.stringUtf8(1048576).map(RulesPayload::new, RulesPayload::json);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
