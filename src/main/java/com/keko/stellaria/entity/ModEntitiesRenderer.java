package com.keko.stellaria.entity;

import com.keko.stellaria.blocks.ModBlockEntity;
import com.keko.stellaria.blocks.borderBreaker.BorderBreakerEntityRenderer;
import com.keko.stellaria.blocks.maceBlock.ZregosMaceBlockEntityRenderer;
import com.keko.stellaria.blocks.portal.DraconicCrystalBlockEntityRenderer;
import com.keko.stellaria.entity.boss.einfarbig.client.EinfarbigStatueRenderer;
import com.keko.stellaria.entity.boss.minions.einfarbig.client.Messier.MessierRenderer;
import com.keko.stellaria.entity.boss.minions.einfarbig.client.StarPhantom.StarPhantomRenderer;
import com.keko.stellaria.entity.boss.minions.einfarbig.client.VoidDealer.VoidDealerRenderer;
import com.keko.stellaria.entity.boss.zregos.client.renderers.ZregosBodyRenderer;
import com.keko.stellaria.entity.boss.zregos.client.renderers.ZregosRenderer;
import com.keko.stellaria.entity.boss.zregos.client.renderers.ZregosTailRenderer;
import com.keko.stellaria.entity.boss.minions.zregos.client.CosmicSpider.CosmicSpiderRenderer;
import com.keko.stellaria.entity.boss.minions.zregos.client.Crystalius.CrystaliusRenderer;
import com.keko.stellaria.entity.boss.minions.zregos.client.ZregosServant.ZregosServantRenderer;
import com.keko.stellaria.entity.decour.zregosShatter.client.spawnShatter.SpawnShatterRenderer;
import com.keko.stellaria.entity.decour.zregosBoulders.client.ZregosBoulderNormalRenderer;
import com.keko.stellaria.entity.decour.zregosBoulders.client.ZregosBoulderShatteredRenderer;
import com.keko.stellaria.entity.decour.zregosMace.client.ZregosMaceRenderer;
import com.keko.stellaria.entity.decour.zregosShatter.client.zregosShatter.ZregosShatterRenderer;
import com.keko.stellaria.entity.projectiles.boss.zregos.client.renderers.*;
import com.keko.stellaria.entity.projectiles.minions.einfarbig.playingCardProj.PlayingCardProjectileRenderer;
import com.keko.stellaria.entity.projectiles.minions.zregos.client.renderers.ZregosServantProjectileRenderer;
import com.keko.stellaria.entity.projectiles.player.client.explosive.CosmicShieldProjectile.CosmicShieldProjectileRenderer;
import com.keko.stellaria.entity.projectiles.player.client.explosive.EndCrystalProjectile.EndCrystalProjectileRenderer;
import com.keko.stellaria.entity.projectiles.player.client.mage.FuryProjectile.FuryProjectileRenderer;
import com.keko.stellaria.entity.projectiles.player.client.mage.amethystProjectile.AmethystProjectileRenderer;
import com.keko.stellaria.entity.projectiles.player.client.melee.MeleeSlashRenderer;
import com.keko.stellaria.entity.projectiles.player.client.melee.SerpentsProjectileRenderer;
import com.keko.stellaria.entity.projectiles.player.client.ranged.CosmicArrowRenderer;
import com.keko.stellaria.entity.projectiles.player.client.ranged.CosmicBoltRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class ModEntitiesRenderer {
    public static void register(){
        EntityRendererRegistry.register(ModEntities.ZREGOS, ZregosRenderer::new);
        EntityRendererRegistry.register(ModEntities.ZREGOS_BODY, ZregosBodyRenderer::new);
        EntityRendererRegistry.register(ModEntities.ZREGOS_TAIL, ZregosTailRenderer::new);
        EntityRendererRegistry.register(ModEntities.CRYSTALIUS, CrystaliusRenderer::new);
        EntityRendererRegistry.register(ModEntities.COSMIC_SPIDER, CosmicSpiderRenderer::new);
        EntityRendererRegistry.register(ModEntities.ZREGOS_SERVANT, ZregosServantRenderer::new);


        EntityRendererRegistry.register(ModEntities.ZREGOS_SHATTER, ZregosShatterRenderer::new);
        EntityRendererRegistry.register(ModEntities.SPAWN_SHATTER, SpawnShatterRenderer::new);
        EntityRendererRegistry.register(ModEntities.ZREGOS_BOULDER_NORMAL, ZregosBoulderNormalRenderer::new);
        EntityRendererRegistry.register(ModEntities.ZREGOS_BOULDER_SHATTERED, ZregosBoulderShatteredRenderer::new);
        EntityRendererRegistry.register(ModEntities.ZREGOS_MACE, ZregosMaceRenderer::new);

        EntityRendererRegistry.register(ModProjectiles.ZREGOS_PROJECTILE, ZregosProjectieRenderer::new);
        EntityRendererRegistry.register(ModProjectiles.ZREGOS_PROJECTILE_LASER, ZregosProjectileLaserRenderer::new);
        EntityRendererRegistry.register(ModProjectiles.ZREGOS_GHOST, ZregosGhostRenderer::new);
        EntityRendererRegistry.register(ModProjectiles.ZREGOS_BOULDERS_CHAINS, ZregosBouldersChainsRenderer::new);
        EntityRendererRegistry.register(ModProjectiles.MELEE_SLASH, MeleeSlashRenderer::new);
        EntityRendererRegistry.register(ModProjectiles.SERPENTS_PROJECTILE, SerpentsProjectileRenderer::new);
        EntityRendererRegistry.register(ModProjectiles.COSMIC_ARROW, CosmicArrowRenderer::new);
        EntityRendererRegistry.register(ModProjectiles.COMSIC_BOLT, CosmicBoltRenderer::new);
        EntityRendererRegistry.register(ModProjectiles.FURY_PROJECTILE, FuryProjectileRenderer::new);
        EntityRendererRegistry.register(ModProjectiles.COSMIC_SHIELD_PROJECTILE, CosmicShieldProjectileRenderer::new);
        EntityRendererRegistry.register(ModProjectiles.END_CRYSTAL_PROJECTILE, EndCrystalProjectileRenderer::new);
        EntityRendererRegistry.register(ModProjectiles.AMETHYST_PROJECTILE, AmethystProjectileRenderer::new);
        EntityRendererRegistry.register(ModProjectiles.MACE_PROJECTILE, MaceProjectileRenderer::new);
        EntityRendererRegistry.register(ModProjectiles.ZREGOS_SERVANT_PROJECTILE, ZregosServantProjectileRenderer::new);

        BlockEntityRenderers.register(ModBlockEntity.DRACONIC_CRYSTAL_BLOCK_ENTITY, DraconicCrystalBlockEntityRenderer::new);
        BlockEntityRenderers.register(ModBlockEntity.ZREGOS_MACE_BLOCK_ENTITY, ZregosMaceBlockEntityRenderer::new);

        //UPDATE
        EntityRendererRegistry.register(ModEntities.EINFARBIG_STATUE, EinfarbigStatueRenderer::new);

        EntityRendererRegistry.register(ModEntities.STAR_PHANTOM, StarPhantomRenderer::new);
        EntityRendererRegistry.register(ModEntities.VOID_DEALER, VoidDealerRenderer::new);
        EntityRendererRegistry.register(ModEntities.MESSIER, MessierRenderer::new);

        EntityRendererRegistry.register(ModProjectiles.PLAYING_CARD_PROJECTILE, PlayingCardProjectileRenderer::new);


        BlockEntityRenderers.register(ModBlockEntity.BORDER_BREAKER_ENTITY_BLOCK_ENTITY, BorderBreakerEntityRenderer::new);
    }
}
