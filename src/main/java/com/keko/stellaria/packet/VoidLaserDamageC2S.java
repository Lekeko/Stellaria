package com.keko.stellaria.packet;

import com.keko.stellaria.packet.networking.ModMessagesServer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public record VoidLaserDamageC2S(Vector3f start, Vector3f end, int id_messier) implements CustomPacketPayload {
    public static final Type<VoidLaserDamageC2S> ID = new Type<>(ModMessagesServer.VOID_LASER_DAMAGE);
    public static final StreamCodec<RegistryFriendlyByteBuf, VoidLaserDamageC2S> CODEC = StreamCodec.composite(
            ByteBufCodecs.VECTOR3F, VoidLaserDamageC2S::start,
            ByteBufCodecs.VECTOR3F, VoidLaserDamageC2S::end,
            ByteBufCodecs.INT, VoidLaserDamageC2S::id_messier,

            VoidLaserDamageC2S::new);

    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}