package com.keko.stellaria.screenVfx.screenShake;

import com.keko.stellaria.mixin.CameraAccessor;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;

public class ScreenShakeHandler {
    public static float amp = 0;
    public static float time = 0;

    public static void tick(WorldRenderContext ctx){
        if (time > 0) {
            time--;
            if (Minecraft.getInstance().level != null) {
                Vec3 shake = new Vec3(
                        (Minecraft.getInstance().level.random.nextFloat() - .5f) * amp + ctx.camera().getPosition().x,
                        (Minecraft.getInstance().level.random.nextFloat() - .5f) * amp + ctx.camera().getPosition().y,
                        (Minecraft.getInstance().level.random.nextFloat() - .5f) * amp + ctx.camera().getPosition().z
                );


                ((CameraAccessor) ctx.camera()).Stellaria$setPos(shake);
            }
        }
    }

    public static void beginShake(float amp1, float time1){
        amp = amp1;
        time = time1;
    }
}
