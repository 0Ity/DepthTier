package net.tysontheember.betterharvestlevel.neoforge;

import net.neoforged.fml.common.Mod;
import net.tysontheember.betterharvestlevel.BHLManager;
import net.tysontheember.betterharvestlevel.Constants;
import net.tysontheember.betterharvestlevel.platform.Services;

@Mod(Constants.MOD_ID)
public final class BetterHarvestLevelNeoForge {
    public BetterHarvestLevelNeoForge() {
        BHLManager.init(Services.PLATFORM.getConfigDir());
    }
}
