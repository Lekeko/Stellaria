package com.keko.stellaria.world.dimension;

import com.keko.stellaria.Stellaria;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;

public class ModDimensions {
    public static final ResourceKey<LevelStem> ARTHA_KEY = ResourceKey.create(Registries.LEVEL_STEM,
            Stellaria.id("artha"));
    public static final ResourceKey<Level> ARTHA_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION,
            Stellaria.id("artha"));
    public static final ResourceKey<DimensionType> ARTHA_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE,
            Stellaria.id("artha"));


    public static void init() {}
}
