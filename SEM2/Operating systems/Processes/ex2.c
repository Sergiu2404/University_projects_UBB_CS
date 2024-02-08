#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>


int main()
{
int i,n=4;
for(i=0;i<n;i++)
{
	int pid;
	if(i==0)
	{
	pid=fork();
	if(pid==0){
	printf("Child %d, id: %d, parent's id: %d\n",i,getpid(),getppid());
	exit(0);
	}
	else{
	printf("PARENT-id: %d, parent's id: %d\n",getpid(),getppid());
	wait(0);//
	}
	}
	else{
	if(pid==0){
	printf("Child %d, id: %d, parent's id: %d\n",i,getpid(),getppid());
	pid=fork();
	if(pid!=0)
	  exit(0);
	}
	else{
	printf("Child %d, id: %d, parent's is: %d\n",i,getpid(),getppid());
	wait(0);
	}
	}
}


return 0;
}
