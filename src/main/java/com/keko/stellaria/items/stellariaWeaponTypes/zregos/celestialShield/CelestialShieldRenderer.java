package com.keko.stellaria.items.stellariaWeaponTypes.zregos.celestialShield;

import com.keko.stellaria.items.stellariaWeaponTypes.zregos.cosmicBow.CosmicBow;
import com.keko.stellaria.items.stellariaWeaponTypes.zregos.cosmicBow.CosmicBowModel;
import com.keko.stellaria.shader.ClientShaderHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Minecart;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoItemRenderer;

import java.awt.*;

public class CelestialShieldRenderer extends GeoItemRenderer<CelestialShield> {
    public CelestialShieldRenderer() {
        super(new CelestialShieldModel());
    }


    @Override
    public void defaultRender(PoseStack poseStack, CelestialShield animatable, MultiBufferSource bufferSource, @Nullable RenderType renderType, @Nullable VertexConsumer buffer, float yaw, float partialTick, int packedLight) {
        if (Minecraft.getInstance().player != null){
            if (Minecraft.getInstance().player.isUsingItem()){
                if (animatable.getClientTick() < 1) {
                    animatable.addClientTick(partialTick / 100.0f);
                }
                ClientShaderHandler.renderShieldBlock(animatable.getClientTick());

            }


        }
        poseStack.pushPose();
        super.defaultRender(poseStack, animatable, bufferSource, renderType, buffer, yaw, partialTick, LightTexture.FULL_BRIGHT);
        poseStack.popPose();

    }



    @Override
    public @Nullable RenderType getRenderType(CelestialShield animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.text(texture);
    }
}
