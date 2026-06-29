package com.keko.stellaria.mixin;


import com.keko.stellaria.component.ModDataComponentTypes;
import com.keko.stellaria.items.ModItems;
import com.keko.stellaria.items.stellariaWeaponTypes.zregos.celestialShield.CelestialShield;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class ShieldMixin {


    @Inject(at = @At("HEAD"), method = "hurt", cancellable = true)
    private void addCosmicShieldCharge(DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity)(Object)this;

        if (!(entity instanceof Player player))
            return;

        if (!player.isUsingItem())
            return;

        ItemStack active = player.getUseItem();

        if (!(active.getItem().getDefaultInstance().is(ModItems.CELESTIAL_SHIELD)))
            return;

        if (!player.isDamageSourceBlocked(damageSource))
            return;

        player.level().playSound(null, player.getOnPos(), SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.PLAYERS, 1, 0.5f + player.level().random.nextFloat());
        if (active.has(ModDataComponentTypes.SHIELD_CHARGES) && active.get(ModDataComponentTypes.SHIELD_CHARGES) < CelestialShield.MAX_CHARGES){
            active.set(ModDataComponentTypes.SHIELD_CHARGES, active.get(ModDataComponentTypes.SHIELD_CHARGES) + 1);
        }
        float reducedDamage = f * 0.1f;
        if (entity.invulnerableTime > 0)
            return;

        entity.setHealth(entity.getHealth() - reducedDamage);
        entity.invulnerableTime = 5;


        cir.setReturnValue(true);
    }
}
