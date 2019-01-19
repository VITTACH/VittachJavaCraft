#ifdef GL_ES
#precision mediump float;
#endif

uniform sampler2D u_texture;

varying vec2 vTexCoord;

void main() {
    vec4 textureColor = texture2D(u_texture, vTexCoord);
    gl_FragColor = textureColor;
}