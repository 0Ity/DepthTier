package net.tysontheember.betterharvestlevel.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.tysontheember.betterharvestlevel.ActiveRules;
import net.tysontheember.betterharvestlevel.BHLManager;
import net.tysontheember.betterharvestlevel.ServerRules;
import net.tysontheember.betterharvestlevel.VanillaProbe;

public final class BHLCommand {
    private BHLCommand() {}

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, Runnable resyncAll) {
        dispatcher.register(Commands.literal("bhl")
                .then(Commands.literal("info").executes(BHLCommand::info))
                .then(Commands.literal("check").executes(BHLCommand::check))
                .then(Commands.literal("reload")
                        .requires(source -> source.hasPermission(2))
                        .executes(context -> reload(context, resyncAll))));
    }

    private static int info(CommandContext<CommandSourceStack> context) {
        BHLManager.tiers().ordered().forEach(tier ->
                context.getSource().sendSuccess(() ->
                        Component.literal(tier.name + " (level " + tier.level + ")"), false));
        return 1;
    }

    private static int check(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        HitResult hit = player.pick(8, 0, false);
        if (!(hit instanceof BlockHitResult blockHit) || hit.getType() != HitResult.Type.BLOCK) {
            context.getSource().sendFailure(Component.literal("Look at a block"));
            return 0;
        }
        BlockState state = player.level().getBlockState(blockHit.getBlockPos());
        ItemStack held = player.getMainHandItem();
        ActiveRules rules = ServerRules.current();
        String blockOverride = rules == null ? null : rules.blockTier(state.getBlock());
        String required = blockOverride != null ? blockOverride : VanillaProbe.requiredTier(state);
        String toolOverride = rules == null ? null : rules.itemTier(held.getItem());
        String tool = toolOverride != null ? toolOverride : String.valueOf(VanillaProbe.toolTier(held));
        boolean can = player.hasCorrectToolForDrops(state);
        context.getSource().sendSuccess(() -> Component.literal(
                "block requires " + required + (blockOverride != null ? " (override)" : "")
                        + ", tool is " + tool + (toolOverride != null ? " (override)" : "")
                        + ", drops: " + (can ? "yes" : "no")), false);
        return 1;
    }

    private static int reload(CommandContext<CommandSourceStack> context, Runnable resyncAll) {
        BHLManager.reload();
        ServerRules.rebuild();
        resyncAll.run();
        context.getSource().sendSuccess(() -> Component.literal("BetterHarvestLevel reloaded"), true);
        return 1;
    }
}
