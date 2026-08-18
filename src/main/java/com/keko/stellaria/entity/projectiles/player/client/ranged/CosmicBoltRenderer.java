package com.keko.stellaria.entity.projectiles.player.client.ranged;

import com.keko.stellaria.entity.projectiles.player.ranged.CosmicBolt;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.keko.stellaria.helper.MathHelpers.lerp;

public class CosmicBoltRenderer extends EntityRenderer<CosmicBolt> {
    private final int STRIP_LENGTH = 200;
    private final Map<UUID, ArrayList<Vec3>> entityPositions = new HashMap<>();

    public CosmicBoltRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(CosmicBolt entity, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        java.awt.Color color1 = new java.awt.Color(255, 255, 255, 255);
        java.awt.Color color2 = new java.awt.Color(0, 0, 0, 238);


        poseStack.pushPose();
        poseStack.last().pose().identity();

        renderTrail(entity, f, g, poseStack, color1, color2);

        poseStack.popPose();


    }

    private void renderTrail(CosmicBolt projectile, float f, float g, PoseStack poseStack, java.awt.Color primaryColor, java.awt.Color secondaryColor) {

        UUID entityUUID = projectile.getUUID();
        updateArrayList(projectile, entityUUID, g);


        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 cameraPos = camera.getPosition();

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR);


        float trailWidth = 0.1f;

        ArrayList<Vec3> positions = entityPositions.get(entityUUID);


        if (positions != null && positions.size() >=2) {

            for (int pose = 0; pose < positions.size() - 1; pose++) {
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

            RenderSystem.disableBlend();
            RenderSystem.enableCull();

            RenderSystem.disableDepthTest();
        }
    }

    private void updateArrayList(CosmicBolt cosmicBolt, UUID entityUUID, float f) {
        ArrayList<Vec3> positions = entityPositions.computeIfAbsent(entityUUID, k -> new ArrayList<>());
        Vec3 currentTickPos = cosmicBolt.getPosition(f);

        positions.addFirst(currentTickPos);

        if (positions.size() > STRIP_LENGTH) {
            positions.removeLast();
        }
    }

    @Override
    public ResourceLocation getTextureLocation(CosmicBolt entity) {
        return null;
    }
}
