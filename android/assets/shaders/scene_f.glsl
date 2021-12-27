#ifdef GL_ES
#define LOWP lowp
#define MED mediump
#define HIGH highp
precision mediump float;
#else
#define MED
#define LOWP
#define HIGH
#endif

varying vec4 v_position;
varying vec2 v_texCoords0;
varying vec4 v_positionLightTrans;
varying float v_intensity;

uniform vec3 u_lightPosition;
uniform vec3 u_cameraPosition;
uniform float u_cameraFar;

uniform sampler2D u_depthMap;
uniform sampler2D u_texture;

uniform float u_fogFar;
uniform vec4 u_fogColor;
uniform float u_fogNear;

void main() {
    vec4 finalColor = texture2D(u_texture, v_texCoords0);
    if (finalColor.a < 0.5) {
        discard;
    }

    finalColor.rgb = finalColor.rgb * v_intensity;

    vec3 depth = (v_positionLightTrans.xyz / v_positionLightTrans.w) * 0.5 + 0.5;
    if (v_positionLightTrans.z >= 0.0 && (depth.x >= 0.0) && (depth.x <= 1.0) && (depth.y >= 0.0) && (depth.y <= 1.0)) {
        float lenToLight = length(v_position.xyz - u_lightPosition) / u_cameraFar;
        float lenDepthMap = texture2D(u_depthMap, depth.xy).a;
        if (lenDepthMap < lenToLight - 0.005) {
            finalColor.rgb *= 0.4;
        } else {
            finalColor.rgb *= 0.4 + 0.6 * (1.0 - lenToLight);
        }
    } else {
        finalColor.rgb *= 0.4;
    }

    float lenCamView = length(v_position.xz - u_cameraPosition.xz);

    // calculate fog
    float fogValue = smoothstep(u_fogNear, u_fogFar, lenCamView) * 0.4;

    gl_FragColor = mix(finalColor, u_fogColor, fogValue);
}