package com.keko.stellaria.component;


import com.keko.stellaria.Stellaria;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import java.util.function.UnaryOperator;

public class ModDataComponentTypes {
    public static final DataComponentType<Integer> SHIELD_CHARGES = register("shield_charges", integerBuilder -> integerBuilder.persistent(Codec.INT));
    public static final DataComponentType<Integer> FURY_STAFF_CHARGES = register("fury_staff_charges", integerBuilder -> integerBuilder.persistent(Codec.INT).cacheEncoding());

    private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Stellaria.id(name),
                builderOperator.apply(DataComponentType.builder()).build());
    }
    public static void init(){

    }
}
