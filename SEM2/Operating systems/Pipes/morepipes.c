#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <sys/wait.h>
#include <sys/types.h>

 

int main(int argc, char** argv)
{
        int pidA, pidB;
        int n = 10;
        int p2a[2];
        pipe(p2a);
        int a2b[2];
        pipe(a2b);
        int b2p[2];
        pipe(b2p);

 

        pidA = fork();
        if(pidA == 0)
        {
                close(a2b[0]);
                close(b2p[0]);
                close(b2p[1]);
                close(p2a[1]);

 

                while(1)
                {
                        if(read(p2a[0], &n, sizeof(int)) <= 0)
                                break;

 

                        if(n == 0)
                                break;

 

                        n--;
                        printf("A: %d\n", n);
                        write(a2b[1], &n, sizeof(int));
                }
                close(p2a[0]);
                close(a2b[1]);
                exit(0);
        }

 

        pidB = fork();
        if(pidB == 0)
        {
                close(p2a[0]);
                close(p2a[1]);
                close(b2p[0]);
                close(a2b[1]);

 

                while(1)
                {
                        if(read(a2b[0], &n, sizeof(int)) <= 0)
                                break;

 

                        if(n == 0)
                                break;

 

                        n--;
                        printf("B: %d\n", n);
                        write(b2p[1], &n, sizeof(int));
                }
                close(a2b[0]);
                close(b2p[1]);
                exit(0);
        }

 

        printf("P: %d\n", n);

 

        close(a2b[0]);
        close(a2b[1]);
        close(b2p[1]);
        close(p2a[0]);
        write(p2a[1], &n, sizeof(int));

 

        while(1)
        {
                if(read(b2p[0], &n, sizeof(int)) <= 0)
                        break;

 

                if(n == 0)
                        break;

 

                n--;
                printf("P: %d\n", n);
                write(p2a[1], &n, sizeof(int));
        }
        close(p2a[1]);
        close(b2p[0]);
        wait(0);
        wait(0);
}



