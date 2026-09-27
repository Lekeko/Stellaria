package com.keko.stellaria.entity;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.projectiles.boss.zregos.*;
import com.keko.stellaria.entity.projectiles.minions.einfarbig.playingCardProj.PlayingCardProjectile;
import com.keko.stellaria.entity.projectiles.minions.zregos.ZregosServantProjectile;
import com.keko.stellaria.entity.projectiles.player.explosive.CosmicShieldProjectile;
import com.keko.stellaria.entity.projectiles.player.explosive.EndCrystalProjectile;
import com.keko.stellaria.entity.projectiles.player.mage.AmethystProjectile;
import com.keko.stellaria.entity.projectiles.player.mage.FuryProjectile;
import com.keko.stellaria.entity.projectiles.player.melee.MeleeSlash;
import com.keko.stellaria.entity.projectiles.player.melee.SerpentsProjectile;
import com.keko.stellaria.entity.projectiles.player.ranged.CosmicArrow;
import com.keko.stellaria.entity.projectiles.player.ranged.CosmicBolt;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModProjectiles {

    public static final EntityType<ZregosProjectile> ZREGOS_PROJECTILE = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, Stellaria.id("zregos_projectile"),
            EntityType.Builder.of(ZregosProjectile::new, MobCategory.MISC).sized(2f, 2f).build());

    public static final EntityType<ZregosProjectileLaser> ZREGOS_PROJECTILE_LASER = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, Stellaria.id("zregos_projectile_laser"),
            EntityType.Builder.of(ZregosProjectileLaser::new, MobCategory.MISC).sized(.1f, .1f).build());

    public static final EntityType<ZregosGhost> ZREGOS_GHOST = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, Stellaria.id("zregos_ghost"),
            EntityType.Builder.of(ZregosGhost::new, MobCategory.MISC).sized(8f, 8f).build());

    public static final EntityType<ZregosBouldersChains> ZREGOS_BOULDERS_CHAINS = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, Stellaria.id("zregos_boulders_chains"),
            EntityType.Builder.of(ZregosBouldersChains::new, MobCategory.MISC).sized(0.1f, 0.1f).build());

    public static final EntityType<MeleeSlash> MELEE_SLASH = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, Stellaria.id("slash_attack_melee"),
            EntityType.Builder.of(MeleeSlash::new, MobCategory.MISC).sized(10f, 0.4f).build());

    public static final EntityType<SerpentsProjectile> SERPENTS_PROJECTILE = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, Stellaria.id("serpents_projectile"),
            EntityType.Builder.of(SerpentsProjectile::new, MobCategory.MISC).sized(2.1f, 2.1f).build());

    public static final EntityType<CosmicArrow> COSMIC_ARROW = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, Stellaria.id("cosmic_arrow"),
            EntityType.Builder.of(CosmicArrow::new, MobCategory.MISC).sized(0.1f, 0.1f).build());
    public static final EntityType<CosmicBolt> COMSIC_BOLT = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, Stellaria.id("cosmic_bolt"),
            EntityType.Builder.of(CosmicBolt::new, MobCategory.MISC).sized(1f, 1f).build());

    public static final EntityType<FuryProjectile> FURY_PROJECTILE = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, Stellaria.id("fury_projectile"),
            EntityType.Builder.of(FuryProjectile::new, MobCategory.MISC).sized(0.1f, 0.1f).build());

    public static final EntityType<CosmicShieldProjectile> COSMIC_SHIELD_PROJECTILE = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, Stellaria.id("cosmic_shield_projectile"),
            EntityType.Builder.of(CosmicShieldProjectile::new, MobCategory.MISC).sized(1.4f, 1.4f).build());

    public static final EntityType<EndCrystalProjectile> END_CRYSTAL_PROJECTILE = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, Stellaria.id("end_crystal_projectile"),
            EntityType.Builder.of(EndCrystalProjectile::new, MobCategory.MISC).sized(2f, 2f).build());

    public static final EntityType<AmethystProjectile> AMETHYST_PROJECTILE = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, Stellaria.id("amethyst_projectile"),
            EntityType.Builder.of(AmethystProjectile::new, MobCategory.MISC).sized(2f, 2f).build());

    public static final EntityType<MaceProjectile> MACE_PROJECTILE = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, Stellaria.id("mace_projectile"),
            EntityType.Builder.of(MaceProjectile::new, MobCategory.MISC).sized(1.4f, 1.4f).build());

    public static final EntityType<ZregosServantProjectile> ZREGOS_SERVANT_PROJECTILE = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, Stellaria.id("zregos_servant_projectile"),
            EntityType.Builder.of(ZregosServantProjectile::new, MobCategory.MISC).sized(1.4f, 1.4f).build());

    //Einfarbig

    public static final EntityType<PlayingCardProjectile> PLAYING_CARD_PROJECTILE = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, Stellaria.id("playing_card_projectile"),
            EntityType.Builder.of(PlayingCardProjectile::new, MobCategory.MISC).sized(1.0f, 1.2f).build());


    public static void init(){}
}
