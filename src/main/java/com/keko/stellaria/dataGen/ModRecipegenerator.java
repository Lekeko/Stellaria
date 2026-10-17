package com.keko.stellaria.dataGen;

import com.keko.stellaria.blocks.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class ModRecipegenerator extends FabricRecipeProvider {
    public ModRecipegenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void buildRecipes(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.DRACONIC_CRYSTAL_BLOCK)
                .pattern(" 1 ")
                .pattern("234")
                .pattern(" 5 ")
                .define('1', Items.AMETHYST_SHARD)
                .define('2', Items.OMINOUS_BOTTLE)
                .define('3', Items.NETHER_STAR)
                .define('4', Items.BLAZE_POWDER)
                .define('5', Items.PAPER)
                .group("multi_bench")
                .unlockedBy(FabricRecipeProvider.getHasName(Items.CRAFTING_TABLE), FabricRecipeProvider.has(Items.CRAFTING_TABLE))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.BORDER_BREAKER)
                .pattern("232")
                .pattern("313")
                .pattern("232")
                .define('1', Items.NETHER_STAR)
                .define('2', Items.TNT)
                .define('3', Items.NETHERITE_SCRAP)
                .group("multi_bench")
                .unlockedBy(FabricRecipeProvider.getHasName(Items.CRAFTING_TABLE), FabricRecipeProvider.has(Items.CRAFTING_TABLE))
                .save(recipeOutput);

    }
}
