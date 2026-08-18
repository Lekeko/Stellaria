package com.keko.stellaria.mixin;

import com.keko.stellaria.entity.ModProjectiles;
import com.keko.stellaria.entity.projectiles.player.ranged.CosmicBolt;
import com.keko.stellaria.world.dimension.ModDimensions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.world.item.BowItem.getPowerForTime;

@Mixin(BowItem.class)
public class VanilaBowMixin {
    @Unique
    private static final int NR_ARR= 5;
    @Unique
    private static final float ZESTY_SPREAD = 12.0F;

    @Unique
    private BowItem bowItem = (BowItem) (Object) this;

    @Inject(method = "releaseUsing", at = @At(value = "HEAD"), cancellable = true)
    private void shootANormalArrowThatIsNotBullshit(ItemStack itemStack, Level level, LivingEntity livingEntity, int iiiiiiiiiiiiii, CallbackInfo ci) {
        if (level.dimension() == ModDimensions.ARTHA_LEVEL_KEY) {
            if (livingEntity instanceof Player player) {
                ItemStack itemStack2 = player.getProjectile(itemStack);
                if (!itemStack2.isEmpty()) {
                    int j = bowItem.getUseDuration(itemStack, livingEntity) - iiiiiiiiiiiiii;
                    float f = getPowerForTime(j);
                    if (!((double) f < 0.1)) {
                        if (level instanceof ServerLevel serverLevel) {
                            int nrOfBolts = 5;
                            for (int i = 0; i < nrOfBolts; i++){
                                CosmicBolt cosmicBolt = new CosmicBolt(ModProjectiles.COMSIC_BOLT, level);
                                Vec3 dir = livingEntity.getViewVector(1.0f).add(
                                        livingEntity.getRandom().nextFloat() -0.5f,
                                        livingEntity.getRandom().nextFloat() -0.5f,
                                        livingEntity.getRandom().nextFloat() -0.5f
                                );

                                float speed = 3.0f;
                                Vec3 vel = dir.scale(speed);
                                cosmicBolt.setPos(livingEntity.position().add(0,0.7,0).add(dir));
                                cosmicBolt.setDeltaMovement(vel);
                                cosmicBolt.setBaseDamage(0.72d);
                                cosmicBolt.setOwner(livingEntity);
                                level.addFreshEntity(cosmicBolt);

                            }
                            player.getCooldowns().addCooldown(bowItem, 20);
                        }

                        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                                SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS,
                                1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                        player.awardStat(Stats.ITEM_USED.get(bowItem));
                    }
                }
            }
            ci.cancel();
        }
    }
}