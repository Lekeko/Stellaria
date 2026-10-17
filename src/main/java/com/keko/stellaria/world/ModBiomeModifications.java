package com.keko.stellaria.world;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import static com.keko.stellaria.features.ModFeatures.*;

public class ModBiomeModifications {
    public static void load() {
        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(ModBiomes.DEEP_VOID),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ResourceKey.create(Registries.PLACED_FEATURE, VOID_LAMP_LIGHTS_FEATURE_ID)
        );
    }
}
