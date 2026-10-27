package net.tysontheember.depthtier.compat.jade;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.tysontheember.depthtier.ActiveRules;
import net.tysontheember.depthtier.ClientRules;
import net.tysontheember.depthtier.VanillaProbe;
import net.tysontheember.depthtier.config.TierDefinition;
import net.tysontheember.depthtier.tier.TierRegistry;
import snownee.jade.addon.harvest.HarvestToolProvider;
import snownee.jade.addon.harvest.ToolHandler;
import snownee.jade.api.JadeIds;

import java.util.List;
import java.util.Map;

public final class BhlToolHandler implements ToolHandler {
    private static final Map<ResourceLocation, TagKey<Block>> TOOL_CLASSES = Map.of(
            JadeIds.JADE("pickaxe"), BlockTags.MINEABLE_WITH_PICKAXE);

    private final ToolHandler vanilla;
    private final TagKey<Block> mineable;
    private final String toolName;

    private BhlToolHandler(ToolHandler vanilla, TagKey<Block> mineable, String toolName) {
        this.vanilla = vanilla;
        this.mineable = mineable;
        this.toolName = toolName;
    }

    public static void wrapAll() {
        TOOL_CLASSES.forEach((uid, tag) -> {
            ToolHandler existing = HarvestToolProvider.TOOL_HANDLERS.get(uid);
            if (existing != null) {
                HarvestToolProvider.registerHandler(new BhlToolHandler(existing, tag, uid.getPath()));
            }
        });
    }

    @Override
    public ItemStack test(BlockState state, Level level, BlockPos pos) {
        ActiveRules rules = ClientRules.current();
        String tier = rules == null ? null : rules.blockTier(state.getBlock());
        if (tier == null) return vanilla.test(state, level, pos);
        if (TierRegistry.HAND.equals(tier)) return ItemStack.EMPTY;
        boolean applicable = state.is(mineable)
                || ("pickaxe".equals(toolName) && !VanillaProbe.inAnyMineableTag(state));
        if (!applicable) return ItemStack.EMPTY;
        return icon(rules, tier);
    }

    private ItemStack icon(ActiveRules rules, String tier) {
        String prefix = switch (tier) {
            case "wood" -> "wooden";
            case "gold" -> "golden";
            default -> tier;
        };
        Item item = BuiltInRegistries.ITEM.getOptional(
                ResourceLocation.withDefaultNamespace(prefix + "_" + toolName)).orElse(null);
        if (item != null) return new ItemStack(item);
        TierDefinition def = rules.tiers().byName(tier);
        if (def != null && def.iconItem != null) {
            ResourceLocation iconId = ResourceLocation.tryParse(def.iconItem);
            if (iconId == null) return ItemStack.EMPTY;
            return BuiltInRegistries.ITEM.getOptional(iconId)
                    .map(ItemStack::new).orElse(ItemStack.EMPTY);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public List<ItemStack> getTools() {
        return vanilla.getTools();
    }

    @Override
    public ResourceLocation getUid() {
        return vanilla.getUid();
    }
}
