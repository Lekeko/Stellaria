package com.keko.stellaria.helper;

public class MathHelpers {
    public static float lerp (float start, float finish, float t){
        return start * (1-t) + finish * t;
    }

}
