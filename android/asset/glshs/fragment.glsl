#ifdef GL_ES
#precision mediump float;
#endif

uniform float uCameraFar;
uniform vec3 uLightPosition;

varying vec4 vPosition;

void main() {
    gl_FragColor = vec4(length(vPosition.xyz - uLightPosition) / uCameraFar);
}