package net.tysontheember.betterharvestlevel.tier;

import net.tysontheember.betterharvestlevel.config.EquivalenceGroup;
import net.tysontheember.betterharvestlevel.config.TierDefinition;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TierRegistryTest {
    private static TierDefinition def(String name, int level) {
        TierDefinition t = new TierDefinition();
        t.name = name;
        t.level = level;
        return t;
    }

    private static TierRegistry registry() {
        EquivalenceGroup g = new EquivalenceGroup();
        g.name = "soft";
        g.tiers = List.of("gold", "stone");
        return TierRegistry.of(
                List.of(def("wood", 0), def("gold", 0), def("stone", 1),
                        def("iron", 2), def("diamond", 3), def("netherite", 4)),
                List.of(g));
    }

    @Test
    void levelsGateMining() {
        TierRegistry r = registry();
        assertTrue(r.canMine("iron", "stone"));
        assertTrue(r.canMine("iron", "iron"));
        assertFalse(r.canMine("stone", "iron"));
    }

    @Test
    void handIsBelowEverythingAndAlwaysKnown() {
        TierRegistry r = registry();
        assertTrue(r.knows(TierRegistry.HAND));
        assertFalse(r.canMine(TierRegistry.HAND, "wood"));
        assertTrue(r.canMine("wood", TierRegistry.HAND));
        assertTrue(r.canMine(TierRegistry.HAND, TierRegistry.HAND));
    }

    @Test
    void equivalenceBridgesLevels() {
        TierRegistry r = registry();
        assertTrue(r.canMine("gold", "stone"));
        assertFalse(r.canMine("gold", "iron"));
    }

    @Test
    void unknownTierDenies() {
        TierRegistry r = registry();
        assertFalse(r.canMine("mystery", "stone"));
        assertFalse(r.canMine("stone", "mystery"));
    }

    @Test
    void duplicatesAndHandRedefinitionIgnored() {
        TierRegistry r = TierRegistry.of(
                List.of(def("iron", 2), def("iron", 9), def(TierRegistry.HAND, 5)),
                List.of());
        assertEquals(2, r.byName("iron").level);
        assertEquals(-1, r.byName(TierRegistry.HAND).level);
    }
}
