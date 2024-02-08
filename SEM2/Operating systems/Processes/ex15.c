#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <string.h>
#include <ctype.h>
int main()
{
	int p2c[2],c2p[2];
	int pid=fork();
	pipe(p2c); pipe(c2p);

	if(pid==0){
		close(p2c[1]); close(c2p[0]);
		char data[20];
		
		read(p2c[0],data,sizeof(data));
		for(int i=0;i<strlen(data);i++)
			data[i]=data[i]-32;
		
		write(c2p[1],data,sizeof(data));

		close(p2c[0]); close(c2p[1]);
		exit(0);
	}
	else{
		close(p2c[0]); close(c2p[1]);

		char string[20]="string";

		write(p2c[1],string,sizeof(string));
		read(c2p[0],string,sizeof(string));
		
		printf("parent received uppercase string: %s",string);
		//for(int i=0;i<strlen(data);i++)
		//	printf("%c",data[i]);
		close(p2c[1]); close(c2p[0]);
		wait(NULL);
	}
	return 0;
}
