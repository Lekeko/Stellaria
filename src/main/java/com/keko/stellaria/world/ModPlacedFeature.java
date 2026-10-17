package com.keko.stellaria.world;

import com.keko.stellaria.Stellaria;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.List;

public class ModPlacedFeature {
    public static final ResourceKey<PlacedFeature> VOID_LIGHTSTONE_SPREAD_PLACED = registerKey("void_lightstone_spread_feature");
    public static final ResourceKey<PlacedFeature> VOID_LAMP_LIGHTS_PLACED = registerKey("void_lamp_lights_feature");

    public static void boostrap(BootstrapContext<PlacedFeature> context){
        var configuredFeatureRegistryEntryLookup = context.lookup(Registries.CONFIGURED_FEATURE);


        register(context, VOID_LIGHTSTONE_SPREAD_PLACED, configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.VOID_LIGHTSTONE_SPREAD_KEY),
                ModOrePlacement.modifiersWithCount(1, HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(300))));

        register(context, VOID_LAMP_LIGHTS_PLACED, configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.VOID_LAMP_LIGHTS_KEY),
                ModOrePlacement.modifiersWithCount(1, HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(300))));
    }

    public static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, Stellaria.id(name));
    }



    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }

    private static <FC extends FeatureConfiguration, F extends net.minecraft.world.level.levelgen.feature.Feature<FC>> void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key,
                                                                                                                                     Holder<ConfiguredFeature<?, ?>> configuration,
                                                                                                                                     PlacementModifier... modifiers) {
        register(context, key, configuration, List.of(modifiers));
    }
}
