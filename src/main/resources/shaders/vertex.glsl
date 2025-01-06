#version 330

layout (location=0) in vec3 vertex;
layout (location=1) in vec2 texCoord;

out vec2 outTextCoord;

uniform mat4 projection, rotation;

void main()
{
    gl_Position = projection * rotation * vec4(vertex, 1.0);
    outTextCoord = texCoord;
}