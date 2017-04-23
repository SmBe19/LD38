int main(int argc, char *argv[])
{
	char a[480*800];
	for (int i = 0; i < sizeof(a); i++) {
		a[i] = 0;
	}
	for (int i = 0; i < 300; i++) {
		a[rand() % sizeof(a)] = 0x80 + (rand() & 0x7f);
	}
	printf("P2\n%d %d\n255\n", 800, 480);
	for (int i = 0; i < sizeof(a); i++) {
		printf("%d ", (int)a[i]);
	}
	return 0;
}
