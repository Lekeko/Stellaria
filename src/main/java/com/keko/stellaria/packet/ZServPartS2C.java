package com.keko.stellaria.packet;

import com.keko.stellaria.packet.networking.ModMessagesClient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record ZServPartS2C(int id) implements CustomPacketPayload {
    public static final Type<ZServPartS2C> ID = new Type<>(ModMessagesClient.ZSERV_PARTICLES);
    public static final StreamCodec<RegistryFriendlyByteBuf, ZServPartS2C> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ZServPartS2C::id,

            ZServPartS2C::new);

    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}