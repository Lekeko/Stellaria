package com.keko.stellaria.packet;

import com.keko.stellaria.packet.networking.ModMessagesServer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record BlackHoleShaderS2C(float tranzProg, float sphereRadius, double x, double y, double z) implements CustomPacketPayload {
    public static final Type<BlackHoleShaderS2C> ID = new Type<>(ModMessagesServer.BLACK_HOLE_SHADER);
    public static final StreamCodec<RegistryFriendlyByteBuf, BlackHoleShaderS2C> CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, BlackHoleShaderS2C::tranzProg,
            ByteBufCodecs.FLOAT, BlackHoleShaderS2C::sphereRadius,
            ByteBufCodecs.DOUBLE, BlackHoleShaderS2C::x,
            ByteBufCodecs.DOUBLE, BlackHoleShaderS2C::y,
            ByteBufCodecs.DOUBLE, BlackHoleShaderS2C::z,

            BlackHoleShaderS2C::new);

    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}