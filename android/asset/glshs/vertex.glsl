attribute vec3 a_Position;
attribute vec2 a_TexCoord;

uniform mat4 model;
uniform mat4 modelView;

varying vec2 vTexCoord;

void main() {
	vTexCoord = a_TexCoord;
    gl_Position = modelView * model * vec4(a_Position, 1.0);
}