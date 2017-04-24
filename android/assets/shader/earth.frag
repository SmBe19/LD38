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


vec2 to_tex(float lat, float lon) {
	float x = -lon / (2.*3.1415926535);
	float y = 0.5 + 0.5 * tan(lat) / tan(u_mercator_fov * 0.5);
	return vec2(x, y);
}
void main() {
	vec2 pos = 2.0*v_uv - 1.0;
	if (length(pos) > 1.)
		discard;
	vec3 normal = vec3(pos, inversesqrt(1.-dot(pos, pos)));
	vec3 sun = vec3(0., cos(u_rotation), sin(u_rotation));

	float lat = asin(pos.x);
	float lon = asin(pos.y/cos(lat)) - u_rotation;
	lat -= 0.1;

	vec2 xy = to_tex(lat, lon);

	float color = smoothstep(0.4, 0.5, texture(u_texture, xy).r);
	float cloud = texture(u_textureClouds, xy).r;


	vec3 posabove = normalize(normal + 0.1 * sun);
	float posabovelat = asin(posabove.x);
	float posabovelon = asin(posabove.x / cos(posabovelat)) - u_rotation;

	float diffuse = clamp(dot(normal, sun), 0.1, 1.);
	float grounddiffuse = texture(u_textureClouds, to_tex(posabovelat, posabovelon).r)*diffuse;
	float specular = grounddiffuse; //exp(-1. + dot(-sun, reflect(vec3(0., 0., -1.), normal)));


	vec4 ground = vec4(mix(grounddiffuse * vec3(0.22, 0.37, 0.05), specular * vec3(0.05, 0.25, 0.75), color), 1.0);
	vec4 cloudcolor = vec4(diffuse * vec3(cloud), 1.0);
	gl_FragColor = mix(ground, cloudcolor, smoothstep(u_cloud_limit-0.1, u_cloud_limit+0.5, cloud));
}

