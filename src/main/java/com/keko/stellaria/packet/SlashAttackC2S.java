package com.keko.stellaria.packet;

import com.keko.stellaria.packet.networking.ModMessagesClient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public record SlashAttackC2S(int SWORD_TYPE) implements CustomPacketPayload {
    public static final Type<SlashAttackC2S> ID = new Type<>(ModMessagesClient.SLASH_ATTACK);
    public static final StreamCodec<RegistryFriendlyByteBuf, SlashAttackC2S> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, SlashAttackC2S::SWORD_TYPE,

            SlashAttackC2S::new);

    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}