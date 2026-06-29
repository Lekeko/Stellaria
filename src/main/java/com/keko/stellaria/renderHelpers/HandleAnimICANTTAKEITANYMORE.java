package com.keko.stellaria.renderHelpers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class HandleAnimICANTTAKEITANYMORE{
    public static float xLean = 0;
    public static float xImpulse = 0;
    public static float zLean = 0;
    public static float zImpulse = 0;
    //if anyone reads this, those impulses values are here because minecraft sync is crap, dont ask just look at the name of the class


    public static void lean(AbstractClientPlayer abstractClientPlayer, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
        float maxLean = 40;
        float leanSpeed = 6;

        if (xImpulse != 0) {
            xImpulse += xImpulse > 0 ? -1 : 1;
            xLean += Minecraft.getInstance().getTimer().getGameTimeDeltaTicks() * leanSpeed * (xImpulse > 0 ? 1 : -1);
        }else {
            xLean += Minecraft.getInstance().getTimer().getGameTimeDeltaTicks() * (xLean > 0 ? -1 : 1) * 4;
            if (Mth.abs(xLean) < 5) xLean = 0;
        }

        if (zImpulse != 0) {
            zImpulse += zImpulse > 0 ? -1 : 1;
            zLean += Minecraft.getInstance().getTimer().getGameTimeDeltaTicks() * leanSpeed * (zImpulse > 0 ? 1 : -1);
        }else {
            zLean += Minecraft.getInstance().getTimer().getGameTimeDeltaTicks() * (zLean > 0 ? -1 : 1) * 4;
            if (Mth.abs(zLean) < 5) zLean = 0;
        }
        if (zLean > maxLean) zLean = maxLean;
        if (zLean < -maxLean) zLean = -maxLean;
        if (xLean > maxLean) xLean = maxLean;
        if (xLean < -maxLean) xLean = -maxLean;
        poseStack.mulPose(Axis.XP.rotationDegrees(xLean * Mth.abs((float) abstractClientPlayer.getDeltaMovement().normalize().z())));
        poseStack.mulPose(Axis.ZP.rotationDegrees(-zLean * Mth.abs((float) abstractClientPlayer.getDeltaMovement().normalize().x())));
    }
}
