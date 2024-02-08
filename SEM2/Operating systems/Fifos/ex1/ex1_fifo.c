#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>

int main()
{
	int a,b,s,p;
	int a2b,b2a;

	a2b=open("a2b",O_WRONLY);
	b2a=open("b2a",O_RDONLY);
	
	while(1)
	{
	scanf("%d%d",&a,&b);

	if(read(b2a,&a,sizeof(int))<=0 || read(b2a,&b,sizeof(int))<=0)
	break;

	read(a2b,&s,sizeof(int));
	read(a2b,&p,sizeof(int));

	s=a+b;
	p=a*b;

	if(s==p)
	break;

	write(a2b,&s,sizeof(int));
	write(a2b,&p,sizeof(int));
	}
	close(a2b);
	close(b2a);
}
