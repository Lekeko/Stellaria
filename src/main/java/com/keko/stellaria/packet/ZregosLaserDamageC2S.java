package com.keko.stellaria.packet;

import com.keko.stellaria.packet.networking.ModMessagesClient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public record ZregosLaserDamageC2S(Vector3f start, Vector3f end, int ID_ZREGOS) implements CustomPacketPayload {
    public static final Type<ZregosLaserDamageC2S> ID = new Type<>(ModMessagesClient.ZREGOS_LASER_DAMAGE);
    public static final StreamCodec<RegistryFriendlyByteBuf, ZregosLaserDamageC2S> CODEC = StreamCodec.composite(
            ByteBufCodecs.VECTOR3F, ZregosLaserDamageC2S::start,
            ByteBufCodecs.VECTOR3F, ZregosLaserDamageC2S::end,
            ByteBufCodecs.INT, ZregosLaserDamageC2S::ID_ZREGOS,

            ZregosLaserDamageC2S::new);

    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}