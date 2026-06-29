package com.keko.stellaria.packet;

import com.keko.stellaria.packet.networking.ModMessagesClient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record BeginEndAnimS2C(int id) implements CustomPacketPayload {
    public static final Type<BeginEndAnimS2C> ID = new Type<>(ModMessagesClient.BEGIN_END_ANIM);
    public static final StreamCodec<RegistryFriendlyByteBuf, BeginEndAnimS2C> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, BeginEndAnimS2C::id,

            BeginEndAnimS2C::new);

    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}