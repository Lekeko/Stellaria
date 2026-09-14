#include veil:space_helper

uniform sampler2D DiffuseSampler0;
uniform sampler2D DiffuseDepthSampler;
uniform sampler2D InsideTexture;

uniform float GameTime;

in vec2 texCoord;
out vec4 fragColor;

uniform vec3 pos;
uniform float size;
uniform float PI = 3.14159265359;

float sphIntersect(vec3 ro, vec3 rd, vec4 sph){
    vec3 oc = ro - sph.xyz;
    float b = dot( oc, rd );
    float c = dot( oc, oc ) - sph.w* sph.w;
    float h = b*b - c;
    if( h<0.0 ){
        return -1.0;
    }
    return -b - sqrt(h);
}

float sphIntersectIn(vec3 ro, vec3 rd, vec4 sph){
    vec3 oc = ro - sph.xyz;
    float b = dot( oc, rd );
    float c = dot( oc, oc ) - sph.w* sph.w;
    float h = b*b - c;
    if( h<0.0 ){
        return -1.0;
    }
    return -b + sqrt(h);
}

vec3 viewPosFromDepth(float depth, vec2 uv) {
    float z = depth * 2.0 - 1.0;
    vec4 positionCS = vec4(uv * 2.0 - 1.0, z, 1.0);
    vec4 positionVS = VeilCamera.IProjMat * positionCS;
    positionVS /= positionVS.w;
    return positionVS.xyz;
}

vec3 viewDirFromUv(vec2 uv) {
    vec3 pointOnFarPlaneVS = viewPosFromDepth(1.0, uv);
    return (VeilCamera.IViewMat * vec4(normalize(pointOnFarPlaneVS), 0.0)).xyz;
}

float atan2(float y, float x){
    if (y > 0.0){
        return atan(y, x) + PI / 2;
    }else{
        return PI - atan(y, -x) + PI / 2;
    }
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
    0.0, cos(angle), -sin(angle),
    0.0, sin(angle),  cos(angle)

    );
}

vec3 viewPosFromDepthSample(float depth, vec2 uv) {
    vec4 positionCS = vec4(uv, depth, 1.0) * 2.0 - 1.0;
    vec4 positionVS = VeilCamera.IProjMat * positionCS;
    positionVS /= positionVS.w;

    return positionVS.xyz;
}

void main() {
    vec3 baseColor = texture(DiffuseSampler0, texCoord).rgb;
    float sceneDepthSample = texture(DiffuseDepthSampler, texCoord).x;
    vec3 viewPos = viewPosFromDepthSample(sceneDepthSample, texCoord);

    fragColor.xyz = baseColor;


    vec3 rayOrigin = VeilCamera.CameraPosition ;
    vec3 rayDir = viewDirFromUv(texCoord);

    vec3 spherePos = pos;
    float sphereRadius = size;
    float dist = length(viewPos);

    float rayHitIn = sphIntersectIn(VeilCamera.CameraPosition, rayDir, vec4(spherePos, sphereRadius));

    if (rayHitIn > 0.0 && rayHitIn < dist) {
        vec3 hitPointWorld = rayOrigin + rayDir * rayHitIn;
        float hitY = hitPointWorld.y - spherePos.y;

        vec3 normal = normalize(hitPointWorld - spherePos)* rotatorLR(GameTime * 100) ;
        vec2 sphereTexture = sphereTexture(normal);
        vec4 actualSphereTexture;
        actualSphereTexture = texture(InsideTexture, sphereTexture) ;
        if (actualSphereTexture.xyz == vec3(1.0, 1.0, 1.0)){
            fragColor.xyz = baseColor;
        }else
        fragColor.xyz = vec3(1.0, 1.0,1.0);

    }

    float rayHit = sphIntersect(VeilCamera.CameraPosition, rayDir, vec4(spherePos, sphereRadius));

    if (rayHit > 0.0 && rayHit < dist) {
        vec3 hitPointWorld = rayOrigin + rayDir * rayHit;
        float hitY = hitPointWorld.y - spherePos.y;

        vec3 normal = normalize(hitPointWorld - spherePos)* rotatorLR(GameTime * 100) ;
        vec2 sphereTexture = sphereTexture(normal);
        vec4 actualSphereTexture;
        actualSphereTexture = texture(InsideTexture, sphereTexture);
        if (actualSphereTexture.xyz == vec3(1.0, 1.0, 1.0)){
        }else
            fragColor = actualSphereTexture;
    }
}