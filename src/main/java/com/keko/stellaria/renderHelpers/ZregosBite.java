package com.keko.stellaria.renderHelpers;

import com.keko.stellaria.Stellaria;
import com.mojang.blaze3d.systems.RenderSystem;
import foundry.veil.api.client.util.Easing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public abstract class ZregosBite {
    private static float ticker = 0;
    private static final int MAX_LIFE= 40;
    private static boolean active = false;

    public static void setActive(boolean active) {
        ZregosBite.active = active;
        ticker = 0;
    }

    public void setTicker(float ticker) {
        this.ticker = ticker;
    }

    public static void tick(GuiGraphics guiGraphics){
        if (active){
            if (ticker < MAX_LIFE) {
                ticker+= Minecraft.getInstance().getTimer().getGameTimeDeltaTicks() * 3;
                float easedVal = Easing.EASE_OUT_EXPO.ease((float) ticker /MAX_LIFE) * 100;
                RenderSystem.enableBlend();
                RenderSystem.setShaderColor(1.0f,1.0f,1.0f, (MAX_LIFE-ticker) /MAX_LIFE);

                guiGraphics.blit(
                        Stellaria.id("textures/hud/hud_effects/bite_up.png"),
                        0,
                        (int) ((int) -140+easedVal),
                        0,
                        0,
                        guiGraphics.guiWidth(),
                        guiGraphics.guiHeight(),
                        guiGraphics.guiWidth(),
                        guiGraphics.guiHeight()
                );
                guiGraphics.blit(
                        Stellaria.id("textures/hud/hud_effects/bite_down.png"),
                        0,
                        (int) ((int) 140-easedVal),
                        0,
                        0,
                        guiGraphics.guiWidth(),
                        guiGraphics.guiHeight(),
                        guiGraphics.guiWidth(),
                        guiGraphics.guiHeight()
                );
            }else active = false;
        }
    }


}
