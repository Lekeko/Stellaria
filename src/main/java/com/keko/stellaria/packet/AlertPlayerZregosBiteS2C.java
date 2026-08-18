package com.keko.stellaria.packet;

import com.keko.stellaria.packet.networking.ModMessagesServer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record AlertPlayerZregosBiteS2C(int useless) implements CustomPacketPayload {
    public static final Type<AlertPlayerZregosBiteS2C> ID = new Type<>(ModMessagesServer.ALERT_ZREGOS_BITE);
    public static final StreamCodec<RegistryFriendlyByteBuf, AlertPlayerZregosBiteS2C> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, AlertPlayerZregosBiteS2C::useless,

            AlertPlayerZregosBiteS2C::new);

    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}