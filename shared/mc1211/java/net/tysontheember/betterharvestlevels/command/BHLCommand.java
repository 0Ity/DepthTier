package net.tysontheember.betterharvestlevels.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.tysontheember.betterharvestlevels.ActiveRules;
import net.tysontheember.betterharvestlevels.BHLManager;
import net.tysontheember.betterharvestlevels.HarvestVerdict;
import net.tysontheember.betterharvestlevels.ServerRules;
import net.tysontheember.betterharvestlevels.VanillaProbe;
import net.tysontheember.betterharvestlevels.config.TierDefinition;
import net.tysontheember.betterharvestlevels.tier.TierRegistry;

public final class BHLCommand {
    private BHLCommand() {}

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, Runnable resyncAll) {
        dispatcher.register(Commands.literal("bhl")
                .then(Commands.literal("info").executes(BHLCommand::info))
                .then(Commands.literal("check")
                        .executes(BHLCommand::check)
                        .then(Commands.literal("hand").executes(BHLCommand::checkHand))
                        .then(Commands.literal("block").executes(BHLCommand::checkBlock)))
                .then(Commands.literal("reload")
                        .requires(source -> source.hasPermission(2))
                        .executes(context -> reload(context, resyncAll))));
    }

    private static int info(CommandContext<CommandSourceStack> context) {
        BHLManager.tiers().ordered().forEach(tier -> {
            Component line = Component.empty()
                    .append(tierName(tier.name))
                    .append(Component.literal(" (level " + tier.level + ")").withStyle(ChatFormatting.GRAY));
            context.getSource().sendSuccess(() -> line, false);
        });
        return 1;
    }

    private static int check(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        BlockState state = targetBlock(player);
        if (state == null) {
            context.getSource().sendFailure(Component.literal("Look at a block"));
            return 0;
        }
        ItemStack held = player.getMainHandItem();
        ActiveRules rules = ServerRules.current();
        boolean vanillaCan = player.hasCorrectToolForDrops(state);
        boolean can = rules == null ? vanillaCan
                : HarvestVerdict.evaluate(rules, held, state, vanillaCan).orElse(vanillaCan);

        MutableComponent message = describeBlock(state, rules, false)
                .append(Component.literal(", holding ").withStyle(ChatFormatting.GRAY))
                .append(describeHand(held, rules, false))
                .append(Component.literal(", drops: ").withStyle(ChatFormatting.GRAY))
                .append(Component.literal(can ? "yes" : "no").withStyle(can ? ChatFormatting.GREEN : ChatFormatting.RED));

        context.getSource().sendSuccess(() -> message, false);
        return 1;
    }

    private static int checkBlock(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        BlockState state = targetBlock(player);
        if (state == null) {
            context.getSource().sendFailure(Component.literal("Look at a block"));
            return 0;
        }
        MutableComponent message = describeBlock(state, ServerRules.current(), true);
        context.getSource().sendSuccess(() -> message, false);
        return 1;
    }

    private static int checkHand(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        MutableComponent message = Component.empty()
                .append(Component.literal("Holding ").withStyle(ChatFormatting.GRAY))
                .append(describeHand(player.getMainHandItem(), ServerRules.current(), true));
        context.getSource().sendSuccess(() -> message, false);
        return 1;
    }

    private static BlockState targetBlock(ServerPlayer player) {
        HitResult hit = player.pick(8, 0, false);
        if (!(hit instanceof BlockHitResult blockHit) || hit.getType() != HitResult.Type.BLOCK) {
            return null;
        }
        return player.level().getBlockState(blockHit.getBlockPos());
    }

    private static MutableComponent describeBlock(BlockState state, ActiveRules rules, boolean withId) {
        String blockOverride = rules == null ? null : rules.blockTier(state.getBlock());
        String required = blockOverride != null ? blockOverride : VanillaProbe.requiredTier(state);
        MutableComponent seg = Component.empty()
                .append(state.getBlock().getName().withStyle(ChatFormatting.AQUA));
        if (withId) {
            seg.append(Component.literal(" (" + BuiltInRegistries.BLOCK.getKey(state.getBlock()) + ")")
                    .withStyle(ChatFormatting.DARK_GRAY));
        }
        if (TierRegistry.HAND.equals(required)) {
            seg.append(Component.literal(" can be mined by hand").withStyle(ChatFormatting.GRAY));
        } else {
            seg.append(Component.literal(" requires ").withStyle(ChatFormatting.GRAY));
            seg.append(tierName(required));
            seg.append(Component.literal(" " + toolWord(state)));
        }
        if (blockOverride != null) {
            seg.append(Component.literal(" (override)").withStyle(ChatFormatting.GRAY));
        }
        return seg;
    }

    private static MutableComponent describeHand(ItemStack held, ActiveRules rules, boolean withId) {
        if (held.isEmpty()) {
            MutableComponent seg = Component.literal("Bare Hand");
            if (withId) {
                seg.append(Component.literal(" (tier ").withStyle(ChatFormatting.GRAY))
                        .append(tierName(TierRegistry.HAND))
                        .append(Component.literal(")").withStyle(ChatFormatting.GRAY));
            }
            return seg;
        }
        String toolOverride = rules == null ? null : rules.itemTier(held.getItem());
        String tool = toolOverride != null ? toolOverride : VanillaProbe.toolTier(held);
        MutableComponent seg = Component.empty().append(held.getHoverName());
        if (withId) {
            seg.append(Component.literal(" (" + BuiltInRegistries.ITEM.getKey(held.getItem()) + ")")
                    .withStyle(ChatFormatting.DARK_GRAY));
        }
        seg.append(Component.literal(" (tier ").withStyle(ChatFormatting.GRAY));
        seg.append(tool != null ? tierName(tool) : Component.literal("Unknown").withStyle(ChatFormatting.GRAY));
        if (toolOverride != null) {
            seg.append(Component.literal(", override").withStyle(ChatFormatting.GRAY));
        }
        seg.append(Component.literal(")").withStyle(ChatFormatting.GRAY));
        return seg;
    }

    private static int reload(CommandContext<CommandSourceStack> context, Runnable resyncAll) {
        BHLManager.reload();
        ServerRules.rebuild();
        resyncAll.run();
        ActiveRules rules = ServerRules.current();
        int tierCount = BHLManager.tiers().ordered().size();
        int blockRuleCount = rules == null ? 0 : rules.blockRuleCount();
        int itemRuleCount = rules == null ? 0 : rules.itemRuleCount();
        context.getSource().sendSuccess(() -> Component.literal("Better Harvest Levels reloaded ("
                + tierCount + " tiers, " + blockRuleCount + " block rules, " + itemRuleCount + " tool rules)"), true);
        return 1;
    }

    private static String toolWord(BlockState state) {
        if (state.is(BlockTags.MINEABLE_WITH_PICKAXE)) return "Pickaxe";
        if (state.is(BlockTags.MINEABLE_WITH_AXE)) return "Axe";
        if (state.is(BlockTags.MINEABLE_WITH_SHOVEL)) return "Shovel";
        if (state.is(BlockTags.MINEABLE_WITH_HOE)) return "Hoe";
        return "Tool";
    }

    private static Component tierName(String tier) {
        TierDefinition def = BHLManager.tiers().byName(tier);
        String label = def != null && def.displayName != null && !def.displayName.isBlank()
                ? def.displayName
                : Character.toUpperCase(tier.charAt(0)) + tier.substring(1);
        MutableComponent text = Component.literal(label);
        if (def != null && def.color != null) {
            TextColor color = TextColor.parseColor(def.color).result().orElse(null);
            if (color != null) text.withStyle(style -> style.withColor(color));
        }
        return text;
    }
}
