package net.tysontheember.betterharvestlevels.fabric.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.tysontheember.betterharvestlevels.ActiveRules;
import net.tysontheember.betterharvestlevels.HarvestVerdict;
import net.tysontheember.betterharvestlevels.RulesAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
abstract class PlayerMixin {
    @Inject(method = "hasCorrectToolForDrops(Lnet/minecraft/world/level/block/state/BlockState;)Z",
            at = @At("RETURN"), cancellable = true)
    private void bhl$applyRules(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        Player self = (Player) (Object) this;
        ActiveRules rules = RulesAccess.get(self.level());
        if (rules == null) return;
        HarvestVerdict.evaluate(rules, self.getMainHandItem(), state, cir.getReturnValueZ())
                .ifPresent(cir::setReturnValue);
    }
}
