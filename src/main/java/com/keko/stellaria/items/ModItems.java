package com.keko.stellaria.items;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.items.stellariaWeaponTypes.zregos.celestialShield.CelestialShield;
import com.keko.stellaria.items.stellariaWeaponTypes.zregos.cosmicBow.CosmicBow;
import com.keko.stellaria.items.stellariaWeaponTypes.zregos.serpentsBlade.SerpentsBlade;
import com.keko.stellaria.items.stellariaWeaponTypes.zregos.staffOfFury.StaffOfFury;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;

public class ModItems {

    public static final Item SERPENTS_BLADE = registerItem(new SerpentsBlade(Tiers.NETHERITE,
            new Item.Properties().attributes(SwordItem.createAttributes(Tiers.NETHERITE, 6, -1.8f)).rarity(Rarity.EPIC)), "serpents_blade");

    public static final Item COSMIC_BOW = registerItem(new CosmicBow(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1)), "cosmic_bow");

    public static final Item STAFF_OF_FURY = registerItem(new StaffOfFury(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1)), "staff_of_fury");

    public static final Item CELESTIAL_SHIELD = registerItem(new CelestialShield(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1)), "celestial_shield");


    public static <T extends net.minecraft.world.item.Item> T registerItem(T item, String path) {
        return Registry.register(BuiltInRegistries.ITEM, Stellaria.id(path), item);
    }

    public static void init() {

    }
}
