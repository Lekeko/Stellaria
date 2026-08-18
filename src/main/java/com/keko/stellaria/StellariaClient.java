package com.keko.stellaria;

import com.keko.stellaria.entity.ModEntitiesRenderer;
import com.keko.stellaria.keyBinds.KeyBindings;
import com.keko.stellaria.midnightLib.StellariaConfigs;
import com.keko.stellaria.packet.networking.ModMessagesClient;
import com.keko.stellaria.particles.ModParticlesBuilder;
import com.keko.stellaria.renderHelpers.ZregosBite;
import com.keko.stellaria.screenVfx.ScreenVfxHandler;
import com.keko.stellaria.screenVfx.screenShake.ScreenShakeHandler;
import com.keko.stellaria.sounds.ModSounds;
import com.keko.stellaria.stellarEvent.ArthaDimensionHandler;
import com.keko.stellaria.screenVfx.stellarHud.ArthaHudHandler;
import com.keko.stellaria.world.dimension.ModDimensions;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Minecraft;

public class StellariaClient implements ClientModInitializer {
    float ticker = 0;


    @Override
    public void onInitializeClient() {
        init();
        events();
        WorldRenderEvents.START.register((ctx -> {

        }));
    }

    private void init() {
        MidnightConfig.init(Stellaria.MOD_ID, StellariaConfigs.class);
        ModSounds.registerSounds();
        ModMessagesClient.registerS2CPacket();
        KeyBindings.registerKeybindings();
        ModEntitiesRenderer.register();
        ModParticlesBuilder.init();

    }

    private void events(){

        ClientTickEvents.END_CLIENT_TICK.register(minecraft -> {

        });

        WorldRenderEvents.AFTER_SETUP.register((ctx)->{
            if (ctx.world() != null){
                if (ctx.world().dimension() == ModDimensions.ARTHA_LEVEL_KEY) {
                    ArthaDimensionHandler.handleSkybox();
                }
            }
        });

        WorldRenderEvents.START.register((ctx)->{
            if (ctx.world() != null){
                if (ctx.world().dimension() == ModDimensions.ARTHA_LEVEL_KEY) {
                    ScreenShakeHandler.tick(ctx);
                }
            }
        });

        HudRenderCallback.EVENT.register(((guiGraphics, deltaTracker) -> {
            ZregosBite.tick(guiGraphics);
            ScreenVfxHandler.chainScreen(guiGraphics, Minecraft.getInstance().player);
            ScreenVfxHandler.boulderHitmarks(guiGraphics, Minecraft.getInstance().player, deltaTracker);
            ScreenVfxHandler.cosmicShieldCharge(guiGraphics, Minecraft.getInstance().player);
            ArthaHudHandler.tick(guiGraphics);
        }));

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            ScreenVfxHandler.getChains().clear();
        });
    }


}
