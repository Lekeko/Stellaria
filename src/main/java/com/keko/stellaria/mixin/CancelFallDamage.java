package com.keko.stellaria.mixin;

import com.keko.stellaria.effects.ModStatusEffects;
import com.keko.stellaria.world.dimension.ModDimensions;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)

public class CancelFallDamage {
    @Unique
    Player player = (Player) (Object) this;

    @Inject(method = "causeFallDamage", at = @At("HEAD"), cancellable = true)
    private void MacelVanitate(float f, float g, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir){
         if (player.level().dimension().equals(ModDimensions.EBONY_LEVEL_KEY))
             cir.cancel();
    }
}