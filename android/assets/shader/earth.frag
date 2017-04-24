#ifdef GL_ES
#define LOWP lowp
    precision mediump float;
#else
    #define LOWP
#endif

varying vec2 v_uv;

uniform float u_rotation;
uniform sampler2D u_texture;

const float u_mercator_fov = 2.5;

void main() {
	vec2 pos = 2.0*v_uv - 1.0;
	if (length(pos) > 1.)
		discard;
	float lat = asin(pos.x);
	float lon = asin(pos.y/cos(lat)) - u_rotation;
	lat -= 0.1;

	float x = -lon / (2.*3.1415926535);
	float y = 0.5 + 0.5 * tan(lat) / tan(u_mercator_fov * 0.5);

	float color = smoothstep(0.4, 0.5, texture(u_texture, vec2(x, y)).r);

	gl_FragColor = mix(vec4(0.22, 0.37, 0.05, 1), vec4(0.05, 0.25, 0.75, 1), color);
}

