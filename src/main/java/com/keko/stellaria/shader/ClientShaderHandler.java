package com.keko.stellaria.shader;

import com.keko.stellaria.midnightLib.StellariaConfigs;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
import java.util.HashMap;

public class ClientShaderHandler {

    public static void renderScreenFireShader(float screenX, float screenY, float size, float amp, Color holeColor){
        HashMap<String, Float> floatHashMap = new HashMap<>();
        HashMap<String, Vec3> vec3HashMap = new HashMap<>();

        floatHashMap.put("screenX",screenX);
        floatHashMap.put("screenY",screenY);
        floatHashMap.put("size",size);
        floatHashMap.put("amp",amp);

        vec3HashMap.put("edgeColor",new Vec3(holeColor.getRed(), holeColor.getGreen(), holeColor.getBlue()).scale((double) 1 /255));

        ClientShaderRenderer.createAndRunShader("screen_fire_shader", floatHashMap, vec3HashMap, null, null);

    }

    public static void renderZregosLaser(double x1, double y1, double z1, double x2, double y2, double z2, float laserThickness){
        HashMap<String, Vec3> vec3HashMap = new HashMap<>();
        HashMap<String, Float> floatHashMap = new HashMap<>();

        vec3HashMap.put("cyl_base", new Vec3(x1, y1, z1));
        vec3HashMap.put("cyl_end", new Vec3(x2, y2, z2));
        floatHashMap.put("laser_thickness", laserThickness);
        ClientShaderRenderer.createAndRunShader("zregos_laser", floatHashMap, vec3HashMap, null, null);
    }

    public static void renderZregosSkybox(float amp){
        HashMap<String, Float> vec3Floats = new HashMap<>();
        vec3Floats.put("amp", amp);

        ClientShaderRenderer.createAndRunShader("zregos_skybox", vec3Floats, null, null, null);

    }
    public static void renderLaserScreen(float amp){
        HashMap<String, Float> vec3Floats = new HashMap<>();
        vec3Floats.put("amp", amp);

        ClientShaderRenderer.createAndRunShader("zregos_laser_screen", vec3Floats, null, null, null);

    }

    public static void renderShieldBlock(float amp){
        HashMap<String, Float> vec3Floats = new HashMap<>();
        vec3Floats.put("amp", amp);

        ClientShaderRenderer.createAndRunShader("celestial_shield_block", vec3Floats, null, null, null);

    }

    public static void renderBoulderBeacon(Vec3 pos, float tickCount){
        HashMap<String, Vec3> vec3HashMap = new HashMap<>();
        HashMap<String, Float> floatHashMap = new HashMap<>();
        vec3HashMap.put("pos", pos);
        floatHashMap.put("amp", tickCount);
        ClientShaderRenderer.createAndRunShader("zregos_boulder_beacon", floatHashMap, vec3HashMap, null, null);

    }

    public static void renderDashFire(float entityYaw, Vec3 position, Vec3 dir) {
        HashMap<String, Vec3> vec3HashMap = new HashMap<>();
        HashMap<String, Float> floatHashMap = new HashMap<>();
        vec3HashMap.put("dirAngle", dir);
        vec3HashMap.put("pos", position);
        floatHashMap.put("yaw", entityYaw);

        ClientShaderRenderer.createAndRunShader("zregos_dash", floatHashMap, vec3HashMap, null, null);
    }

    public static void renderClouds(float laserThickness) {
        HashMap<String, Float> floatHashMap = new HashMap<>();
        floatHashMap.put("amp", laserThickness);
        floatHashMap.put("quality", (float) StellariaConfigs.cloud_quality);

        ClientShaderRenderer.createAndRunShader("raymarching_test", floatHashMap, null, null, null);
    }

    public static void renderIdleSky(){

        ClientShaderRenderer.createAndRunShader("skybox", null, null, null, null);

    }

    public static void renderZregosMinionsSkybox(float amp, float color){
        HashMap<String, Float> floatHashMap = new HashMap<>();
        floatHashMap.put("amp", amp);
        floatHashMap.put("color", color);

        ClientShaderRenderer.createAndRunShader("zregos_minions_skybox", floatHashMap, null, null, null);
    }

    public static void renderBlackHole(float tranzProg, float sphereRadius, Vec3 spherePos){
        HashMap<String, Float> floatHashMap = new HashMap<>();
        floatHashMap.put("tranzProg", tranzProg);
        floatHashMap.put("sphereRadius", sphereRadius);

        HashMap<String, Vec3> vec3HashMap = new HashMap<>();
        vec3HashMap.put("spherePos", spherePos);

        ClientShaderRenderer.createAndRunShader("black_hole_menu", floatHashMap, vec3HashMap, null, null);

    }

    //UPDSATE

    public static void renderDiggingSite(float size, Vec3 boxPos){
        HashMap<String, Float> floatHashMap = new HashMap<>();
        floatHashMap.put("size", size);

        HashMap<String, Vec3> vec3HashMap = new HashMap<>();
        vec3HashMap.put("cubePos", boxPos);

        ClientShaderRenderer.createAndRunShader("digging_site", floatHashMap, vec3HashMap, null, null);

    }

    public static void renderVoidCubes(){
        ClientShaderRenderer.createAndRunShader("void_vortex", null, null, null, null);

    }

    public static void renderEbonyidleSkybox(){
        ClientShaderRenderer.createAndRunShader("ebony_idle_skybox", null, null, null, null);

    }

    public static void renderEbonyEnemiesSkybox(){
        ClientShaderRenderer.createAndRunShader("ebony_enemies_skybox", null, null, null, null);
    }

    public static void renderEiniphase1Skybox(){
        ClientShaderRenderer.createAndRunShader("einfarbig_skybox", null, null, null, null);
    }

    public static void renderEiniphase2Skybox(){
        ClientShaderRenderer.createAndRunShader("einfarbig_skybox_mono", null, null, null, null);
    }

    public static void renderMessier(Vec3 pos, float size){
        HashMap<String, Float> floatHashMap = new HashMap<>();
        floatHashMap.put("size", size);

        HashMap<String, Vec3> vec3HashMap = new HashMap<>();
        vec3HashMap.put("pos", pos);
        ClientShaderRenderer.createAndRunShader("messier", floatHashMap, vec3HashMap, null, null);
    }

    public static void voidLaserShader(double x1, double y1, double z1, double x2, double y2, double z2, float laserThickness){
        HashMap<String, Vec3> vec3HashMap = new HashMap<>();
        HashMap<String, Float> floatHashMap = new HashMap<>();

        vec3HashMap.put("cyl_base", new Vec3(x1, y1, z1));
        vec3HashMap.put("cyl_end", new Vec3(x2, y2, z2));
        floatHashMap.put("laser_thickness", laserThickness);
        ClientShaderRenderer.createAndRunShader("void_laser", floatHashMap, vec3HashMap, null, null);
    }
}
