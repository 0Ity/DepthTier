package net.tysontheember.betterharvestlevels.fabric.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.tysontheember.betterharvestlevels.ActiveRules;
import net.tysontheember.betterharvestlevels.HarvestVerdict;
import net.tysontheember.betterharvestlevels.RulesAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
abstract class ItemStackMixin {
    @Inject(method = "isCorrectToolForDrops(Lnet/minecraft/world/level/block/state/BlockState;)Z",
            at = @At("RETURN"), cancellable = true)
    private void bhl$applyRules(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        ActiveRules rules = RulesAccess.current();
        if (rules == null) return;
        HarvestVerdict.evaluate(rules, (ItemStack) (Object) this, state, cir.getReturnValueZ())
                .ifPresent(cir::setReturnValue);
    }
}
