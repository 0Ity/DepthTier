package net.tysontheember.depthtier;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.tysontheember.depthtier.rules.ResolvedRules;
import net.tysontheember.depthtier.tier.TierRegistry;

import java.util.IdentityHashMap;
import java.util.Map;

public final class ActiveRules {
    private final Map<Block, String> blocks;
    private final Map<Item, String> items;
    private final TierRegistry tiers;

    private ActiveRules(Map<Block, String> blocks, Map<Item, String> items, TierRegistry tiers) {
        this.blocks = blocks;
        this.items = items;
        this.tiers = tiers;
    }

    public static ActiveRules from(ResolvedRules rules) {
        Map<Block, String> blocks = new IdentityHashMap<>();
        Map<Item, String> items = new IdentityHashMap<>();
        rules.blocks.forEach((id, tier) ->
                BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse(id)).ifPresent(b -> blocks.put(b, tier)));
        rules.items.forEach((id, tier) ->
                BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse(id)).ifPresent(i -> items.put(i, tier)));
        return new ActiveRules(blocks, items, TierRegistry.of(rules.tiers, rules.equivalences));
    }

    public String blockTier(Block block) {
        return blocks.get(block);
    }

    public String itemTier(Item item) {
        return items.get(item);
    }

    public TierRegistry tiers() {
        return tiers;
    }

    public int blockRuleCount() {
        return blocks.size();
    }

    public int itemRuleCount() {
        return items.size();
    }
}
