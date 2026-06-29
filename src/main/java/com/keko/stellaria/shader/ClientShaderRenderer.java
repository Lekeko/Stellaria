package com.keko.stellaria.shader;

import com.keko.stellaria.Stellaria;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.post.PostPipeline;
import foundry.veil.api.client.render.post.PostProcessingManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.HashMap;

public class ClientShaderRenderer {

        public static void createAndRunShader(String name,
                                        HashMap<String, Float> floats,
                                        HashMap<String, Vec3> vec3HashMap,
                                        HashMap<String, Matrix3f> matrix3fHashMap,
                                        HashMap<String, Matrix4f> matrix4fHashMap){ //99.999% sure I ain't gonna use this
            try {


                PostProcessingManager postProcessingManager = VeilRenderSystem.renderer().getPostProcessingManager();
                PostPipeline postPipeline = postProcessingManager.getPipeline(ResourceLocation.fromNamespaceAndPath(Stellaria.MOD_ID, name));

                if (postPipeline == null){
                    System.out.println("Idk but i think smth is bad cuz postPipeline is null");
                    return;
                }

                if (floats != null) {
                    for (var entry : floats.entrySet()) {
                        postPipeline.getUniformSafe(entry.getKey())
                                .setFloat(entry.getValue());
                    }
                }

                if (vec3HashMap != null) {
                    for (var entry : vec3HashMap.entrySet()) {
                        postPipeline.getUniformSafe(entry.getKey())
                                .setVector((float) entry.getValue().x, (float)entry.getValue().y, (float)entry.getValue().z);
                    }
                }

                if (matrix3fHashMap != null) {
                    for (var entry : matrix3fHashMap.entrySet()) {
                        postPipeline.getUniformSafe(entry.getKey())
                                .setMatrix(entry.getValue());
                    }
                }

                if (matrix4fHashMap != null) {
                    for (var entry : matrix4fHashMap.entrySet()) {
                        postPipeline.getUniformSafe(entry.getKey())
                                .setMatrix(entry.getValue());
                    }
                }
                postProcessingManager.runPipeline(postPipeline);
            }catch (Exception e){
            }
        }
}
