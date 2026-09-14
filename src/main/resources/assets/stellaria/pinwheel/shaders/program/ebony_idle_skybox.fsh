#include veil:space_helper

uniform sampler2D DiffuseSampler0;
uniform sampler2D DiffuseDepthSampler;
uniform sampler2D HighlightTexture;
uniform sampler2D SoftTexture;
uniform sampler2D StarTexture;

uniform float GameTime;
uniform vec3 primaryColor;

in vec2 texCoord;
out vec4 fragColor;

uniform float PI = 3.14159265359;
uniform float amp;

float sphIntersect(vec3 ro, vec3 rd, vec4 sph){
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

void main() {
    vec3 baseColor = texture(DiffuseSampler0, texCoord).rgb;
    float sceneDepthSample = texture(DiffuseDepthSampler, texCoord).x;
    fragColor.xyz = baseColor;

    if (sceneDepthSample < 1.0) {
        return;
    }


    vec3 rayOrigin = VeilCamera.CameraPosition ;
    vec3 rayDir = viewDirFromUv(texCoord);

    vec3 spherePos = VeilCamera.CameraPosition ;
    float sphereRadius = 15.1;

    float rayHit = sphIntersect(VeilCamera.CameraPosition, rayDir, vec4(spherePos, sphereRadius));

    fragColor = vec4(0,0,0,0);


    for(int i = 0; i < 2; i++){
        if (rayHit > 0.0) {
            vec3 hitPointWorld = rayOrigin + rayDir * rayHit;
            float hitY = hitPointWorld.y - spherePos.y;

            vec3 normal = normalize(hitPointWorld - spherePos) *rotatorLR(GameTime * pow(i*1, 2.9)) ;
            vec2 sphereTexture = sphereTexture(normal);
            vec4 actualSphereTexture;
            switch(i){
                case 0 : actualSphereTexture = texture(StarTexture, sphereTexture); break;
                case 1 : actualSphereTexture = texture(SoftTexture, sphereTexture); break;
            }

            fragColor += actualSphereTexture;
        }
    }
}