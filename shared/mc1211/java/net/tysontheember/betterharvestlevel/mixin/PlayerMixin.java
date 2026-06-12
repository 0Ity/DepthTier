package net.tysontheember.betterharvestlevel.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.tysontheember.betterharvestlevel.HarvestCheck;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Tightens the drop gate for blocks BHL gates that vanilla doesn't
 * (no requiresCorrectToolForDrops), where ItemStackMixin is never consulted.
 */
@Mixin(Player.class)
public abstract class PlayerMixin {

    @Inject(method = "hasCorrectToolForDrops", at = @At("RETURN"), cancellable = true)
    private void betterharvestlevel$hasCorrectToolForDrops(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        try {
            Player player = (Player) (Object) this;
            if (cir.getReturnValueZ() && HarvestCheck.deniesHarvest(player.getMainHandItem(), state)) {
                cir.setReturnValue(false);
            }
        } catch (IllegalStateException ignored) {
            // BHLManager not initialized yet
        }
    }
}
