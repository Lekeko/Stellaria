package com.keko.stellaria.packet;

import com.keko.stellaria.packet.networking.ModMessagesServer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record PlayZregosLaserSoundC2S(int I) implements CustomPacketPayload {
    public static final Type<PlayZregosLaserSoundC2S> ID = new Type<>(ModMessagesServer.PLAY_ZREGOS_LASER_SOUND);
    public static final StreamCodec<RegistryFriendlyByteBuf, PlayZregosLaserSoundC2S> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, PlayZregosLaserSoundC2S::I,

            PlayZregosLaserSoundC2S::new);

    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}