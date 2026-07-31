package net.tysontheember.betterharvestlevels.forge.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.tysontheember.betterharvestlevels.ClientRules;
import net.tysontheember.betterharvestlevels.Constants;
import net.tysontheember.betterharvestlevels.ServerRules;

import java.util.function.Supplier;

public final class BHLNetwork {
    private static final String PROTOCOL = "1";
    private static final int MAX_JSON = 1048576;
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Constants.MOD_ID, "rules"),
            () -> PROTOCOL,
            NetworkRegistry.acceptMissingOr(PROTOCOL),
            NetworkRegistry.acceptMissingOr(PROTOCOL));

    private BHLNetwork() {}

    public static void init() {
        CHANNEL.registerMessage(0, RulesMessage.class,
                (msg, buf) -> buf.writeUtf(msg.json(), MAX_JSON),
                buf -> new RulesMessage(buf.readUtf(MAX_JSON)),
                RulesMessage::handle);
    }

    public static void sendTo(ServerPlayer player) {
        if (CHANNEL.isRemotePresent(player.connection.connection)) {
            CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new RulesMessage(ServerRules.json()));
        }
    }

    public record RulesMessage(String json) {
        void handle(Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> ClientRules.accept(json));
            ctx.get().setPacketHandled(true);
        }
    }
}
