varying vec2 v_uv;

uniform float u_rotation;
uniform sampler2D u_texture;

const float u_mercator_fov = 1.0;

void main() {
	vec2 pos = 2.0*v_uv - 1.0;
	if (length(pos) < 1.)
		discard;
	float lat = asin(pos.x);
	float lon = asin(pos.y) + u_rotation;

	float x = lon / (2.*3.1415926535);
	float y = 0.5 + 0.5 * tan(lat) / tan(u_mercator_fov * 0.5);

	gl_FragColor = texture(u_texture, vec2(x, y));
}
