package com.keko.stellaria.logic.movement;

import com.keko.stellaria.effects.ModStatusEffects;
import com.keko.stellaria.renderHelpers.HandleAnimICANTTAKEITANYMORE;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public abstract class ArthaGlide {
    private static boolean wasSprinting = false;
    private static float dodgeTimer = 0;

    public static double deltaX;
    public static double deltaZ;


    public static void glidePLayer(Player player, float forwardImpulse, float leftImpulse, boolean jump, boolean shift){
        if (player.isCreative())
            return;


        if (dodgeTimer > 0) dodgeTimer--;
        if (dodgeTimer < 0) dodgeTimer++;
        double yawRad = Math.toRadians(player.getYRot());

        double deltaX_forward = -Mth.sin((float) yawRad) * forwardImpulse;
        double deltaZ_forward =  Mth.cos((float) yawRad) * forwardImpulse;

        double deltaX_left =  Mth.cos((float) yawRad) * leftImpulse;
        double deltaZ_left =  Mth.sin((float) yawRad) * leftImpulse;


        deltaX = deltaX_forward + deltaX_left;
        deltaZ = deltaZ_forward + deltaZ_left;

        double deltaY = 0.0;
        if (shift && !jump) deltaY = -0.03;
        if (!shift && jump) deltaY =  0.03;

        if (forwardImpulse !=0)
            deltaY-=player.getXRot() / 3000;

        float speed= 3.3f;

        deltaX *=speed;
        deltaY *=speed;
        deltaZ *=speed;

        float ticker = Minecraft.getInstance().getTimer().getGameTimeDeltaTicks() * 30;

        float impulseStr = 80;
        if (deltaZ != 0) {
           HandleAnimICANTTAKEITANYMORE.xImpulse = ((float) deltaZ > 0 ? 1 : -1) * impulseStr;
        }
        if (deltaX != 0) {
            HandleAnimICANTTAKEITANYMORE.zImpulse = ((float) deltaX > 0 ? 1 : -1) * impulseStr;
        }



        player.push(deltaX/20, deltaY, deltaZ/20);


        if (wasSprinting != shift && dodgeTimer >= 0)
            dodgeTimer += 50;

        if (dodgeTimer >= 110)
            dodgePlayer(player, deltaX, deltaZ);

        player.hurtMarked = true;
        wasSprinting = shift;

        if (player.hasEffect(ModStatusEffects.CHAINED)){
            player.setDeltaMovement(0.0f,0.0f,0.0f);
        }

    }

    private static void dodgePlayer(Player player,double deltaX, double deltaZ) {
        player.push(deltaX, 0, deltaZ);
        dodgeTimer = -30;
    }

    private static void dodgePlayer(float forwardImpulse) {

    }

}
