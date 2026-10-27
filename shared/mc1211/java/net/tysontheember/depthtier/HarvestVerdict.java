package net.tysontheember.depthtier;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.block.state.BlockState;
import net.tysontheember.depthtier.tier.TierRegistry;

import java.util.Optional;

public final class HarvestVerdict {
    private static final TagKey<Item> PICKAXES = TagKey.create(Registries.ITEM,
            ResourceLocation.withDefaultNamespace("pickaxes"));

    private HarvestVerdict() {}

    public static Optional<Boolean> evaluate(ActiveRules rules, ItemStack stack, BlockState state, boolean vanillaVerdict) {
        if (!stack.isEmpty() && !isPickaxeType(stack)) return Optional.empty();
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
        return Optional.of(ok);
    }

    private static boolean isPickaxeType(ItemStack stack) {
        return stack.is(PICKAXES) || stack.getItem() instanceof PickaxeItem;
    }
}
