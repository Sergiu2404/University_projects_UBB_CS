// program c.c

#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <fcntl.h>

int main()
{
	int n;

	int b2c, c2a;
	b2c = open("b2c", O_RDONLY);
	c2a = open("c2a", O_WRONLY);

	while (1)
	{
		read(b2c, &n, sizeof(int));
		//	break;

		if (n == 0)
			break;

		n--;
		printf("C: %d\n", n);

		write(c2a, &n, sizeof(int));
	}

	close(b2c); close(c2a);

	return 0;
}		
