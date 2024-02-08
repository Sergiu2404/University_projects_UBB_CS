#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>


int main(int argc,char* argv[])
{

int a2b[2],b2a[2];
pipe(a2b); pipe(b2a);
int f=fork();
int n=0;
printf("number n: ");
scanf("%d",&n);
if(f==0){

	close(a2b[0]); close(b2a[1]);
	if(read(b2a[0],&n,sizeof(int))<0)
		perror("error at reading from child b\n");
	printf("child 1 read %d\n",n);

	while(n!=10){
		n=random()%10+1;
		if(write(a2b[1],&n,sizeof(int))<0)
			perror("error at writing n to child 2\n");
		if(n==10) break;
		if(read(b2a[0],&n,sizeof(int))<0)
			perror("error at reading from child 2\n");
		printf("child 1 read %d\n",n);
	}
	close(a2b[1]); close(b2a[0]);
	exit(0);
}

f=fork();
if(f==0){
	close(b2a[0]); close(a2b[1]);
	if(read(a2b[0],&n,sizeof(int))<0)
		perror("error at reading from child 2\n");
	printf("child 2 read %d",n);
	
	while(n!=10){
		n=random()%10+1;
		if(write(b2a[1],&n,sizeof(int))<0)
			perror("error at writing to child 1\n");
		if(n==10) break;
		if(read(a2b[0],&n,sizeof(int))<0)
			perror("error at reading from child 1\n");
		printf("child 2 read %d",n);
	}
	close(b2a[1]); close(a2b[0]);
	exit(0);
}
wait(0);
wait(0);
return 0;
}
