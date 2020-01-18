attribute vec3 a_Position;
attribute vec2 a_TexCoord;

uniform mat4 model;
uniform mat4 modelView;

varying vec2 vTexCoord;
varying vec4 vPosition;

void main() {
    vPosition = model * vec4(a_Position, 1.0);
	vTexCoord = a_TexCoord;
    gl_Position = modelView * vPosition;
}