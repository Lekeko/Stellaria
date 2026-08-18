package com.keko.stellaria.logic.overrides;

import com.keko.stellaria.items.stellariaWeaponTypes.VanillaModified.explosive.EndCrystalListener;
import com.keko.stellaria.items.stellariaWeaponTypes.VanillaModified.mage.CrystalListener;
import com.keko.stellaria.world.dimension.ModDimensions;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class VanillaItemsOverride {

    public static void listen(Player player, Level world, InteractionHand hand) {
        if (world.dimension() == ModDimensions.ARTHA_LEVEL_KEY){
            EndCrystalListener.listen(player, world, hand);
            CrystalListener.listen(player, world, hand);
        }
    }
}
