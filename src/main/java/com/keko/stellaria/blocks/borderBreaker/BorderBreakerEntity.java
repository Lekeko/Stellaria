package com.keko.stellaria.blocks.borderBreaker;

import com.keko.stellaria.blocks.ModBlockEntity;
import com.keko.stellaria.blocks.portal.DraconicCrystalBlockEntity;
import com.keko.stellaria.sounds.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class BorderBreakerEntity extends BlockEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private int progress = 0;
    private int size = 1;
    private boolean drilling = false;
    private boolean breaking = false;

    public BorderBreakerEntity( BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntity.BORDER_BREAKER_ENTITY_BLOCK_ENTITY, blockPos, blockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, BorderBreakerEntity entity) {
        if (level.isClientSide) {
            float spread = entity.getSize();
            Vec3 particlePos = new Vec3((level.random.nextFloat()- .5f) * spread, (level.random.nextFloat()- .5f) * spread, (level.random.nextFloat()- .5f) * spread)
                    .add(pos.getCenter());

            Vec3 vel = Vec3.ZERO;
            if (entity.getProgress() > 0)
                vel = pos.getCenter().subtract(particlePos).normalize().scale(0.1);
            level.addAlwaysVisibleParticle(ParticleTypes.END_ROD.getType(), particlePos.x, particlePos.y, particlePos.z ,vel.x, vel.y, vel.z);

            if (entity.getProgress() >= 5.3 * 20){
                int count = 700;
                for (int i = 0; i < count; i++){
                    vel = new Vec3((level.random.nextFloat() - .5f) * spread, (level.random.nextFloat() - .5f) * spread, (level.random.nextFloat() - .5f) * spread);
                    level.addAlwaysVisibleParticle(ParticleTypes.END_ROD.getType(), pos.getCenter().x, pos.getCenter().y, pos.getCenter().z, vel.x, vel.y, vel.z);
                }
            }
            return;
        }



        if (entity.isDrilling()){
            entity.setProgress(entity.getProgress() + 1);
            if (entity.getProgress() == 5 * 20){
                entity.triggerAnim("controller", "border_breaker.break");
            }
            if (entity.getProgress() > 5.3 * 20 && !entity.breaking){
                entity.breakBedrock(level, pos, entity);
            }
        }
    }
    //                   0
    //                  000 -size/2 : size/2
    //                 00000
    private void breakBedrock(Level level, BlockPos pos, BorderBreakerEntity entity) {
        entity.breaking = true;
        int depth = 4;
        for (int i = 0; i < depth; i++){
            for (int j = -size/2; j <= size/2; j++)
                for (int k = -size/2; k <= size/2; k++)
                    level.destroyBlock(new BlockPos(entity.getBlockPos().below().offset(j, -i, k)), true, null);

        }
        level.destroyBlock(new BlockPos(entity.getBlockPos()), true, null);
        level.playSound(null, pos, ModSounds.DRILL_BREAK, SoundSource.PLAYERS, 1.0f, 1.0f);

    }


    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag, provider);

        this.progress = compoundTag.getInt("Progress");
        this.size = compoundTag.getInt("Size");
        this.drilling = compoundTag.getBoolean("Drilling");
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag, provider);
        compoundTag.putInt("Progress", this.progress);
        compoundTag.putInt("Size", this.size);
        compoundTag.putBoolean("Drilling", this.drilling);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag tag = super.getUpdateTag(provider);
        tag.putInt("Progress", this.progress);
        tag.putInt("Size", this.size);
        tag.putBoolean("Drilling", this.drilling);
        return tag;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void setChanged() {
        super.setChanged();

        if (level == null) return;

        BlockState state = getBlockState();
        level.sendBlockUpdated(worldPosition, state, state, Block.UPDATE_ALL);

    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        this.setChanged();
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
        this.setChanged();
    }

    public boolean isDrilling() {
        return drilling;
    }

    public void setDrilling(boolean drilling) {
        this.drilling = drilling;
        getLevel().playSound(null, getBlockPos(), ModSounds.DRILLING, SoundSource.PLAYERS, 1.0f, 1.0f);
        triggerAnim("controller", "border_breaker.drill");
        this.setChanged();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<BorderBreakerEntity> controller = new AnimationController<>(this, "controller", 0, this::predicate);

        controller.triggerableAnim("border_breaker.idle", RawAnimation.begin().thenLoop("border_breaker.idle"));
        controller.triggerableAnim("border_breaker.drill", RawAnimation.begin().thenLoop("border_breaker.drill"));
        controller.triggerableAnim("border_breaker.deploy", RawAnimation.begin().thenLoop("border_breaker.deploy"));
        controller.triggerableAnim("border_breaker.break", RawAnimation.begin().thenPlay("border_breaker.break"));

        controllers.add(controller);
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        return tAnimationState.setAndContinue(RawAnimation.begin().thenPlay("border_breaker.deploy").thenLoop("border_breaker.idle"));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
