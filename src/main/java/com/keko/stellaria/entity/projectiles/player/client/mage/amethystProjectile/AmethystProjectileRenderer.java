package com.keko.stellaria.entity.projectiles.player.client.mage.amethystProjectile;

import com.keko.stellaria.entity.projectiles.player.mage.AmethystProjectile;
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
import org.joml.Matrix4f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AmethystProjectileRenderer extends GeoEntityRenderer<AmethystProjectile> {
    private final Map<UUID, ArrayList<Vec3>> entityPositions = new HashMap<>();
    private final int STRIP_LENGTH = 200;

    public AmethystProjectileRenderer(EntityRendererProvider.Context context) {
        super(context, new AmethystProjectileModel());
    }

    @Override
    public void render(AmethystProjectile entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        entity.addClientTIck(partialTick);


        Color color1 = new Color(213, 129, 255, 255);
        Color color2 = new Color(255, 255, 255, 255);
        poseStack.pushPose();
        poseStack.last().pose().identity();

        renderTrail(entity, entityYaw, partialTick, poseStack, bufferSource, color1, color2);
        poseStack.popPose();

        renderQuad(entity, entityYaw, partialTick, poseStack, bufferSource);

    }

    private void renderQuad(AmethystProjectile entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource) {
        poseStack.pushPose();

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);

        poseStack.translate(0, 0.5, 0);

        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));

        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix = poseStack.last().pose();
        float size = Mth.clamp(entity.getClientTick() / 30f, 0.0f, 1.0f);
        size = Easing.EASE_IN_EXPO.ease(size);

        VertexConsumer consumer = bufferSource.getBuffer(
                RenderType.entityTranslucentEmissive(getTextureLocation(entity)));

        buffer.addVertex(matrix, -size, -size, 0).setUv(0, 1).setColor(255, 255, 255, 255);
        buffer.addVertex(matrix,  size, -size, 0).setUv(1, 1).setColor(255, 255, 255, 255);
        buffer.addVertex(matrix,  size,  size, 0).setUv(1, 0).setColor(255, 255, 255, 255);
        buffer.addVertex(matrix, -size,  size, 0).setUv(0, 0).setColor(255, 255, 255, 255);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.enableDepthTest();

        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, getTextureLocation(entity));
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        BufferUploader.drawWithShader(buffer.buildOrThrow());

        RenderSystem.enableCull();
        RenderSystem.disableBlend();

        poseStack.popPose();

    }

    public float lerp (float start, float finish, float t){
        return start * (1-t) + finish * t;
    }


    private void   renderTrail(AmethystProjectile amethystProjectile, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, Color primaryColor, Color secondaryColor) {
        UUID entityUUID = amethystProjectile.getUUID();

        updateArrayList(amethystProjectile, entityUUID, g);


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

                int red = (int) lerp(primaryColor.getRed(), secondaryColor.getRed(), ((float) pose )/newStripLength);
                int green = (int) lerp(primaryColor.getGreen(), secondaryColor.getGreen(), ((float) pose )/newStripLength);
                int blue = (int) lerp(primaryColor.getBlue(), secondaryColor.getBlue(), ((float)pose)/newStripLength);
                int alpha = (int) lerp(255, 0, ((float) pose )/newStripLength);


                buffer.addVertex(poseStack.last().pose(), (float) v1.x, (float) (v1.y + 1.2), (float) v1.z).setColor(red, green, blue, alpha);
                buffer.addVertex(poseStack.last().pose(), (float) v2.x, (float) (v2.y + 1.2), (float) v2.z).setColor(red, green, blue, alpha);
            }
            RenderSystem.enableBlend();
            RenderSystem.disableCull();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();

            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            BufferUploader.drawWithShader(buffer.buildOrThrow());
            RenderSystem.disableDepthTest();

            RenderSystem.enableCull();

        }
    }

    @Override
    public void preRender(PoseStack poseStack, AmethystProjectile animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    protected void applyRotations(AmethystProjectile animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick, nativeScale);
    }

    @Override
    public @Nullable RenderType getRenderType(AmethystProjectile animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.text(texture);
    }

    private void updateArrayList(AmethystProjectile amethystProjectile, UUID entityUUID, float f) {
        ArrayList<Vec3> positions = entityPositions.computeIfAbsent(entityUUID, k -> new ArrayList<>());
        Vec3 currentTickPos = amethystProjectile.getPosition(f).add(0.0f, -0.5f, 0.0f);

            positions.addFirst(currentTickPos);
            if (positions.size() > STRIP_LENGTH) {
                positions.removeLast();
            }
    }
}
