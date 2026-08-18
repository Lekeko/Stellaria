package com.keko.stellaria.entity.boss.zregos.client.renderers;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.boss.zregos.client.models.ZregosTailModel;
import com.keko.stellaria.entity.boss.zregos.ZregosTail;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import static com.keko.stellaria.entity.boss.zregos.client.renderers.ZregosRenderer.SIZE_MODIFIER;

public class ZregosTailRenderer extends GeoEntityRenderer<ZregosTail> {
    public ZregosTailRenderer(EntityRendererProvider.Context context) {
        super(context, new ZregosTailModel());
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(ZregosTail zregos) {
        return Stellaria.id("textures/entity/zregos_tail.png");
    }

    @Override
    protected void applyRotations(ZregosTail animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {

    }

    @Override
    public void render(ZregosTail zregos, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        int red = 255;
        int green = 255;
        int blue = 255;
        int alpha = 255;
        int color = (alpha << 24) | (red << 16) | (green << 8) | blue;
        poseStack.pushPose();
        poseStack.scale(SIZE_MODIFIER,SIZE_MODIFIER,SIZE_MODIFIER);
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, zregos.yRotO, zregos.getYRot()) - 180.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(Mth.lerp(partialTick, zregos.xRotO, zregos.getXRot())
        ));
        poseStack.scale(SIZE_MODIFIER/2,SIZE_MODIFIER/2,SIZE_MODIFIER/2);
        super.actuallyRender(poseStack, zregos, this.model.getBakedModel(this.model.getModelResource(zregos)),
                RenderType.text(this.getTextureLocation(zregos)),bufferSource,
                bufferSource.getBuffer(RenderType.text(this.getTextureLocation(zregos))), true, partialTick, 255, OverlayTexture.NO_OVERLAY, color);
        poseStack.popPose();
    }

    @Override
    public boolean shouldRender(ZregosTail entity, Frustum frustum, double d, double e, double f) {
        return entity.tickCount > 80 * 2;
    }
}
