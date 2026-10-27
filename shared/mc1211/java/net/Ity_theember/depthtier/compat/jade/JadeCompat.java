package net.Ity_theember.depthtier.compat.jade;

import net.Ity_theember.depthtier.platform.Services;

public final class JadeCompat {
    private JadeCompat() {}

    public static void invalidateHarvestCache() {
        if (Services.PLATFORM.isModLoaded("jade")) {
            Impl.run();
        }
    }

    private static final class Impl {
        static void run() {
            snownee.jade.addon.harvest.HarvestToolProvider.INSTANCE.resultCache.invalidateAll();
        }
    }
}
