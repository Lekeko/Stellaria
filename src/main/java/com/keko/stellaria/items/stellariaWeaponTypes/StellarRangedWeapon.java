package com.keko.stellaria.items.stellariaWeaponTypes;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class StellarRangedWeapon extends Item {
    private float cooldown = 0;

    public StellarRangedWeapon(Properties properties) {
        super(properties);
    }

    public void setCooldown(float cooldown) {
        this.cooldown = cooldown;
    }

    public float getCooldown() {
        return cooldown;
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean bl) {
        super.inventoryTick(itemStack, level, entity, i, bl);
        if (cooldown > 0)
            cooldown--;
    }


}
