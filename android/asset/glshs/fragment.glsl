#ifdef GL_ES
#precision mediump float;
#endif

uniform sampler2D u_texture;
uniform float uCameraFar;
uniform vec3 uLightPosition;

varying vec4 vPosition;
varying vec2 vTexCoord;

void main() {
    vec4 uTexture = texture2D(u_texture, vTexCoord);
    if (uTexture.a < 0.5f) {
        discard;
    }

    vec4 uAmbiant = vec4(length(vPosition.xyz - uLightPosition) / uCameraFar);

    gl_FragColor = uTexture * uAmbiant;
}