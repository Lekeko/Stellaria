package com.keko.stellaria.world;

import com.keko.stellaria.Stellaria;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

public class ModBiomes {
    public static final ResourceKey<Biome> DEEP_VOID = register("deep_void");

    public static void init() {
    }


    private static ResourceKey<Biome> register(String name) {
        return ResourceKey.create(Registries.BIOME, Stellaria.id(name));
    }
}
