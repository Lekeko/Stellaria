package com.keko.stellaria.mixin;

import com.keko.stellaria.world.dimension.ModDimensions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class NoGravityMixin {
    @Shadow
    private Level level;
    @Unique
    private Entity entity = (Entity)(Object)this;


	@Inject(at = @At("HEAD"), method = "getGravity", cancellable = true)
	private void setNoGravityInArtha(CallbackInfoReturnable<Double> cir) {
        if (entity.level().dimension() == ModDimensions.ARTHA_LEVEL_KEY || entity.level().dimension() == ModDimensions.EBONY_LEVEL_KEY) {
            cir.setReturnValue(0.0d);
            cir.cancel();
        }
    }
}