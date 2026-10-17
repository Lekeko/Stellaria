package com.keko.stellaria.packet.networking;


import com.keko.stellaria.Stellaria;
import com.keko.stellaria.blocks.ModBlocks;
import com.keko.stellaria.component.ModDataComponentTypes;
import com.keko.stellaria.entity.ModProjectiles;
import com.keko.stellaria.entity.boss.minions.einfarbig.Messier;
import com.keko.stellaria.entity.boss.minions.einfarbig.VoidDealer;
import com.keko.stellaria.entity.boss.zregos.Zregos;
import com.keko.stellaria.entity.projectiles.player.explosive.CosmicShieldProjectile;
import com.keko.stellaria.entity.projectiles.player.melee.MeleeSlash;
import com.keko.stellaria.entity.projectiles.player.melee.SerpentsProjectile;
import com.keko.stellaria.items.ModItems;
import com.keko.stellaria.logic.StellariaConstants;
import com.keko.stellaria.logic.movement.EbonyWalk;
import com.keko.stellaria.logic.movement.StellarGlide;
import com.keko.stellaria.packet.*;
import com.keko.stellaria.stellarEvent.artha.StellarEventHandler;
import com.keko.stellaria.world.dimension.ModDimensions;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.List;
import java.util.Set;

public class ModMessagesServer {


    public static final ResourceLocation ARTHA_MOVEMENT = Stellaria.id("artha_movement_packet");
    public static final ResourceLocation ALERT_ZREGOS_BITE = Stellaria.id("alert_z_b");
    public static final ResourceLocation ZREGOS_LASER = Stellaria.id("zregos_laser_mmi");//multamuie international
    public static final ResourceLocation ZREGOS_LASER_DAMAGE = Stellaria.id("zregos_laser_damage");
    public static final ResourceLocation SLASH_ATTACK = Stellaria.id("slash_attack_packet");
    public static final ResourceLocation PLAY_ZREGOS_LASER_SOUND = Stellaria.id("play_zregos_laser_sound");
    public static final ResourceLocation RESET_SHIELD_CHARGES = Stellaria.id("reset_shield_charges");
    public static final ResourceLocation SHAKE_LASER = Stellaria.id("shake_laser");
    public static final ResourceLocation ZSERV_PARTICLES = Stellaria.id("zserv_part");
    public static final ResourceLocation ZREGOS_MINIONS_SKYBOX = Stellaria.id("zregos_minions_skybox");
    public static final ResourceLocation IDLE_SKYBOX = Stellaria.id("idle_skybox");
    public static final ResourceLocation BLACK_HOLE_SHADER = Stellaria.id("black_hole_shader");
    public static final ResourceLocation STELLAR_EVENT_HUD_SWITCH = Stellaria.id("stellar_event_hud_switch");
    public static final ResourceLocation BEGIN_END_ANIM = Stellaria.id("begin_end_anim");
    public static final ResourceLocation SEND_HOME = Stellaria.id("send_home");
    public static final ResourceLocation SEND_ENEMYSPAWN_TIMER = Stellaria.id("send_enemyspawn_timer");
    public static final ResourceLocation VOID_LASER_DAMAGE = Stellaria.id("void_laser_damage");
    public static final ResourceLocation PHANTOM_TP_PART = Stellaria.id("phantom_tp_part");
    public static final ResourceLocation EINI_TP_PART = Stellaria.id("eini_tp_part");



    public static void registerC2SPacket() {
        //SERVER
        PayloadTypeRegistry.playC2S().register(ArthaMovementC2S.ID, ArthaMovementC2S.CODEC);
        PayloadTypeRegistry.playS2C().register(AlertPlayerZregosBiteS2C.ID, AlertPlayerZregosBiteS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(RenderZregosLaserS2C.ID, RenderZregosLaserS2C.CODEC);
        PayloadTypeRegistry.playC2S().register(ZregosLaserDamageC2S.ID, ZregosLaserDamageC2S.CODEC);
        PayloadTypeRegistry.playC2S().register(SlashAttackC2S.ID, SlashAttackC2S.CODEC);
        PayloadTypeRegistry.playS2C().register(PlayZregosLaserSoundC2S.ID, PlayZregosLaserSoundC2S.CODEC);
        PayloadTypeRegistry.playC2S().register(ResetShieldChargesC2S.ID, ResetShieldChargesC2S.CODEC);
        PayloadTypeRegistry.playS2C().register(ShakeLaserS2C.ID, ShakeLaserS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(ZServPartS2C.ID, ZServPartS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(ZregosMinionsSkyboxS2C.ID, ZregosMinionsSkyboxS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(IdleSkyboxS2C.ID, IdleSkyboxS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(BlackHoleShaderS2C.ID, BlackHoleShaderS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(StellarEventHudSwitchS2C.ID, StellarEventHudSwitchS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(BeginEndAnimS2C.ID, BeginEndAnimS2C.CODEC);
        PayloadTypeRegistry.playC2S().register(SendHomeC2S.ID, SendHomeC2S.CODEC);
        PayloadTypeRegistry.playS2C().register(SendEnemySpawnTimerC2S.ID, SendEnemySpawnTimerC2S.CODEC);
        PayloadTypeRegistry.playC2S().register(VoidLaserDamageC2S.ID, VoidLaserDamageC2S.CODEC);
        PayloadTypeRegistry.playS2C().register(PhantomTPPartS2C.ID, PhantomTPPartS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(EiniTPPartS2C.ID, EiniTPPartS2C.CODEC);



        ServerPlayNetworking.registerGlobalReceiver(ArthaMovementC2S.ID, (payload, context) -> {
            context.server().execute(() -> {
                if (context.player().level().dimension() == ModDimensions.ARTHA_LEVEL_KEY)
                    StellarGlide.glidePLayer(context.player(), payload.forwardImpulse(), payload.leftImpulse(), payload.jump(), payload.shift());
                if (context.player().level().dimension() == ModDimensions.EBONY_LEVEL_KEY)
                    EbonyWalk.movementTick(context.player(), payload.forwardImpulse(), payload.leftImpulse(), payload.jump(), payload.shift());
            });
        });
        ServerPlayNetworking.registerGlobalReceiver(ResetShieldChargesC2S.ID, (payload, context) -> {
            context.server().execute(() -> {

                CosmicShieldProjectile cosmicShieldProjectile = new CosmicShieldProjectile(ModProjectiles.COSMIC_SHIELD_PROJECTILE, context.player().level());
                cosmicShieldProjectile.setPos(context.player().position().add(context.player().getViewVector(1.0f)));
                cosmicShieldProjectile.setPower(payload.amount());
                float speed = 8.0f;
                cosmicShieldProjectile.setDeltaMovement(context.player().getViewVector(1.0f).scale(speed));
                cosmicShieldProjectile.setOwner(context.player());
                context.player().level().addFreshEntity(cosmicShieldProjectile);
                context.player().getMainHandItem().set(ModDataComponentTypes.SHIELD_CHARGES, 0);

            });
        });
        ServerPlayNetworking.registerGlobalReceiver(ZregosLaserDamageC2S.ID, (payload, context) -> {
            context.server().execute(() -> {

                Vector3f start = payload.start();
                Vector3f end = payload.end();

                Vector3f a = start.sub(end).normalize();
                Zregos zregos = (Zregos) context.player().level().getEntity(payload.ID_ZREGOS());
                if (zregos == null) {
                    System.out.println("COULDNT FIND ZREGOS");
                    return;
                }

                for (Player player :  zregos.level().players()) {

                    Vector3f b = player.getPosition(1.0f).subtract(zregos.getPosition(1.0f)).normalize().toVector3f();
                    double dot = a.x * b.x + a.y * b.y + a.z * b.z;
                    dot = Math.max(-1.0, Math.min(1.0, dot));
                    double angleDeg = Math.acos(dot) * (180.0 / Math.PI);
                    float radToDmg = 4.5f;
                    float shakeAmp = (float) Mth.clamp(1.0f - angleDeg / (radToDmg * 10), 0.0f, 1.0f);
                    ServerPlayNetworking.send((ServerPlayer) player, new ShakeLaserS2C(shakeAmp * 2));


                    if (angleDeg < radToDmg) {
                        ServerPlayNetworking.send((ServerPlayer) player, new RenderZregosLaserS2C(zregos.getId(), false, false,1.0f));
                        player.hurt(zregos.level().damageSources().mobAttack(zregos), 40.0f);
                    }else {
                        ServerPlayNetworking.send((ServerPlayer) player, new RenderZregosLaserS2C(zregos.getId(), false, false,-1.0f));

                    }
                }
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(SlashAttackC2S.ID, (payload, context) -> {
            context.server().execute(() -> {

                MeleeSlash meleeSlash = new MeleeSlash(ModProjectiles.MELEE_SLASH, context.player().level());
                meleeSlash.setPos(context.player().getPosition(1.0f).add(0.0f,1.0f,0.0f));
                Vec3 look = context.player().getLookAngle();
                Vec3 adjusted = new Vec3(look.x, look.y, look.z).normalize();
                meleeSlash.setSlashType(payload.SWORD_TYPE());

                meleeSlash.setDeltaMovement(adjusted.scale(7.0f));
                if (payload.SWORD_TYPE() == StellariaConstants.VOID_GLAIVE.ordinal()) {
                    meleeSlash.setDamageMult(2.0f);
                    meleeSlash.setDeltaMovement(adjusted.scale(10.0f));
                }

                meleeSlash.setOwner(context.player());
                context.player().level().addFreshEntity(meleeSlash);


                if (payload.SWORD_TYPE() == StellariaConstants.SERPENTS_BLADE.ordinal()){
                    int proj = 3;
                    float disp = 0.5f;
                    float speed = 4.4f;
                    Vec3 dir = adjusted;

                    for (int i = 0; i < proj; i++) {
                        SerpentsProjectile serpentsProjectile = new SerpentsProjectile(ModProjectiles.SERPENTS_PROJECTILE, context.player().level());
                        serpentsProjectile.setPos(context.player().getPosition(1.0f).add(0.0f, 1.0f, 0.0f));
                        serpentsProjectile.setDeltaMovement(dir.add(
                                (context.player().getRandom().nextFloat() -0.5f) * disp,
                                (context.player().getRandom().nextFloat() -0.5f) * disp,
                                (context.player().getRandom().nextFloat() -0.5f) * disp
                        ).scale(speed)
                        );

                        context.player().level().addFreshEntity(serpentsProjectile);
                    }
                }

            });
        });

        ServerPlayNetworking.registerGlobalReceiver(SendHomeC2S.ID, (payload, context) -> {
            context.server().execute(() -> {
                ServerPlayer player = context.player();
                StellarEventHandler.cancelEvent(context.player().level());

                BlockPos spawnPos = player.getRespawnPosition();
                ResourceKey<Level> spawnDim = player.getRespawnDimension();
                float spawnAngle = player.getRespawnAngle();

                if (spawnPos == null) {
                    ServerLevel overworld = player.server.overworld();
                    spawnPos = overworld.getSharedSpawnPos();
                    spawnDim = Level.OVERWORLD;
                    spawnAngle = overworld.getSharedSpawnAngle();
                }

                ServerLevel spawnLevel = player.server.getLevel(spawnDim);
                player.teleportTo(spawnLevel,
                        spawnPos.getX() + 0.5f,
                        spawnPos.getY(),
                        spawnPos.getZ() + 0.5f,
                        Set.of(),
                        spawnAngle,
                        0.0f
                );
                List<Item> weaponPoolZregos = List.of(
                        ModItems.SERPENTS_BLADE,
                        ModItems.STAFF_OF_FURY,
                        ModItems.CELESTIAL_SHIELD,
                        ModItems.COSMIC_BOW
                );
                List<Item> weaponPoolEin = List.of(
                        ModItems.WHITEOUT_STAFF,
                        ModItems.VOID_GLAIVE,
                        ModItems.TRICKSTERS_HAT
                );
                switch (payload.id()){
                    case 1 -> {
                        Item weapon = weaponPoolZregos.get(player.getRandom().nextInt(weaponPoolZregos.size()));
                        ItemStack weaponStack = new ItemStack(weapon, 1);

                        int macesCount = player.getRandom().nextInt(33) + 32;
                        ItemStack macesStack = new ItemStack(ModBlocks.ZREGOS_MACE_BLOCK, macesCount);

                        ItemEntity weaponItem = new ItemEntity(spawnLevel, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), weaponStack);
                        ItemEntity macesItem = new ItemEntity(spawnLevel, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), macesStack);

                        spawnLevel.addFreshEntity(weaponItem);
                        spawnLevel.addFreshEntity(macesItem);
                    }
                    case 2 -> {
                        Item weapon = weaponPoolEin.get(player.getRandom().nextInt(weaponPoolEin.size()));
                        ItemStack weaponStack = new ItemStack(weapon, 1);

                        ItemEntity weaponItem = new ItemEntity(spawnLevel, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), weaponStack);

                        spawnLevel.addFreshEntity(weaponItem);
                    }

                }
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(VoidLaserDamageC2S.ID, (payload, context) -> {
            context.server().execute(() -> {

                Vec3 start = new Vec3(payload.start());
                Vec3 end = new Vec3(payload.end());
                Vec3 dir = end.subtract(start);
                double lenSqr = dir.lengthSqr();

                LivingEntity bruh = (LivingEntity) context.player().level().getEntity(payload.id_messier());

                for (Player player : bruh.level().players()){
                    Vec3 playerPos = player.position().add(0.0f, player.getBbHeight() / 2.0f, 0.0f);
                    double t = Mth.clamp(playerPos.subtract(start).dot(dir) / lenSqr, 0.0d, 1.0d);
                    Vec3 closest = start.add(dir.scale(t));

                    double radius = 6.0f / (bruh instanceof VoidDealer ? 2 : 1);
                    if (closest.distanceToSqr(playerPos) <= radius * radius) {
                        player.hurt(bruh.level().damageSources().mobAttack(bruh), 50.0f);
                        if (bruh instanceof VoidDealer)
                            player.invulnerableTime = 60;
                    }
                }
            });
        });
    }
}
