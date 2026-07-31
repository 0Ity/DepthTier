package net.tysontheember.betterharvestlevels.rules;

import net.tysontheember.betterharvestlevels.config.TierDefinition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResolvedRulesTest {
    @Test
    void jsonRoundTrip() {
        ResolvedRules rules = new ResolvedRules();
        rules.blocks.put("minecraft:stone", "iron");
        rules.items.put("minecraft:golden_pickaxe", "diamond");
        TierDefinition t = new TierDefinition();
        t.name = "copper";
        t.level = 2;
        t.iconItem = "minecraft:copper_ingot";
        rules.tiers.add(t);

        ResolvedRules back = ResolvedRules.fromJson(rules.toJson());
        assertEquals("iron", back.blocks.get("minecraft:stone"));
        assertEquals("diamond", back.items.get("minecraft:golden_pickaxe"));
        assertEquals("copper", back.tiers.get(0).name);
        assertEquals("minecraft:copper_ingot", back.tiers.get(0).iconItem);
        assertTrue(back.equivalences.isEmpty());
    }
}
