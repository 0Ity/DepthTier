package net.tysontheember.betterharvestlevel;

import net.tysontheember.betterharvestlevel.rules.ResolvedRules;

public final class ClientRules {
    private static volatile ActiveRules active;

    private ClientRules() {}

    public static void accept(String json) {
        active = ActiveRules.from(ResolvedRules.fromJson(json));
    }

    public static void clear() {
        active = null;
    }

    public static ActiveRules current() {
        return active;
    }
}
