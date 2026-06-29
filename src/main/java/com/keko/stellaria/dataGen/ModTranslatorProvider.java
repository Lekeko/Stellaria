package com.keko.stellaria.dataGen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class ModTranslatorProvider extends FabricLanguageProvider {
    public ModTranslatorProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider provider, TranslationBuilder translationBuilder) {
        //Boss
        translationBuilder.add("entity.stellaria.zregos", "Zregos");
        translationBuilder.add("entity.stellaria.zregos_body", "Zregos Body");
        translationBuilder.add("entity.stellaria.zregos_tail", "Zregos Tail");

        //Boss minions
        translationBuilder.add("entity.stellaria.crystalius", "Crystalius");
        translationBuilder.add("entity.stellaria.cosmic_spider", "Cosmic Spider");
        translationBuilder.add("entity.stellaria.zregos_servant", "Zregos Servant");

        //Entities
        translationBuilder.add("entity.stellaria.zregos_shatter", "Zregos Shatter");
        translationBuilder.add("entity.stellaria.spawn_shatter", "Spawn Shatter");
        translationBuilder.add("entity.stellaria.zregos_boulders_normal", "Zregos Boulder");
        translationBuilder.add("entity.stellaria.zregos_boulders", "Zregos Boulder (Shattered)");
        translationBuilder.add("entity.stellaria.zregos_mace", "Zregos Mace");
        translationBuilder.add("entity.stellaria.zregos_projectile", "Zregos Projectile");
        translationBuilder.add("entity.stellaria.zregos_projectile_laser", "Zregos Laser");
        translationBuilder.add("entity.stellaria.zregos_ghost", "Zregos Ghost");
        translationBuilder.add("entity.stellaria.zregos_boulders_chains", "Zregos Boulder Chains");
        translationBuilder.add("entity.stellaria.slash_attack_melee", "Melee Slash");
        translationBuilder.add("entity.stellaria.serpents_projectile", "Serpent's Projectile");
        translationBuilder.add("entity.stellaria.cosmic_arrow", "Cosmic Arrow");
        translationBuilder.add("entity.stellaria.cosmic_bolt", "Cosmic Bolt");

        //Blocks
        translationBuilder.add("block.stellaria.draconic_crystal_block", "Draconic Crystal Block");
        translationBuilder.add("block.stellaria.zregos_mace_block", "Zregos Mace Block");

        //Items
        translationBuilder.add("item.stellaria.serpents_blade", "Serpent's Blade");
        translationBuilder.add("item.stellaria.cosmic_bow", "Cosmic Bow");
        translationBuilder.add("item.stellaria.staff_of_fury", "Staff of Fury");
        translationBuilder.add("item.stellaria.celestial_shield", "Celestial Shield");

    }
}
