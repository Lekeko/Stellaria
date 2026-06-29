package com.keko.stellaria.packet;

import com.keko.stellaria.packet.networking.ModMessagesClient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record RenderZregosLaserS2C(int id, boolean active, boolean modifySky, float amp) implements CustomPacketPayload {
    public static final Type<RenderZregosLaserS2C> ID = new Type<>(ModMessagesClient.ZREGOS_LASER);
    public static final StreamCodec<RegistryFriendlyByteBuf, RenderZregosLaserS2C> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, RenderZregosLaserS2C::id,
            ByteBufCodecs.BOOL, RenderZregosLaserS2C::active,
            ByteBufCodecs.BOOL, RenderZregosLaserS2C::modifySky,
            ByteBufCodecs.FLOAT, RenderZregosLaserS2C::amp,

            RenderZregosLaserS2C::new);

    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}