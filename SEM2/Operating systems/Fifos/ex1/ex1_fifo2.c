#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <fcntl.h>

int main()
{
int a,b,s,p;
int a2b,b2a;

a2b=open("a2b",O_RDONLY);
b2a=open("b2a",O_WRONLY);

while(1)
{
scanf("%d%d",&a,&b);

if(read(a2b,&a,sizeof(int))<=0 || read(a2b,&b,sizeof(int))<=0)
break;

//read...
//read... Don t know for sure if it is ok

s=a+b;
p=a*b;

if(s==p)
break;

write(b2a,&s,sizeof(int));
write(b2a,&p,sizeof(int));
}
close(a2b); close(b2a);

return 0;
}
