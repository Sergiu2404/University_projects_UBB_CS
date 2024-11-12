using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;

class Program
{
    const int MATRIX_SIZE = 4;
    const int NUM_TASKS = 4;

    public static void Main()
    {
        int[,] A = new int[MATRIX_SIZE, MATRIX_SIZE];
        int[,] B = new int[MATRIX_SIZE, MATRIX_SIZE];
        int[,] C = new int[MATRIX_SIZE, MATRIX_SIZE];

        InitializeMatrices(ref A, ref B);
        Console.WriteLine("Matrix A: ");
        PrintMatrix(A);
        Console.WriteLine("Matrix B: ");
        PrintMatrix(B);

        // Basic / Low-level threading approach
        var stopwatch = System.Diagnostics.Stopwatch.StartNew();
        for (int i = 0; i < 10; i++)
        {
            MatrixMultiplicationLowLevel(ref A, ref B, ref C);
        }
        stopwatch.Stop();
        Console.WriteLine($"Time for (Low-Level Threads): {stopwatch.ElapsedMilliseconds} ms");

        Console.WriteLine("Result matrix from basic approach: ");
        PrintMatrix(C);
        Console.WriteLine("=======================================================");


        C = new int[MATRIX_SIZE, MATRIX_SIZE];




        // THREAD POOL
        stopwatch.Restart();
        for (int i = 0; i < 10; i++)
        {
            MatrixMultiplicationThreadPool(ref A, ref B, ref C);
        }
        stopwatch.Stop();
        Console.WriteLine($"Time for (Thread Pool): {stopwatch.ElapsedMilliseconds} ms");

        Console.WriteLine("Result matrix from thread pool approach: ");
        PrintMatrix(C);
    }

    static int ComputeElement(int[,] A, int[,] B, int row, int col)
    {
        int sum = 0;
        for (int k = 0; k < MATRIX_SIZE; ++k)
        {
            sum += A[row, k] * B[k, col];
        }
        return sum;
    }

    static void ComputeElements(int[,] A, int[,] B, ref int[,] C, int startElem, int numElements)
    {
        int count = 0;
        for (int i = 0; i < MATRIX_SIZE; ++i)
        {
            for (int j = 0; j < MATRIX_SIZE; ++j)
            {
                if (count >= startElem && count < startElem + numElements)
                {
                    C[i, j] = ComputeElement(A, B, i, j);
                }
                ++count;
            }
        }
    }

    static void InitializeMatrices(ref int[,] A, ref int[,] B)
    {
        Random rand = new Random();
        for (int i = 0; i < MATRIX_SIZE; i++)
        {
            for (int j = 0; j < MATRIX_SIZE; j++)
            {
                A[i, j] = rand.Next(0, 5);
                B[i, j] = rand.Next(0, 5);
            }
        }
    }

    // Low-level threads approach
    static void MatrixMultiplicationLowLevel(ref int[,] A, ref int[,] B, ref int[,] C)
    {
        int elementsPerTask = (MATRIX_SIZE * MATRIX_SIZE) / NUM_TASKS;
        List<Thread> threads = new List<Thread>();

        for (int i = 0; i < NUM_TASKS; ++i)
        {
            int startElem = i * elementsPerTask;
            int numElements = (i == NUM_TASKS - 1) ? (MATRIX_SIZE * MATRIX_SIZE) - startElem : elementsPerTask;

            int[,] ACopy = A;
            int[,] BCopy = B;
            int[,] CCopy = C;

            Thread t = new Thread(() => ComputeElements(ACopy, BCopy, ref CCopy, startElem, numElements));
            threads.Add(t);
            t.Start();
        }

        foreach (var t in threads)
        {
            t.Join();
        }
    }

    // Thread pool approach to matrix multiplication
    static void MatrixMultiplicationThreadPool(ref int[,] A, ref int[,] B, ref int[,] C)
    {
        ThreadPool pool = new ThreadPool(NUM_TASKS);
        List<Task> tasks = new List<Task>();
        int elementsPerTask = (MATRIX_SIZE * MATRIX_SIZE) / NUM_TASKS;

        for (int i = 0; i < NUM_TASKS; ++i)
        {
            int startElem = i * elementsPerTask;
            int numElements = (i == NUM_TASKS - 1) ? (MATRIX_SIZE * MATRIX_SIZE) - startElem : elementsPerTask;

            int[,] ACopy = A;
            int[,] BCopy = B;
            int[,] CCopy = C;

            // add tasks to execute inside the thread pool
            tasks.Add(pool.Enqueue(() => ComputeElements(ACopy, BCopy, ref CCopy, startElem, numElements)));
        }

        // Wait for all tasks to complete
        Task.WhenAll(tasks).Wait();
    }

    static void PrintMatrix(int[,] matrix)
    {
        for (int i = 0; i < MATRIX_SIZE; i++)
        {
            for (int j = 0; j < MATRIX_SIZE; j++)
            {
                Console.Write($"{matrix[i, j]} ");
            }
            Console.WriteLine();
        }
    }
}

//custom thread pool implementation
class ThreadPool
{
    private List<Thread> workers;
    private Queue<Action> taskQueue;
    private object lockObj = new object();
    private bool stop = false;

    public ThreadPool(int numThreads)
    {
        workers = new List<Thread>(); //worker thr
        taskQueue = new Queue<Action>(); //queue of NUM_TASKS

        // Start workers with all available threads
        for (int i = 0; i < numThreads; i++)
        {
            var worker = new Thread(() =>
            {
                while (true) //for checking continuosly if any tasks available
                {
                    Action task = null;
                    lock (lockObj)
                    {
                        if (taskQueue.Count > 0)
                        {
                            task = taskQueue.Dequeue(); // task can be taken from the ququeue of tasks to execute
                        }
                        else if (stop) // exit if no more tasks to do
                        {
                            break;
                        }  
                    }

                    if (task != null)
                    {
                        task(); // task gets eecuted
                    }
                    else
                    {
                        Thread.Sleep(10); // for avoiding consuing CPU resources
                    }
                }
            });

            workers.Add(worker);
            worker.Start();
        }
    }

    public Task Enqueue(Action task)
    {
        lock (lockObj) //for making sure thread accesses a consistent list of tasks
        {
            if (!stop)
            {
                taskQueue.Enqueue(task);
            }
        }
        return Task.CompletedTask; // Simulating async Task with the custom thread pool
    }

    public void Stop()
    {
        lock (lockObj)
        {
            stop = true; // stop when no more tasks remained
        }
    }
}
