//Program A

#include <stdlib.h>
#include <unistd.h>
#include <stdlib.h>
#include <fcntl.h>
#include <stdio.h>

int main()
{
  int n=10;
  int a2b,c2a;

  a2b=open("a2b",O_WRONLY);
  c2a=open("c2a",O_RDONLY);
  printf("A: %d\n",n);

  write(a2b,&n,sizeof(int));
  
  while(1)
  {
	if(read(c2a,&n,sizeof(int))<=0)
	  break;

	if(n==0)
	  break;

	n--;
	printf("A: %d\n",n);

	write(a2b,&n,sizeof(int));
  }

  close(a2b);
  close(c2a);

}
