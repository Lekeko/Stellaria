package com.keko.stellaria.features.ebony.voidLightsSpread;

import com.keko.stellaria.blocks.ModBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.phys.Vec3;

public class VoidLightsFeature extends Feature<VoidLightsFeatureConfig> {
    public VoidLightsFeature(Codec<VoidLightsFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<VoidLightsFeatureConfig> featurePlaceContext) {
        WorldGenLevel world = featurePlaceContext.level();
        BlockPos origin = featurePlaceContext.origin();
        RandomSource random = featurePlaceContext.random();

        int height = random.nextIntBetweenInclusive(45, 65);
        int maxRadius = random.nextIntBetweenInclusive(7, 12);

        BlockPos selectedPos = origin.atY(0);
        if (random.nextFloat() < 0.999f) return false;

        for (int y = 0; y < world.getHeight(); y++) {

            if (world.getBlockState(selectedPos).is(ModBlocks.VOID_STONE)
                    && world.getBlockState(selectedPos.above()).isAir()) {

                BlockPos crystalOrigin = selectedPos.above();

                for (int yy = 0; yy < height; yy++) {

                    float progress = (float) yy / (height - 1);

                    float radius = maxRadius * (1.0f - progress);

                    int currentRadius = Math.max(0, Math.round(radius));

                    for (int x = -currentRadius; x <= currentRadius; x++) {
                        for (int z = -currentRadius; z <= currentRadius; z++) {

                            if (x * x + z * z <= currentRadius * currentRadius) {

                                BlockPos crystalPos = crystalOrigin.offset(x, yy, z);

                                if (world.getBlockState(crystalPos).isAir()) {
                                    world.setBlock(crystalPos, ModBlocks.VOID_LIGHTSTONE.defaultBlockState(), 10);
                                }
                            }
                        }
                    }
                }

                return true;
            }

            selectedPos = selectedPos.above();
        }

        return false;
    }
}
