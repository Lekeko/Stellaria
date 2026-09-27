package com.keko.stellaria.world.gen;

import com.keko.stellaria.world.ModBiomeModifications;

public class ModWorldGen {
    public static void init(){
        ModOreGen.generateOres();
        ModBiomeModifications.load();
    }
}
