#ifdef GL_ES
#define LOWP lowp
    precision mediump float;
#else
    #define LOWP
#endif

varying vec2 v_uv;

uniform float u_rotation;
uniform sampler2D u_texture;
uniform sampler2D u_textureClouds;

const float u_mercator_fov = 2.5;
const float u_cloud_limit = 0.4;

void main() {
	vec2 pos = 2.0*v_uv - 1.0;
	if (length(pos) > 1.)
		discard;
	vec3 normal = vec3(pos, inversesqrt(1.-dot(pos, pos)));
	vec3 sun = vec3(0., cos(u_rotation), sin(u_rotation));

	float lat = asin(pos.x);
	float lon = asin(pos.y/cos(lat)) - u_rotation;
	lat -= 0.1;

	float x = -lon / (2.*3.1415926535);
	float y = 0.5 + 0.5 * tan(lat) / tan(u_mercator_fov * 0.5);

	float color = smoothstep(0.4, 0.5, texture(u_texture, vec2(x, y)).r);
	float cloud = texture(u_textureClouds, vec2(x, y)).r;


	float diffuse = clamp(dot(normal, sun), 0., 1.);
	float specular = exp(-1. - dot(sun, reflect(vec3(0., 0., 1.), normal)));

	vec4 ground = mix(diffuse * vec4(0.22, 0.37, 0.05, 1), specular * vec4(0.05, 0.25, 0.75, 1), color);
	vec4 cloudcolor = vec4(cloud);
	gl_FragColor = mix(ground, cloudcolor, smoothstep(u_cloud_limit-0.1, u_cloud_limit+0.5, cloud));
}

