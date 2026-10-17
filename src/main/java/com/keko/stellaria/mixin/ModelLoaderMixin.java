package com.keko.stellaria.mixin;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(ModelBakery.class)
public abstract class ModelLoaderMixin {
    @Shadow
    protected abstract void loadSpecialItemModelAndDependencies(ModelResourceLocation id);

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/ModelBakery;loadSpecialItemModelAndDependencies(Lnet/minecraft/client/resources/model/ModelResourceLocation;)V"))
    public void addTheInvThingy(BlockColors blockColors, ProfilerFiller profiler, Map jsonUnbakedModels, Map blockStates, CallbackInfo ci) {
        this.loadSpecialItemModelAndDependencies(ModelResourceLocation.inventory(ResourceLocation.parse("stellaria:serpents_blade_inv")));
        this.loadSpecialItemModelAndDependencies(ModelResourceLocation.inventory(ResourceLocation.parse("stellaria:cosmic_bow_inv")));
        this.loadSpecialItemModelAndDependencies(ModelResourceLocation.inventory(ResourceLocation.parse("stellaria:staff_of_fury_inv")));
        this.loadSpecialItemModelAndDependencies(ModelResourceLocation.inventory(ResourceLocation.parse("stellaria:celestial_shield_inv")));
        this.loadSpecialItemModelAndDependencies(ModelResourceLocation.inventory(ResourceLocation.parse("stellaria:void_glaive_inv")));
        this.loadSpecialItemModelAndDependencies(ModelResourceLocation.inventory(ResourceLocation.parse("stellaria:whiteout_staff_inv")));
        this.loadSpecialItemModelAndDependencies(ModelResourceLocation.inventory(ResourceLocation.parse("stellaria:tricksters_hat_inv")));
    }
}
