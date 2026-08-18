package com.keko.stellaria.mixin;


import com.keko.stellaria.world.dimension.ModDimensions;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class CloudsBeGoneMixin {

    @Shadow
    private ClientLevel level;


    @Inject(method = "renderClouds", at = @At("HEAD"), cancellable = true)
    private void iWillDisableThisByMyselfBecauseIKnowPeopleWillReportITASANISSUE(PoseStack poseStack, Matrix4f matrix4f, Matrix4f matrix4f2, float f, double d, double e, double g, CallbackInfo ci){
        if (this.level != null && this.level.dimension().equals(ModDimensions.ARTHA_LEVEL_KEY)){
            ci.cancel();
        }
    }
}
