package com.keko.stellaria.items.stellariaWeaponTypes.zregos.celestialShield;

import com.keko.stellaria.component.ModDataComponentTypes;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class CelestialShield extends ShieldItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private float clientTick = 0;

    public static final int MAX_CHARGES = 7;


    public float getClientTick() {
        return clientTick;
    }

    public void addClientTick(float clientTick) {
        this.clientTick += clientTick;
    }

    public CelestialShield(Properties properties) {
        super(properties);
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private final CelestialShieldRenderer celestialShieldRenderer = new CelestialShieldRenderer();
            @Override
            public @Nullable BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                return this.celestialShieldRenderer;
            }
        });
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean bl) {
        super.inventoryTick(itemStack, level, entity, i, bl);
        if (!itemStack.has(ModDataComponentTypes.SHIELD_CHARGES)){
            itemStack.set(ModDataComponentTypes.SHIELD_CHARGES, 0);

        }

        if (level.isClientSide && !((Player)entity).isUsingItem()){
            if (getClientTick() > 0) {
                addClientTick(-0.1f);
            }
            if (clientTick < 0) addClientTick(-getClientTick());
        }

        if (!level.isClientSide){
        }

    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if (level.isClientSide){

        }
        return super.use(level, player, interactionHand);
    }



    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int i) {
        super.onUseTick(level, livingEntity, itemStack, i);
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<CelestialShield> controller = new AnimationController<>(this, "controller", 0, this::predicate);


        controllers.add(controller);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
