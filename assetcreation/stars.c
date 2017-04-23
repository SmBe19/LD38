int main(int argc, char *argv[])
{
	char a[480*800];
	for (int i = 0; i < sizeof(a); i++) {
		a[i] = 0;
	}
	for (int i = 0; i < 1000; i++) {
		a[rand() % sizeof(a)] = 1;
	}
	printf("P1\n%d %d\n", 800, 480);
	for (int i = 0; i < sizeof(a); i++) {
		printf("%d", !a[i]);
	}
	return 0;
}
