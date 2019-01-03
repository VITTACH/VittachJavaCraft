attribute vec3 a_Position;
attribute vec2 a_TexCoord;

uniform mat4 model;
uniform mat4 modelView;

varying vec4 vPosition;
varying vec2 vTexCoord;

void main() {
	vTexCoord = a_TexCoord;
    vPosition = model * vec4(a_Position, 1.0);
    gl_Position = modelView * vPosition;
}