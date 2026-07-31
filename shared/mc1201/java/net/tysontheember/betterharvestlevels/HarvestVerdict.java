package net.tysontheember.betterharvestlevels;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.tysontheember.betterharvestlevels.tier.TierRegistry;

import java.util.Optional;

public final class HarvestVerdict {
    private HarvestVerdict() {}

    public static Optional<Boolean> evaluate(ActiveRules rules, ItemStack stack, BlockState state, boolean vanillaVerdict) {
        String blockOverride = rules.blockTier(state.getBlock());
        String toolOverride = rules.itemTier(stack.getItem());
        if (blockOverride == null && toolOverride == null) return Optional.empty();
        String required = blockOverride != null ? blockOverride : VanillaProbe.requiredTier(state);
        if (TierRegistry.HAND.equals(required)) {
            return blockOverride != null ? Optional.of(true) : Optional.empty();
        }
        String tool = toolOverride != null ? toolOverride : VanillaProbe.toolTier(stack);
        if (tool == null) return Optional.empty();
        boolean ok = VanillaProbe.toolTypeOk(stack, state) && rules.tiers().canMine(tool, required);
        // ponytail: tool-only overrides can only grant; block overrides decide fully
        return Optional.of(blockOverride != null ? ok : ok || vanillaVerdict);
    }
}
