#include veil:space_helper

uniform sampler2D DiffuseSampler0;
uniform sampler2D DiffuseDepthSampler;
uniform sampler2D DistortTexture;
uniform sampler2D StarsTexture;

uniform float GameTime;
uniform vec2 ScreenSize;

uniform float screenX;
uniform float screenY;
uniform float size;
uniform float amp;
uniform vec3 edgeColor;

in vec2 texCoord;
out vec4 fragColor;



void main() {
    vec3 baseColor = texture(DiffuseSampler0, texCoord).rgb;

    vec2 uv = gl_FragCoord.xy / ScreenSize.xy;
    float ratio = ScreenSize.x / ScreenSize.y;
    uv.x *= ratio;

    float edgeThickness = 0.2;

    vec4 distortText1 = texture(DistortTexture, vec2(uv.x- GameTime*50 , uv.y + GameTime*50));
    vec4 distortText2 = texture(DistortTexture, vec2(uv.x- GameTime*50 + uv.y - GameTime*50, uv.y));
    vec4 starsText = texture(StarsTexture, vec2(uv.x +GameTime*4 , uv.y +GameTime*4));
    vec2 pos = vec2(screenX * ratio,screenY);

    vec4 distortText = distortText1 + distortText2;

    float distance = distance(uv, pos);


    fragColor = vec4(baseColor, 1.0);
    float normDist =  distance/size ;

    if (distance <= size){
        float fuckassMixValue = clamp(normDist * distortText.x * amp, 0.0, 1.0);
        vec3 color = mix(edgeColor.xyz, baseColor, round(fuckassMixValue));
        if(fuckassMixValue < 0.4) color =  starsText.xyz;
        fragColor = vec4(color, 1.0);
    }
}