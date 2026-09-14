#include veil:space_helper

uniform sampler2D DiffuseSampler0;
uniform sampler2D DiffuseDepthSampler;

uniform float GameTime;
uniform vec3 primaryColor;

in vec2 texCoord;
out vec4 fragColor;

uniform float size;
uniform vec3 cubePos;

//by Inigo Quilez
vec2 boxIntersection(in vec3 ro, in vec3 rd, vec3 boxSize, vec3 outNormal) {
    vec3 m = 1.0 / rd;
    vec3 n = m * ro;
    vec3 k = abs(m) * boxSize;
    vec3 t1 = -n - k;
    vec3 t2 = -n + k;
    float tN = max(max(t1.x, t1.y), t1.z);
    float tF = min(min(t2.x, t2.y), t2.z);
    if (tN > tF || tF < 0.0) return vec2(-1.0);
    outNormal = (tN > 0.0) ? step(vec3(tN), t1) :
    step(t2, vec3(tF));
    outNormal *= -sign(rd);
    return vec2(tN, tF);
}



vec3 viewPosFromDepthSample(float depth, vec2 uv) {
    vec4 positionCS = vec4(uv, depth, 1.0) * 2.0 - 1.0;
    vec4 positionVS = VeilCamera.IProjMat * positionCS;
    positionVS /= positionVS.w;

    return positionVS.xyz;
}

void main() {
    vec3 baseColor = texture(DiffuseSampler0, texCoord).rgb;
    float depthSample = texture(DiffuseDepthSampler, texCoord).x;
    vec3 viewPos = viewPosFromDepthSample(depthSample, texCoord);

    vec3 rayOrigin = VeilCamera.CameraPosition + VeilCamera.CameraBobOffset;
    vec3 rayDir = viewDirFromUv(texCoord);
    float dist = length(viewPos);
    vec3 boxNormal;
    vec3 modifPos = cubePos - vec3(0,size/4 ,0);
    vec2 rayHit = boxIntersection(rayOrigin - modifPos, rayDir, vec3((size+ 0.01) * 0.5), boxNormal);


    if (rayHit.x > 0.0 && rayHit.x < rayHit.y && rayHit.x < dist){
        vec3 hitPos = rayOrigin + rayDir * rayHit.x;

        vec3 localHit = hitPos - modifPos;

        float heightT = clamp((localHit.y +( size+ 0.01) * (0.5 + (sin(GameTime * 3000) + 1) / 20)) / (size+ 0.01), 0.0, 1.0);

        vec3 cubeColor = mix(vec3(229, 226, 236) / 255, baseColor, heightT);

        fragColor.xyz = cubeColor;
        return;
    }
    fragColor = vec4(vec3(baseColor), 1);
}