// program b.c

#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <fcntl.h>

int main()
{
	int n;
	int a2b, b2c;
	a2b = open("a2b", O_RDONLY);
	b2c = open("b2c", O_WRONLY);

	while (1)
	{
		read(a2b, &n, sizeof(int));
		//	break;

		if (n == 0)
			break;

		n--;
		printf("B: %d\n", n);

		write(b2c, &n, sizeof(int));
	}

	close(a2b); close(b2c);

	return 0;
}		
