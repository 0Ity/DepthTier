package net.tysontheember.depthtier.config;

public class TierDefinition {
    public String name;
    public int level;
    public String displayName;
    public String color;
    public boolean builtIn;
    public String iconItem;

    public TierDefinition() {}

    public TierDefinition(String name, int level, String displayName, String color, boolean builtIn) {
        this.name = name;
        this.level = level;
        this.displayName = displayName;
        this.color = color;
        this.builtIn = builtIn;
    }

    public TierDefinition(String name, int level, String displayName, String color, boolean builtIn, String iconItem) {
        this(name, level, displayName, color, builtIn);
        this.iconItem = iconItem;
    }
}
