package com.keko.stellaria.items.stellariaWeaponTypes.VanillaModified.mage;

import com.keko.stellaria.entity.ModProjectiles;
import com.keko.stellaria.entity.projectiles.player.explosive.EndCrystalProjectile;
import com.keko.stellaria.entity.projectiles.player.mage.AmethystProjectile;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class CrystalListener {
    public static void listen(Player player, Level level, InteractionHand hand) {

        if (player.getItemInHand(hand).is(Items.AMETHYST_SHARD) && !level.isClientSide && !player.getCooldowns().isOnCooldown(Items.AMETHYST_SHARD)){
            player.getItemInHand(hand).consume(1, player);
            player.getCooldowns().addCooldown(player.getItemInHand(hand).getItem(), 10);

            for (int i = -1; i <= 1; i = i + 2){
                for (int j = -1; j <= 1; j = j + 2){
                    AmethystProjectile amethystProjectile = new AmethystProjectile(ModProjectiles.AMETHYST_PROJECTILE, level);
                    amethystProjectile.setOwner(player);

                    float speed = 4.0f;
                    double spread = Math.toRadians(20.0f);

                    Vec3 vel = player.getViewVector(1.0f).scale(speed);

                    double x = vel.x * Math.cos(spread * i) - vel.z * Math.sin(spread * i);
                    double z = vel.x * Math.sin(spread * i) + vel.z * Math.cos(spread * i);


                    amethystProjectile.setDeltaMovement(x, j + vel.y, z);


                    amethystProjectile.setPos(player.position().add(vel));


                    level.addFreshEntity(amethystProjectile);
                }
            }
        }

    }
}
