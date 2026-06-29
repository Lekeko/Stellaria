package com.keko.stellaria.packet;

import com.keko.stellaria.packet.networking.ModMessagesClient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record ArthaMovementC2S(float forwardImpulse, float leftImpulse, boolean jump, boolean shift) implements CustomPacketPayload {
    public static final Type<ArthaMovementC2S> ID = new Type<>(ModMessagesClient.ARTHA_MOVEMENT);
    public static final StreamCodec<RegistryFriendlyByteBuf, ArthaMovementC2S> CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, ArthaMovementC2S::forwardImpulse,
            ByteBufCodecs.FLOAT, ArthaMovementC2S::leftImpulse,
            ByteBufCodecs.BOOL, ArthaMovementC2S::jump,
            ByteBufCodecs.BOOL, ArthaMovementC2S::shift,

            ArthaMovementC2S::new);

    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}