package net.tysontheember.depthtier;

import net.minecraft.world.level.Level;

public final class RulesAccess {
    private RulesAccess() {}

    public static ActiveRules get(Level level) {
        return level.isClientSide ? ClientRules.current() : ServerRules.current();
    }

    public static ActiveRules current() {
        ActiveRules server = ServerRules.current();
        return server != null ? server : ClientRules.current();
    }
}
