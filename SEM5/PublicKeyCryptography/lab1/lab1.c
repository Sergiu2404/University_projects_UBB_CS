#include <stdio.h>
#include <stdlib.h>
#include <time.h>

// gcd euclid
int gcd_euclid(int a, int b) {
    while (b != 0) {
        int temp = b;
        b = a % b;
        a = temp;
    }
    return a;
}

// gcd euclid rec
int gcd_euclid_rec(int a, int b) {
    if (b == 0)
        return a;
    return gcd_euclid_rec(b, a % b);
}

// gcd sub
int gcd_subtraction(int a, int b) {
    while (a != b) {
        if (a > b)
            a = a - b;
        else
            b = b - a;
    }
    return a;
}

// compare algorithms
void compare_algorithms(int a, int b) {
    struct timespec start, end;
    double time_taken;
    const int repetitions = 100000;

    // gcd euclid basic
    clock_gettime(CLOCK_MONOTONIC, &start);
    for (int i = 0; i < repetitions; i++) {
        (void)gcd_euclid(a, b);
    }
    clock_gettime(CLOCK_MONOTONIC, &end);
    time_taken = (end.tv_sec - start.tv_sec) + (end.tv_nsec - start.tv_nsec) / 1e9;
    printf("Basic Euclid GCD of %d and %d is %d, Time: %f seconds\n", a, b, gcd_euclid(a, b), time_taken);

    // euclid gcd rec
    clock_gettime(CLOCK_MONOTONIC, &start);
    for (int i = 0; i < repetitions; i++) {
        (void)gcd_euclid_rec(a, b);
    }
    clock_gettime(CLOCK_MONOTONIC, &end);
    time_taken = (end.tv_sec - start.tv_sec) + (end.tv_nsec - start.tv_nsec) / 1e9;
    printf("Rec Euclid GCD of %d and %d is %d, Time: %f seconds\n", a, b, gcd_euclid_rec(a, b), time_taken);

    // Subtraction GCD
    clock_gettime(CLOCK_MONOTONIC, &start);
    for (int i = 0; i < repetitions; i++) {
        (void)gcd_subtraction(a, b);
    }
    clock_gettime(CLOCK_MONOTONIC, &end);
    time_taken = (end.tv_sec - start.tv_sec) + (end.tv_nsec - start.tv_nsec) / 1e9;
    printf("Sub GCD of %d and %d is %d, Time: %f seconds\n", a, b, gcd_subtraction(a, b), time_taken);

    printf("\n");
}

int main() {
    const int num_inputs = 10;
    int inputs[11][2] = {
        {56, 98}, {270, 192}, {462, 1071}, {7864, 2048}, {252, 105},
        {24, 36}, {123459, 789012}, {600, 4095}, {48, 18}, {666666, 777777},
        {99, 77}
    };

    time_t seconds;
    seconds = time(NULL);
    printf("secunde = %ld\n", seconds);

    for (int i = 0; i < num_inputs; i++) {
        int a = inputs[i][0];
        int b = inputs[i][1];
        printf("%d)\n", i + 1);


        compare_algorithms(a, b);
    }

    return 0;
}
