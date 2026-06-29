package com.keko.stellaria.effects.debuffs;

import com.keko.stellaria.screenVfx.ScreenVfxHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

public class ChainedZregosEffect extends MobEffect {
    public ChainedZregosEffect() {
        super(MobEffectCategory.HARMFUL, 0x735072);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int i) {
        livingEntity.setPos(livingEntity.getPosition(1.0f));

        return super.applyEffectTick(livingEntity, i);
    }

    @Override
    public void onEffectAdded(LivingEntity livingEntity, int i) {
        super.onEffectAdded(livingEntity, i);
        if (!livingEntity.level().isClientSide && ScreenVfxHandler.getChains().isEmpty()){
            ScreenVfxHandler.initChains();
        }
    }



    @Override
    public void removeAttributeModifiers(AttributeMap attributeMap) {
        super.removeAttributeModifiers(attributeMap);
        if (Minecraft.getInstance().player != null){
            if (Minecraft.getInstance().player.level().isClientSide){
                ScreenVfxHandler.clearChains();

            }
        }
    }
}
