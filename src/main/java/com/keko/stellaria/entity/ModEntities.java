package com.keko.stellaria.entity;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.boss.einfarbig.EinfarbigStatue;
import com.keko.stellaria.entity.boss.minions.einfarbig.Messier;
import com.keko.stellaria.entity.boss.minions.einfarbig.StarPhantom;
import com.keko.stellaria.entity.boss.minions.einfarbig.VoidDealer;
import com.keko.stellaria.entity.boss.minions.zregos.CosmicSpider;
import com.keko.stellaria.entity.boss.minions.zregos.Crystalius;
import com.keko.stellaria.entity.boss.minions.zregos.ZregosServant;
import com.keko.stellaria.entity.boss.zregos.Zregos;
import com.keko.stellaria.entity.boss.zregos.ZregosBody;
import com.keko.stellaria.entity.boss.zregos.ZregosTail;
import com.keko.stellaria.entity.decour.zregosBoulders.ZregosBoulderNormal;
import com.keko.stellaria.entity.decour.zregosBoulders.ZregosBoulderShattered;
import com.keko.stellaria.entity.decour.zregosMace.ZregosMace;
import com.keko.stellaria.entity.decour.zregosShatter.SpawnShatter;
import com.keko.stellaria.entity.decour.zregosShatter.ZregosShatter;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {

    public static final EntityType<Zregos> ZREGOS = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, Stellaria.id("zregos"),
            EntityType.Builder.of(Zregos::new, MobCategory.MONSTER).sized(14.0f, 14.0f).build());
    public static final EntityType<ZregosBody> ZREGOS_BODY = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, Stellaria.id("zregos_body"),
            EntityType.Builder.of(ZregosBody::new, MobCategory.MONSTER).sized(9.0f, 9.0f).build());
    public static final EntityType<ZregosTail> ZREGOS_TAIL = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, Stellaria.id("zregos_tail"),
            EntityType.Builder.of(ZregosTail::new, MobCategory.MONSTER).sized(9.0f, 9.0f).build());

    //ZREGOS MINIONS
    public static final EntityType<Crystalius> CRYSTALIUS = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, Stellaria.id("crystalius"),
            EntityType.Builder.of(Crystalius::new, MobCategory.MONSTER).sized(3.0f, 3.0f).build());
    public static final EntityType<CosmicSpider> COSMIC_SPIDER = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, Stellaria.id("cosmic_spider"),
            EntityType.Builder.of(CosmicSpider::new, MobCategory.MONSTER).sized(9.0f, 9.0f).build());
    public static final EntityType<ZregosServant> ZREGOS_SERVANT = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, Stellaria.id("zregos_servant"),
            EntityType.Builder.of(ZregosServant::new, MobCategory.MONSTER).sized(9.0f, 9.0f).build());



    //decour
    public static final EntityType<ZregosShatter> ZREGOS_SHATTER = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, Stellaria.id("zregos_shatter"),
            EntityType.Builder.of(ZregosShatter::new, MobCategory.MISC).sized(.1f, .1f).build());
    public static final EntityType<SpawnShatter> SPAWN_SHATTER = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, Stellaria.id("spawn_shatter"),
            EntityType.Builder.of(SpawnShatter::new, MobCategory.MISC).sized(.1f, .1f).build());

    public static final EntityType<ZregosBoulderNormal> ZREGOS_BOULDER_NORMAL = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, Stellaria.id("zregos_boulders_normal"),
            EntityType.Builder.of(ZregosBoulderNormal::new, MobCategory.MISC).sized(9f, 9f).build());
    public static final EntityType<ZregosBoulderShattered> ZREGOS_BOULDER_SHATTERED = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, Stellaria.id("zregos_boulders"),
            EntityType.Builder.of(ZregosBoulderShattered::new, MobCategory.MISC).sized(9f, 9f).build());
    public static final EntityType<ZregosMace> ZREGOS_MACE = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, Stellaria.id("zregos_mace"),
            EntityType.Builder.of(ZregosMace::new, MobCategory.MISC).sized(5f, 5f).build());

    //EINFERBIG

    public static final EntityType<EinfarbigStatue> EINFARBIG_STATUE = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, Stellaria.id("einfarbig_statue"),
            EntityType.Builder.of(EinfarbigStatue::new, MobCategory.MISC).sized(1.0f, 1.0f).build());

    public static final EntityType<StarPhantom> STAR_PHANTOM = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, Stellaria.id("star_phantom"),
            EntityType.Builder.of(StarPhantom::new, MobCategory.MONSTER).sized(10.0f, 10.0f).build());

    public static final EntityType<VoidDealer> VOID_DEALER = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, Stellaria.id("void_dealer"),
            EntityType.Builder.of(VoidDealer::new, MobCategory.MONSTER).sized(8.0f, 8.0f).build());

    public static final EntityType<Messier> MESSIER = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, Stellaria.id("messier"),
            EntityType.Builder.of(Messier::new, MobCategory.MONSTER).sized(8.0f, 8.0f).build());

    public static void init(){}

}
