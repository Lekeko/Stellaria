package com.keko.stellaria.sounds;

import com.keko.stellaria.Stellaria;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class ModSounds {
    public static final SoundEvent ZREGOS_LASER = register("zregos_laser") ;
    public static final SoundEvent ZREGOS_LASER_BEAM = register("zregos_laser_beam");
    public static final SoundEvent ZREGOS_SHATTER = register("zregos_shatter");
    public static final SoundEvent ZREGOS_BOULDERS_BREAK = register("zregos_boulders_break");
    public static final SoundEvent ZREGOS_BOULDERS_SUMMON = register("zregos_boulders_summon");
    public static final SoundEvent COSMIC_ARROW_SHOT = register("cosmic_arrow_shot");
    public static final SoundEvent DRILLING = register("drilling");
    public static final SoundEvent DRILL_BREAK = register("drill_break");
    public static final SoundEvent EBONY_AMBIANCE = register("ebony_ambiance");
    public static final SoundEvent PAPER_HIT  = register("paper_hit");
    public static final SoundEvent MESSIER_STAR_IMPACT  = register("messier_star_impact");
    public static final SoundEvent VOID_GLAIVE_ACTIVATE = register("void_glaive_activate");
    public static final SoundEvent STAFF_SPAWN = register("staff_spawn");
    public static final SoundEvent BUNNY_LAUNCH = register("bunny_launch");
    public static final SoundEvent STATUE_SUMMON = register("statue_summon");
    public static final SoundEvent MESSIER_SPAWN = register("messier_spawn");
    public static final SoundEvent MESSIER_LASERS = register("messier_lasers");
    public static final SoundEvent VOID_DEALER_SPAWN = register("void_dealer_spawn");
    public static final SoundEvent VOID_DEALER_LASER = register("void_dealer_laser");
    public static final SoundEvent STAR_PHANTOM_TP = register("star_phantom_tp");
    public static final SoundEvent STAR_PHANTOM_SLASH = register("star_phantom_slash");
    public static final SoundEvent STAR_PHANTOM_SHOOT = register("star_phantom_shoot");
    public static final SoundEvent EINIFARBIG_SPAWN = register("einifarbig_spawn");
    public static final SoundEvent EINI_DEFEAT = register("eini_defeat");
    public static final SoundEvent EINI_LASER = register("eini_laser");
    public static final SoundEvent EINI_PHASE = register("eini_phase");
    public static final SoundEvent EINI_STARS = register("eini_stars");
    public static final SoundEvent EINI_TP = register("eini_tp");

    private static SoundEvent register(String path) {
        ResourceLocation identifier = Stellaria.id(path);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, identifier, SoundEvent.createVariableRangeEvent(identifier));

    }

    public static void registerSounds() {

    }
}
