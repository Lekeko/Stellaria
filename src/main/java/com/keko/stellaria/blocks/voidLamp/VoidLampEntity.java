package com.keko.stellaria.blocks.voidLamp;

import com.keko.stellaria.blocks.ModBlockEntity;
import com.keko.stellaria.sounds.ModSounds;
import com.mojang.math.Axis;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.light.data.AreaLightData;
import foundry.veil.api.client.render.light.data.PointLightData;
import foundry.veil.api.client.render.light.renderer.LightRenderHandle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class VoidLampEntity extends BlockEntity implements GeoBlockEntity {
    private static final Set<VoidLampEntity> ACTIVE_LAMPS = new HashSet<>();

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private PointLightData pointLightData;
    LightRenderHandle<PointLightData> handle;


    public VoidLampEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntity.VOID_LAMP_ENTITY_BLOCK, blockPos, blockState);

    }

    @Override
    public void setRemoved() {
        if (level.isClientSide){
            removeLight();
        }
        super.setRemoved();
    }


    public PointLightData getPointLightData() {
        return pointLightData;
    }

    public void removeLight(){
        if (handle != null) {
            handle.free();
            handle = null;
        }
        ACTIVE_LAMPS.remove(this);
    }

    public void addLight(){
        if (handle == null && !isRemoved()){
            this.setPointLightData(new PointLightData()
                    .setBrightness(4.480f)
                    .setColor(1.0f, 1.0f, 1.0f)
                    .setRadius(85.0f)
                    .setOcclusionEnabled(false)
                    .setInscatteringStrength(2.810f)
                    .setPosition(this.getBlockPos().getX() + 0.5f, this.getBlockPos().getY() + 0.5f + 18, this.getBlockPos().getZ() + 0.5f));

            handle = VeilRenderSystem.renderer().getLightRenderer().addLight(this.getPointLightData());
            ACTIVE_LAMPS.add(this);
        }
    }

    public void setPointLightData(PointLightData pointLightData) {
        this.pointLightData = pointLightData;
    }

    public LightRenderHandle<PointLightData> getHandle() {
        return handle;
    }

    public void setHandle(LightRenderHandle<PointLightData> handle) {
        this.handle = handle;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, VoidLampEntity entity) {

    }

    @Override
    public boolean isRemoved() {
        return super.isRemoved();
    }


    public static void clearAllLights() {
        for (VoidLampEntity lamp : new ArrayList<>(ACTIVE_LAMPS)) {//fck ur itterrators
            lamp.removeLight();
        }
        ACTIVE_LAMPS.clear();
    }

    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag, provider);

    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag, provider);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag tag = super.getUpdateTag(provider);
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

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<VoidLampEntity> controller = new AnimationController<>(this, "controller", 0, this::predicate);

        controllers.add(controller);
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
