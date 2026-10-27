package net.tysontheember.depthtier.config;

import java.util.List;

public class EquivalenceGroup {
    public String name;
    public List<String> tiers;

    public EquivalenceGroup() {}

    public EquivalenceGroup(String name, List<String> tiers) {
        this.name = name;
        this.tiers = tiers;
    }
}
