package com.keko.stellaria.mixin;

import com.keko.stellaria.world.dimension.ModDimensions;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class StellariaKeepInvMixin {



    @Inject(method = "dropEquipment", at = @At("HEAD"), cancellable = true)
    private void stellaria$noEquipmentDrop(CallbackInfo ci) {
        Player player = ((Player) (Object) this);
        if (player.level().dimension().equals(ModDimensions.ARTHA_LEVEL_KEY) || player.level().dimension().equals(ModDimensions.EBONY_LEVEL_KEY)) {
            ci.cancel();
        }
    }
}
