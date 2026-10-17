package com.keko.stellaria.items.stellariaWeaponTypes.einfarbig.voidGlaive;

import com.keko.stellaria.component.ModDataComponentTypes;
import com.keko.stellaria.items.stellariaWeaponTypes.StellarSwordWeapon;
import com.keko.stellaria.logic.StellariaConstants;
import com.keko.stellaria.sounds.ModSounds;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.RenderUtil;

import java.util.function.Consumer;

public class VoidGlaive extends StellarSwordWeapon implements GeoItem {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public VoidGlaive(Tier tier, Properties properties) {
        super(tier, properties);
        this.setSwordType(StellariaConstants.VOID_GLAIVE.ordinal());
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private final VoidGlaiveRenderer voidGlaiveRenderer = new VoidGlaiveRenderer();
            @Override
            public @Nullable BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                return this.voidGlaiveRenderer;
            }
        });
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean bl) {
        super.inventoryTick(itemStack, level, entity, i, bl);
        if (!itemStack.has(ModDataComponentTypes.VOID_GLAIVE_ACTIVE))
            itemStack.set(ModDataComponentTypes.VOID_GLAIVE_ACTIVE, 0);

        if (itemStack.get(ModDataComponentTypes.VOID_GLAIVE_ACTIVE) > 0)
            itemStack.set(ModDataComponentTypes.VOID_GLAIVE_ACTIVE, itemStack.get(ModDataComponentTypes.VOID_GLAIVE_ACTIVE) - 1);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
            ItemStack stack = player.getItemInHand(interactionHand);
            {
                if (!player.getCooldowns().isOnCooldown(this)){
                    stack.set(ModDataComponentTypes.VOID_GLAIVE_ACTIVE, 100);
                    player.getCooldowns().addCooldown(this, 30 * 20);
                    level.playSound(null, player.getOnPos(), ModSounds.VOID_GLAIVE_ACTIVATE, SoundSource.PLAYERS, 1.0f, 1.0f);
                }
            }
        return super.use(level, player, interactionHand);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        return super.getDefaultInstance();
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
