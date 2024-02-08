#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>

void f(int n)
{
if(n>0)
{
  int pid=fork();
  if(pid==0)
  {
	if(n==4){
	  printf("PARENT-id: %d, parent's id: %d\n",getpid(),getppid());
	  f(n-1);
	}
	else{
	printf("Child %d, PID: %d, PPID: %d\n",n,getpid(),getppid());
	f(n-1);
	}
  }
}
wait(0);
exit(0);
}

int main()
{
f(4);
return 0;
}
