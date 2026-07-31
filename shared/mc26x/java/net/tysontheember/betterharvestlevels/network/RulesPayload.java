package net.tysontheember.betterharvestlevels.network;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.tysontheember.betterharvestlevels.Constants;

public record RulesPayload(String json) implements CustomPacketPayload {
    public static final Type<RulesPayload> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "rules"));
    public static final StreamCodec<io.netty.buffer.ByteBuf, RulesPayload> STREAM_CODEC =
            ByteBufCodecs.stringUtf8(1048576).map(RulesPayload::new, RulesPayload::json);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
