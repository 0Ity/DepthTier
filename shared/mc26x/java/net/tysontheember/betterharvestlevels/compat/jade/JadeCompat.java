package net.tysontheember.betterharvestlevels.compat.jade;

import net.tysontheember.betterharvestlevels.platform.Services;

public final class JadeCompat {
    private JadeCompat() {}

    public static void invalidateHarvestCache() {
        if (Services.PLATFORM.isModLoaded("jade")) {
            Impl.run();
        }
    }

    private static final class Impl {
        static void run() {
            snownee.jade.addon.harvest.HarvestToolProvider.INSTANCE.invalidateCache();
        }
    }
}
