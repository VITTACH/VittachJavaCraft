#ifdef GL_ES
uniform lowp sampler2D u_texture;
uniform mediump float uCameraFar;
uniform mediump vec3 uLightPosition;

varying mediump vec4 vPosition;
varying lowp vec2 vTexCoord;
#else
uniform sampler2D u_texture;
uniform float uCameraFar;
uniform vec3 uLightPosition;

varying vec4 vPosition;
varying vec2 vTexCoord;
#endif


void main() {
    #ifdef GL_ES
    lowp vec4 uTexture;
    mediump vec4 uAmbiant;
    #else
    vec4 uTexture;
    vec4 uAmbiant;
    #endif

    uTexture = texture2D(u_texture, vTexCoord);
    if (uTexture.a < 0.5) {
        discard;
    }

    uAmbiant = vec4(max(length(vPosition.xz - uLightPosition.xz) / uCameraFar, 0.5));

    gl_FragColor = uTexture * uAmbiant;
}