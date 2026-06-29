package com.keko.stellaria.mixin;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.items.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    @ModifyVariable(method = "render", at = @At(value = "HEAD"), argsOnly = true)
    public BakedModel useCyraModel(BakedModel value, ItemStack stack, ItemDisplayContext modelTransformation, boolean leftHanded, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int light, int overlay){
        if (stack.is(ModItems.SERPENTS_BLADE) && modelTransformation == ItemDisplayContext.GUI) {
            return ((ItemRendererAccessor) this).stellaria$getModel().getModelManager().getModel(new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(Stellaria.MOD_ID, "serpents_blade_inv"), "inventory"));
        }
        if (stack.is(ModItems.COSMIC_BOW) && modelTransformation == ItemDisplayContext.GUI) {
            return ((ItemRendererAccessor) this).stellaria$getModel().getModelManager().getModel(new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(Stellaria.MOD_ID, "cosmic_bow_inv"), "inventory"));
        }
        if (stack.is(ModItems.STAFF_OF_FURY) && modelTransformation == ItemDisplayContext.GUI) {
            return ((ItemRendererAccessor) this).stellaria$getModel().getModelManager().getModel(new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(Stellaria.MOD_ID, "staff_of_fury_inv"), "inventory"));
        }
        if (stack.is(ModItems.CELESTIAL_SHIELD) && modelTransformation == ItemDisplayContext.GUI) {
            return ((ItemRendererAccessor) this).stellaria$getModel().getModelManager().getModel(new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(Stellaria.MOD_ID, "celestial_shield_inv"), "inventory"));
        }
        return value;
    }
}
