package net.tysontheember.betterharvestlevels;

import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.tysontheember.betterharvestlevels.tier.TierRegistry;

import java.util.Map;

public final class VanillaProbe {
    private static final Map<Tier, String> VANILLA_TIERS = Map.of(
            Tiers.WOOD, "wood", Tiers.GOLD, "gold", Tiers.STONE, "stone",
            Tiers.IRON, "iron", Tiers.DIAMOND, "diamond", Tiers.NETHERITE, "netherite");
    private static final Map<TagKey<Block>, String> BY_INCORRECT_TAG = Map.of(
            BlockTags.INCORRECT_FOR_WOODEN_TOOL, "wood",
            BlockTags.INCORRECT_FOR_GOLD_TOOL, "gold",
            BlockTags.INCORRECT_FOR_STONE_TOOL, "stone",
            BlockTags.INCORRECT_FOR_IRON_TOOL, "iron",
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL, "diamond",
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL, "netherite");

    private VanillaProbe() {}

    public static String requiredTier(BlockState state) {
        if (state.is(BlockTags.INCORRECT_FOR_DIAMOND_TOOL)) return "netherite";
        if (state.is(BlockTags.INCORRECT_FOR_IRON_TOOL)) return "diamond";
        if (state.is(BlockTags.INCORRECT_FOR_STONE_TOOL)) return "iron";
        if (state.is(BlockTags.INCORRECT_FOR_WOODEN_TOOL)) return "stone";
        return state.requiresCorrectToolForDrops() ? "wood" : TierRegistry.HAND;
    }

    public static String toolTier(ItemStack stack) {
        if (stack.isEmpty()) return TierRegistry.HAND;
        if (stack.getItem() instanceof TieredItem tiered) {
            Tier tier = tiered.getTier();
            String known = VANILLA_TIERS.get(tier);
            if (known != null) return known;
            return BY_INCORRECT_TAG.get(tier.getIncorrectBlocksForDrops());
        }
        return stack.has(DataComponents.TOOL) ? null : TierRegistry.HAND;
    }

    public static String tierFloor(ItemStack stack) {
        Tool tool = stack.get(DataComponents.TOOL);
        if (tool == null) return null;
        if (!denied(tool, Blocks.OBSIDIAN)) return "diamond";
        if (!denied(tool, Blocks.DIAMOND_ORE)) return "iron";
        if (!denied(tool, Blocks.IRON_ORE)) return "stone";
        return "wood";
    }

    private static boolean denied(Tool tool, Block block) {
        BlockState state = block.defaultBlockState();
        for (Tool.Rule rule : tool.rules()) {
            if (rule.correctForDrops().isEmpty() || rule.correctForDrops().get()) continue;
            if (state.is(rule.blocks())) return true;
        }
        return false;
    }

    public static boolean toolTypeOk(ItemStack stack, BlockState state) {
        if (!inAnyMineableTag(state)) return true;
        Tool tool = stack.get(DataComponents.TOOL);
        if (tool == null) return false;
        for (Tool.Rule rule : tool.rules()) {
            if (rule.correctForDrops().orElse(false) && state.is(rule.blocks())) return true;
        }
        return false;
    }

    public static boolean inAnyMineableTag(BlockState state) {
        return state.is(BlockTags.MINEABLE_WITH_PICKAXE) || state.is(BlockTags.MINEABLE_WITH_AXE)
                || state.is(BlockTags.MINEABLE_WITH_SHOVEL) || state.is(BlockTags.MINEABLE_WITH_HOE);
    }
}
