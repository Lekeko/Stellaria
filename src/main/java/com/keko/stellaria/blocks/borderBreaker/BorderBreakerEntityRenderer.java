package com.keko.stellaria.blocks.borderBreaker;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.blocks.portal.DraconicCrystalBlockEntity;
import com.keko.stellaria.blocks.portal.DraconicCrystalBlockEntityModel;
import com.keko.stellaria.shader.ClientShaderHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class BorderBreakerEntityRenderer extends GeoBlockRenderer<BorderBreakerEntity> {
    public BorderBreakerEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(new BorderBreakerEntityModel());
    }

    @Override
    public ResourceLocation getTextureLocation(BorderBreakerEntity animatable) {
        return Stellaria.id("textures/block/border_breaker.png");
    }

    @Override
    public void render(BorderBreakerEntity animatable, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        super.render(animatable, partialTick, poseStack, bufferSource, LightTexture.FULL_BRIGHT, packedOverlay);

        renderDiggingArea(animatable, packedLight);

    }

    private void renderDiggingArea(BorderBreakerEntity animatable, int packedLight) {
        int newSize = animatable.getSize();
        int oldSize = animatable.getSize() == 1 ? 5 : animatable.getSize() - 1;

        ClientShaderHandler.renderDiggingSite(newSize, animatable.getBlockPos().getCenter());
    }

    @Override
    public @Nullable RenderType getRenderType(BorderBreakerEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.text(getTextureLocation(animatable));
    }
}

