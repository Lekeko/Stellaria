package com.keko.stellaria.items.stellariaWeaponTypes;

import com.keko.stellaria.logic.StellariaConstants;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class StellarSwordWeapon extends SwordItem {
    int swordType = StellariaConstants.VANILLA.ordinal();

    public StellarSwordWeapon(Tier tier, Properties properties) {
        super(tier, properties);
    }

    public int getSwordType() {
        return swordType;
    }

    public void setSwordType(int swordType) {
        this.swordType = swordType;
    }
}
