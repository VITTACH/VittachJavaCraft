attribute vec3 a_Position;

uniform mat4 modelView;

varying vec4 vPosition;
uniform mat4 model;

void main() {
    vPosition = model * vec4(a_Position, 1.0);
    gl_Position = modelView * vPosition;
}