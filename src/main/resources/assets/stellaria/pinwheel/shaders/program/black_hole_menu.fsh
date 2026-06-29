#include veil:space_helper

uniform sampler2D DiffuseSampler0;
uniform sampler2D DiffuseDepthSampler;
uniform sampler2D BlackHoleMenuTexture;
uniform sampler2D ConeTexture;

uniform float GameTime;
uniform vec3 primaryColor;

in vec2 texCoord;
out vec4 fragColor;

uniform float PI = 3.14159265359;
uniform float tranzProg;
uniform float sphereRadius;
uniform vec3 spherePos;

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

float dot2( in vec3 v ) { return dot(v,v); }

vec4 coneIntersect( vec3 ro, vec3 rd, vec3 pa, vec3 pb, float ra, float rb )
{
    vec3  ba = pb - pa;
    vec3  oa = ro - pa;
    vec3  ob = ro - pb;
    float m0 = dot(ba,ba);
    float m1 = dot(oa,ba);
    float m2 = dot(rd,ba);
    float m3 = dot(rd,oa);
    float m5 = dot(oa,oa);
    float m9 = dot(ob,ba);

    // caps

    if( m1<0.0 )
    {
        if( dot2(oa*m2-rd*m1)<(ra*ra*m2*m2) ) // delayed division
        return vec4(-m1/m2,-ba*inversesqrt(m0));
    }
    else if( m9>0.0 )
    {
        float t = -m9/m2;                     // NOTE delayed division
        if( dot2(ob+rd*t)<(rb*rb) )
        return vec4(t,ba*inversesqrt(m0));
    }

    // body
    float rr = ra - rb;
    float hy = m0 + rr*rr;
    float k2 = m0*m0    - m2*m2*hy;
    float k1 = m0*m0*m3 - m1*m2*hy + m0*ra*(rr*m2*1.0        );
    float k0 = m0*m0*m5 - m1*m1*hy + m0*ra*(rr*m1*2.0 - m0*pow(ra,10));
    float h = k1*k1 - k2*k0;
    if( h<0.0 ) return vec4(-1.0); //no intersection
    float t = (-k1-sqrt(h))/k2;
    float y = m1 + t*m2;
    if( y<0.0 || y>m0 ) return vec4(-1.0); //no intersection
    return vec4(t, normalize(m0*(m0*(oa+t*rd)+rr*ba*ra)-ba*hy*y));
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

vec2 sphereTexture(vec3 coord){
    return vec2(
    atan2(coord.x , coord.z) / (PI * 2 ),
    acos(coord.y) / PI
    );
}

vec2 coneTexture(vec3 coord){
    return vec2(
    atan2(coord.x , coord.z ) / (PI * 2 ),
    acos(coord.y) / PI
    );
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

mat3 rotatorIDK(float angle){
    return mat3(
    cos(angle), -sin(angle), 0,
    sin(angle),  cos(angle), 0,
    0,0,1

    );
}

void main() {
    vec3 baseColor = texture(DiffuseSampler0, texCoord).rgb;
    float sceneDepthSample = texture(DiffuseDepthSampler, texCoord).x;

    if (sceneDepthSample < 1.0) {
        fragColor.xyz = baseColor;
        return;
    }


    vec3 rayOrigin = VeilCamera.CameraPosition ;
    vec3 rayDir = viewDirFromUv(texCoord);

    vec3 spherePos = vec3(200.5, 2000.0, 200.5) ;
    float sphereOutline = 1.0;
    float coneSpeed = 3;
    float sphereSpeed = 1000;

    fragColor.xyz = vec3(baseColor);

    //bottom cone
    float height = 630.0;
    vec3 conePos = vec3(spherePos.x, spherePos.y - height/2, spherePos.z);

    vec3 top = vec3(conePos.x, conePos.y + height/2 - (sphereRadius-1), conePos.z);
    vec3 bot = vec3(conePos.x, conePos.y - height/2, conePos.z);

    float topRad = 1.5;
    float botRad = 400;

    float nrLayer = 4;
    float layerSomethingidk = 3;

    for(int i = 1; i <= nrLayer; i++){
        vec3 modifBot = bot + vec3(0,height/(i*layerSomethingidk), 0);
        vec4 rayHitCone1 = coneIntersect(rayOrigin, rayDir, top, modifBot, topRad, botRad);
        float rot = i % 2 == 0 ? 1 : -1;


        if (rayHitCone1.x > 0){
            vec3 hitPointWorld = rayOrigin + rayDir * rayHitCone1.x;
            float hitY = hitPointWorld.y - spherePos.y;

            vec3 axis = modifBot - top;
            float axisLen = length(axis);
            vec3 axisDir = axis / axisLen;

            float tAxis = dot(hitPointWorld - top, axisDir) / axisLen;
            float fade = 1.0 - smoothstep(0.0, 1.0, tAxis);


            vec3 normal = normalize(hitPointWorld - spherePos) * rotatorLR(GameTime*coneSpeed *rot);
            vec2 sphereTexture = coneTexture(normal);
            sphereTexture.y += GameTime * coneSpeed;
            vec4 actualSphereTexture = texture(ConeTexture, sphereTexture*4);
            fragColor += actualSphereTexture * 10 * fade;
        }
    }
    //top cone

    conePos = vec3(spherePos.x, spherePos.y + height/2, spherePos.z);

    top = vec3(conePos.x, conePos.y + height/2, conePos.z);
    bot = vec3(conePos.x, conePos.y - height/2 + (sphereRadius-1), conePos.z);


    for(int i = 1; i <= nrLayer; i++){
        vec3 modifTopp = top - vec3(0,height/(i*layerSomethingidk),0);
        vec4 rayHitCone2 = coneIntersect(rayOrigin, rayDir, bot, modifTopp , topRad, botRad);
        float rot = i % 2 == 0 ? 1 : -1;

        if (rayHitCone2.x > 0){
            vec3 hitPointWorld = rayOrigin + rayDir * rayHitCone2.x;
            float hitY = hitPointWorld.y - spherePos.y;

            vec3 axis = modifTopp - bot;
            float axisLen = length(axis);
            vec3 axisDir = axis / axisLen;

            float tAxis = dot(hitPointWorld - bot, axisDir) / axisLen;
            float fade = 1.0 - smoothstep(0.0, 1.0, tAxis);

            vec3 normal = normalize(hitPointWorld - spherePos) * rotatorLR(-GameTime*coneSpeed *rot);
            vec2 sphereTexture = coneTexture(normal);
            sphereTexture.y -= GameTime * coneSpeed;
            vec4 actualSphereTexture = texture(ConeTexture, sphereTexture * 4 );
            fragColor += actualSphereTexture * 10 * fade;
        }
    }
    //hoooleeeeeeee
    float rayHit1 = sphIntersect(rayOrigin, rayDir, vec4(spherePos, sphereRadius+sphereOutline));
    float rayHit2 = sphIntersect(rayOrigin, rayDir, vec4(spherePos, sphereRadius));

    if(rayHit1 > 0.0){
        fragColor = vec4(1);

    }

    if (rayHit2 > 0.0) {
        vec3 hitPointWorld = rayOrigin + rayDir * rayHit2;
        float hitY = hitPointWorld.y - spherePos.y;

        vec3 normal = normalize(hitPointWorld - spherePos) * rotatorLR(GameTime * sphereSpeed) ;
        vec2 sphereTexture = sphereTexture(normal);
        vec4 actualSphereTexture = texture(BlackHoleMenuTexture, sphereTexture);

        fragColor = actualSphereTexture;
    }

    vec4 white = vec4(1.0f,1.0f,1.0f,1.0f);
    fragColor = mix(fragColor, white, tranzProg);


}