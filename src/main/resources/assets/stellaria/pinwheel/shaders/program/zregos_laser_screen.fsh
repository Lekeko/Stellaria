#include veil:space_helper

uniform sampler2D DiffuseSampler0;
uniform sampler2D DiffuseDepthSampler;

uniform float amp;
uniform float GameTime;

in vec2 texCoord;
out vec4 fragColor;

uniform float PI = 3.14159265359;



void main() {
    vec3 baseColor = texture(DiffuseSampler0, texCoord).rgb;
    vec2 uv = texCoord - 0.5f;
    uv *= uv;
    uv *= 4;
    float dist = distance(uv, vec2(0,0));

    vec4 cyan = vec4(143, 255, 231, 255)/ 255;
    vec4 magenta = vec4(248, 143, 255, 255)/255;

    fragColor = vec4(baseColor, 1.0f) + ((mix(cyan, magenta, (sin(GameTime * 5000) / 2) + 0.5)) * amp * 1.5) * dist;
}