package com.keko.stellaria.particles;

import com.keko.stellaria.Stellaria;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;

public class ModParticles {
    public static final SimpleParticleType ZREGOS_STAR_BURST_NORMAL =
            registerParticle("zregos_star_burst_normal", FabricParticleTypes.simple(true));
    public static final SimpleParticleType ZREGOS_STAR_LASER =
            registerParticle("zregos_star_laser", FabricParticleTypes.simple(true));
    public static final SimpleParticleType WHITE_BOOM_BIG =
            registerParticle("white_boom_big", FabricParticleTypes.simple(true));

    private static SimpleParticleType registerParticle(String name, SimpleParticleType particleType){
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE, Stellaria.id(name), particleType);
    }

    public static void init(){

    }
}
