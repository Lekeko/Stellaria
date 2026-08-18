package com.keko.stellaria.entity.decour.zregosBoulders.client;

import com.keko.stellaria.entity.decour.zregosBoulders.ZregosBoulderShattered;
import com.keko.stellaria.shader.ClientShaderHandler;
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

public class ZregosBoulderShatteredRenderer extends GeoEntityRenderer<ZregosBoulderShattered> {
    public ZregosBoulderShatteredRenderer(EntityRendererProvider.Context context) {
        super(context, new ZregosBoulderShatteredModel());
    }

    @Override
    public void preRender(PoseStack poseStack, ZregosBoulderShattered zregosBoulderShattered, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        float tranzition = Mth.clamp(zregosBoulderShattered.getClientTicker(), 0.0f, zregosBoulderShattered.getASCEND_TIMER())/zregosBoulderShattered.getASCEND_TIMER();
        float easedTranz = 1.0f - Easing.EASE_OUT_EXPO.ease(tranzition);

        float coolAssFloatThatMakesItLookLikeItsAscendingHehehe = -200 * easedTranz;

        poseStack.translate(0,-1 + coolAssFloatThatMakesItLookLikeItsAscendingHehehe,0);
        super.preRender(poseStack, zregosBoulderShattered, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public void render(ZregosBoulderShattered zregosBoulderShattered, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        zregosBoulderShattered.setClientTicker(zregosBoulderShattered.getClientTicker() + Minecraft.getInstance().getTimer().getGameTimeDeltaTicks());


        renderBoulderBeacon(zregosBoulderShattered, zregosBoulderShattered.tickCount);

        super.render(zregosBoulderShattered, entityYaw, partialTick, poseStack, bufferSource, LightTexture.FULL_BRIGHT);
    }

    private void renderBoulderBeacon(ZregosBoulderShattered zregosBoulderShattered, float tickCount) {
        Vec3 position = zregosBoulderShattered.getPosition(1.0f).add(0.0f,0.2f,0.0f);

        ClientShaderHandler.renderBoulderBeacon(position, Mth.clamp(tickCount - zregosBoulderShattered.getASCEND_TIMER(), 0.0f, 6767.0f));
    }

    @Override
    public boolean shouldRender(ZregosBoulderShattered zregosBoulderShattered, Frustum frustum, double d, double e, double f) {
        return true;
    }

    @Override
    public @Nullable RenderType getRenderType(ZregosBoulderShattered zregosBoulderShattered, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.text(texture);
    }
}