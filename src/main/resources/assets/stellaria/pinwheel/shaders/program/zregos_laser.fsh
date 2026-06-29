#include veil:space_helper

uniform sampler2D DiffuseSampler0;
uniform sampler2D DiffuseDepthSampler;
uniform sampler2D DistortTexture;
uniform sampler2D SoftTexture;
uniform sampler2D StarTexture;

uniform vec3 cyl_base;
uniform vec3 cyl_end;
uniform float laser_thickness;

uniform float GameTime;
uniform vec3 primaryColor;

in vec2 texCoord;
out vec4 fragColor;

uniform float PI = 3.14159265359;
uniform float amp;

vec4 sdgCylinder(in vec3 p, in vec3 a, in vec3 b, in float r)
{
    vec3  ba = (b-a)*0.5;
    vec3  ce = (b+a)*0.5;
    float l = length(ba);
    vec3  d = ba/l;

    vec3  q = p - ce;
    float v = dot(q,d);
    vec3  u = q-v*d;
    float k = length(u); // k = sqrt(dot(q,q)-v*v);
    vec2  e = vec2(k-r,abs(v)-l);

    vec2  h = max(e,0.0);
    float f = length(h);
    float g = max(e.x,e.y);

    vec3 du = u/k;
    vec3 dv = v<0.0?-d:d;

    return (g<=0.0) ? vec4( g, (e.x>e.y)?du:dv ):
    vec4( f, (h.x*du+h.y*dv)/f );
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
    float k0 = m0*m0*m5 - m1*m1*hy + m0*ra*(rr*m1*2.0 - m0*ra*ra);
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
vec2 coneTexture(vec3 coord){
    return vec2(
    atan2(coord.x , coord.z ) / (PI * 2 ),
    acos(coord.y) / PI
    );
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



void main() {
    vec3 baseColor = texture(DiffuseSampler0, texCoord).rgb;
    float sceneDepthSample = texture(DiffuseDepthSampler, texCoord).x;
    fragColor.xyz = baseColor;
    vec3 viewPos = viewPosFromDepth(sceneDepthSample, texCoord);


    vec3 rayOrigin = VeilCamera.CameraPosition;
    vec3 rayDir = viewDirFromUv(texCoord);

    vec3 cyan = vec3(0,255,255)/ 255;
    vec3 magenta = vec3(255,0,255)/255;

    float height = 630.0;

    //vec3 top = vec3(conePos.x, conePos.y + height/2 - (sphereRadius-1), conePos.z);
    //vec3 bot = vec3(conePos.x, conePos.y - height/2, conePos.z);


    float topRad = 1 * laser_thickness;
    float botRad = 8 * laser_thickness;
    float coneSpeed = GameTime * 100;

    vec3 actualCylEnd = mix(cyl_end,cyl_base, laser_thickness);


    vec3 cyl = vec3((actualCylEnd.x + cyl_end.x)/81, (actualCylEnd.y + cyl_end.y)/4, (actualCylEnd.z + cyl_end.z)/4);

    float nrLayer = 5;



    for(int i = 1; i <= nrLayer; i++){
        botRad += i*1 * laser_thickness ;
        vec4 rayHitCone1 = coneIntersect(rayOrigin, rayDir, cyl_end, (actualCylEnd), topRad, botRad);
        float rot = i % 2 == 0 ? 1 : -1;

        float dist = length(viewPos);

        if (rayHitCone1.x > 0 && dist - sceneDepthSample - rayHitCone1.x > 1.0){
            vec3 hitPointWorld = rayOrigin + rayDir * rayHitCone1.x;


            vec3 normal = normalize(hitPointWorld - cyl) * rotatorLR(GameTime*coneSpeed *rot);
            vec2 sphereTexture = coneTexture(normal);
            sphereTexture.y += GameTime * -coneSpeed;
            vec4 actualSphereTexture = texture(DistortTexture, sphereTexture);
            float brightness = (actualSphereTexture.x + actualSphereTexture.y + actualSphereTexture.z)/3;
            vec4 finalColor = vec4(mix(cyan, magenta, brightness), 1.0);


            fragColor += (finalColor/2) * laser_thickness;
        }
    }

}