package com.keko.stellaria.blocks.maceBlock;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.blocks.portal.DraconicCrystalBlockEntity;
import com.keko.stellaria.blocks.portal.DraconicCrystalBlockEntityModel;
import com.keko.stellaria.entity.decour.zregosMace.ZregosMace;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

import java.awt.*;

public class ZregosMaceBlockEntityRenderer extends GeoBlockRenderer<ZregosMaceBlockEntity> {
    public ZregosMaceBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(new ZregosMaceBlockEntityModel());
    }

    @Override
    public ResourceLocation getTextureLocation(ZregosMaceBlockEntity animatable) {
        return Stellaria.id("textures/block/zregos_mace_block.png");
    }

    @Override
    public void render(ZregosMaceBlockEntity animatable, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        super.render(animatable, partialTick, poseStack, bufferSource, LightTexture.FULL_BRIGHT, packedOverlay);
        poseStack.pushPose();
        renderInBetweenChain(animatable, partialTick, poseStack, bufferSource);
        poseStack.popPose();

        animatable.addClientTick(partialTick);

    }

    private void renderInBetweenChain(ZregosMaceBlockEntity entity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource) {
        for (ZregosMaceBlockEntity zregosMace : entity.getConnections()){
            Vec3 firstPos = new Vec3(entity.getBlockPos().getX(), entity.getBlockPos().getY(), entity.getBlockPos().getZ());
            Vec3 secondPos = new Vec3(zregosMace.getBlockPos().getX(), zregosMace.getBlockPos().getY(), zregosMace.getBlockPos().getZ());

            Vec3 startPos = firstPos.add(0.5f, 0.5f, 0.5f);
            Vec3 endPos = secondPos.add(0.5f, 0.5f, 0.5f);

            Vec3 dir = endPos.subtract(startPos);

            Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();

            float chainLen = 1.0f;
            for (float i = 0; i < dir.length(); i += chainLen){
                Vec3 startPosSeg = startPos.lerp(endPos, i/ dir.length());
                Vec3 endPosSeg = startPosSeg.add(dir.normalize());

                float chainWidth = 0.2f;
                Vec3 cameraLookVec = new Vec3(camera.getLookVector());
                Vec3 side1 = dir.cross(cameraLookVec).normalize().scale(chainWidth / 2);
                Vec3 side2 = side1.scale(-1);

                Vec3 v1 = startPosSeg.add(side1);
                Vec3 v2 = startPosSeg.add(side2);
                Vec3 v3 = endPosSeg.add(side2);
                Vec3 v4 = endPosSeg.add(side1);

                Vec3 entityPos = firstPos;
                v1 = v1.subtract(entityPos);
                v2 = v2.subtract(entityPos);
                v3 = v3.subtract(entityPos);
                v4 = v4.subtract(entityPos);

                VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucent(
                        Stellaria.id("textures/normal_vfx/zregos_mace_chain.png")
                ));

                Color chainColor = new Color(255, 255, 255, 255);
                int red = chainColor.getRed();
                int green = chainColor.getGreen();
                int blue = chainColor.getBlue();
                int alpha = chainColor.getAlpha();

                PoseStack.Pose pose = poseStack.last();
                vertexConsumer.addVertex(pose.pose(), (float) v1.x, (float) v1.y, (float) v1.z)
                        .setColor(red, green, blue, alpha)
                        .setUv(0f, 1f)
                        .setOverlay(OverlayTexture.NO_OVERLAY)
                        .setLight(LightTexture.FULL_BRIGHT)
                        .setNormal(pose, 0, 1, 0);
                vertexConsumer.addVertex(pose.pose(), (float) v2.x, (float) v2.y, (float) v2.z)
                        .setColor(red, green, blue, alpha)
                        .setUv(0f, 0f)
                        .setOverlay(OverlayTexture.NO_OVERLAY)
                        .setLight(LightTexture.FULL_BRIGHT)
                        .setNormal(pose, 0, 1, 0);
                vertexConsumer.addVertex(pose.pose(), (float) v3.x, (float) v3.y, (float) v3.z)
                        .setColor(red, green, blue, alpha)
                        .setUv(1f, 0f)
                        .setOverlay(OverlayTexture.NO_OVERLAY)
                        .setLight(LightTexture.FULL_BRIGHT)
                        .setNormal(pose, 0, 1, 0);
                vertexConsumer.addVertex(pose.pose(), (float) v4.x, (float) v4.y, (float) v4.z)
                        .setColor(red, green, blue, alpha)
                        .setUv(1f, 1f)
                        .setOverlay(OverlayTexture.NO_OVERLAY)
                        .setLight(LightTexture.FULL_BRIGHT)
                        .setNormal(pose, 0, 1, 0);

            }
        }
    }

    @Override
    public void preRender(PoseStack poseStack, ZregosMaceBlockEntity animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
        float slow = 10.0f;
        poseStack.translate(0.0f, 0.5f, 0.0f);

        poseStack.mulPose(Axis.YP.rotationDegrees(animatable.getClientTick()/slow));
        poseStack.mulPose(Axis.XN.rotationDegrees(animatable.getClientTick()/slow));
        poseStack.translate(0.0f, -0.5f, 0.0f);

        poseStack.scale(0.6f, 0.6f, 0.6f);
    }

    @Override
    public void applyRenderLayers(PoseStack poseStack, ZregosMaceBlockEntity animatable, BakedGeoModel model, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        super.applyRenderLayers(poseStack, animatable, model, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);

    }



    @Override
    public boolean shouldRender(ZregosMaceBlockEntity blockEntity, Vec3 vec3) {
        return true;
    }

    @Override
    public @Nullable RenderType getRenderType(ZregosMaceBlockEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.text(getTextureLocation(animatable));
    }
}
