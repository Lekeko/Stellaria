package com.keko.stellaria.entity.boss.minions.einfarbig.client.Messier;

import com.keko.stellaria.entity.boss.minions.einfarbig.Messier;
import com.keko.stellaria.entity.boss.minions.einfarbig.client.StarPhantom.StarPhantomModel;
import com.keko.stellaria.shader.ClientShaderHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MessierRenderer extends GeoEntityRenderer<Messier> {
    public MessierRenderer(EntityRendererProvider.Context context) {
        super(context, new MessierModel());
    }

    @Override
    public void preRender(PoseStack poseStack, Messier animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
        poseStack.scale(0.0f, 0.0f, 0.0f);
    }

    @Override
    public void render(Messier entity, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        super.render(entity, f, g, poseStack, multiBufferSource, i);

    }

    @Override
    public void postRender(PoseStack poseStack, Messier animatable, BakedGeoModel model, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.postRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);

    }

    @Override
    protected void renderNameTag(Messier entity, Component component, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, float f) {
        super.renderNameTag(entity, component, poseStack, multiBufferSource, i, f);
    }

    @Override
    public void reRender(BakedGeoModel model, PoseStack poseStack, MultiBufferSource bufferSource, Messier animatable, RenderType renderType, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.reRender(model, poseStack, bufferSource, animatable, renderType, buffer, partialTick, packedLight, packedOverlay, colour);

    }

    @Override
    public void renderFinal(PoseStack poseStack, Messier animatable, BakedGeoModel model, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.renderFinal(poseStack, animatable, model, bufferSource, buffer, partialTick, packedLight, packedOverlay, colour);

    }

    @Override
    public boolean shouldRender(Messier entity, Frustum frustum, double d, double e, double f) {
        if (Minecraft.getInstance().player != null){
            if (!Minecraft.getInstance().player.isDeadOrDying() && Vec3.atCenterOf(entity.getOnPos()).closerThan(new Vec3(d, e, f), Minecraft.getInstance().options.getEffectiveRenderDistance() * 16)) {
                entity.addLight();
            } else {
                entity.removeLight();
            }
        }
        return true;
    }

}
