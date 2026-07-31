package net.tysontheember.betterharvestlevels.forge;

import net.minecraftforge.fml.common.Mod;
import net.tysontheember.betterharvestlevels.BHLManager;
import net.tysontheember.betterharvestlevels.Constants;
import net.tysontheember.betterharvestlevels.forge.network.BHLNetwork;
import net.tysontheember.betterharvestlevels.platform.Services;

@Mod(Constants.MOD_ID)
public final class BetterHarvestLevelsForge {
    public BetterHarvestLevelsForge() {
        BHLManager.init(Services.PLATFORM.getConfigDir());
        BHLNetwork.init();
    }
}
