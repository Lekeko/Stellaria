package com.keko.stellaria.blocks;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.blocks.borderBreaker.BorderBreaker;
import com.keko.stellaria.blocks.maceBlock.ZregosMaceBlock;
import com.keko.stellaria.blocks.portal.DraconicCrystalBlock;
import com.keko.stellaria.blocks.voidLamp.VoidLamp;
import com.keko.stellaria.items.ModItems;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ModBlocks {


    public static final DraconicCrystalBlock DRACONIC_CRYSTAL_BLOCK = (DraconicCrystalBlock) registerBlock(new DraconicCrystalBlock(
            BlockBehaviour.Properties.of()
            .strength(3.0f, 10.0f)
            .lightLevel(state -> 12)
            .noOcclusion()
    ), "draconic_crystal_block", new Item.Properties());

    public static final ZregosMaceBlock ZREGOS_MACE_BLOCK = (ZregosMaceBlock) registerBlock(new ZregosMaceBlock(
            BlockBehaviour.Properties.of()
                    .strength(1.0f, 1.0f)
                    .lightLevel(state -> 12)
                    .noOcclusion()
    ), "zregos_mace_block", new Item.Properties());

    public static final BorderBreaker BORDER_BREAKER = (BorderBreaker) registerBlock(new BorderBreaker(
            BlockBehaviour.Properties.of()
                    .strength(3.0f, 10.0f)
                    .lightLevel(state -> 12)
                    .noOcclusion()
    ), "border_breaker", new Item.Properties());

    public static final Block VOID_STONE =  registerBlock(new Block(
            BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE)
    ), "void_stone", new Item.Properties());

    public static final Block VOID_LIGHTSTONE =  registerBlock(new Block(
            BlockBehaviour.Properties.ofFullCopy(Blocks.GLOWSTONE)
    ), "void_lightstone", new Item.Properties());

    public static final VoidLamp VOID_LAMP = (VoidLamp) registerBlock(new VoidLamp(
            BlockBehaviour.Properties.of()
                    .strength(3.0f, 10.0f)
                    .lightLevel(state -> 12)
                    .noOcclusion()
    ), "void_lamp", new Item.Properties());

    private static Block registerBlock(Block block, String path, Item.Properties settings) {
        ModItems.registerItem(new BlockItem(block, settings), path);
        return Registry.register(BuiltInRegistries.BLOCK, Stellaria.id(path), block);

    }
    public static void init(){

    }
}
