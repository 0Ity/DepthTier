package net.Ity_theember.depthtier;

import net.Ity_theember.depthtier.config.DTConfig;
import net.Ity_theember.depthtier.config.ConfigIO;
import net.Ity_theember.depthtier.tier.TierRegistry;

import java.nio.file.Path;

public final class DTManager {
    private static Path configDir;
    private static DTConfig config;
    private static TierRegistry tiers;

    private DTManager() {}

    public static synchronized void init(Path dir) {
        configDir = dir;
        reload();
    }

    public static synchronized void reload() {
        config = ConfigIO.load(configDir);
        tiers = TierRegistry.of(config.getTiersConfig().getTiers(), config.getEquivalencesConfig().getGroups());
        config.getBlockOverrides().removeIf(o -> {
            if (!tiers.knows(o.getRequiredTier())) {
                Constants.LOG.error("Dropping block override '{}': unknown tier '{}'", o.getTarget(), o.getRequiredTier());
                return true;
            }
            return false;
        });
        config.getToolOverrides().removeIf(o -> {
            if (!tiers.knows(o.getTier())) {
                Constants.LOG.error("Dropping tool override '{}': unknown tier '{}'", o.getTarget(), o.getTier());
                return true;
            }
            return false;
        });
        Constants.LOG.info("Loaded {} tiers, {} block overrides, {} tool overrides",
                tiers.ordered().size(), config.getBlockOverrides().size(), config.getToolOverrides().size());
    }

    public static DTConfig config() {
        return config;
    }

    public static TierRegistry tiers() {
        return tiers;
    }
}
