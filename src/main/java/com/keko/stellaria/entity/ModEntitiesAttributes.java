package com.keko.stellaria.entity;

import com.keko.stellaria.entity.boss.minions.einfarbig.StarPhantom;
import com.keko.stellaria.entity.boss.minions.einfarbig.VoidDealer;
import com.keko.stellaria.entity.boss.minions.zregos.CosmicSpider;
import com.keko.stellaria.entity.boss.minions.zregos.Crystalius;
import com.keko.stellaria.entity.boss.minions.zregos.ZregosServant;
import com.keko.stellaria.entity.boss.zregos.Zregos;
import com.keko.stellaria.entity.boss.zregos.ZregosBody;
import com.keko.stellaria.entity.boss.zregos.ZregosTail;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

public class ModEntitiesAttributes {
    public static void init(){
        FabricDefaultAttributeRegistry.register(ModEntities.ZREGOS, Zregos.setAtributes());
        FabricDefaultAttributeRegistry.register(ModEntities.ZREGOS_BODY, ZregosBody.setAtributes());
        FabricDefaultAttributeRegistry.register(ModEntities.ZREGOS_TAIL, ZregosTail.setAtributes());

        FabricDefaultAttributeRegistry.register(ModEntities.CRYSTALIUS, Crystalius.setAtributes());
        FabricDefaultAttributeRegistry.register(ModEntities.COSMIC_SPIDER, CosmicSpider.setAtributes());
        FabricDefaultAttributeRegistry.register(ModEntities.ZREGOS_SERVANT, ZregosServant.setAtributes());

        //einferbig

        FabricDefaultAttributeRegistry.register(ModEntities.STAR_PHANTOM, StarPhantom.setAtributes());
        FabricDefaultAttributeRegistry.register(ModEntities.VOID_DEALER, VoidDealer.setAtributes());
        FabricDefaultAttributeRegistry.register(ModEntities.MESSIER, VoidDealer.setAtributes());

    }
}
