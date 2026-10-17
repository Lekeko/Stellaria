package com.keko.stellaria.items.stellariaWeaponTypes.einfarbig.whiteoutStaff;

import com.keko.stellaria.entity.ModProjectiles;
import com.keko.stellaria.entity.projectiles.player.mage.VoidBat;
import com.keko.stellaria.helper.Vec3Helper;
import com.keko.stellaria.items.stellariaWeaponTypes.StellarMageWeapon;
import com.keko.stellaria.sounds.ModSounds;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
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

public class WhiteOutStaff extends StellarMageWeapon implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public WhiteOutStaff(Properties properties) {
        super(properties);
        GeoItem.registerSyncedAnimatable(this);
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        return stack;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private final WhiteOutStaffRenderer whiteOutStaffRenderer = new WhiteOutStaffRenderer();
            @Override
            public @Nullable BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                return this.whiteOutStaffRenderer;
            }
        });
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean bl) {
        super.inventoryTick(itemStack, level, entity, i, bl);
        if (!(entity instanceof Player player))
            return;

    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack stack = player.getItemInHand(interactionHand);
        if (!level.isClientSide) {
            triggerAnim(player, GeoItem.getOrAssignId(stack, (ServerLevel) level), "controller", "whiteout_staff.attack");
        }
        player.startUsingItem(interactionHand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int i) {
        if (!level.isClientSide){
            triggerAnim(livingEntity, GeoItem.getOrAssignId(itemStack, (ServerLevel) level), "controller", "whiteout_staff.idle");
        }
        super.releaseUsing(itemStack, level, livingEntity, i);
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int i) {
        super.onUseTick(level, livingEntity, itemStack, i);
        if (!level.isClientSide && i % 2 == 0){
            spawnBats(level, livingEntity);
        }

    }

    private void spawnBats(Level level, LivingEntity livingEntity) {
        level.playSound(null, livingEntity.getOnPos(), ModSounds.STAFF_SPAWN, SoundSource.PLAYERS, 1.0f, 1.0f);

        Vec3 frontPos = livingEntity.getViewVector(1.0f).add(livingEntity.position().add(0.0f, livingEntity.getBbHeight(), 0.0f));
        Vec3 dir = livingEntity.getViewVector(1.0f).add(Vec3Helper.getRandomVec3(livingEntity.getRandom(), 0.2f));

        float speed = 3.0f;
        VoidBat bat = new VoidBat(ModProjectiles.VOID_BAT, level);
        bat.setOwner(livingEntity);
        bat.setDeltaMovement(dir.scale(speed));
        bat.setPos(frontPos);
        bat.setNoGravity(true);

        level.addFreshEntity(bat);
    }

    @Override
    public int getUseDuration(ItemStack itemStack, LivingEntity livingEntity) {
        return 6767;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.NONE;
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        if (tAnimationState.getController().getCurrentRawAnimation() == null) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().thenLoop("whiteout_staff.idle"));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<WhiteOutStaff> controller = new AnimationController<>(this, "controller", 0, this::predicate);

        controller.triggerableAnim("whiteout_staff.idle", RawAnimation.begin().thenLoop("whiteout_staff.idle"));
        controller.triggerableAnim("whiteout_staff.attack", RawAnimation.begin().thenLoop("whiteout_staff.attack"));

        controllers.add(controller);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
