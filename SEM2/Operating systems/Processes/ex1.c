#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <unistd.h>
#include <sys/wait.h>

int main()
{
int i,n=4;

for(i=0;i<n;i++)
{
	int pid=fork();
	if(pid==-1)
	  perror("error on fork");
	else if(pid==0){
	  printf("Child %d, chiild's id is: %d, parent's id is: %d\n",i,getpid(),getppid());
	  exit(0);
	}
	else{
	  printf("PARENT-id: %d, parent's id: %d\n",getpid(),getppid());
	  //wait(0);
	}
	wait(0);
}
//for(i=0;i<n;i++)
	//wait(0);
printf("Done\n");
return 0;
}
