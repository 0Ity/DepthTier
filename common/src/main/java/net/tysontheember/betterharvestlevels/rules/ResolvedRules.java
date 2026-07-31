package net.tysontheember.betterharvestlevels.rules;

import com.google.gson.Gson;
import net.tysontheember.betterharvestlevels.config.EquivalenceGroup;
import net.tysontheember.betterharvestlevels.config.TierDefinition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ResolvedRules {
    private static final Gson GSON = new Gson();

    public Map<String, String> blocks = new HashMap<>();
    public Map<String, String> items = new HashMap<>();
    public List<TierDefinition> tiers = new ArrayList<>();
    public List<EquivalenceGroup> equivalences = new ArrayList<>();

    public String toJson() {
        return GSON.toJson(this);
    }

    public static ResolvedRules fromJson(String json) {
        return GSON.fromJson(json, ResolvedRules.class);
    }
}
