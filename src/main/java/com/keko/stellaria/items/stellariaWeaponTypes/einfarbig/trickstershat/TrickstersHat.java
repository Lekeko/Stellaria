package com.keko.stellaria.items.stellariaWeaponTypes.einfarbig.trickstershat;

import com.keko.stellaria.entity.ModProjectiles;
import com.keko.stellaria.entity.projectiles.boss.einfarbig.TntBunny;
import com.keko.stellaria.entity.projectiles.player.mage.VoidBat;
import com.keko.stellaria.helper.Vec3Helper;
import com.keko.stellaria.items.stellariaWeaponTypes.StellarMageWeapon;
import com.keko.stellaria.items.stellariaWeaponTypes.einfarbig.whiteoutStaff.WhiteOutStaffRenderer;
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

public class TrickstersHat extends StellarMageWeapon implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public TrickstersHat(Properties properties) {
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
            private final TrickstersHatRenderer trickstersHatRenderer = new TrickstersHatRenderer();
            @Override
            public @Nullable BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                return this.trickstersHatRenderer;
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
        }
        player.startUsingItem(interactionHand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int i) {
        super.releaseUsing(itemStack, level, livingEntity, i);
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int i) {
        super.onUseTick(level, livingEntity, itemStack, i);
        if (!level.isClientSide && i % 20== 0){
            spawnBunny(level, livingEntity, itemStack);
        }

    }

    private void spawnBunny(Level level, LivingEntity livingEntity, ItemStack itemStack) {
        level.playSound(null, livingEntity.getOnPos(), ModSounds.BUNNY_LAUNCH, SoundSource.PLAYERS, 1.0f, 1.0f);

        triggerAnim(livingEntity, GeoItem.getOrAssignId(itemStack, (ServerLevel) level), "controller", "tricksters_hat.attack");

        Vec3 frontPos = livingEntity.getViewVector(1.0f).add(livingEntity.position().add(0.0f, livingEntity.getBbHeight() * 0.2, 0.0f));
        Vec3 dir = livingEntity.getViewVector(1.0f);

        float speed = 3.0f;

        TntBunny tntBunny = new TntBunny(ModProjectiles.TNT_BUNNY, level);
        tntBunny.setOwner(livingEntity);
        tntBunny.setDeltaMovement(dir.scale(speed));
        tntBunny.setPos(frontPos);
        tntBunny.setFriendly(true);
        tntBunny.setDamage(50.0f);

        level.addFreshEntity(tntBunny);
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
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<TrickstersHat> controller = new AnimationController<>(this, "controller", 0, this::predicate);

        controller.triggerableAnim("tricksters_hat.attack", RawAnimation.begin().thenPlay("tricksters_hat.attack"));

        controllers.add(controller);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
