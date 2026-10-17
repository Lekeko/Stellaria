package com.keko.stellaria.helper;

import java.awt.*;

public class MathHelpers {
    public static float lerp (float start, float finish, float t){
        return start * (1-t) + finish * t;
    }

    public static Color lerpColor(Color c1, Color c2, float t) {
        int r = Math.round(lerp(c1.getRed(), c2.getRed(), t));
        int g = Math.round(lerp(c1.getGreen(), c2.getGreen(), t));
        int b = Math.round(lerp(c1.getBlue(), c2.getBlue(), t));
        return new Color(r, g, b);
    }
}
