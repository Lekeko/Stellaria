package com.keko.stellaria.packet;

import com.keko.stellaria.packet.networking.ModMessagesClient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record StellarEventHudSwitchS2C(
        int onOffIdle,
        int wave,
        int alive,
        int toSpawn,
        int intermision
        ) implements CustomPacketPayload {
    public static final Type<StellarEventHudSwitchS2C> ID = new Type<>(ModMessagesClient.STELLAR_EVENT_HUD_SWITCH);
    public static final StreamCodec<RegistryFriendlyByteBuf, StellarEventHudSwitchS2C> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, StellarEventHudSwitchS2C::onOffIdle,
            ByteBufCodecs.INT, StellarEventHudSwitchS2C::wave,
            ByteBufCodecs.INT, StellarEventHudSwitchS2C::alive,
            ByteBufCodecs.INT, StellarEventHudSwitchS2C::toSpawn,
            ByteBufCodecs.INT, StellarEventHudSwitchS2C::intermision,

            StellarEventHudSwitchS2C::new);

    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}