package com.keko.stellaria.midnightLib;

import eu.midnightdust.lib.config.MidnightConfig;

public class StellariaConfigs extends MidnightConfig{
    public static final String CLOUD_CONFIG = "cloud_config";

    @Entry(category = CLOUD_CONFIG, name = "Cloud quality (Higher better but lowers fps)") public static int cloud_quality = 12;
}
