package com.keko.stellaria.effects;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.effects.debuffs.ChainedZregosEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;

public class ModStatusEffects {
    public static Holder<MobEffect> CHAINED = register("chained", new ChainedZregosEffect());

    private static Holder<MobEffect> register(String id, MobEffect effect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, Stellaria.id(id), effect);
    }

    public static void init(){
    }
}