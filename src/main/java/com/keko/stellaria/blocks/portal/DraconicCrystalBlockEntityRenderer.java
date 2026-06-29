package com.keko.stellaria.blocks.portal;

import com.keko.stellaria.Stellaria;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class DraconicCrystalBlockEntityRenderer extends GeoBlockRenderer<DraconicCrystalBlockEntity> {
    public DraconicCrystalBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(new DraconicCrystalBlockEntityModel());
    }

    @Override
    public ResourceLocation getTextureLocation(DraconicCrystalBlockEntity animatable) {
        return Stellaria.id("textures/block/draconic_crystal_block.png");
    }

    @Override
    public void render(DraconicCrystalBlockEntity animatable, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        super.render(animatable, partialTick, poseStack, bufferSource, LightTexture.FULL_BRIGHT, packedOverlay);
    }

    @Override
    public @Nullable RenderType getRenderType(DraconicCrystalBlockEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.text(getTextureLocation(animatable));
    }
}
