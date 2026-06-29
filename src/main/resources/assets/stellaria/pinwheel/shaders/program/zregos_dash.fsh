#include veil:space_helper

uniform sampler2D DiffuseSampler0;
uniform sampler2D DiffuseDepthSampler;
uniform sampler2D DistortTexture;
uniform sampler2D SoftTexture;
uniform sampler2D StarTexture;

uniform float GameTime;
uniform float yaw;
uniform vec3 primaryColor;
uniform vec3 dirAngle;
uniform vec3 pos;

in vec2 texCoord;
out vec4 fragColor;

uniform float PI = 3.14159265359;
uniform float amp;


float sphIntersect(vec3 ro, vec3 rd, vec4 sph) {
    vec3 oc = ro - sph.xyz;
    float b = dot(oc, rd);
    float c = dot(oc, oc) - sph.w * sph.w;
    float h = b * b - c;
    if (h < 0.0) {
        return -1.0;
    }
    h = sqrt(h);
    return -b - h;
}

vec3 viewPosFromDepth(float depth, vec2 uv) {
    float z = depth * 2.0 - 1.0;
    vec4 positionCS = vec4(uv * 2.0 - 1.0, z, 1.0);
    vec4 positionVS = VeilCamera.IProjMat * positionCS;
    positionVS /= positionVS.w;
    return positionVS.xyz;
}
float atan2(float y, float x){
    if (y > 0.0){
        return atan(y, x) + PI / 2;
    }else{
        return PI - atan(y, -x) + PI / 2;
    }
}



vec3 viewDirFromUv(vec2 uv) {
    vec3 pointOnFarPlaneVS = viewPosFromDepth(1.0, uv);
    return (VeilCamera.IViewMat * vec4(normalize(pointOnFarPlaneVS), 0.0)).xyz;
}



vec2 sphereTexture(vec3 coord){
    return vec2(
    atan2(coord.x , coord.z) / (PI * 2 ),
    acos(coord.y) / PI
    );
}
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

mat3 rotatorUD(float angle){
    return mat3(
    1.0, 0.0, 0.0,
    0.0, cos(angle), sin(angle),
    0.0, sin(angle), -cos(angle)

    );
}

mat2 rotateVec2Side(float angle){
    return mat2(
    cos(angle), -sin(angle),
    sin(angle), cos(angle)
    );
}

mat2 rotateVec2Uppi(float angle){
    return mat2(
    cos(angle), sin(angle),
    sin(angle), -cos(angle)
    );
}





void main() {
    vec3 baseColor = texture(DiffuseSampler0, texCoord).rgb;
    float sceneDepthSample = texture(DiffuseDepthSampler, texCoord).x;
    vec3 viewPos = viewPosFromDepth(sceneDepthSample, texCoord);
    if (sceneDepthSample < 1.0) {
        fragColor.xyz = baseColor;
    }
    float speed = GameTime * 1000;
    vec3 sceneWorldPos = screenToWorldSpace(texCoord, sceneDepthSample).xyz;

    vec3 rayOrigin = VeilCamera.CameraPosition;
    vec3 rayDir = viewDirFromUv(texCoord);


    vec3 spherePos = pos;
    float sphereRadius = 20.1;

    float rayHit = sphIntersect(rayOrigin, rayDir, vec4(spherePos, sphereRadius));

    float dist = length(viewPos);

    fragColor.xyz = baseColor;

    if (rayHit > 0.0 &&  dist - sceneDepthSample - rayHit > 1) {
        vec3 hitPointWorld = rayOrigin + rayDir * rayHit;
        vec3 fadeDir = normalize(dirAngle);
        float dir = dot(hitPointWorld - spherePos, fadeDir);

        float slice = (dir + sphereRadius) / sphereRadius;

        vec3 normal = normalize(hitPointWorld - spherePos);

        float udAngle = mix(0, PI, (dirAngle.y + 1)/2);
        float lrAngle = yaw * PI/180 - PI/2;

        mat3 rotUD = rotatorUD(udAngle);
        mat3 rotLR = rotatorLR(lrAngle);

        vec3 rotatedNormal = rotUD * rotLR * normal;

        vec2 sphereTexture = sphereTexture(rotatedNormal);
        vec3 actualSphereTexture = texture(DistortTexture, sphereTexture).xyz;


        float sphereAlpha = slice /3;

        fragColor.xyz = mix(baseColor, actualSphereTexture.xyz,  clamp(sphereAlpha, 0, 1));
    }
}