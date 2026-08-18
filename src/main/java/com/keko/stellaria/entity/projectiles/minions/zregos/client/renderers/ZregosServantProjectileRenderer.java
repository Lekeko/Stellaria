package com.keko.stellaria.entity.projectiles.minions.zregos.client.renderers;

import com.keko.stellaria.entity.projectiles.minions.zregos.client.models.ZregosServantProjectileModel;
import com.keko.stellaria.entity.projectiles.minions.zregos.ZregosServantProjectile;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import foundry.veil.api.client.util.Easing;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.keko.stellaria.helper.MathHelpers.lerp;

public class ZregosServantProjectileRenderer extends GeoEntityRenderer<ZregosServantProjectile> {
    private final int STRIP_LENGTH = 200;
    private final Map<UUID, ArrayList<Vec3>> entityPositions = new HashMap<>();

    public ZregosServantProjectileRenderer(EntityRendererProvider.Context context) {
        super(context, new ZregosServantProjectileModel());
    }

    @Override
    public void render(ZregosServantProjectile entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        java.awt.Color color1 = new java.awt.Color(87, 255, 221, 255);
        java.awt.Color color2 = new java.awt.Color(195, 79, 255, 238);


        poseStack.pushPose();
        poseStack.last().pose().identity();

        renderTrail(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight, color1, color2);

        poseStack.popPose();
    }

    @Override
    public void postRender(PoseStack poseStack, ZregosServantProjectile animatable, BakedGeoModel model, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.postRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public void preRender(PoseStack poseStack, ZregosServantProjectile animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
        poseStack.scale(3,3,3);
    }

    @Override
    protected void applyRotations(ZregosServantProjectile animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick, nativeScale);
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, animatable.yRotO, animatable.getYRot()) + 180));
        poseStack.mulPose(Axis.XN.rotationDegrees(Mth.lerp(partialTick, animatable.xRotO, animatable.getXRot()) + 180f));
    }

    @Override
    public @Nullable RenderType getRenderType(ZregosServantProjectile animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.text(getTextureLocation(animatable));
    }

    private void updateArrayList(ZregosServantProjectile zregosServantProjectile, UUID entityUUID, float f) {
        ArrayList<Vec3> positions = entityPositions.computeIfAbsent(entityUUID, k -> new ArrayList<>());
        Vec3 currentTickPos = zregosServantProjectile.getPosition(f).add(0, -1.4f, 0 );

        positions.addFirst(currentTickPos);

        if (positions.size() > STRIP_LENGTH) {
            positions.removeLast();
        }
    }

    private void renderTrail(ZregosServantProjectile projectile, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, java.awt.Color primaryColor, java.awt.Color secondaryColor) {

        UUID entityUUID = projectile.getUUID();
        updateArrayList(projectile, entityUUID, g);


        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 cameraPos = camera.getPosition();

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR);


        float maxTrailWidth = 1.5f;
        float trailWidth;

        ArrayList<Vec3> positions = entityPositions.get(entityUUID);


        if (positions != null && positions.size() >=2) {

            for (int pose = 0; pose < positions.size() - 1; pose++) {
                trailWidth = maxTrailWidth * Easing.EASE_IN_CIRC.ease((1.0f - (float)pose / (float)positions.size()));
                Vec3 pos1 = positions.get(pose);
                Vec3 pos2 = positions.get(pose + 1);

                Vec3 dir = pos2.subtract(pos1).normalize();
                Vec3 cameraLookVec = new Vec3(camera.getUpVector());
                Vec3 side1 = dir.cross(cameraLookVec).normalize().scale(trailWidth / 2);
                Vec3 side2 = side1.scale(-1);


                Vec3 v1 = pos1.add(side1);
                Vec3 v2 = pos1.add(side2);
                Vec3 v3 = pos2.add(side2);
                Vec3 v4 = pos2.add(side1);

                v1 = v1.subtract(cameraPos);
                v2 = v2.subtract(cameraPos);
                v3 = v3.subtract(cameraPos);
                v4 = v4.subtract(cameraPos);

                int newStripLength = positions.size();

                int red = (int) lerp(primaryColor.getRed(), secondaryColor.getRed(), ((float) pose )/newStripLength);
                int green = (int) lerp(primaryColor.getGreen(), secondaryColor.getGreen(), ((float) pose )/newStripLength);
                int blue = (int) lerp(primaryColor.getBlue(), secondaryColor.getBlue(), ((float) pose )/newStripLength);
                int alpha = (int) lerp(255, 0, ((float) pose )/newStripLength);

                buffer.addVertex(poseStack.last().pose(), (float)v1.x, (float)(v1.y+1.2), (float)v1.z).setColor(red, green, blue, alpha);
                buffer.addVertex(poseStack.last().pose(), (float)v2.x, (float)(v2.y+1.2), (float)v2.z).setColor(red, green, blue, alpha);
                buffer.addVertex(poseStack.last().pose(), (float)v3.x, (float)(v3.y+1.2), (float)v3.z).setColor(red, green, blue, alpha);

                buffer.addVertex(poseStack.last().pose(), (float)v1.x, (float)(v1.y+1.2), (float)v1.z).setColor(red, green, blue, alpha);
                buffer.addVertex(poseStack.last().pose(), (float)v3.x, (float)(v3.y+1.2), (float)v3.z).setColor(red, green, blue, alpha);
                buffer.addVertex(poseStack.last().pose(), (float)v4.x, (float)(v4.y+1.2), (float)v4.z).setColor(red, green, blue, alpha);
            }
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableCull();
            RenderSystem.enableDepthTest();

            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            BufferUploader.drawWithShader(buffer.buildOrThrow());
            RenderSystem.enableCull();

            RenderSystem.disableDepthTest();
        }
    }
}
