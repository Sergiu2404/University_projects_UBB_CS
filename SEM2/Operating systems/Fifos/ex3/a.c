// program a.c

#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <fcntl.h>

int main()
{
	int n = 20;
	int a2b, c2a;
	a2b = open("a2b", O_WRONLY);
	c2a = open("c2a", O_RDONLY);

	printf("A: %d\n", n);

	write(a2b, &n, sizeof(int));

	while (1)
	{
		read(c2a, &n, sizeof(int));  
		//	break;

		if (n == 0)
			break;

		n--;
		printf("A: %d\n", n);

		write(a2b, &n, sizeof(int));
	}

	close(a2b); close(c2a);

	return 0;
}
