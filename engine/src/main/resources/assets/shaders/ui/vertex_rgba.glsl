#version 330

//@vertex
#import "assets:shaders/formats/vertex_rgba.glsl"

out vec4 pass_rgba;

uniform mat4 u_model;

layout (std140) uniform u_matrices
{
    mat4 u_projection;
    mat4 u_view;
};

void main()
{
    vec4 vertex = vec4(in_vertex, 1.0);

    gl_Position = u_projection * u_view * u_model * vertex;

    pass_rgba = in_rgba;
}

//@fragment
in vec4 pass_rgba;

out vec4 out_color;

uniform vec4 u_color;

void main()
{
    out_color = pass_rgba * u_color;
}