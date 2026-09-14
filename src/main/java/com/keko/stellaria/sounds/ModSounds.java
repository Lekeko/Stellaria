package com.keko.stellaria.sounds;

import com.keko.stellaria.Stellaria;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class ModSounds {
    public static final SoundEvent ZREGOS_LASER =register("zregos_laser") ;
    public static final SoundEvent ZREGOS_LASER_BEAM =register("zregos_laser_beam");
    public static final SoundEvent ZREGOS_SHATTER =register("zregos_shatter");
    public static final SoundEvent ZREGOS_BOULDERS_BREAK =register("zregos_boulders_break");
    public static final SoundEvent ZREGOS_BOULDERS_SUMMON =register("zregos_boulders_summon");
    public static final SoundEvent COSMIC_ARROW_SHOT =register("cosmic_arrow_shot");
    public static final SoundEvent DRILLING =register("drilling");
    public static final SoundEvent DRILL_BREAK =register("drill_break");

    private static SoundEvent register(String path) {
        ResourceLocation identifier = Stellaria.id(path);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, identifier, SoundEvent.createVariableRangeEvent(identifier));

    }

    public static void registerSounds() {

    }
}
