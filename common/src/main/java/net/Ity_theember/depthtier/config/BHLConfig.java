package net.Ity_theember.depthtier.config;

import java.util.ArrayList;
import java.util.List;

public class BHLConfig {
    private TiersConfig tiersConfig;
    private List<BlockOverride> blockOverrides;
    private List<ToolOverride> toolOverrides;
    private EquivalencesConfig equivalencesConfig;

    public BHLConfig() {
        this.tiersConfig = TiersConfig.createDefault();
        this.blockOverrides = new ArrayList<>();
        this.toolOverrides = new ArrayList<>();
        this.equivalencesConfig = new EquivalencesConfig();
    }

    public TiersConfig getTiersConfig() { return tiersConfig; }
    public List<BlockOverride> getBlockOverrides() { return blockOverrides; }
    public List<ToolOverride> getToolOverrides() { return toolOverrides; }
    public EquivalencesConfig getEquivalencesConfig() { return equivalencesConfig; }

    public void setTiersConfig(TiersConfig tiersConfig) { this.tiersConfig = tiersConfig; }
    public void setBlockOverrides(List<BlockOverride> blockOverrides) { this.blockOverrides = blockOverrides; }
    public void setToolOverrides(List<ToolOverride> toolOverrides) { this.toolOverrides = toolOverrides; }
    public void setEquivalencesConfig(EquivalencesConfig equivalencesConfig) { this.equivalencesConfig = equivalencesConfig; }
}
