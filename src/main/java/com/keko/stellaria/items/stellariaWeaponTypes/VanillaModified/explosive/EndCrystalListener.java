package com.keko.stellaria.items.stellariaWeaponTypes.VanillaModified.explosive;

import com.keko.stellaria.entity.ModProjectiles;
import com.keko.stellaria.entity.projectiles.player.explosive.EndCrystalProjectile;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EndCrystalListener {
    public static void listen(Player player, Level level, InteractionHand hand) {

        if (player.getItemInHand(hand).is(Items.END_CRYSTAL) && !level.isClientSide && !player.getCooldowns().isOnCooldown(Items.END_CRYSTAL)){
            player.getItemInHand(hand).consume(1, player);
            player.getCooldowns().addCooldown(player.getItemInHand(hand).getItem(), 20);

            EndCrystalProjectile endCrystalProjectile = new EndCrystalProjectile(ModProjectiles.END_CRYSTAL_PROJECTILE, level);
            endCrystalProjectile.setOwner(player);

            float speed = 4.0f;
            Vec3 vel = player.getViewVector(1.0f).scale(speed);
            endCrystalProjectile.setDeltaMovement(vel);

            endCrystalProjectile.setPos(player.position().add(vel));


            level.addFreshEntity(endCrystalProjectile);
        }

    }
}
