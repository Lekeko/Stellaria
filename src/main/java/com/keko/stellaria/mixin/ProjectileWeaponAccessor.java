package com.keko.stellaria.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(ProjectileWeaponItem.class)
public interface ProjectileWeaponAccessor {


    @Invoker("draw")
    List<ItemStack> Stellaria$draw(ItemStack itemStack, ItemStack itemStack2, LivingEntity livingEntity);

    @Invoker("shoot")
    void Stellaria$shoot(ServerLevel serverLevel, LivingEntity livingEntity, InteractionHand interactionHand, ItemStack itemStack, List<ItemStack> list, float f, float g, boolean bl, @Nullable LivingEntity livingEntity2);
}

