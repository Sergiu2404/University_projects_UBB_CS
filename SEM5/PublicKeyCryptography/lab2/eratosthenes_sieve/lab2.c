#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>  // for malloc and free

void sieveOfEratosthenes(int bound) {
    bool *prime = (bool*)malloc((bound + 1) * sizeof(bool));
    
    if (prime == NULL) {
        printf("Memory allocation failed\n");
        return;
    }
    
    for (int i = 0; i <= bound; i++) {
        prime[i] = true;
    }

    for (int p = 2; p * p <= bound; p++) {
        if (prime[p]) {
            //mark multiples of p as nonprimes
            for (int i = p * p; i <= bound; i += p) {
                prime[i] = false;
            }
        }
    }

    printf("Prime numbers up to %d are: ", bound);
    for (int p = 2; p <= bound; p++) {
        if (prime[p]) {
            printf("%d ", p);
        }
    }
    printf("\n");

    // free the allocated memory
    free(prime);
}

int main() {
    int bound;
    
    printf("Enter a bound: ");
    scanf("%d", &bound);

    sieveOfEratosthenes(bound);

    return 0;
}
