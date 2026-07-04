package net.tysontheember.betterharvestlevel.forge;

import net.minecraftforge.fml.common.Mod;
import net.tysontheember.betterharvestlevel.BHLManager;
import net.tysontheember.betterharvestlevel.Constants;
import net.tysontheember.betterharvestlevel.forge.network.BHLNetwork;
import net.tysontheember.betterharvestlevel.platform.Services;

@Mod(Constants.MOD_ID)
public final class BetterHarvestLevelForge {
    public BetterHarvestLevelForge() {
        BHLManager.init(Services.PLATFORM.getConfigDir());
        BHLNetwork.init();
    }
}
