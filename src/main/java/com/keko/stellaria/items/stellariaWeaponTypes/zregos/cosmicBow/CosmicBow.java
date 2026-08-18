package com.keko.stellaria.items.stellariaWeaponTypes.zregos.cosmicBow;

import com.keko.stellaria.entity.ModProjectiles;
import com.keko.stellaria.entity.projectiles.player.ranged.CosmicArrow;
import com.keko.stellaria.items.stellariaWeaponTypes.StellarRangedWeapon;
import com.keko.stellaria.sounds.ModSounds;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class CosmicBow extends StellarRangedWeapon implements GeoItem {
    private final float BASE_CD = 50;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);


    public CosmicBow(Properties properties) {
        super(properties);
        GeoItem.registerSyncedAnimatable(this);
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private final CosmicBowRenderer cosmicBowRenderer = new CosmicBowRenderer();
            @Override
            public @Nullable BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                return this.cosmicBowRenderer;
            }
        });
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        player.getCooldowns().addCooldown(this, (int) BASE_CD);
        if (!level.isClientSide) {
            if (this.getCooldown() > 0)
                return super.use(level, player, interactionHand);
            triggerAnim(player, GeoItem.getOrAssignId(player.getItemInHand(interactionHand), (ServerLevel) level), "controller", "cosmic_bow.shoot");

            float speed = 5.0f;

            level.playSound(null, player.getOnPos(),ModSounds.COSMIC_ARROW_SHOT, SoundSource.PLAYERS, 1.0f, 0.7f + level.random.nextFloat());
            CosmicArrow cosmicArrow = new CosmicArrow(ModProjectiles.COSMIC_ARROW, level);
            cosmicArrow.setPos(player.position().add(0, 1.2, 0).add(player.getViewVector(1.0f)));
            cosmicArrow.setDeltaMovement(player.getViewVector(1.0f).scale(speed));
            cosmicArrow.setOwner(player);

            level.addFreshEntity(cosmicArrow);
        }
        return super.use(level, player, interactionHand);
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<CosmicBow> controller = new AnimationController<>(this, "controller", 0, this::predicate);

        controller.triggerableAnim("cosmic_bow.shoot", RawAnimation.begin().thenPlay("cosmic_bow.shoot"));

        controllers.add(controller);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

}
