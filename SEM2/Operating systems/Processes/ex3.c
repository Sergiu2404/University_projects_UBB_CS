#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
int pid;
void handle_child(int sig)
{
  printf("child process terminating...\n");
  exit(0);
}
void handle_parent(int sig){
  printf("parent process finishing...\n");
  kill(pid,SIGUSR1);
  wait(0);
  exit(0);
}
void handle_zombie(int sig){
  printf("parent waiting for the child process tp end...\n");
  wait(0);
}

int main(){

pid=fork();
if(pid==0){
  signal(SIGUSR1,handle_child);
  printf("Child-id: %d, parent's id: %d\n",getpid(),getppid());
  while(1){
	printf("child process runing...\n");
	sleep(3);
  }
}
else{
  signal(SIGUSR1,handle_parent);
  signal(SIGCHLD,handle_zombie);
  printf("Parent-id: %d, parent's id: %d\n",getpid(),getppid());
  while(1){
	printf("parent process runing...\n");
        sleep(3);
  }
}

return 0;
}
