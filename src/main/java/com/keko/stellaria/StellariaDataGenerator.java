package com.keko.stellaria;

import com.keko.stellaria.dataGen.*;
import com.keko.stellaria.world.ModConfiguredFeatures;
import com.keko.stellaria.world.ModPlacedFeature;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

public class StellariaDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ModLootTableGenerator::new);
        pack.addProvider(ModRecipegenerator::new);
        pack.addProvider(ModModelProvider::new);
        pack.addProvider(ModWorldGenerator::new);
        pack.addProvider(ModitemTagProvider::new);
        pack.addProvider(ModBlockTagProvider::new);
        pack.addProvider(ModTranslatorProvider::new);
	}

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        registryBuilder.add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::boostrap);
        registryBuilder.add(Registries.PLACED_FEATURE, ModPlacedFeature::boostrap);
    }
}
