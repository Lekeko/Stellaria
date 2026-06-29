package com.keko.stellaria.mixin;

import net.minecraft.client.Camera;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Camera.class)
public interface CameraAccessor {
    @Accessor("xRot")
    float Stellaria$getXRot();
    @Accessor("yRot")
    float Stellaria$getYRot();


    @Accessor("xRot")
    void Stellaria$setXRot(float rotationX);
    @Accessor("yRot")
    void Stellaria$setYRot(float rotationY);


    @Accessor("position")
    Vec3 Stellaria$getPos();
    @Accessor("position")
    void Stellaria$setPos(Vec3 pos);

}
