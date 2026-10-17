package com.keko.stellaria.blocks;


import com.keko.stellaria.Stellaria;
import com.keko.stellaria.blocks.borderBreaker.BorderBreakerEntity;
import com.keko.stellaria.blocks.maceBlock.ZregosMaceBlockEntity;
import com.keko.stellaria.blocks.portal.DraconicCrystalBlockEntity;
import com.keko.stellaria.blocks.voidLamp.VoidLampEntity;
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
    public static final BlockEntityType<BorderBreakerEntity> BORDER_BREAKER_ENTITY_BLOCK = register("border_breaker",
            BlockEntityType.Builder.of(BorderBreakerEntity::new, ModBlocks.BORDER_BREAKER)
                    .build());
    public static final BlockEntityType<VoidLampEntity> VOID_LAMP_ENTITY_BLOCK = register("void_lamp",
            BlockEntityType.Builder.of(VoidLampEntity::new, ModBlocks.VOID_LAMP)
                    .build());



    public static <T extends BlockEntity> BlockEntityType<T> register(String name, BlockEntityType<T> type) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Stellaria.id(name), type);
    }
    public static void init() {

    }
}
