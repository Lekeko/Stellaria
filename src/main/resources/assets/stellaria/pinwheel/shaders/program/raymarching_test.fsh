#include veil:space_helper


uniform sampler2D DiffuseSampler0;
uniform sampler2D DiffuseDepthSampler;
uniform vec2 ScreenSize;

in vec2 texCoord;
out vec4 fragColor;

uniform float GameTime;
uniform float amp;
uniform float quality;

float rand(float v){
    //source : The book of shaders
    //author : Patricio Gonzalez Vivo & Jen Lowe
    return fract(sin(dot(v, 12.9898))*43758.5453123);
}

float rand2D(vec2 st) {
    return fract(sin(dot(st.xy,
    vec2(0.290,0.060)))
    * 43758.5453123);}

float rand3D(vec3 st) {
    return fract(sin(dot(st.xyz,
    vec3(0.290,0.060, 0.921)))
    * 43758.5453123);}


float noise1D(vec2 coord) {
    float i = floor(coord.x);
    float f = fract(coord.x);
    return f;
}

float noise2D(vec2 coord) {
    vec2 i = floor(coord);
    vec2 f = fract(coord);

    vec2 u = f * f * (3.0 - 2.0 * f);

    float a = rand2D(i);
    float b = rand2D(i + vec2(1.0, 0.0));
    float c = rand2D(i + vec2(0.0, 1.0));
    float d = rand2D(i + vec2(1.0, 1.0));

    return mix(mix(a, b, u.x), mix(c, d, u.x), u.y);
}

float noise3D(vec3 coord) {
    vec3 i = floor(coord);
    vec3 f = fract(coord);

    vec3 u = f * f * (3.0 - 2.0 * f); //same shi as smoothstep

    float a = rand3D(i);
    float b  = rand3D(i + vec3(1.0, 0.0, 0.0));
    float c  = rand3D(i + vec3(0.0, 1.0, 0.0));
    float d  = rand3D(i + vec3(1.0, 1.0, 0.0));

    float aa = rand3D(i + vec3(0.0, 0.0, 1.0));
    float bb = rand3D(i + vec3(1.0, 0.0, 1.0));
    float cc = rand3D(i + vec3(0.0, 1.0, 1.0));
    float dd = rand3D(i + vec3(1.0, 1.0, 1.0));

    float down = mix(mix(a, b, u.x), mix(c, d, u.x), u.y);
    float up = mix(mix(aa, bb, u.x), mix(cc, dd, u.x), u.y);

    return mix(down ,up, u.z);
}

float fbm(vec2 st) {
    // Initial values
    float value = 0.0;
    float amplitude = .5;
    float frequency = 0.;
    //
    // Loop of octaves
    int OCTAVES = 5;
    for (int i = 0; i < OCTAVES; i++) {
        value += amplitude * noise2D(st);
        st *= 2.;
        amplitude *= .5;
    }
    return value;
}

float fbm3d(vec3 st) {
    // Initial values
    float value = 0.0;
    float amplitude = 0.5;
    //
    // Loop of octaves
    int OCTAVES = 5;
    for (int i = 0; i < OCTAVES; i++) {
        value += amplitude * noise3D(st);
        st *= 2.0;
        amplitude *= 0.5;
    }
    return value;
}

vec3 viewPosFromDepth(float depth, vec2 uv) {
    float z = depth * 2.0 - 1.0;
    vec4 positionCS = vec4(uv * 2.0 - 1.0, z, 1.0);
    vec4 positionVS = VeilCamera.IProjMat * positionCS;
    positionVS /= positionVS.w;
    return positionVS.xyz;
}


void main() {
    vec3 baseColor = texture(DiffuseSampler0, texCoord).rgb;
    float sceneDepthSample = texture(DiffuseDepthSampler, texCoord).x;
    vec3 viewPos = viewPosFromDepth(sceneDepthSample, texCoord);

    vec2 uv = gl_FragCoord.xy / ScreenSize.xy;
    float ratio = ScreenSize.x / ScreenSize.y;
    float size = 10;

    uv.x *= ratio;
    uv *= size;

    fragColor.xyz = baseColor;

    float noise = noise2D(uv);
    float newNoise = fbm(vec2(uv.x, uv.y + GameTime * 0));




    vec3 rayDir = viewDirFromUv(texCoord);
    vec3 rayOrigin = VeilCamera.CameraPosition ;

    // float noiseClouds = noise3D(rayOrigin);
    // y top and bot so i wont kill my pc

    float yTop = 1111;
    float yBot = -111;

    float tBot = (yBot - rayOrigin.y) / rayDir.y;
    float tTop = (yTop - rayOrigin.y) / rayDir.y;

//    float tEntry = max(min(tBot, tTop), 0.0);
//    float tExit = max(tBot, tTop);

    float tEntry = 110;
    float tExit = 2000;

    float dist = length(viewPos);

    vec3 cloudColor11 = vec3(117, 212, 255) / 35.0f;
    vec3 cloudColor12 = vec3(255, 140, 255) / 35.0f;
    vec3 finalColorActualColor;
    vec3 cloudColor2 = baseColor;


    if(tExit > 0){

        float sceneDist = (sceneDepthSample >= 1.0) ? 1e9 : length(viewPos);

        int numStep = int(quality);
        float stepSize = (tExit-tEntry)/numStep;

        vec3 pos = rayOrigin + rayDir * tEntry;
        float transmittance = 1.0;

        for (int i = 0; i < numStep; i++) {
            float currentT = tEntry + i * stepSize;
            if (currentT > sceneDist)
                break;
            float density = fbm3d(pos * 0.0015);
            finalColorActualColor = mix(cloudColor11, cloudColor12, density);
            float d = max(0.0, density - 0.4);
            density = d * d * d * d;

            transmittance *= exp(-density * stepSize);
            if (transmittance < 0.01)
                break;

            pos += rayDir * stepSize;
        }

        float cloudAlpha = 1.0 - transmittance;
        // coloring this bitcvhasses

        vec3 cloudColor = mix(cloudColor2, finalColorActualColor, pow(transmittance / (1.6 + amp * 1.5), 1));


        fragColor.xyz = mix(baseColor, cloudColor, cloudAlpha);
    }


}