package com.keko.stellaria.blocks.voidLamp;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.blocks.borderBreaker.BorderBreakerEntity;
import com.keko.stellaria.blocks.borderBreaker.BorderBreakerEntityModel;
import com.keko.stellaria.shader.ClientShaderHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

import java.util.Random;

public class VoidLampEntityRenderer extends GeoBlockRenderer<VoidLampEntity> {
    public VoidLampEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(new VoidLampEntityModel());
    }

    @Override
    public ResourceLocation getTextureLocation(VoidLampEntity animatable) {
        return Stellaria.id("textures/block/void_lamp.png");
    }

    @Override
    public void render(VoidLampEntity animatable, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        super.render(animatable, partialTick, poseStack, bufferSource, packedLight, packedOverlay);

    }

    @Override
    public boolean shouldRender(VoidLampEntity blockEntity, Vec3 vec3) {

        if(!Minecraft.getInstance().player.isDeadOrDying() && Vec3.atCenterOf(blockEntity.getBlockPos()).closerThan(vec3, Minecraft.getInstance().options.getEffectiveRenderDistance() * 16)){
            blockEntity.addLight();
        }else {
            blockEntity.removeLight();
        }

        return true;
    }

    @Override
    public void preRender(PoseStack poseStack, VoidLampEntity animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
        float scaleMult = 3.1f;
        poseStack.scale(scaleMult,scaleMult,scaleMult);
    }


    @Override
    protected void rotateBlock(Direction facing, PoseStack poseStack) {
        super.rotateBlock(facing, poseStack);
        Axis axis;
        if (getAnimatable().getBlockPos().getX() % 2 == 0){
            axis = Axis.XN;
        }else axis = Axis.XP;

        poseStack.mulPose(axis.rotation(
                Mth.sin(getAnimatable().getBlockPos().getZ()) / 10.0f
        ));
    }

    @Override
    public @Nullable RenderType getRenderType(VoidLampEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutoutNoCull(getTextureLocation(animatable));
    }
}

