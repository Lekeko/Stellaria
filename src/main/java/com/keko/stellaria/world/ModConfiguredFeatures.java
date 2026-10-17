package com.keko.stellaria.world;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.blocks.ModBlocks;
import com.keko.stellaria.features.ModFeatures;
import com.keko.stellaria.features.ebony.voidLamp.VoidLampLightsConfig;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

import java.util.List;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> VOID_LIGHTSTONE_SPREAD_KEY = registerKey("void_lightstone_spread");
    public static final ResourceKey<ConfiguredFeature<?, ?>> VOID_LAMP_LIGHTS_KEY = registerKey("void_lamp_lights");

    public static void boostrap (BootstrapContext<ConfiguredFeature<?, ?>> context){
        RuleTest voidStoneReplaceables = new BlockMatchTest(ModBlocks.VOID_STONE);

        List<OreConfiguration.TargetBlockState> void_lightstone =
                List.of(OreConfiguration.target(voidStoneReplaceables, ModBlocks.VOID_LIGHTSTONE.defaultBlockState()));


        register(context, VOID_LIGHTSTONE_SPREAD_KEY, Feature.SCATTERED_ORE, new OreConfiguration(void_lightstone, 64));
        register(context, VOID_LAMP_LIGHTS_KEY, ModFeatures.VOID_LAMP_LIGHTS_FEATURE, new VoidLampLightsConfig(1, Stellaria.id("void_lamp")));
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name){
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, Stellaria.id(name));
    }

    private static <FC extends FeatureConfiguration, F extends net.minecraft.world.level.levelgen.feature.Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context,
                                                                                                                                     ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration){
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
