package com.keko.stellaria.items.stellariaWeaponTypes.einfarbig.voidGlaive;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.component.ModDataComponentTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class VoidGlaiveRenderer extends GeoItemRenderer<VoidGlaive> {
    public VoidGlaiveRenderer() {
        super(new VoidGlaiveModel());
    }

    @Override
    public ResourceLocation getTextureLocation(VoidGlaive animatable) {
        if (Minecraft.getInstance().player.getItemInHand(InteractionHand.MAIN_HAND).getOrDefault(ModDataComponentTypes.VOID_GLAIVE_ACTIVE, 0) > 0){
            return Stellaria.id("textures/item/void_glaive_model_1.png");
        }
        return super.getTextureLocation(animatable);
    }

    @Override
    public void defaultRender(PoseStack poseStack, VoidGlaive animatable, MultiBufferSource bufferSource, @Nullable RenderType renderType, @Nullable VertexConsumer buffer, float yaw, float partialTick, int packedLight) {
        super.defaultRender(poseStack, animatable, bufferSource, renderType, buffer, yaw, partialTick, LightTexture.FULL_BRIGHT);
    }

    @Override
    public @Nullable RenderType getRenderType(VoidGlaive animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.text(texture);
    }
}
