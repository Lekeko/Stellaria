package com.keko.stellaria.blocks;


import com.keko.stellaria.Stellaria;
import com.keko.stellaria.blocks.maceBlock.ZregosMaceBlockEntity;
import com.keko.stellaria.blocks.portal.DraconicCrystalBlockEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntity {

    public static final BlockEntityType<DraconicCrystalBlockEntity> DRACONIC_CRYSTAL_BLOCK_ENTITY = register("draconic_crystal_block",
            BlockEntityType.Builder.of(DraconicCrystalBlockEntity::new, ModBlocks.DRACONIC_CRYSTAL_BLOCK)
                    .build());
    public static final BlockEntityType<ZregosMaceBlockEntity> ZREGOS_MACE_BLOCK_ENTITY = register("zregos_mace_block",
            BlockEntityType.Builder.of(ZregosMaceBlockEntity::new, ModBlocks.ZREGOS_MACE_BLOCK)
                    .build());



    public static <T extends BlockEntity> BlockEntityType<T> register(String name, BlockEntityType<T> type) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Stellaria.id(name), type);
    }
    public static void init() {

    }
}
