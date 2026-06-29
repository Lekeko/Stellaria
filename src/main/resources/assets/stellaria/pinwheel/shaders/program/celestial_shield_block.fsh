#include veil:space_helper

uniform sampler2D DiffuseSampler0;
uniform sampler2D DiffuseDepthSampler;
uniform sampler2D StarsTexture;

uniform float amp;
uniform float GameTime;

in vec2 texCoord;
out vec4 fragColor;
uniform vec2 ScreenSize;

uniform float PI = 3.14159265359;

vec4 pow(vec4 col, float p){
    return vec4(
    col.r * p,
    col.g * p,
    col.b * p,
    col.a * p
    );
}

void main() {
    vec3 baseColor = texture(DiffuseSampler0, texCoord).rgb;

    vec2 uv = texCoord - 0.5f;
    uv *= uv;



    float dist = distance(uv, vec2(0,0));

    uv = gl_FragCoord.xy / ScreenSize.xy;
    float ratio = ScreenSize.x / ScreenSize.y;
    uv.x *= ratio;

    vec2 centeredUV = (gl_FragCoord.xy / ScreenSize.xy) - 0.5;
    dist = length(centeredUV);

    float edgeStart = 0.95;
    float edgeEnd = 0.47;
    float mask = smoothstep(edgeEnd, edgeStart, dist);

    vec4 starsText = texture(StarsTexture, vec2(uv.x +GameTime*4 , uv.y +GameTime*4));


    vec4 cyan = vec4(143, 255, 231, 255)/ 255;
    vec4 magenta = vec4(248, 143, 255, 255)/255;
    vec4 color = (mix(cyan, magenta, (sin(GameTime * 5000) / 2) + 0.5));


    fragColor = (vec4(baseColor, 1.0f) + (color * 3 + starsText / 2) * mask * amp);
}