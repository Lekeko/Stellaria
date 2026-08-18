package com.keko.stellaria.packet;

import com.keko.stellaria.packet.networking.ModMessagesServer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record IdleSkyboxS2C(float smth) implements CustomPacketPayload {
    public static final Type<IdleSkyboxS2C> ID = new Type<>(ModMessagesServer.IDLE_SKYBOX);
    public static final StreamCodec<RegistryFriendlyByteBuf, IdleSkyboxS2C> CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, IdleSkyboxS2C::smth,

            IdleSkyboxS2C::new);

    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}