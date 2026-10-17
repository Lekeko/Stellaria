package com.keko.stellaria.mixin;


import com.keko.stellaria.world.dimension.ModDimensions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class VoidDamageMixin {
    LivingEntity livingEntity = (LivingEntity)(Object) this;


    @Inject(method = "onBelowWorld", at = @At("HEAD"), cancellable = true)
    private void checkForTp(CallbackInfo ci){

        if (livingEntity instanceof Player && !livingEntity.level().isClientSide){
            if (livingEntity instanceof ServerPlayer serverPlayer && livingEntity.level().dimension() == Level.OVERWORLD) {
                if (livingEntity.getY() < -200) {
                    ServerLevel artha = livingEntity.getServer().getLevel(ModDimensions.EBONY_LEVEL_KEY);

                    if (artha != null) {
                        serverPlayer.teleportTo(
                                artha,
                                0.0f, 400.0d, 130.0f, 0.0f, 0.0f
                        );
                        serverPlayer.setDeltaMovement(Vec3.ZERO);
                        serverPlayer.hurtMarked = true;
                    }
                }                                                                                                             ci.cancel(); //now why is this here you may ask
                                                                                                                            //I am not obligated to answer that
            }
        }
    }
}
