#include <stdio.h>
#include <stdlib.h>
#include <sys/time.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>

int main(int argc, char** argv){
int pid=fork();

struct timeval tv1,tv2;
gettimeofday(&tv1,NULL);
if(pid==0){
  if(execvp(argv[1],argv+1)==-1){
	printf("error running the command\n");
	return 1;
  }
  exit(0);
}
else{
  wait(0);
  gettimeofday(&tv2,NULL);
  printf("time: %f seconds\n", (double)(tv2.tv_usec - tv1.tv_usec)/1000000);
}
return 0;
}
