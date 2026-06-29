package com.keko.stellaria.screenVfx.chains;

import net.minecraft.client.Minecraft;

public class ZregosChain {
    private float xRand = 0;
    private final float rotation;
    private float clientTicker = 0;

    public float getRotation() {
        return rotation;
    }

    public float getxRand() {
        return xRand;
    }

    public void setxRand(float xRand) {
        this.xRand = xRand;
    }

    public ZregosChain(float xRand, float rotation){
        this.xRand = xRand;
        this.rotation = rotation;
    }

    public void tick(int i){
        if (clientTicker < 20 && clientTicker >= 0) {
            clientTicker += Minecraft.getInstance().getTimer().getGameTimeDeltaTicks() /2 * (1+ i);
        }else
            clientTicker = 20;

    }

    public float getClientTicker() {
        return clientTicker;
    }

    public void setClientTicker(float clientTicker) {
        this.clientTicker = clientTicker;
    }
}
