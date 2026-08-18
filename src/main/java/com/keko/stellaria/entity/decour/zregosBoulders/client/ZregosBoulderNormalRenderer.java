package com.keko.stellaria.entity.decour.zregosBoulders.client;

import com.keko.stellaria.entity.decour.zregosBoulders.ZregosBoulderNormal;
import com.keko.stellaria.shader.ClientShaderHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import foundry.veil.api.client.util.Easing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ZregosBoulderNormalRenderer extends GeoEntityRenderer<ZregosBoulderNormal> {
    public ZregosBoulderNormalRenderer(EntityRendererProvider.Context context) {
        super(context, new ZregosBoulderNormalModel());
    }

    @Override
    public void preRender(PoseStack poseStack, ZregosBoulderNormal zregosBoulderNormal, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        float tranzition = Mth.clamp(zregosBoulderNormal.getClientTicker(), 0.0f, zregosBoulderNormal.getASCEND_TIMER())/zregosBoulderNormal.getASCEND_TIMER();
        float easedTranz = 1.0f - Easing.EASE_OUT_EXPO.ease(tranzition);

        float coolAssFloatThatMakesItLookLikeItsAscendingHehehe = -200 * easedTranz;

        poseStack.translate(0,4.0f + coolAssFloatThatMakesItLookLikeItsAscendingHehehe,0);
        super.preRender(poseStack, zregosBoulderNormal, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public void render(ZregosBoulderNormal zregosBoulderNormal, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        zregosBoulderNormal.setClientTicker(zregosBoulderNormal.getClientTicker() + Minecraft.getInstance().getTimer().getGameTimeDeltaTicks());
        super.render(zregosBoulderNormal, entityYaw, partialTick, poseStack, bufferSource, LightTexture.FULL_BRIGHT);
        renderBoulderBeacon(zregosBoulderNormal, zregosBoulderNormal.tickCount);
        RenderSystem.enableDepthTest();
    }


    private void renderBoulderBeacon(ZregosBoulderNormal zregosBoulderNormal, float tickCount) {
        Vec3 position = zregosBoulderNormal.getPosition(1.0f).add(0.0f,2.5f,0.0f);

        ClientShaderHandler.renderBoulderBeacon(position, Mth.clamp(tickCount - zregosBoulderNormal.getASCEND_TIMER(), 0.0f, 6767.0f));
    }

    @Override
    public boolean shouldRender(ZregosBoulderNormal entity, Frustum frustum, double d, double e, double f) {
        return true;
    }

    @Override
    public @Nullable RenderType getRenderType(ZregosBoulderNormal animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.text(texture);
    }

}
