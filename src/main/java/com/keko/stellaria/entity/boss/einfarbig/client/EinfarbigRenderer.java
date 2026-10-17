package com.keko.stellaria.entity.boss.einfarbig.client;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.boss.einfarbig.Einfarbig;
import com.keko.stellaria.entity.boss.einfarbig.EinfarbigStatue;
import com.keko.stellaria.shader.ClientShaderHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class EinfarbigRenderer extends GeoEntityRenderer<Einfarbig> {
    public EinfarbigRenderer(EntityRendererProvider.Context context) {
        super(context, new EinfarbigModel());
    }

    @Override
    public ResourceLocation getTextureLocation(Einfarbig animatable) {
        ResourceLocation id = Stellaria.id("textures/entity/einfarbig.png");
        if (animatable.getPhase() == 2){
            id = Stellaria.id("textures/entity/einfarbig_mono.png");
        }

        return id;
    }

    @Override
    public void preRender(PoseStack poseStack, Einfarbig animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
        float scaleFactor = 2.0f;
        poseStack.scale(scaleFactor, scaleFactor, scaleFactor);
        poseStack.translate(0.0f, -scaleFactor, 0.0f);
    }

    @Override
    public void render(Einfarbig entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        RenderSystem.disableCull();
        if (entity.getShouldRender())
            super.render(entity, entityYaw, partialTick, poseStack, bufferSource, LightTexture.FULL_BRIGHT);

        if (entity.getPhase() == 1)
            ClientShaderHandler.renderEiniphase1Skybox();
        else ClientShaderHandler.renderEiniphase2Skybox();


    }


    @Override
    public boolean shouldRender(Einfarbig entity, Frustum frustum, double d, double e, double f) {
        if (Minecraft.getInstance().player != null){
            if (!Minecraft.getInstance().player.isDeadOrDying() && Vec3.atCenterOf(entity.getOnPos()).closerThan(new Vec3(d, e, f), Minecraft.getInstance().options.getEffectiveRenderDistance() * 16)) {
                entity.addLight();
            } else {
                entity.removeLight();
            }
        }
        return true;
    }

    @Override
    public @Nullable RenderType getRenderType(Einfarbig animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.text(texture);
    }
}
