package com.keko.stellaria.screenVfx;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.component.ModDataComponentTypes;
import com.keko.stellaria.items.ModItems;
import com.keko.stellaria.renderHelpers.ScreenHelpers;
import com.keko.stellaria.screenVfx.chains.ZregosChain;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import foundry.veil.api.client.util.Easing;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class ScreenVfxHandler {
    private static ArrayList<ZregosChain> chains = new ArrayList<>();
    private static ArrayList<Entity> boulders = new ArrayList<>();

    public static ArrayList<ZregosChain> getChains() {
        return chains;
    }

    public static void initChains(){

        int chainNr = 4;
        float rotationStr = 6767f;
        int x = Minecraft.getInstance().getWindow().getGuiScaledWidth();

        for (int i = 0; i < chainNr; i++){
            ZregosChain zregosChain = new ZregosChain(i * x/chainNr, (new Random().nextFloat() - 0.5f) * rotationStr);
            zregosChain.setxRand((new Random().nextFloat() - 0.5f) * 100f);
            chains.add(zregosChain);
        }
    }

    public static void clearChains(){
        chains.clear();
    }

    public static void chainScreen(GuiGraphics guiGraphics, Player player){
        if (!chains.isEmpty()){
            if (player.isDeadOrDying()){
                chains.clear();
            }

            for (int i = 0; i < chains.size(); i++){
                PoseStack poseStack = guiGraphics.pose();
                ZregosChain zregosChain = chains.get(i);
                float chainTravelTime = 20 ;

                float slide = zregosChain.getClientTicker();

                zregosChain.tick(i);

                float easedSlide = 1- Easing.EASE_OUT_EXPO.ease(Mth.clamp(slide/chainTravelTime, 0.0f, 1.0f));

                int texW = 1280;
                int texH = 32;
                float spacer = (float) guiGraphics.guiWidth() / (chains.size());

                float x = i * spacer + spacer/2 + zregosChain.getxRand();
                float y = guiGraphics.guiHeight() / 2.0f;


                float travelDist = texW * easedSlide;

                poseStack.pushPose();

                poseStack.translate(x, y, 0);
                poseStack.mulPose(Axis.ZN.rotationDegrees(zregosChain.getRotation()));

                poseStack.translate(0, travelDist, 0);
                poseStack.mulPose(Axis.ZN.rotationDegrees(90));
                poseStack.translate(
                        (player.getRandom().nextFloat() - 0.5f) * 1.5f,
                        (player.getRandom().nextFloat() - 0.5f) * 1.5f,
                        (player.getRandom().nextFloat() - 0.5f) * 1.5f
                );
                float xFucker = Mth.sin(player.getYRot() / 500.0f) * 50;

                //so fucking glad i randomly tought of this shit, its soo good looking like im a fucking genius, everyone should praise me for this shit right, here, isntead of adding the oder bosses o finishing zregos, im doing an overcomplicated stun screen beause why tf not
                poseStack.translate(player.getXRot()/ 70,xFucker , 0);

                guiGraphics.blit(
                        Stellaria.id("textures/normal_vfx/zregos_chain_screen.png"),
                        -texW / 2, -texH / 2,
                        0, 0,
                        texW, texH,
                        texW, texH
                );

                poseStack.popPose();
            }
        }


    }


    public static void boulderHitmarks(GuiGraphics guiGraphics, LocalPlayer player, DeltaTracker deltaTracker) {
        if (!boulders.isEmpty()){
            boulders.removeIf(Entity::isRemoved);
            float alphaTime = 40;

            for (Entity boulder : boulders){
                float alpha = Easing.EASE_OUT_EXPO.ease(Mth.clamp((1.0f - boulder.tickCount/alphaTime) , 0.0f, 1.0f));
                ScreenHelpers.renderQuadWithTexture(
                        guiGraphics,
                        Stellaria.id("textures/hud/hud_effects/hitmark.png"),
                        (int) (30 + alpha * 10f),
                        Color.WHITE,
                        boulder.getPosition(deltaTracker.getGameTimeDeltaTicks()).add(0.0f,3.5f,0.0f),
                        alpha,
                        false);
            }
        }


    }

    public static void addBoulder(Entity entity) {
        if (entity != null)
            boulders.add(entity);
    }


    private static float shieldChargeTimerHud = 0;
    public static void cosmicShieldCharge(GuiGraphics guiGraphics, LocalPlayer player) {
        ItemStack itemStack = player.getMainHandItem();

        if (itemStack.is(ModItems.CELESTIAL_SHIELD) && itemStack.has(ModDataComponentTypes.SHIELD_CHARGES)){
            int width = guiGraphics.guiWidth();
            int height = guiGraphics.guiHeight();
            int sizebg = 121;
            int size = 96;
            float maxTime = 30;
            if (shieldChargeTimerHud < maxTime)
                shieldChargeTimerHud += Minecraft.getInstance().getTimer().getGameTimeDeltaTicks();

            float widthE = Easing.EASE_OUT_EXPO.ease(shieldChargeTimerHud/maxTime) * 100;
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(widthE - 80,0,0);

            guiGraphics.blit(
                    Stellaria.id("textures/hud/weapons_vfx/shield_capacity_bg.png"),

                    (int) 0,
                    (int) height / 2 - sizebg / 2,
                    0,
                    0,
                    sizebg,
                    sizebg,
                    sizebg,
                    sizebg);

            guiGraphics.blit(
                    Stellaria.id("textures/hud/weapons_vfx/shield_capacity_" + itemStack.get(ModDataComponentTypes.SHIELD_CHARGES) +".png"),

                    (int) 60 - size / 2,
                    (int) height / 2 - size / 2,
                    0,
                    0,
                    size,
                    size,
                    size,
                    size);
            guiGraphics.pose().popPose();

        }else {
            shieldChargeTimerHud = 0;
        }
    }
}
