package com.keko.stellaria.packet;

import com.keko.stellaria.packet.networking.ModMessagesServer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public record PhantomTPPartS2C(Vector3f start) implements CustomPacketPayload {
    public static final Type<PhantomTPPartS2C> ID = new Type<>(ModMessagesServer.PHANTOM_TP_PART);
    public static final StreamCodec<RegistryFriendlyByteBuf, PhantomTPPartS2C> CODEC = StreamCodec.composite(
            ByteBufCodecs.VECTOR3F, PhantomTPPartS2C::start,

            PhantomTPPartS2C::new);

    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}