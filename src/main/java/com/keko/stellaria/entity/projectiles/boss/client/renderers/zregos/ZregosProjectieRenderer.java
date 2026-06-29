package com.keko.stellaria.entity.projectiles.boss.client.renderers.zregos;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.projectiles.boss.zregos.ZregosProjectile;
import com.keko.stellaria.entity.projectiles.boss.client.models.zregos.ZregosProjectieModel;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ZregosProjectieRenderer extends GeoEntityRenderer<ZregosProjectile> {
    public ZregosProjectieRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ZregosProjectieModel());
    }
    private final int STRIP_LENGTH = 400;
    private final Map<UUID, ArrayList<Vec3>> entityPositions = new HashMap<>();
    private float ticker = 0;

    @Override
    public @NotNull ResourceLocation getTextureLocation(ZregosProjectile zregosProjectile) {
        return Stellaria.id("textures/entity/zregos_projectile.png");
    }

    public float lerp (float start, float finish, float t){
        return start * (1-t) + finish * t;
    }


    @Override
    public void render(ZregosProjectile zregosProjectile, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i){
        Color primaryColor = Color.CYAN;
        Color secondaryColor = new Color(226, 102, 255, 255);
        ticker += Minecraft.getInstance().getTimer().getGameTimeDeltaTicks();
        poseStack.pushPose();
        poseStack.last().pose().identity();

        renderTrail(zregosProjectile, f, g, poseStack, multiBufferSource, i, primaryColor, secondaryColor);
        rederWarningLaser(zregosProjectile, g, poseStack);

        poseStack.popPose();


        double speed = Math.sqrt(Math.pow(zregosProjectile.getDeltaMovement().x, 2) + Math.pow(zregosProjectile.getDeltaMovement().y, 2) + Math.pow(zregosProjectile.getDeltaMovement().z, 2))/3;

        poseStack.pushPose();
        poseStack.translate(0,0.5f,0);
        poseStack.scale(4f, 4f,4f);
        poseStack.mulPose(Axis.YP.rotationDegrees(ticker));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.sin(ticker)));
        poseStack.mulPose(Axis.XP.rotationDegrees(ticker));
        poseStack.popPose();
    }

    private void rederWarningLaser(ZregosProjectile zregosProjectile, float g, PoseStack poseStack) {
        if (zregosProjectile.getTarget() == null) return;

        Vec3 targetPos = zregosProjectile.getTarget().getPosition(g).add(0.0, .1f,0.0);
        float distance = 1.0f;

        Vec3 newTargetPos = targetPos.add(targetPos.subtract(zregosProjectile.getPosition(g)).normalize().scale(distance));
        Vec3 entityPos = zregosProjectile.getPosition(g);

        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 cameraPos = camera.getPosition();

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);


        float stripW = 0.02f;



        Vec3 dir = entityPos.subtract(newTargetPos).normalize();
        Vec3 cameraLookVec = new Vec3(camera.getLookVector());
        Vec3 side1 = dir.cross(cameraLookVec).normalize().scale(stripW / 2);
        Vec3 side2 = side1.scale(-1);
        Vec3 v1 = newTargetPos.add(side1);
        Vec3 v2 = newTargetPos.add(side2);
        Vec3 v3 = entityPos.add(side2);
        Vec3 v4 = entityPos.add(side1);

        v1 = v1.subtract(cameraPos);
        v2 = v2.subtract(cameraPos);
        v3 = v3.subtract(cameraPos);
        v4 = v4.subtract(cameraPos);

        Color color = new Color(9, 245, 255, 255);


        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        int alpha = color.getAlpha();
        alpha = (int) Mth.clamp(alpha - zregosProjectile.tickCount * 10, 0.0f, 255.0f);

        buffer.addVertex(poseStack.last().pose(), (float) v1.x, (float) (v1.y + 1.2), (float) v1.z).setColor(red, green, blue, alpha);
        buffer.addVertex(poseStack.last().pose(), (float) v2.x, (float) (v2.y + 1.2), (float) v2.z).setColor(red, green, blue, alpha);
        buffer.addVertex(poseStack.last().pose(), (float) v3.x, (float) (v3.y + 1.2), (float) v3.z).setColor(red, green, blue, alpha);
        buffer.addVertex(poseStack.last().pose(), (float) v4.x, (float) (v4.y + 1.2), (float) v4.z).setColor(red, green, blue, alpha);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        BufferUploader.drawWithShader(buffer.buildOrThrow());



    }

    public static Vec3 catmullRom(float t, Vec3 p0, Vec3 p1, Vec3 p2, Vec3 p3) {

        double x = 0.5 * ((2 * p1.x) + (-p0.x + p2.x) * t + (2 * p0.x - 5 * p1.x + 4 * p2.x - p3.x) * (t * t) + (-p0.x + 3 * p1.x - 3 * p2.x + p3.x) * (t * t * t));
        double y = 0.5 * ((2 * p1.y) + (-p0.y + p2.y) * t + (2 * p0.y - 5 * p1.y + 4 * p2.y - p3.y) * (t * t) + (-p0.y + 3 * p1.y - 3 * p2.y + p3.y) * (t * t * t));
        double z = 0.5 * ((2 * p1.z) + (-p0.z + p2.z) * t + (2 * p0.z - 5 * p1.z + 4 * p2.z - p3.z) * (t * t) + (-p0.z + 3 * p1.z - 3 * p2.z + p3.z) * (t * t * t));

        return new Vec3(x, y, z);
    }

    private void renderTrail(ZregosProjectile zregosProjectile, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, Color primaryColor,  Color secondaryColor) {
        UUID entityUUID = zregosProjectile.getUUID();

            updateArrayList(zregosProjectile, entityUUID, g);


        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 cameraPos = camera.getPosition();

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);


        float maxTrailWidth = 2f;
        float trailWidth;

        ArrayList<Vec3> positions = entityPositions.get(entityUUID);


        if (positions != null && positions.size() >=2) {

            for (int pose = 0; pose < positions.size() - 1; pose++) {
                trailWidth = maxTrailWidth * (1.0f - (float)pose / (float)positions.size());

                Vec3 pos1 = positions.get(pose);
                Vec3 pos2 = positions.get(pose + 1);

                Vec3 dir = pos2.subtract(pos1).normalize();
                Vec3 cameraLookVec = new Vec3(camera.getLookVector());
                Vec3 side1 = dir.cross(cameraLookVec).normalize().scale(trailWidth / 2);
                Vec3 side2 = side1.scale(-1);


                Vec3 v1 = pos1.add(side1);
                Vec3 v2 = pos1.add(side2);

                v1 = v1.subtract(cameraPos);
                v2 = v2.subtract(cameraPos);

                int newStripLength = positions.size();

                int red = (int) lerp(primaryColor.getRed(), secondaryColor.getRed(), ((float) pose *1.5f )/newStripLength);
                int green = (int) lerp(primaryColor.getGreen(), secondaryColor.getGreen(), ((float) pose*1.5f  )/newStripLength);
                int blue = (int) lerp(primaryColor.getBlue(), secondaryColor.getBlue(), ((float) pose*1.5f  )/newStripLength);
                int alpha = (int) lerp(255, 0, ((float) pose )/newStripLength);


                buffer.addVertex(poseStack.last().pose(), (float) v1.x, (float) (v1.y + 1.2), (float) v1.z).setColor(red, green, blue, alpha);
                buffer.addVertex(poseStack.last().pose(), (float) v2.x, (float) (v2.y + 1.2), (float) v2.z).setColor(red, green, blue, alpha);
            }
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableCull();
            RenderSystem.enableDepthTest();

            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            BufferUploader.drawWithShader(buffer.buildOrThrow());

            RenderSystem.disableDepthTest();
            RenderSystem.enableCull();
        }
    }

    @Override
    public @Nullable RenderType getRenderType(ZregosProjectile zregosProjectile, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.eyes(getTextureLocation(zregosProjectile));
    }

    @Override
    public boolean shouldRender(ZregosProjectile zregosProjectile, Frustum frustum, double d, double e, double f) {
        return zregosProjectile.tickCount > 1;
    }


    private void updateArrayList(ZregosProjectile zregosProjectile, UUID entityUUID, float f) {
        ArrayList<Vec3> positions = entityPositions.computeIfAbsent(entityUUID, k -> new ArrayList<>());
        Vec3 currentTickPos = zregosProjectile.getPosition(f);

        positions.addFirst(currentTickPos);

        if (positions.size() > STRIP_LENGTH) {
            positions.removeLast();
        }
    }
}
