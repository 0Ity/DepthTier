package net.tysontheember.depthtier.tier;

import net.tysontheember.depthtier.Constants;
import net.tysontheember.depthtier.config.BlockOverride;
import net.tysontheember.depthtier.config.ToolOverride;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public final class OverrideResolver {

    private OverrideResolver() {}

    public static Map<String, String> resolveBlockOverrides(
            List<BlockOverride> overrides,
            Set<String> allBlockIds,
            Function<String, Set<String>> tagResolver) {

        Map<String, String> resolved = new LinkedHashMap<>();

        for (BlockOverride override : overrides) {
            String target = override.getTarget();
            String type = override.getType();
            String tier = override.getRequiredTier();

            switch (type) {
                case "block":
                    if (allBlockIds.contains(target)) {
                        putWithConflictLog(resolved, target, tier, "block");
                    }
                    break;

                case "tag":
                    if (target.startsWith("#")) {
                        Set<String> tagMembers = tagResolver.apply(target.substring(1));
                        if (tagMembers != null) {
                            for (String id : tagMembers) {
                                putWithConflictLog(resolved, id, tier, "block");
                            }
                        }
                    }
                    break;

                case "mod":
                    String namespace = target.endsWith(":*") ? target.substring(0, target.length() - 2) : target;
                    for (String id : allBlockIds) {
                        if (id.startsWith(namespace + ":")) {
                            putWithConflictLog(resolved, id, tier, "block");
                        }
                    }
                    break;

                case "regex":
                    try {
                        Pattern pattern = Pattern.compile(target);
                        for (String id : allBlockIds) {
                            if (pattern.matcher(id).matches()) {
                                putWithConflictLog(resolved, id, tier, "block");
                            }
                        }
                    } catch (PatternSyntaxException e) {
                        Constants.LOG.error("Invalid regex pattern in block override: '{}' - {}", target, e.getMessage());
                    }
                    break;

                default:
                    Constants.LOG.warn("Unknown block override type: '{}'", type);
            }
        }

        return resolved;
    }

    public static Map<String, String> resolveToolOverrides(
            List<ToolOverride> overrides,
            Set<String> allItemIds,
            Function<String, Set<String>> tagResolver) {

        Map<String, String> resolved = new LinkedHashMap<>();

        for (ToolOverride override : overrides) {
            String target = override.getTarget();
            String type = override.getType();
            String tier = override.getTier();

            switch (type) {
                case "item":
                    if (allItemIds.contains(target)) {
                        putWithConflictLog(resolved, target, tier, "tool");
                    }
                    break;

                case "tag":
                    if (target.startsWith("#")) {
                        Set<String> tagMembers = tagResolver.apply(target.substring(1));
                        if (tagMembers != null) {
                            for (String id : tagMembers) {
                                putWithConflictLog(resolved, id, tier, "tool");
                            }
                        }
                    }
                    break;

                case "mod":
                    String namespace = target.endsWith(":*") ? target.substring(0, target.length() - 2) : target;
                    for (String id : allItemIds) {
                        if (id.startsWith(namespace + ":")) {
                            putWithConflictLog(resolved, id, tier, "tool");
                        }
                    }
                    break;

                case "regex":
                    try {
                        Pattern pattern = Pattern.compile(target);
                        for (String id : allItemIds) {
                            if (pattern.matcher(id).matches()) {
                                putWithConflictLog(resolved, id, tier, "tool");
                            }
                        }
                    } catch (PatternSyntaxException e) {
                        Constants.LOG.error("Invalid regex pattern in tool override: '{}' - {}", target, e.getMessage());
                    }
                    break;

                default:
                    Constants.LOG.warn("Unknown tool override type: '{}'", type);
            }
        }

        return resolved;
    }

    private static void putWithConflictLog(Map<String, String> resolved, String id, String newTier, String kind) {
        String existing = resolved.put(id, newTier);
        if (existing != null && !existing.equals(newTier)) {
            Constants.LOG.warn("BHL {} override conflict: '{}' changed from tier '{}' to '{}' (later override wins)",
                kind, id, existing, newTier);
        }
    }
}
