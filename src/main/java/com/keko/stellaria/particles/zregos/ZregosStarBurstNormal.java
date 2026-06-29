package com.keko.stellaria.particles.zregos;

import foundry.veil.api.client.util.Easing;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class ZregosStarBurstNormal extends SimpleAnimatedParticle {
    private final float MAX_LIFE = 80;
    private float ticker = 0;
    private float oQuadSize;
    private boolean revRot = false;

    public ZregosStarBurstNormal(ClientLevel clientLevel, double x, double y, double z, double xd, double yd, double zd, SpriteSet spriteSet) {
        super(clientLevel, x, y, z, spriteSet, 0.0125F);

        this.friction = 0.9f;
        this.lifetime = (int) MAX_LIFE;

        Color fuckasscolor1 = new Color(150, 255, 245);
        Color fuckasscolor2 = new Color(248, 148, 255);
        this.setColor(clientLevel.random.nextBoolean() ? fuckasscolor2 : fuckasscolor1);

        revRot = random.nextBoolean();

        float radius = 5;

        double u = Math.random();
        double v = Math.random();
        double theta = 2 * Math.PI * u;
        double phi = Math.acos(2 * v - 1);
        this.xd = (radius * Math.sin(phi) * Math.cos(theta));
        this.yd = (radius * Math.sin(phi) * Math.sin(theta));
        this.zd = (radius * Math.cos(phi));

        this.setSpriteFromAge(spriteSet);

    }

    private void setColor(Color color) {
        this.setColor(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f);
        if (random.nextFloat() < 0.3f)
            this.setColor(1.0f,1.0f,1.0f);

    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        this.oRoll = this.roll;
        this.oQuadSize = this.quadSize;

        if (ticker < MAX_LIFE){
            ticker ++;
        }


        this.quadSize = (1 - Easing.EASE_OUT_EXPO.ease(ticker / MAX_LIFE));
        this.alpha = this.quadSize;
        this.roll = Easing.EASE_OUT_EXPO.ease(ticker /MAX_LIFE) * 2 * (revRot ? 1.0f : -1.0f);

        super.tick();
    }

    @Override
    public float getQuadSize(float partialTicks) {
        return this.oQuadSize + (this.quadSize - this.oQuadSize) * partialTicks;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public @Nullable Particle createParticle(SimpleParticleType particleOptions, ClientLevel clientLevel, double d, double e, double f, double g, double h, double i) {
            return new ZregosStarBurstNormal(clientLevel, d, e, f, g, h, i, this.spriteSet);
        }
    }
}