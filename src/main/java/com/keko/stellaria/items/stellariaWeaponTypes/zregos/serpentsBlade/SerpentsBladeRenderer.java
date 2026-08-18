package com.keko.stellaria.items.stellariaWeaponTypes.zregos.serpentsBlade;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class SerpentsBladeRenderer extends GeoItemRenderer<SerpentsBlade> {
    public SerpentsBladeRenderer() {
        super(new SerpentsBladeModel());
    }


    @Override
    public void defaultRender(PoseStack poseStack, SerpentsBlade animatable, MultiBufferSource bufferSource, @Nullable RenderType renderType, @Nullable VertexConsumer buffer, float yaw, float partialTick, int packedLight) {
        super.defaultRender(poseStack, animatable, bufferSource, renderType, buffer, yaw, partialTick, LightTexture.FULL_BRIGHT);
    }

    @Override
    public @Nullable RenderType getRenderType(SerpentsBlade animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.text(texture);
    }
}
