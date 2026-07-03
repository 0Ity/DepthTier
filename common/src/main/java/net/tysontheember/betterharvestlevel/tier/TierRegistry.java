package net.tysontheember.betterharvestlevel.tier;

import net.tysontheember.betterharvestlevel.Constants;
import net.tysontheember.betterharvestlevel.config.EquivalenceGroup;
import net.tysontheember.betterharvestlevel.config.TierDefinition;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class TierRegistry {
    public static final String HAND = "hand";

    private final Map<String, TierDefinition> byName = new LinkedHashMap<>();
    private final Map<String, Set<String>> equivalents = new HashMap<>();
    private final Set<String> warned = ConcurrentHashMap.newKeySet();

    private TierRegistry() {}

    public static TierRegistry of(List<TierDefinition> tiers, List<EquivalenceGroup> groups) {
        TierRegistry r = new TierRegistry();
        TierDefinition hand = new TierDefinition();
        hand.name = HAND;
        hand.level = -1;
        hand.builtIn = true;
        r.byName.put(HAND, hand);
        for (TierDefinition t : tiers) {
            if (t.name == null || t.name.isBlank() || HAND.equals(t.name)) {
                Constants.LOG.error("Ignoring invalid or reserved tier definition '{}'", t.name);
                continue;
            }
            if (r.byName.putIfAbsent(t.name, t) != null) {
                Constants.LOG.error("Ignoring duplicate tier '{}'", t.name);
            }
        }
        for (EquivalenceGroup g : groups) {
            List<String> names = g.tiers == null ? List.of() : g.tiers;
            for (String name : names) {
                if (!r.byName.containsKey(name)) {
                    Constants.LOG.error("Equivalence group '{}' references unknown tier '{}'", g.name, name);
                    continue;
                }
                for (String other : names) {
                    if (!other.equals(name) && r.byName.containsKey(other)) {
                        r.equivalents.computeIfAbsent(name, k -> new HashSet<>()).add(other);
                    }
                }
            }
        }
        return r;
    }

    public boolean knows(String name) {
        return byName.containsKey(name);
    }

    public TierDefinition byName(String name) {
        return byName.get(name);
    }

    public List<TierDefinition> ordered() {
        List<TierDefinition> list = new ArrayList<>(byName.values());
        list.sort(Comparator.comparingInt(t -> t.level));
        return list;
    }

    public boolean canMine(String toolTier, String requiredTier) {
        TierDefinition tool = byName.get(toolTier);
        TierDefinition required = byName.get(requiredTier);
        if (tool == null || required == null) {
            String missing = tool == null ? toolTier : requiredTier;
            if (warned.add(missing)) {
                Constants.LOG.warn("Unknown tier '{}' in harvest comparison, denying", missing);
            }
            return false;
        }
        if (tool.level >= required.level) {
            return true;
        }
        return equivalents.getOrDefault(toolTier, Set.of()).contains(requiredTier);
    }
}
