package com.keko.stellaria.features.ebony.voidLamp;

import com.keko.stellaria.blocks.ModBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class VoidLampLightsFeature extends Feature<VoidLampLightsConfig> {
    public VoidLampLightsFeature(Codec<VoidLampLightsConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<VoidLampLightsConfig> featurePlaceContext) {
        WorldGenLevel world = featurePlaceContext.level();
        BlockPos origin = featurePlaceContext.origin();
        RandomSource random = featurePlaceContext.random();

        BlockPos selectedPos = origin.atY(0);
        if (random.nextFloat() < 0.97f) return false;

        BlockPos lastViablePos = null;

        for (int y = 0; y < world.getHeight(); y++) {
            if (world.getBlockState(selectedPos).is(ModBlocks.VOID_STONE)
                    && world.getBlockState(selectedPos.above()).isAir()) {
                lastViablePos = selectedPos.above();
            }

            selectedPos = selectedPos.above();

        }

        if (lastViablePos != null){
            world.setBlock(lastViablePos, ModBlocks.VOID_LAMP.defaultBlockState(), 10);
            return true;
        }


        return false;
    }
}
