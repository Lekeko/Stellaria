package com.keko.stellaria.entity.projectiles.minions.einfarbig.VoidLaserWarn;

import com.keko.stellaria.entity.ModProjectiles;
import com.keko.stellaria.entity.projectiles.minions.einfarbig.voidLaser.VoidLaser;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class VoidLaserWarn extends AbstractArrow {
    private float timer = 20;
    public VoidLaserWarn(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
        this.pickup = Pickup.DISALLOWED;
    }
    private float clientTicker = 0;

    public float getClientTicker() {
        return clientTicker;
    }

    public void setClientTicker(float clientTicker) {
        this.clientTicker = clientTicker;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        return false;
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {

    }

    @Override
    public void tick() {
        if (!level().isClientSide){
            if (--timer <= 0){
                VoidLaser voidLaser = new VoidLaser(ModProjectiles.VOID_LASER, level());
                voidLaser.setPos(position().add(0.0f, -1.0f, 0.0f));
                voidLaser.setOwner(getOwner());

                level().addFreshEntity(voidLaser);
                discard();
                return;
            }
        }
        super.tick();
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return Items.GLASS.getDefaultInstance();
    }

    @Override
    protected boolean tryPickup(Player player) {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }
}
