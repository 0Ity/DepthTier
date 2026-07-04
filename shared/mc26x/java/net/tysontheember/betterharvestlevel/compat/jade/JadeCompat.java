package net.tysontheember.betterharvestlevel.compat.jade;

import net.tysontheember.betterharvestlevel.platform.Services;

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
