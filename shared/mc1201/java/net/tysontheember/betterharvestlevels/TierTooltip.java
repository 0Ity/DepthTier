package net.tysontheember.betterharvestlevels;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.ItemStack;
import net.tysontheember.betterharvestlevels.config.ClientSettings;
import net.tysontheember.betterharvestlevels.config.TierDefinition;
import net.tysontheember.betterharvestlevels.tier.TierRegistry;

import java.util.List;

public final class TierTooltip {
    private TierTooltip() {}

    public static void append(ItemStack stack, List<Component> lines) {
        if (!ClientSettings.toolTierTooltip()) return;
        ActiveRules rules = ClientRules.current();
        if (rules == null) return;
        String tier = rules.itemTier(stack.getItem());
        if (tier == null) tier = VanillaProbe.toolTier(stack);
        if (tier == null || TierRegistry.HAND.equals(tier)) return;
        TierDefinition def = rules.tiers().byName(tier);
        String label = def != null && def.displayName != null && !def.displayName.isBlank()
                ? def.displayName
                : Character.toUpperCase(tier.charAt(0)) + tier.substring(1);
        MutableComponent name = Component.literal(label);
        if (def != null && def.color != null) {
            TextColor color = TextColor.parseColor(def.color);
            if (color != null) name.withStyle(style -> style.withColor(color));
        }
        lines.add(Component.translatable("tooltip.betterharvestlevels.tier", name)
                .withStyle(ChatFormatting.GRAY));
    }
}
