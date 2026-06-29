package com.keko.stellaria.items.stellariaWeaponTypes.zregos.serpentsBlade;

import com.keko.stellaria.items.stellariaWeaponTypes.StellarSwordWeapon;
import com.keko.stellaria.logic.StellariaConstants;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Tier;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.RenderUtil;

import java.util.function.Consumer;

public class SerpentsBlade extends StellarSwordWeapon implements GeoItem {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public SerpentsBlade(Tier tier, Properties properties) {
        super(tier, properties);
        this.setSwordType(StellariaConstants.SERPENTS_BLADE.ordinal());
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private final SerpentsBladeRenderer serpentsBladeRenderer = new SerpentsBladeRenderer();
            @Override
            public @Nullable BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                return this.serpentsBladeRenderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public double getTick(Object itemStack) {
        return RenderUtil.getCurrentTick();
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
