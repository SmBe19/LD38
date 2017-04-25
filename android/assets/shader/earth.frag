#ifdef GL_ES
#define LOWP lowp
    precision mediump float;
#else
    #define LOWP
#endif

vec4 my_texture2D(sampler2D tex, vec2 pos) {
	pos = fract(pos);
	return texture2D(tex, pos * vec2(634.,477.) / vec2(1024., 1024.));
}
#define texture2D my_texture2D

varying vec2 v_uv;

uniform float u_rotation;
uniform sampler2D u_texture;
uniform sampler2D u_textureClouds;
uniform float u_sun_offset;
uniform float u_cloud_limit;
uniform float u_cloud_brightness;

uniform vec3 u_ground_color;
uniform vec3 u_sea_color;
uniform float u_explosion_limit;


const float u_mercator_fov = 2.5;


vec2 to_tex(float lat, float lon) {
	float x = -lon / (2.*3.1415926535);
	float y = 0.5 + 0.5 * tan(lat) / tan(u_mercator_fov * 0.5);
	return vec2(x, y);
}
void main() {
	vec2 pos = 2.0*v_uv - 1.0;
	pos = 1.2 * pos;
	if (length(pos) > 1.2)
		discard;
	vec3 sun = vec3(0., cos(-u_rotation + u_sun_offset), sin(-u_rotation + u_sun_offset));
	if (length(pos) > 1.0) {
		float len = length(pos);
		pos = pos / len;
		float bright = clamp(dot(sun, vec3(pos, 0.0)), 0.1, 0.8);
		gl_FragColor = mix(bright * vec4(0.4, 0.9, 1.0, 0.9), vec4(0.), (len - 1.0) / 0.2);
		return;
	}
	vec3 normal = vec3(pos, sqrt(1.-dot(pos, pos)));

	float lat = asin(pos.x);
	float lon = asin(pos.y/cos(lat)) - u_rotation;
	lat -= 0.1;

	vec2 xy = to_tex(lat, lon);

	float color = smoothstep(0.4, 0.5, texture2D(u_texture, xy).r);
	float cloud = texture2D(u_textureClouds, xy).r;

	float rng1 = texture2D(u_textureClouds, xy + vec2(0.319, 0.627) + vec2(0.016, 0.073) * u_rotation).r;
	float rng2 = texture2D(u_textureClouds, xy + vec2(0.536, 0.456) + vec2(0.092, 0.023) * u_rotation).r;

	float rng = rng1 * rng2;


	vec3 posabove = normalize(normal + 0.1 * sun);
	float posabovelat = asin(posabove.x);
	float posabovelon = asin(posabove.x / cos(posabovelat)) - u_rotation;

	float diffuse = clamp(dot(normal, sun), 0.2, 1.);
	float grounddiffuse = texture2D(u_textureClouds, to_tex(posabovelat, posabovelon)).r*diffuse;
	float specular = 0.5*grounddiffuse + 0.5*exp(-1. + dot(sun, reflect(vec3(0., 0., -1.), normal)));


	vec4 ground = vec4(mix(grounddiffuse * u_ground_color, specular * u_sea_color, color), 1.0);
	vec4 cloudcolor = vec4(u_cloud_brightness * diffuse * vec3(cloud), 1.0);
	gl_FragColor = mix(ground, cloudcolor, smoothstep(u_cloud_limit-0.1, u_cloud_limit+0.5, cloud));
	if (u_explosion_limit != 1.0) {
		gl_FragColor += clamp((rng-u_explosion_limit) / (1.0 - u_explosion_limit), 0., 1.) * vec4(0.9, 0.5, 0.1, 0.0);
	}
}

