package com.keko.stellaria.helper;

import com.keko.stellaria.entity.projectiles.minions.zregos.ZregosServantProjectile;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import foundry.veil.api.client.util.Easing;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
import java.util.ArrayList;
import java.util.UUID;

import static com.keko.stellaria.helper.MathHelpers.lerp;

public class RenderUtilHelper {

    public static void renderTrail(Entity entity, float partialTick, PoseStack poseStack, Color primaryColor, Color secondaryColor, Trail trail, Vec3 offset, float maxWidth, Easing easing){
        poseStack.pushPose();



        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 cameraPos = camera.getPosition();

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);



        ArrayList<Vec3> positions = trail.getEntityPositions().get(entity.getUUID());
        int vertexCount = 0;

        if (positions != null && positions.size() >=2) {
            Vec3 lastSide = null;
            int n = positions.size();
            for (int pose = 0; pose < positions.size(); pose++) {
                Vec3 p = positions.get(pose);


                Vec3 pos1 = positions.get(Math.max(pose - 1, 0));
                Vec3 pos2 = positions.get(Math.min(pose + 1, n - 1));
                Vec3 dir = pos2.subtract(pos1);

                Vec3 toPoint = p.subtract(cameraPos);
                Vec3 side = dir.cross(toPoint);

                if (side.lengthSqr() < 1.0E-8) {
                    if (lastSide == null) continue;
                    side = lastSide;
                } else {
                    side = side.normalize();
                    if (lastSide != null && side.dot(lastSide) < 0) side = side.scale(-1);
                }
                lastSide = side;
                float t = 1.0f - (float) pose / (n - 1);
                float half = maxWidth * easing.ease(t) / 2.0f;
                Vec3 offsety = side.scale(half);

                Vec3 v1 = p.add(offsety).subtract(cameraPos).add(offset);
                Vec3 v2 = p.subtract(offsety).subtract(cameraPos).add(offset);

                float pp = 1.0f - t;
                int red = (int) lerp(primaryColor.getRed(), secondaryColor.getRed(), pp);
                int green = (int) lerp(primaryColor.getGreen(), secondaryColor.getGreen(), pp);
                int blue = (int) lerp(primaryColor.getBlue(), secondaryColor.getBlue(), pp);
                int alpha = (int) lerp(255, 0, pp);

                buffer.addVertex(poseStack.last().pose(), (float)v1.x, (float)v1.y, (float)v1.z).setColor(red, green, blue, alpha);
                buffer.addVertex(poseStack.last().pose(), (float)v2.x, (float)v2.y, (float)v2.z).setColor(red, green, blue, alpha);
                vertexCount+=2;
            }
            if (vertexCount >= 4){
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                RenderSystem.enableDepthTest();

                RenderSystem.setShader(GameRenderer::getPositionColorShader);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                BufferUploader.drawWithShader(buffer.buildOrThrow());

                RenderSystem.disableDepthTest();
            }
        }
        poseStack.popPose();

    }
}
