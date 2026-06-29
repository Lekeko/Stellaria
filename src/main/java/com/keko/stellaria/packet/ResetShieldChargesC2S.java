package com.keko.stellaria.packet;

import com.keko.stellaria.packet.networking.ModMessagesClient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record ResetShieldChargesC2S(int amount) implements CustomPacketPayload {
    public static final Type<ResetShieldChargesC2S> ID = new Type<>(ModMessagesClient.RESET_SHIELD_CHARGES);
    public static final StreamCodec<RegistryFriendlyByteBuf, ResetShieldChargesC2S> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ResetShieldChargesC2S::amount,


            ResetShieldChargesC2S::new);

    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}