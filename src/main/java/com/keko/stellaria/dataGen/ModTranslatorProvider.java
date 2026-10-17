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

        translationBuilder.add("entity.stellaria.einfarbig", "Einfarbig");
        translationBuilder.add("entity.stellaria.einfarbig_statue", "Einfarbig Statue");

        //Boss minions
        translationBuilder.add("entity.stellaria.crystalius", "Crystalius");
        translationBuilder.add("entity.stellaria.cosmic_spider", "Cosmic Spider");
        translationBuilder.add("entity.stellaria.zregos_servant", "Zregos Servant");

        translationBuilder.add("entity.stellaria.star_phantom", "Star Phantom");
        translationBuilder.add("entity.stellaria.void_dealer", "Void Dealer");
        translationBuilder.add("entity.stellaria.messier", "Messier");

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
        translationBuilder.add("entity.stellaria.fury_projectile", "Fury Projectile");
        translationBuilder.add("entity.stellaria.cosmic_shield_projectile", "Cosmic Shield Projectile");
        translationBuilder.add("entity.stellaria.end_crystal_projectile", "End Crystal Projectile");
        translationBuilder.add("entity.stellaria.amethyst_projectile", "Amethyst Projectile");
        translationBuilder.add("entity.stellaria.mace_projectile", "Mace Projectile");
        translationBuilder.add("entity.stellaria.zregos_servant_projectile", "Zregos Servant Projectile");
        translationBuilder.add("entity.stellaria.playing_card_projectile", "Playing Card");
        translationBuilder.add("entity.stellaria.void_laser", "Void Laser");
        translationBuilder.add("entity.stellaria.void_laser_warn", "Void Laser Warning");
        translationBuilder.add("entity.stellaria.messier_star", "Messier Star");
        translationBuilder.add("entity.stellaria.void_star", "Void Star");
        translationBuilder.add("entity.stellaria.bunny_hat", "Bunny Hat");
        translationBuilder.add("entity.stellaria.tnt_bunny", "TNT Bunny");
        translationBuilder.add("entity.stellaria.hole_vortex", "Hole Vortex");
        translationBuilder.add("entity.stellaria.void_sword", "Void Sword");
        translationBuilder.add("entity.stellaria.void_bat", "Void Bat");

        //Blocks
        translationBuilder.add("block.stellaria.draconic_crystal_block", "Draconic Crystal Block");
        translationBuilder.add("block.stellaria.zregos_mace_block", "Zregos Mace Block");
        translationBuilder.add("block.stellaria.border_breaker", "Border Breaker");
        translationBuilder.add("block.stellaria.void_stone", "Void Stone");
        translationBuilder.add("block.stellaria.void_lightstone", "Void LightStone");
        translationBuilder.add("block.stellaria.void_lamp", "Void Lamp");

        //Items
        translationBuilder.add("item.stellaria.serpents_blade", "Serpent's Blade");
        translationBuilder.add("item.stellaria.cosmic_bow", "Cosmic Bow");
        translationBuilder.add("item.stellaria.staff_of_fury", "Staff of Fury");
        translationBuilder.add("item.stellaria.celestial_shield", "Celestial Shield");
        translationBuilder.add("item.stellaria.void_glaive", "Void Glaive");
        translationBuilder.add("item.stellaria.whiteout_staff", "Whiteout Staff");
        translationBuilder.add("item.stellaria.tricksters_hat", "Trickster's Hat");

    }
}
