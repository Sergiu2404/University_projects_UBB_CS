#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <string.h>

int main(int argc,char* argv[])
{

int p2c[2],c2p[2];
pipe(p2c); pipe(c2p);
int pid=fork();

if(pid==-1){
	perror("error at fork\n");
	exit(1);
}
else if(pid==0){
	close(c2p[0]); close(p2c[1]);
	int nr,i,n;
	float rez;
	if(read(p2c[0],&n,sizeof(int))<0){
		perror("error at reading from parent\n");
		close(c2p[1]); close(p2c[0]);
		exit(1);
	}
	for(i=0;i<n;i++){
		if(read(p2c[0],&nr,sizeof(int))<0){
			perror("error at reading nr from parent\n");
			close(c2p[1]); close(p2c[0]);
			exit(1);
		}
		rez+=nr;
	}
	rez/=n;
	if(write(c2p[1],&rez,sizeof(float))<0){
		perror("error at writing rez to parent\n");
		close(c2p[1]); close(p2c[0]);
		exit(1);
	}
	close(c2p[1]); close(p2c[0]);
	exit(0);
}
else{
	close(p2c[0]); close(c2p[1]);
	int n=atoi(argv[1]);
	int nr,i;
	float rez=-1;

	if(write(p2c[1],&n,sizeof(int))<0){
		perror("error at writing to child\n");
		close(c2p[0]); close(p2c[1]);
		wait(0);
		exit(1);
	}

	for(i=0;i<n;i++){
		nr=random() % 100;
		if(write(p2c[1],&nr,sizeof(int))<0){
			perror("error at writing nr to child\n");
			close(c2p[0]); close(p2c[1]);
		}
		printf("number %d is: %d\n",i,nr);
	}

	wait(0);
	if(read(c2p[0],&rez,sizeof(float))<0){
		perror("error at reading rez from child\n");
	}
	printf("average is: %f\n",rez);
	close(p2c[1]); close(c2p[0]);
}

return 0;
}
