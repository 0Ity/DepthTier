package net.tysontheember.betterharvestlevel.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.tysontheember.betterharvestlevel.HarvestCheck;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Tightens ItemStack-level mining checks with BHL's tier system. Vanilla's
 * tool-type verdict is preserved; BHL can only deny drops, never grant them.
 */
@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Inject(method = "getDestroySpeed", at = @At("RETURN"), cancellable = true)
    private void betterharvestlevel$getDestroySpeed(BlockState state, CallbackInfoReturnable<Float> cir) {
        try {
            if (HarvestCheck.deniesHarvest((ItemStack) (Object) this, state)) {
                cir.setReturnValue(cir.getReturnValue() * 0.1f);
            }
        } catch (IllegalStateException ignored) {
            // BHLManager not initialized yet
        }
    }

    @Inject(method = "isCorrectToolForDrops", at = @At("RETURN"), cancellable = true)
    private void betterharvestlevel$isCorrectToolForDrops(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        try {
            if (cir.getReturnValueZ() && HarvestCheck.deniesHarvest((ItemStack) (Object) this, state)) {
                cir.setReturnValue(false);
            }
        } catch (IllegalStateException ignored) {
            // BHLManager not initialized yet
        }
    }
}
