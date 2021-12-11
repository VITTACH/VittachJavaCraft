#ifdef GL_ES
varying mediump vec4 vPosition;
varying lowp vec2 vTexCoord;

uniform lowp sampler2D uTexture;
uniform lowp sampler2D uShadowMap;
uniform mediump vec3 uLightPosition;

uniform mediump vec4 uFogColor;
uniform mediump float uFogNear;
uniform mediump float uFogFar;
#else
varying vec4 vPosition;
varying vec2 vTexCoord;

uniform sampler2D uTexture;
uniform sampler2D uShadowMap;
uniform vec3 uLightPosition;

uniform vec4 uFogColor;
uniform float uFogNear;
uniform float uFogFar;
#endif

void main() {
    #ifdef GL_ES
    lowp vec4 fogColor;
    lowp vec4 textureColor;
    mediump float fogValue;
    mediump float distance;
    #else
    vec4 fogColor;
    vec4 textureColor;
    float fogValue;
    float distance;
    #endif

    textureColor = texture2D(uTexture, vTexCoord);
    if (textureColor.a < 0.5) {
        discard;
    }

    distance = length(vPosition.xz - uLightPosition.xz);

    // calculate fog
    fogValue = smoothstep(uFogNear, uFogFar, distance) * 0.4;
    fogColor = mix(textureColor, uFogColor, fogValue);

    gl_FragColor = fogColor;
}