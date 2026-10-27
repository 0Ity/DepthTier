package net.Ity_theember.depthtier;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.Ity_theember.depthtier.config.DTConfig;
import net.Ity_theember.depthtier.rules.ResolvedRules;
import net.Ity_theember.depthtier.tier.OverrideResolver;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ServerRules {
    private static volatile ResolvedRules resolved;
    private static volatile ActiveRules active;

    private ServerRules() {}

    public static synchronized void rebuild() {
        DTConfig config = DTManager.config();
        ResolvedRules rules = new ResolvedRules();
        rules.tiers = config.getTiersConfig().getTiers();
        rules.equivalences = config.getEquivalencesConfig().getGroups();

        Set<String> blockIds = BuiltInRegistries.BLOCK.keySet().stream()
                .map(ResourceLocation::toString).collect(Collectors.toSet());
        Set<String> itemIds = BuiltInRegistries.ITEM.keySet().stream()
                .map(ResourceLocation::toString).collect(Collectors.toSet());
        Function<String, Set<String>> blockTags = tag -> {
            ResourceLocation id = ResourceLocation.tryParse(tag);
            if (id == null) {
                Constants.LOG.error("Ignoring malformed tag id '{}'", tag);
                return Set.of();
            }
            return BuiltInRegistries.BLOCK.getTag(TagKey.create(Registries.BLOCK, id))
                    .map(named -> named.stream()
                            .map(h -> h.unwrapKey().orElseThrow().location().toString())
                            .collect(Collectors.toSet()))
                    .orElse(Set.of());
        };
        Function<String, Set<String>> itemTags = tag -> {
            ResourceLocation id = ResourceLocation.tryParse(tag);
            if (id == null) {
                Constants.LOG.error("Ignoring malformed tag id '{}'", tag);
                return Set.of();
            }
            return BuiltInRegistries.ITEM.getTag(TagKey.create(Registries.ITEM, id))
                    .map(named -> named.stream()
                            .map(h -> h.unwrapKey().orElseThrow().location().toString())
                            .collect(Collectors.toSet()))
                    .orElse(Set.of());
        };

        rules.blocks = OverrideResolver.resolveBlockOverrides(config.getBlockOverrides(), blockIds, blockTags);
        rules.items = OverrideResolver.resolveToolOverrides(config.getToolOverrides(), itemIds, itemTags);

        resolved = rules;
        active = ActiveRules.from(rules);
        Constants.LOG.info("Resolved {} block rules, {} tool rules", rules.blocks.size(), rules.items.size());
    }

    public static void ensure() {
        if (resolved == null) rebuild();
    }

    public static ActiveRules current() {
        return active;
    }

    public static String json() {
        return resolved.toJson();
    }

    public static void clear() {
        resolved = null;
        active = null;
    }
}
