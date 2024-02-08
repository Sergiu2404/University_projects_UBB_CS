#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <sys/wait.h>


int main()
{
	int a,b,s,p;
	int p2c[2],c2p[2];

	pipe(p2c); pipe(c2p);

	int pid=fork();

	if(pid==0)
	{
	close(p2c[1]); close(c2p[0]);

	while(1)
	{
	if(read(p2c[0],&a,sizeof(int))<=0)
	break;
	
	if(read(p2c[0],&b,sizeof(int))<=0)
	break;

	s=a+b;
	p=a*b;

	write(c2p[1],&s,sizeof(int));
	write(c2p[1],&p,sizeof(int));
	}
	close(p2c[0]); close(c2p[1]);

	exit(0);
	}

	close(p2c[0]); close(c2p[1]);

	while(1)
	{
	scanf("%d%d",&a,&b);
	
	write(p2c[1],&a,sizeof(int));
	write(p2c[1],&b,sizeof(int));

	read(c2p[0],&s,sizeof(int));
	read(c2p[0],&p,sizeof(int));

	printf("Sum: %d\nProduct: %d\n\n",s,p);
	if(s==p)
	break;
	}
	close(p2c[1]); close(c2p[0]);

	wait(0);
	return 0;
}
