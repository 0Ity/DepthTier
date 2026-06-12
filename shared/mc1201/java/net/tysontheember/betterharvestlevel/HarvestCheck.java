package net.tysontheember.betterharvestlevel;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.block.state.BlockState;
import net.tysontheember.betterharvestlevel.platform.Services;

/**
 * Tier gating shared by PlayerMixin and ItemStackMixin. BHL only ever tightens
 * vanilla's verdict: the tool-type check (mineable/* tags) stays with vanilla,
 * and items without a resolvable tier are deferred to vanilla, never denied.
 */
public final class HarvestCheck {

    private HarvestCheck() {}

    public static boolean deniesHarvest(ItemStack stack, BlockState state) {
        String requiredTier = getRequiredTier(state);
        if (requiredTier == null) return false;

        String toolTier = getToolTier(stack);
        if (toolTier == null) return false;

        return !BHLManager.getInstance().getTierRegistry().canMine(toolTier, requiredTier);
    }

    public static String getRequiredTier(BlockState state) {
        String blockId = BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString();
        String requiredTier = Services.TIER_SERVICE.getBlockRequiredTier(blockId);
        if (requiredTier != null) return requiredTier;

        if (state.is(BlockTags.NEEDS_DIAMOND_TOOL)) return "diamond";
        if (state.is(BlockTags.NEEDS_IRON_TOOL)) return "iron";
        if (state.is(BlockTags.NEEDS_STONE_TOOL)) return "stone";
        return null;
    }

    public static String getToolTier(ItemStack stack) {
        if (stack.isEmpty()) return null;

        String tierName = Services.TIER_SERVICE.getToolTier(
            BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
        if (tierName != null) return tierName;

        if (stack.getItem() instanceof TieredItem tieredItem) {
            tierName = VanillaTierMapper.getTierName(tieredItem.getTier());
            if (tierName == null) {
                tierName = VanillaTierMapper.getTierNameByVanillaLevel(
                    tieredItem.getTier().getLevel());
            }
            return tierName;
        }

        return null;
    }
}
