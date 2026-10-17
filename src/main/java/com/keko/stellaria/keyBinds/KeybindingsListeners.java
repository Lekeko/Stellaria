package com.keko.stellaria.keyBinds;

import com.keko.stellaria.component.ModDataComponentTypes;
import com.keko.stellaria.items.ModItems;
import com.keko.stellaria.items.stellariaWeaponTypes.StellarSwordWeapon;
import com.keko.stellaria.logic.StellariaConstants;
import com.keko.stellaria.packet.ArthaMovementC2S;
import com.keko.stellaria.packet.ResetShieldChargesC2S;
import com.keko.stellaria.packet.SlashAttackC2S;
import com.keko.stellaria.world.dimension.ModDimensions;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;

public abstract class KeybindingsListeners {
    static int swingCurrentCooldown = 0;

    public static void movementListener(Minecraft client){


        if (client.player == null || client.level == null) return;

        Input input = client.player.input;
        float forwardImpulse = input.forwardImpulse;
        float leftImpulse = input.leftImpulse;
        boolean jump = input.jumping;
        boolean shift = input.shiftKeyDown;
        if (client.player.level().dimension() == ModDimensions.ARTHA_LEVEL_KEY || client.player.level().dimension() == ModDimensions.EBONY_LEVEL_KEY)
            ClientPlayNetworking.send(new ArthaMovementC2S(forwardImpulse, leftImpulse, jump, shift));
    }

    public static void checkSwordSlash(Minecraft client) {
        if (client.player == null || client.level == null) return;
        int swingCooldown = 10;

        if (swingCurrentCooldown > 0)
            swingCurrentCooldown--;

        ItemStack stack = client.player.getItemInHand(InteractionHand.MAIN_HAND);

        if (client.player.level().dimension() == ModDimensions.ARTHA_LEVEL_KEY || client.player.level().dimension() == ModDimensions.EBONY_LEVEL_KEY || stack.getOrDefault(ModDataComponentTypes.VOID_GLAIVE_ACTIVE,0) > 0){
            if (client.options.keyAttack.isDown() && swingCurrentCooldown <= 0 && (client.player.getMainHandItem().getItem() instanceof SwordItem heldItem)){ //dafuq is a pattern variable intellij???
                int sword = StellariaConstants.VANILLA.ordinal();
                swingCurrentCooldown = swingCooldown;
                //TODO modded swords
                if (client.player.getMainHandItem().getItem() instanceof StellarSwordWeapon)
                    sword = ((StellarSwordWeapon)client.player.getMainHandItem().getItem()).getSwordType();


                client.player.playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 0.2f, 1f);

                client.player.swing(client.player.getUsedItemHand());
                ClientPlayNetworking.send(new SlashAttackC2S(sword));
            }
        }




    }

    public static void checkCelestialShieldAttack(Minecraft client) {
        if (client.player == null || client.level == null) return;

        ItemStack itemStack = client.player.getMainHandItem();

        if (itemStack.is(ModItems.CELESTIAL_SHIELD) && itemStack.has(ModDataComponentTypes.SHIELD_CHARGES)) {
            int nrOfCharges = itemStack.get(ModDataComponentTypes.SHIELD_CHARGES);

            if (client.options.keyAttack.isDown() && nrOfCharges > 0) {
                ClientPlayNetworking.send(new ResetShieldChargesC2S(nrOfCharges));;
            }
        }
    }
}
