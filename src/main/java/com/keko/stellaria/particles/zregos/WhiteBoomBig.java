package com.keko.stellaria.particles.zregos;

import foundry.veil.api.client.util.Easing;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class WhiteBoomBig extends SimpleAnimatedParticle {
    private final float MAX_LIFE = 20;
    private float ticker = 0;
    private float oQuadSize;
    private final SpriteSet sprites;

    public WhiteBoomBig(ClientLevel clientLevel, double x, double y, double z, double xd, double yd, double zd, SpriteSet spriteSet) {
        super(clientLevel, x, y, z, spriteSet, 0.0F);
        this.sprites = spriteSet;

        this.friction = 0.9f;
        this.lifetime = (int) MAX_LIFE;
        this.setColor(1.0f, 1.0f, 1.0f);

        this.setSpriteFromAge(spriteSet);

    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        this.oRoll = this.roll;
        this.oQuadSize = this.quadSize;

        this.quadSize = (float) (Math.pow(Easing.EASE_OUT_EXPO.ease(age / MAX_LIFE), 0.9f) * 20);
        this.alpha = 1 - age / MAX_LIFE;

        super.tick();
        this.setSpriteFromAge(sprites);
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
            return new WhiteBoomBig(clientLevel, d, e, f, g, h, i, this.spriteSet);
        }
    }
}