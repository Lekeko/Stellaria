package com.keko.stellaria.items.stellariaWeaponTypes.zregos.staffOfFury;

import com.keko.stellaria.component.ModDataComponentTypes;
import com.keko.stellaria.entity.ModProjectiles;
import com.keko.stellaria.entity.projectiles.player.mage.FuryProjectile;
import com.keko.stellaria.items.stellariaWeaponTypes.StellarMageWeapon;
import com.keko.stellaria.world.dimension.ModDimensions;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class StaffOfFury extends StellarMageWeapon implements GeoItem {
    private boolean isAttacking = false;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public StaffOfFury(Properties properties) {
        super(properties);
        GeoItem.registerSyncedAnimatable(this);
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        stack.set(ModDataComponentTypes.FURY_STAFF_CHARGES, 0);
        return stack;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private final StaffOfFuryRenderer cosmicBowRenderer = new StaffOfFuryRenderer();
            @Override
            public @Nullable BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                return this.cosmicBowRenderer;
            }
        });
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean bl) {
        super.inventoryTick(itemStack, level, entity, i, bl);
        if (!(entity instanceof Player player))
            return;

        if (!level.isClientSide){
            int projectiles = itemStack.getOrDefault(ModDataComponentTypes.FURY_STAFF_CHARGES, 0);

            if (projectiles > 0){
                float speed = 8.0f;

                Vec3 spawnPos = player.getEyePosition().add(player.getViewVector(1.0f).scale(5.0d));
                Vec3 playerDirection = player.getViewVector(1.0f);

                float xRand = level.random.nextIntBetweenInclusive(0, 360);
                float zRand = level.random.nextIntBetweenInclusive(0, 360);

                double x = playerDirection.x * Mth.cos(xRand) - playerDirection.z * Mth.sin(xRand);
                double y = (level.random.nextFloat() -0.5f);
                double z = playerDirection.x * Mth.sin(zRand) + playerDirection.z * Mth.cos(zRand);

                Vec3 offsetDir = new Vec3(x,y,z).scale(level.random.nextFloat() * 5);

                if (!level.dimension().equals(ModDimensions.ARTHA_LEVEL_KEY)) {
                    offsetDir = offsetDir.scale(0.01d);
                    spawnPos = spawnPos.add(player.getViewVector(1.0f).scale(-3.0d));
                }

                FuryProjectile furyProjectile = new FuryProjectile(ModProjectiles.FURY_PROJECTILE, level);
                furyProjectile.setPos(spawnPos.add(offsetDir));
                furyProjectile.setDeltaMovement(player.getViewVector(1.0f).scale(speed));
                furyProjectile.setOwner(player);
                level.playSound(null, player.getOnPos(), SoundEvents.AMETHYST_CLUSTER_HIT, SoundSource.PLAYERS, 10,0.3f + level.random.nextFloat());

                level.addFreshEntity(furyProjectile);
                projectiles--;
                itemStack.set(ModDataComponentTypes.FURY_STAFF_CHARGES, itemStack.getOrDefault(ModDataComponentTypes.FURY_STAFF_CHARGES, 0) - 1);

                if (projectiles == 0) {
                    isAttacking = false;
                }
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack stack = player.getItemInHand(interactionHand);
        player.getCooldowns().addCooldown(this, 15);
        if (!level.isClientSide) {
            triggerAnim(player, GeoItem.getOrAssignId(stack, (ServerLevel) level), "controller", "staff_of_fury.attack");

            stack.set(ModDataComponentTypes.FURY_STAFF_CHARGES, 3 + level.random.nextIntBetweenInclusive(0, 2));
        }
        return super.use(level, player, interactionHand);


    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        if (!isAttacking) {
            tAnimationState.getController().setAnimation(
                    RawAnimation.begin().thenLoop("staff_of_fury.idle")
            );
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<StaffOfFury> controller = new AnimationController<>(this, "controller", 0, this::predicate);

        controller.triggerableAnim("staff_of_fury.attack", RawAnimation.begin().thenPlay("staff_of_fury.attack"));

        controllers.add(controller);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
