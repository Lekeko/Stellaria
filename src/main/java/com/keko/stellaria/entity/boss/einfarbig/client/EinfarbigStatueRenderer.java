package com.keko.stellaria.entity.boss.einfarbig.client;

import com.keko.stellaria.entity.boss.einfarbig.EinfarbigStatue;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class EinfarbigStatueRenderer extends GeoEntityRenderer<EinfarbigStatue> {
    public EinfarbigStatueRenderer(EntityRendererProvider.Context context) {
        super(context, new EinfarbigStatueModel());
    }

    @Override
    public void preRender(PoseStack poseStack, EinfarbigStatue animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
        poseStack.scale(3.0f,3.0f,3.0f);

    }

    @Override
    protected void applyRotations(EinfarbigStatue animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick, nativeScale);
    }

    @Override
    public void render(EinfarbigStatue entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        RenderSystem.disableCull();
    }

    @Override
    public void renderFinal(PoseStack poseStack, EinfarbigStatue animatable, BakedGeoModel model, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.renderFinal(poseStack, animatable, model, bufferSource, buffer, partialTick, packedLight, packedOverlay, colour);

    }

    @Override
    public void postRender(PoseStack poseStack, EinfarbigStatue animatable, BakedGeoModel model, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.postRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public @Nullable RenderType getRenderType(EinfarbigStatue animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.text(texture);
    }

    @Override
    public boolean shouldRender(EinfarbigStatue entity, Frustum frustum, double d, double e, double f) {
        if(Vec3.atCenterOf(entity.getOnPos()).closerThan(new Vec3(d,e,f), Minecraft.getInstance().options.getEffectiveRenderDistance() * 16)){
            entity.addLight();
        }else {
            entity.removeLight();
        }

        return true;
    }
}
