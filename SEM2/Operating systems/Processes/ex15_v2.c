#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <string.h>
#include <ctype.h>

int main()
{
    int p2c[2], c2p[2];
    pipe(p2c); pipe(c2p);
    //if (pipe(p2c) == -1 || pipe(c2p) == -1) {
    //    perror("Pipe creation failed");
    //    return 1;
    //}

    int pid = fork();
    //if (pid == -1) {
    //    perror("Fork failed");
    //    return 1;
    //}

    if (pid == 0) {
        // Child process
        close(p2c[1]); // Close unused write end of p2c pipe
        close(c2p[0]); // Close unused read end of c2p pipe

        char data[20];
        read(p2c[0], data, sizeof(data));

        for (int i = 0; i < strlen(data); i++)
            data[i]=data[i]-32;
//data[i] = toupper(data[i]);

        write(c2p[1], data, sizeof(data));

        close(p2c[0]);
        close(c2p[1]);
        exit(0);
    } else {
        // Parent process
        close(p2c[0]); // Close unused read end of p2c pipe
        close(c2p[1]); // Close unused write end of c2p pipe

        char string[20] = "string";
        write(p2c[1], string, sizeof(string));

        read(c2p[0], string, sizeof(string));

        printf("Parent received uppercase string: %s\n", string);

        close(p2c[1]);
        close(c2p[0]);
        wait(NULL);
    }

    return 0;
}
