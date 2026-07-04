package net.tysontheember.betterharvestlevel.compat.jade;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.tysontheember.betterharvestlevel.ActiveRules;
import net.tysontheember.betterharvestlevel.ClientRules;
import net.tysontheember.betterharvestlevel.VanillaProbe;
import net.tysontheember.betterharvestlevel.config.TierDefinition;
import net.tysontheember.betterharvestlevel.tier.TierRegistry;
import snownee.jade.addon.harvest.HarvestToolProvider;
import snownee.jade.addon.harvest.ToolHandler;
import snownee.jade.api.JadeIds;

import java.util.List;
import java.util.Map;

public final class BhlToolHandler implements ToolHandler {
    private static final Map<Identifier, TagKey<Block>> TOOL_CLASSES = Map.of(
            JadeIds.JADE("pickaxe"), BlockTags.MINEABLE_WITH_PICKAXE,
            JadeIds.JADE("axe"), BlockTags.MINEABLE_WITH_AXE,
            JadeIds.JADE("shovel"), BlockTags.MINEABLE_WITH_SHOVEL,
            JadeIds.JADE("hoe"), BlockTags.MINEABLE_WITH_HOE);

    private final ToolHandler vanilla;
    private final TagKey<Block> mineable;
    private final String toolName;
    private final Identifier uid;

    private BhlToolHandler(ToolHandler vanilla, TagKey<Block> mineable, Identifier uid) {
        this.vanilla = vanilla;
        this.mineable = mineable;
        this.toolName = uid.getPath();
        this.uid = uid;
    }

    public static void wrapAll() {
        TOOL_CLASSES.forEach((uid, tag) -> HarvestToolProvider.registerHandler(() ->
                new BhlToolHandler(HarvestToolProvider.TOOL_HANDLERS.get(uid), tag, uid)));
    }

    @Override
    public ItemStack test(BlockState state, Level level, BlockPos pos) {
        ActiveRules rules = ClientRules.current();
        String tier = rules == null ? null : rules.blockTier(state.getBlock());
        if (tier == null) return vanilla == null ? ItemStack.EMPTY : vanilla.test(state, level, pos);
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
                Identifier.fromNamespaceAndPath("minecraft", prefix + "_" + toolName)).orElse(null);
        if (item != null) return new ItemStack(item);
        TierDefinition def = rules.tiers().byName(tier);
        if (def != null && def.iconItem != null) {
            Identifier iconId = Identifier.tryParse(def.iconItem);
            if (iconId == null) return ItemStack.EMPTY;
            return BuiltInRegistries.ITEM.getOptional(iconId)
                    .map(ItemStack::new).orElse(ItemStack.EMPTY);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public List<ItemStack> getTools() {
        return vanilla == null ? List.of() : vanilla.getTools();
    }

    @Override
    public Identifier getUid() {
        return uid;
    }
}
