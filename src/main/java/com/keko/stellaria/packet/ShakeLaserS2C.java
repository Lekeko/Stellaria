package com.keko.stellaria.packet;

import com.keko.stellaria.packet.networking.ModMessagesServer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record ShakeLaserS2C(float amp) implements CustomPacketPayload {
    public static final Type<ShakeLaserS2C> ID = new Type<>(ModMessagesServer.SHAKE_LASER);
    public static final StreamCodec<RegistryFriendlyByteBuf, ShakeLaserS2C> CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, ShakeLaserS2C::amp,

            ShakeLaserS2C::new);

    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}