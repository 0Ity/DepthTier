package net.Ity_theember.depthtier;

import net.Ity_theember.depthtier.compat.jade.JadeCompat;
import net.Ity_theember.depthtier.config.ClientSettings;
import net.Ity_theember.depthtier.rules.ResolvedRules;

public final class ClientRules {
    private static volatile ActiveRules active;

    private ClientRules() {}

    public static void accept(String json) {
        ClientSettings.invalidate();
        active = ActiveRules.from(ResolvedRules.fromJson(json));
        JadeCompat.invalidateHarvestCache();
    }

    public static void clear() {
        active = null;
    }

    public static ActiveRules current() {
        return active;
    }
}
