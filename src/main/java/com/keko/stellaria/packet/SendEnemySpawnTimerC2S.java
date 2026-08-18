package com.keko.stellaria.packet;

import com.keko.stellaria.packet.networking.ModMessagesServer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record SendEnemySpawnTimerC2S(int second) implements CustomPacketPayload {
    public static final Type<SendEnemySpawnTimerC2S> ID = new Type<>(ModMessagesServer.SEND_ENEMYSPAWN_TIMER);
    public static final StreamCodec<RegistryFriendlyByteBuf, SendEnemySpawnTimerC2S> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, SendEnemySpawnTimerC2S::second,

            SendEnemySpawnTimerC2S::new);

    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}