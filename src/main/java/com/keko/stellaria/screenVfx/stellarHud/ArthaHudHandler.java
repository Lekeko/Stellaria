package com.keko.stellaria.screenVfx.stellarHud;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.packet.SendHomeC2S;
import com.keko.stellaria.screenVfx.screenShake.ScreenShakeHandler;
import com.keko.stellaria.world.dimension.ModDimensions;
import com.mojang.blaze3d.vertex.PoseStack;
import foundry.veil.api.client.util.Easing;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;

import java.awt.*;
import java.util.Random;


public class ArthaHudHandler {
    private static float ticker = -1;
    private static int wave = 0;
    private static int alive = 0;
    private static int toSpawn = 0;
    private static int boss;

    public static void setAlive(int alive) {
        ArthaHudHandler.alive = alive;
    }


    public static void setToSpawn(int toSpawn) {
        ArthaHudHandler.toSpawn = toSpawn;
    }

    public static void setWave(int wave) {
        ArthaHudHandler.wave = wave;
    }

    public static void beginTicker(){
        ticker = 0;
    }

    public static void endTicker(){
        ticker = -1;
    }

    private static boolean timeToGo = false;
    private static float endAnimation = 0.0f;
    private static final float MAX_END_ANIM_TIME = 5 * 20.0f;

    public static void tick(GuiGraphics guiGraphics) {
        if (timeToGo){
            renderTheFinaleScreen(guiGraphics);
            ticker = -1;
        }

        if (ticker != -1 && Minecraft.getInstance().player != null && Minecraft.getInstance().player.level().dimension().equals(ModDimensions.ARTHA_LEVEL_KEY)){
            timeToGo = false;
            ticker += Minecraft.getInstance().getTimer().getGameTimeDeltaTicks();
            float pullUpHudTimer = 20;
            float pullUpHud = Mth.clamp(ticker/pullUpHudTimer, 0.0f, 1.0f);
            pullUpHud = Easing.EASE_OUT_CIRC.ease(pullUpHud);

            float scale = 1.6f;
            int spriteWidth = (int) (50 * scale);
            int spriteHeight = (int) (100 * scale);
            int x = (int) (guiGraphics.guiWidth() - spriteWidth * pullUpHud);
            int y = guiGraphics.guiHeight()/2 - spriteHeight / 2;

            Color waveColor = new Color(202, 255, 249, 255);
            Color aliveColor = new Color(255, 158, 166, 255);
            Color remainingColor = new Color(219, 149, 255, 255);

            PoseStack poseStack = guiGraphics.pose();

            renderHudBase(x, y, poseStack, guiGraphics, waveColor, aliveColor, remainingColor, spriteWidth, spriteHeight, scale);
            renderNumbers(x, y, poseStack, guiGraphics, waveColor, aliveColor, remainingColor, spriteWidth, spriteHeight, scale);





        }

    }

    private static void renderNumbers(int x, int y, PoseStack poseStack, GuiGraphics guiGraphics, Color waveColor, Color aliveColor, Color remainingColor, int spriteWidth, int spriteHeight, float scale) {
        x += spriteWidth/2;
        y += spriteHeight/2;

        int xSpacer = (int) (5 * scale);
        int ySpacer = (int) (spriteHeight / 3.4f);

        x += xSpacer;

        float numberScale = 2.0f;
        y -= (int) (4 * numberScale);
        poseStack.pushPose();
        poseStack.translate(x,y+ 5,0);
        poseStack.scale(numberScale,numberScale,numberScale);
        guiGraphics.drawCenteredString(
                Minecraft.getInstance().font,
                String.valueOf(wave),
                0,0,waveColor.getRGB()
        );
        poseStack.scale(0.5f, 0.5f, 0.5f);
        poseStack.translate(0, -13 ,0);
        guiGraphics.drawCenteredString(
                Minecraft.getInstance().font,
                "Wave",
                0,0,waveColor.getRGB()
        );
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(x,y - ySpacer,0);
        guiGraphics.drawCenteredString(
                Minecraft.getInstance().font,
                String.valueOf(toSpawn),
                0,0,remainingColor.getRGB()
        );
        poseStack.scale(0.8f, 0.8f, 0.8f);
        poseStack.translate(0, 12 ,0);
        guiGraphics.drawCenteredString(
                Minecraft.getInstance().font,
                "Coming",
                0,0,remainingColor.getRGB()
        );
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(x,y + ySpacer + 11,0);
        guiGraphics.drawCenteredString(
                Minecraft.getInstance().font,
                String.valueOf(alive),
                0,0,aliveColor.getRGB()
        );
        poseStack.scale(0.8f, 0.8f, 0.8f);
        poseStack.translate(0, -12 ,0);
        guiGraphics.drawCenteredString(
                Minecraft.getInstance().font,
                "Alive",
                0,0,aliveColor.getRGB()
        );
        poseStack.popPose();
    }

    private static void renderHudBase(int x, int y, PoseStack poseStack, GuiGraphics guiGraphics, Color waveColor, Color aliveColor, Color remainingColor, int spriteWidth, int spriteHeight, float scale) {
        poseStack.pushPose();
        poseStack.translate(x,y,0);
        guiGraphics.blit(
                Stellaria.id("textures/hud/stellaria_menu/event_data.png"),
                0, 0,
                0, 0,
                spriteWidth, spriteHeight,
                spriteWidth, spriteHeight
        );
        poseStack.popPose();
    }

    public static void setTimeToGo(boolean timeToGo, int bosss) {
        ArthaHudHandler.timeToGo = timeToGo;
        boss = bosss;
    }

    private static void renderTheFinaleScreen(GuiGraphics guiGraphics) {
        if (endAnimation < MAX_END_ANIM_TIME)
            endAnimation += Minecraft.getInstance().getTimer().getGameTimeDeltaTicks();
        else {
            ClientPlayNetworking.send(new SendHomeC2S(boss));
            timeToGo = false;
            endAnimation = 0;
        }

        float shakeFactor = endAnimation/MAX_END_ANIM_TIME;
        float shakeScale = 2.3f;
        ScreenShakeHandler.beginShake(shakeFactor * shakeScale, 20);
        Random random = new Random();
        guiGraphics.pose().translate(
                (random.nextFloat() - 0.5f) * shakeScale * shakeFactor,
                (random.nextFloat() - 0.5f) * shakeScale * shakeFactor,
                (random.nextFloat() - 0.5f) * shakeScale * shakeFactor
        );

        int alpha = (int) (shakeFactor * 255);
        int color = FastColor.ARGB32.color(alpha, 255, 255, 255);

        guiGraphics.fill(0, 0, guiGraphics.guiWidth(), guiGraphics.guiHeight(), color);

    }
}
