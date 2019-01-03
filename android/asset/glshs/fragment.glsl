#ifdef GL_ES
#precision mediump float;
#endif

// uniform sampler2D u_texture;
uniform float uCameraFar;
uniform vec3 uLightPosition;

varying vec4 vPosition;
varying vec2 vTexCoord;

void main() {
    vec4 lightAmbient = vec4(length(vPosition.xyz - uLightPosition) / uCameraFar);
    // vec4 textureColor = texture2D(u_texture, vTexCoord);
    gl_FragColor = lightAmbient;// * textureColor;
}