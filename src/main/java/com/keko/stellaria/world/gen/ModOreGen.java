package com.keko.stellaria.world.gen;

import com.keko.stellaria.world.ModBiomeModifications;
import com.keko.stellaria.world.ModBiomes;
import com.keko.stellaria.world.ModPlacedFeature;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.level.levelgen.GenerationStep;

public class ModOreGen {
    public static void generateOres(){
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.DEEP_VOID),
                GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeature.VOID_LIGHTSTONE_SPREAD_PLACED);
    }

}
