package com.keko.stellaria.items;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.blocks.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModGroupItems {
    public static final CreativeModeTab STELLARIA_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModItems.STAFF_OF_FURY))
            .title(Component.translatable("Stellaria"))
            .displayItems((context, entries) -> {
                entries.accept(ModItems.SERPENTS_BLADE);
                entries.accept(ModItems.COSMIC_BOW);
                entries.accept(ModItems.STAFF_OF_FURY);
                entries.accept(ModItems.CELESTIAL_SHIELD);
                entries.accept(ModBlocks.DRACONIC_CRYSTAL_BLOCK);
                entries.accept(ModBlocks.ZREGOS_MACE_BLOCK);
            })
            .build();

    public static void init() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Stellaria.id("affix_group"), STELLARIA_GROUP);

    }
}