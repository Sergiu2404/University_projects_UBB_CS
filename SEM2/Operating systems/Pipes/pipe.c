#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>
#include <sys/types.h>


int main(int argc,char** argv)
{
int pid;
int a[4]={1,2,3,4};
int p[2];
pipe(p);

pid=fork();

if(pid==0){
	a[2]+=a[3];
	printf("I am a child\n");
	printf("%d %d\n",a[0],a[2]);

	close(p[0]);
	write(p[1],&a[2],sizeof(int));
	close(p[1]);
	exit(0);
}
else{
	a[0]+=a[1];
	printf("I am parent\n");
	printf("%d %d\n",a[0],a[2]);

	close(p[1]);
	read(p[0],&a[2],sizeof(int));
	close(p[0]);

	a[0]+=a[2];
	wait(0);
	printf("The sum is: %d\n",a[0]);
}
}
