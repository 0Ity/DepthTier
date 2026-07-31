package net.tysontheember.betterharvestlevels;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.state.BlockState;
import net.tysontheember.betterharvestlevels.tier.TierRegistry;

import java.util.List;
import java.util.Map;

public final class VanillaProbe {
    private static final Map<Tier, String> VANILLA_TIERS = Map.of(
            Tiers.WOOD, "wood", Tiers.GOLD, "gold", Tiers.STONE, "stone",
            Tiers.IRON, "iron", Tiers.DIAMOND, "diamond", Tiers.NETHERITE, "netherite");
    private static final List<TagKey<Item>> TOOL_TAGS = List.of(
            ItemTags.PICKAXES, ItemTags.AXES, ItemTags.SHOVELS, ItemTags.HOES, ItemTags.SWORDS);

    private VanillaProbe() {}

    public static String requiredTier(BlockState state) {
        if (state.is(BlockTags.NEEDS_DIAMOND_TOOL)) return "diamond";
        if (state.is(BlockTags.NEEDS_IRON_TOOL)) return "iron";
        if (state.is(BlockTags.NEEDS_STONE_TOOL)) return "stone";
        return state.requiresCorrectToolForDrops() ? "wood" : TierRegistry.HAND;
    }

    public static String toolTier(ItemStack stack) {
        if (stack.isEmpty()) return TierRegistry.HAND;
        if (stack.getItem() instanceof TieredItem tiered) {
            Tier tier = tiered.getTier();
            String known = VANILLA_TIERS.get(tier);
            if (known != null) return known;
            int level = tier.getLevel();
            if (level >= 4) return "netherite";
            return switch (level) {
                case 3 -> "diamond";
                case 2 -> "iron";
                case 1 -> "stone";
                default -> "wood";
            };
        }
        return TOOL_TAGS.stream().anyMatch(stack::is) ? null : TierRegistry.HAND;
    }

    public static boolean toolTypeOk(ItemStack stack, BlockState state) {
        if (!inAnyMineableTag(state)) return true;
        return stack.getDestroySpeed(state) > 1.0F;
    }

    public static boolean inAnyMineableTag(BlockState state) {
        return state.is(BlockTags.MINEABLE_WITH_PICKAXE) || state.is(BlockTags.MINEABLE_WITH_AXE)
                || state.is(BlockTags.MINEABLE_WITH_SHOVEL) || state.is(BlockTags.MINEABLE_WITH_HOE);
    }
}
