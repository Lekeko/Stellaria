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
uniform vec3 pos;
uniform float amp;

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
    float k0 = m0*m0*m5 - m1*m1*hy + m0*ra*(rr*m1*2.0 - m0*pow(ra,2));
    float h = k1*k1 - k2*k0;
    if( h<0.0 ) return vec4(-1.0); //no intersection
    float t = (-k1-sqrt(h))/k2;
    float y = m1 + t*m2;
    if( y<0.0 || y>m0 ) return vec4(-1.0); //no intersection
    return vec4(t, normalize(m0*(m0*(oa+t*rd)+rr*ba*ra)-ba*hy*y));
}


vec3 coneColor(vec3 rayOrigin, vec3 rayDir, vec4 rayHitCone, vec3 top, vec3 bot){
    vec3 cyan = vec3(0,255,255)/ 255;
    vec3 magenta = vec3(255,0,255)/255;
    vec3 color;
    vec3 hitPointWorld = rayOrigin + rayDir * rayHitCone.x;


    vec3 axis = bot - top;
    float axisLen = length(axis);
    vec3 axisDir = axis / axisLen;

    float tAxis = dot(hitPointWorld - top, axisDir) / axisLen;
    float fade = smoothstep(0.6, 1.0, tAxis);


    color = mix(magenta, cyan, fade) * fade;


    return color * clamp(amp/30, 0.0f, 1.0f);
}

vec3 viewPosFromDepthSample(float depth, vec2 uv) {
    vec4 positionCS = vec4(uv, depth, 1.0) * 2.0 - 1.0;
    vec4 positionVS = VeilCamera.IProjMat * positionCS;
    positionVS /= positionVS.w;

    return positionVS.xyz;
}
float dist;
void main() {
    vec3 baseColor = texture(DiffuseSampler0, texCoord).rgb;
    float sceneDepthSample = texture(DiffuseDepthSampler, texCoord).x;
    vec3 viewPos = viewPosFromDepthSample(sceneDepthSample, texCoord);

    vec3 rayOrigin = VeilCamera.CameraPosition ;
    vec3 rayDir = viewDirFromUv(texCoord);

    fragColor.xyz = vec3(baseColor);

    float height = 230.0;

    vec3 top = pos + vec3(0.0,height,0.0);
    vec3 bot = pos;

    float topRad = 0;
    float botRad = 5;

    vec4 rayHitCone1 = coneIntersect(rayOrigin, rayDir, top, bot, topRad, botRad);
    vec4 rayHitCone2 = coneIntersect(rayOrigin, rayDir, bot - vec3(0.0,height,0.0),bot, topRad, botRad);

    float dist = length(viewPos);

    if (rayHitCone1.x > 0 && dist - sceneDepthSample - rayHitCone1.x > 1){
        vec3 color = coneColor(rayOrigin, rayDir, rayHitCone1, top, bot);

        fragColor.xyz += color;
    }
    if (rayHitCone2.x > 0 && dist - sceneDepthSample - rayHitCone2.x > 1){
        vec3 color = coneColor(rayOrigin, rayDir, rayHitCone2,  bot - vec3(0.0,height,0.0),bot);
        fragColor.xyz += color;
    }





}