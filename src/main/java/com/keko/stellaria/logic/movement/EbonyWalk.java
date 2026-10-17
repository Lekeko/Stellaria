package com.keko.stellaria.logic.movement;

import com.keko.stellaria.effects.ModStatusEffects;
import com.keko.stellaria.renderHelpers.HandleAnimICANTTAKEITANYMORE;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public class EbonyWalk {

    public static double deltaX;
    public static double deltaZ;
    private static float dodgeTimer = 0;
    private static boolean wasSprinting = false;



    public static float getEbonyJump(){
        return 3.1f;
    }
    public static float getEbonySpeed(){
        return 1.0f;
    }

    public static void movementTick(Player player, float forwardImpulse, float leftImpulse, boolean jump, boolean shift){
        double yawRad = Math.toRadians(player.getYRot());

        double deltaX_forward = -Mth.sin((float) yawRad) * forwardImpulse;
        double deltaZ_forward =  Mth.cos((float) yawRad) * forwardImpulse;

        double deltaX_left =  Mth.cos((float) yawRad) * leftImpulse;
        double deltaZ_left =  Mth.sin((float) yawRad) * leftImpulse;

        if (dodgeTimer > 0) dodgeTimer--;
        if (dodgeTimer < 0) dodgeTimer++;

        deltaX = deltaX_forward + deltaX_left;
        deltaZ = deltaZ_forward + deltaZ_left;

        float speed= 1.4f;
        if (player.onGround())
            speed*= 3.0f;

        if (player.isSprinting())
            speed *=1.3f;

        deltaX *=speed;
        deltaZ *=speed;


        if (!player.isFallFlying()){
            player.push(deltaX / 20, 0, deltaZ / 20);
        }



        float deltaY = -player.getXRot();

        if (wasSprinting != shift && dodgeTimer >= 0)
            dodgeTimer += 50;

        if (dodgeTimer >= 110 && player.getFoodData().getFoodLevel() > 6 && !player.isFallFlying())
            dodgePlayer(player, deltaX,deltaY / 10, deltaZ);

        player.hurtMarked = true;
        wasSprinting = shift;


        player.hurtMarked = true;

    }

    private static void dodgePlayer(Player player,double deltaX,double deltaY, double deltaZ) {
        float dashMult = 1.8f;
        deltaX *= dashMult;
        deltaZ *= dashMult;

        player.startAutoSpinAttack(10, 15.0F, null);
        player.push(deltaX, deltaY, deltaZ);
        dodgeTimer = -80;
        player.getFoodData().addExhaustion(2.0f);
    }

}
