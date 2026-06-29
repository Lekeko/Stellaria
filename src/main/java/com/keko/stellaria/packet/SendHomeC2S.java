package com.keko.stellaria.packet;

import com.keko.stellaria.packet.networking.ModMessagesClient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record SendHomeC2S(int id) implements CustomPacketPayload {
    public static final Type<SendHomeC2S> ID = new Type<>(ModMessagesClient.SEND_HOME);
    public static final StreamCodec<RegistryFriendlyByteBuf, SendHomeC2S> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, SendHomeC2S::id,

            SendHomeC2S::new);

    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}