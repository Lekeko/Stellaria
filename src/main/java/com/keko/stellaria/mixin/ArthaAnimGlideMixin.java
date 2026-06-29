package com.keko.stellaria.mixin;

import com.keko.stellaria.renderHelpers.HandleAnimICANTTAKEITANYMORE;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public class ArthaAnimGlideMixin {

    @Inject(method = "render", at = @At("HEAD"))
    private void miau(AbstractClientPlayer abstractClientPlayer, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci){
        HandleAnimICANTTAKEITANYMORE.lean(abstractClientPlayer, f, g, poseStack, multiBufferSource, i , ci);
    }
}
