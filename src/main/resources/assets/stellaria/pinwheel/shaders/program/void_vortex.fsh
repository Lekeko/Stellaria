#include veil:space_helper

uniform sampler2D DiffuseSampler0;
uniform sampler2D DiffuseDepthSampler;

uniform float GameTime;

in vec2 texCoord;
out vec4 fragColor;

float random (vec2 st) {
    return fract(sin(dot(st.xy,
            vec2(12.9898,78.233)))*
    43758.5453123);
}

mat3 rotatorLR(float angle){
    return mat3(
            cos(angle), 0.0, sin(angle),
            0.0, 1.0, 0.0,
            sin(angle) , 0.0,  -cos(angle)

    );
}

vec2 boxIntersection(in vec3 ro, in vec3 rd, vec3 boxSize) {
    vec3 m = 1.0 / rd;
    vec3 n = m * ro;
    vec3 k = abs(m) * boxSize;
    vec3 t1 = -n - k;
    vec3 t2 = -n + k;
    float tN = max(max(t1.x, t1.y), t1.z);
    float tF = min(min(t2.x, t2.y), t2.z);

    if (tN > tF || tF < 0.0) {
        return vec2(-1.0);
    }

    return vec2(tN, tF);
}


void main() {
    vec3 baseColor = texture(DiffuseSampler0, texCoord).rgb;
    float sceneDepthSample = texture(DiffuseDepthSampler, texCoord).x;
    if (sceneDepthSample < 1.0) {
        fragColor.xyz = baseColor;
        return;
    }

    float amp = 1;
    float cubeCount = 200;
    vec3 color = vec3(1,1,1);

    float speed = GameTime * 13;

    vec3 rayOrigin = VeilCamera.CameraPosition;
    vec3 rayDir = viewDirFromUv(texCoord);


    vec3 clusterOrigin = VeilCamera.CameraBobOffset + rayOrigin;
    float clusterSize = cubeCount;

    float yProg = clamp((-58.0 - clusterOrigin.y) / 4.0, 0.0, 1.0);
    color *= yProg;

    if(yProg <= 0) {
        fragColor.xyz = baseColor;
        return;
    }


    float dispersion = 1000;

    vec3 finalColor = vec3(0,0,0);

    for (int i = 1; i <= clusterSize; ++i) {
        float boxSize = amp + (i % 3);

        vec3 posOffset = (vec3(
                random(vec2(i/clusterSize, 1)),
                random(vec2(i/clusterSize, 2)),
                random(vec2(i/clusterSize, 3))
        ) - 0.5) * dispersion  * rotatorLR(speed *  (i % 2));
        if(posOffset.y > -60){
            continue;
        }

        vec3 rooooo = clusterOrigin - posOffset - vec3(clusterOrigin.x, 0.0, clusterOrigin.z);
        vec2 hit = boxIntersection(rooooo, rayDir, vec3(boxSize));

        if (hit.x > 0.0) {
            finalColor = color;
        }
    }

    finalColor += baseColor;
    fragColor.xyz = finalColor;
}