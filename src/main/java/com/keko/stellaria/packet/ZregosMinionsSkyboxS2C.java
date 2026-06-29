package com.keko.stellaria.packet;

import com.keko.stellaria.packet.networking.ModMessagesClient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record ZregosMinionsSkyboxS2C(float color) implements CustomPacketPayload {
    public static final Type<ZregosMinionsSkyboxS2C> ID = new Type<>(ModMessagesClient.ZREGOS_MINIONS_SKYBOX);
    public static final StreamCodec<RegistryFriendlyByteBuf, ZregosMinionsSkyboxS2C> CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, ZregosMinionsSkyboxS2C::color,

            ZregosMinionsSkyboxS2C::new);

    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}