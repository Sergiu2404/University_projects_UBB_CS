using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text.Json;
using MPI;

namespace puzzle
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("Choose the implementation:");
            Console.WriteLine("1. Distributed (MPI)");
            Console.WriteLine("2. Threaded/Task-Based");
            Console.Write("Enter your choice: ");
            int choice = int.Parse(Console.ReadLine());

            switch (choice)
            {
                case 1:
                    RunMPIImplementation(args);
                    break;
                case 2:
                    RunThreadedImplementation();
                    break;
                default:
                    Console.WriteLine("Invalid choice. Exiting.");
                    break;
            }
        }
        // Add these modifications to your Program class:

        static void RunMPIImplementation(string[] args)
        {
            var totalTime = System.Diagnostics.Stopwatch.StartNew();

            using (new MPI.Environment(ref args))
            {
                Intracommunicator comm = Communicator.world;
                int rank = comm.Rank;

                if (rank == 0)
                {
                    Console.WriteLine("Starting MPI Implementation...");
                    var loadTime = System.Diagnostics.Stopwatch.StartNew();

                    Matrix matrix = Matrix.FromFile("C:\\Users\\SergiuGoian\\Desktop\\Uni_projects_UBB_CS\\University_projects_UBB_CS\\SEM5\\PDP\\project\\puzzle\\puzzle\\mat.in");
                    Console.WriteLine($"Time to load puzzle: {loadTime.ElapsedMilliseconds}ms");

                    if (!Matrix.IsSolvable(matrix))
                    {
                        Console.WriteLine("The provided puzzle is unsolvable.");
                        return;
                    }

                    var searchTime = System.Diagnostics.Stopwatch.StartNew();
                    SearchMaster(matrix, comm);
                    Console.WriteLine($"Total search time: {searchTime.ElapsedMilliseconds}ms");
                }
                else
                {
                    SearchWorker(comm);
                }
            }

            if (Communicator.world.Rank == 0)
            {
                Console.WriteLine($"Total execution time: {totalTime.ElapsedMilliseconds}ms");
            }
        }

        static void RunThreadedImplementation()
        {
            var totalTime = System.Diagnostics.Stopwatch.StartNew();
            Console.WriteLine("Starting Threaded Implementation...");

            var loadTime = System.Diagnostics.Stopwatch.StartNew();
            Matrix root = Matrix.FromFile("C:\\Users\\SergiuGoian\\Desktop\\Uni_projects_UBB_CS\\University_projects_UBB_CS\\SEM5\\PDP\\project\\puzzle\\puzzle\\mat.in");
            Console.WriteLine($"Time to load puzzle: {loadTime.ElapsedMilliseconds}ms");

            if (!Matrix.IsSolvable(root))
            {
                Console.WriteLine("The provided puzzle is unsolvable.");
                return;
            }

            var solveTime = System.Diagnostics.Stopwatch.StartNew();
            var result = SolveUsingThreads(root);
            Console.WriteLine($"Solution search time: {solveTime.ElapsedMilliseconds}ms");

            if (result != null)
            {
                Console.WriteLine($"Solution found in {result.NumOfSteps} steps.");
                Console.WriteLine("Final state:");
                Console.WriteLine(result);
                PrintSolutionPath(result);
            }
            else
            {
                Console.WriteLine("No solution found.");
            }

            Console.WriteLine($"Total execution time: {totalTime.ElapsedMilliseconds}ms");
        }

        // Add this helper method to print the solution path
        static void PrintSolutionPath(Matrix solution)
        {
            var path = new List<Matrix>();
            var current = solution;
            while (current != null)
            {
                path.Add(current);
                current = current.PreviousState;
            }
            path.Reverse();

            Console.WriteLine("\nSolution path:");
            for (int i = 0; i < path.Count; i++)
            {
                Console.WriteLine($"\nStep {i}:");
                Console.WriteLine(path[i]);
                if (i < path.Count - 1)
                {
                    Console.WriteLine($"Move: {path[i + 1].Move}");
                }
            }
        }

       

        // Modify the Search method to use IsGoalState instead of just ManhattanDistance
        static Pair<int, Matrix> Search(Matrix current, int numSteps, int bound)
        {
            int estimation = numSteps + current.ManhattanDistance;

            if (estimation > bound) return new Pair<int, Matrix>(estimation, current);

            if (current.IsGoalState())
            {
                Console.WriteLine($"Solution found at depth {numSteps}!");
                return new Pair<int, Matrix>(-1, current);
            }

            int min = int.MaxValue;
            Matrix solution = null;

            foreach (var next in current.GenerateMoves())
            {
                var result = Search(next, numSteps + 1, bound);
                int t = result.First;

                if (t == -1) return new Pair<int, Matrix>(-1, result.Second);
                if (t < min)
                {
                    min = t;
                    solution = result.Second;
                }
            }

            return new Pair<int, Matrix>(min, solution);
        }
        //static void RunMPIImplementation(string[] args)
        //{
        //    string projectRoot = AppDomain.CurrentDomain.BaseDirectory;
        //    string relativePath = Path.Combine(projectRoot, "mat.in");

        //    using (new MPI.Environment(ref args))
        //    {
        //        Intracommunicator comm = Communicator.world;
        //        int rank = comm.Rank;

        //        if (rank == 0)
        //        {
        //            // Master process
        //            Matrix matrix = Matrix.FromFile("C:\\Users\\SergiuGoian\\Desktop\\Uni_projects_UBB_CS\\University_projects_UBB_CS\\SEM5\\PDP\\project\\puzzle\\puzzle\\mat.in");

        //            if (!Matrix.IsSolvable(matrix))
        //            {
        //                Console.WriteLine("The provided puzzle is unsolvable.");
        //                return;
        //            }

        //            SearchMaster(matrix, comm);
        //        }
        //        else
        //        {
        //            // Worker process
        //            SearchWorker(comm);
        //        }
        //    }
        //}

        //static void RunThreadedImplementation()
        //{
        //    string projectRoot = AppDomain.CurrentDomain.BaseDirectory;
        //    string relativePath = Path.Combine(projectRoot, "mat.in");
        //    Matrix root = Matrix.FromFile("C:\\Users\\SergiuGoian\\Desktop\\Uni_projects_UBB_CS\\University_projects_UBB_CS\\SEM5\\PDP\\project\\puzzle\\puzzle\\mat.in");

        //    if (!Matrix.IsSolvable(root))
        //    {
        //        Console.WriteLine("The provided puzzle is unsolvable.");
        //        return;
        //    }

        //    var result = SolveUsingThreads(root);

        //    if (result != null)
        //    {
        //        Console.WriteLine($"Solution found in {result.NumOfSteps} steps.");
        //        Console.WriteLine(result);
        //    }
        //    else
        //    {
        //        Console.WriteLine("No solution found.");
        //    }
        //}

        static Matrix SolveUsingThreads(Matrix root)
        {
            var taskQueue = new ConcurrentQueue<Matrix>();
            var solutionFound = new ManualResetEventSlim(false);
            Matrix solution = null;

            int bound = root.ManhattanDistance;
            int threadCount = System.Environment.ProcessorCount;

            void Worker()
            {
                while (!solutionFound.IsSet)
                {
                    if (taskQueue.TryDequeue(out Matrix current))
                    {
                        int estimation = current.NumOfSteps + current.ManhattanDistance;

                        if (estimation > bound)
                            continue;

                        if (current.ManhattanDistance == 0)
                        {
                            solution = current;
                            solutionFound.Set();
                            return;
                        }

                        foreach (var next in current.GenerateMoves())
                        {
                            taskQueue.Enqueue(next);
                        }
                    }
                }
            }

            // Initialize task queue with root state
            taskQueue.Enqueue(root);

            // Start worker threads
            var threads = Enumerable.Range(0, threadCount)
                                     .Select(_ => new Thread(Worker))
                                     .ToList();

            threads.ForEach(t => t.Start());
            threads.ForEach(t => t.Join());

            return solution;
        }

        static void SearchMaster(Matrix root, Intracommunicator comm)
        {
            int size = comm.Size;
            int workers = size - 1;
            int minBound = root.ManhattanDistance;
            bool found = false;
            var time = DateTime.Now;

            // Generate initial configurations for workers
            Queue<Matrix> q = new Queue<Matrix>();
            q.Enqueue(root);

            while (q.Count + q.Peek().GenerateMoves().Count - 1 <= workers)
            {
                Matrix curr = q.Dequeue();
                foreach (var neighbor in curr.GenerateMoves())
                {
                    q.Enqueue(neighbor);
                }
            }

            while (!found)
            {
                Queue<Matrix> temp = new Queue<Matrix>(q);

                for (int i = 0; i < q.Count; i++)
                {
                    Matrix curr = temp.Dequeue();
                    SendObject(false, i + 1, 0, comm);
                    SendObject(curr, i + 1, 0, comm);
                    SendObject(minBound, i + 1, 0, comm);
                }

                var pairs = new Pair<int, Matrix>[workers];
                for (int i = 1; i <= q.Count; i++)
                {
                    pairs[i - 1] = ReceiveObject<Pair<int, Matrix>>(i, 0, comm);
                }

                int newMinBound = int.MaxValue;

                foreach (var pair in pairs.Where(p => p != null))
                {
                    if (pair.First == -1)
                    {
                        Console.WriteLine($"Solution found in {pair.Second.NumOfSteps} steps");
                        Console.WriteLine("Solution is:");
                        Console.WriteLine(pair.Second);
                        Console.WriteLine($"Execution time: {(DateTime.Now - time).TotalMilliseconds} ms");
                        found = true;
                        break;
                    }
                    else if (pair.First < newMinBound)
                    {
                        newMinBound = pair.First;
                    }
                }

                if (!found)
                {
                    if (newMinBound == int.MaxValue)
                    {
                        Console.WriteLine("No solution found within the given constraints.");
                        break;
                    }

                    Console.WriteLine($"Depth {newMinBound} reached in {(DateTime.Now - time).TotalMilliseconds} ms");
                    minBound = newMinBound;
                }
            }

            for (int i = 1; i < size; i++)
            {
                SendObject(true, i, 0, comm);
            }
        }

        static void SearchWorker(Intracommunicator comm)
        {
            while (true)
            {
                bool end = ReceiveObject<bool>(0, 0, comm);
                if (end) return;

                Matrix current = ReceiveObject<Matrix>(0, 0, comm);
                int bound = ReceiveObject<int>(0, 0, comm);

                Pair<int, Matrix> result = Search(current, current.NumOfSteps, bound);
                SendObject(result, 0, 0, comm);
            }
        }

        //static Pair<int, Matrix> Search2(Matrix current, int numSteps, int bound)
        //{
        //    int estimation = numSteps + current.ManhattanDistance;
        //    Console.WriteLine($"Searching: Depth {numSteps}, Estimation {estimation}, Bound {bound}");

        //    if (estimation > bound) return new Pair<int, Matrix>(estimation, current);

        //    if (current.ManhattanDistance == 0)
        //    {
        //        Console.WriteLine("Solution found!");
        //        return new Pair<int, Matrix>(-1, current);
        //    }

        //    int min = int.MaxValue;
        //    Matrix solution = null;

        //    foreach (var next in current.GenerateMoves())
        //    {
        //        var result = Search(next, numSteps + 1, bound);
        //        int t = result.First;

        //        if (t == -1) return new Pair<int, Matrix>(-1, result.Second);
        //        if (t < min)
        //        {
        //            min = t;
        //            solution = result.Second;
        //        }
        //    }

        //    return new Pair<int, Matrix>(min, solution);
        //}
        public static void SendObject<T>(T obj, int destination, int tag, Intracommunicator comm)
        {
            string json = JsonSerializer.Serialize(obj);
            comm.Send(json, destination, tag);
        }

        public static T ReceiveObject<T>(int source, int tag, Intracommunicator comm)
        {
            string json = comm.Receive<string>(source, tag);
            return JsonSerializer.Deserialize<T>(json);
        }

    }

    public class Matrix
    {
        public byte[][] Tiles { get; set; }
        public int FreePosI { get; set; }
        public int FreePosJ { get; set; }
        public int NumOfSteps { get; set; }
        public Matrix PreviousState { get; set; }
        public string Move { get; set; }

        public Matrix() { }

        public Matrix(byte[][] tiles, int freePosI, int freePosJ, int numOfSteps, Matrix previousState, string move)
        {
            Tiles = tiles;
            FreePosI = freePosI;
            FreePosJ = freePosJ;
            NumOfSteps = numOfSteps;
            PreviousState = previousState;
            Move = move;
        }
        // Modify the Matrix class to add a proper goal state check
        public bool IsGoalState()
        {
            for (int i = 0; i < 4; i++)
            {
                for (int j = 0; j < 4; j++)
                {
                    if (i == 3 && j == 3)
                    {
                        if (Tiles[i][j] != 0) return false;
                    }
                    else
                    {
                        int expectedValue = i * 4 + j + 1;
                        if (Tiles[i][j] != expectedValue) return false;
                    }
                }
            }
            return true;
        }

        public static Matrix FromFile(string filePath)
        {
            var tiles = new byte[4][];
            int freeI = -1, freeJ = -1;

            using (var reader = new StreamReader(filePath))
            {
                for (int i = 0; i < 4; i++)
                {
                    tiles[i] = reader.ReadLine()
                        .Split()
                        .Select(byte.Parse)
                        .ToArray();

                    for (int j = 0; j < 4; j++)
                    {
                        if (tiles[i][j] == 0)
                        {
                            freeI = i;
                            freeJ = j;
                        }
                    }
                }
            }

            return new Matrix(tiles, freeI, freeJ, 0, null, "");
        }

        public int ManhattanDistance
        {
            get
            {
                int s = 0;
                for (int i = 0; i < 4; i++)
                {
                    for (int j = 0; j < 4; j++)
                    {
                        if (Tiles[i][j] != 0)
                        {
                            int targetI = (Tiles[i][j] - 1) / 4;
                            int targetJ = (Tiles[i][j] - 1) % 4;
                            s += Math.Abs(i - targetI) + Math.Abs(j - targetJ);
                        }
                    }
                }
                return s;
            }
        }

        public List<Matrix> GenerateMoves()
        {
            var moves = new List<Matrix>();
            int[] dx = { 0, -1, 0, 1 };
            int[] dy = { -1, 0, 1, 0 };

            for (int k = 0; k < 4; k++)
            {
                int newI = FreePosI + dx[k];
                int newJ = FreePosJ + dy[k];

                if (newI >= 0 && newI < 4 && newJ >= 0 && newJ < 4)
                {
                    var newTiles = Tiles.Select(row => row.ToArray()).ToArray();
                    newTiles[FreePosI][FreePosJ] = newTiles[newI][newJ];
                    newTiles[newI][newJ] = 0;

                    moves.Add(new Matrix(newTiles, newI, newJ, NumOfSteps + 1, this, $"Move to ({newI}, {newJ})"));
                }
            }

            return moves;
        }

        public override string ToString()
        {
            return string.Join("\n", Tiles.Select(row => string.Join(" ", row)));
        }

        public static bool IsSolvable(Matrix matrix)
        {
            byte[] tiles = matrix.Tiles.SelectMany(row => row).ToArray();

            int inversions = 0;
            for (int i = 0; i < tiles.Length; i++)
            {
                if (tiles[i] == 0) continue;
                for (int j = i + 1; j < tiles.Length; j++)
                {
                    if (tiles[j] == 0) continue;
                    if (tiles[i] > tiles[j]) inversions++;
                }
            }

            int blankRowFromBottom = 4 - matrix.FreePosI;
            int total = inversions + blankRowFromBottom;

            Console.WriteLine($"Inversions: {inversions}, Blank Row from Bottom: {blankRowFromBottom}, Total: {total}");

            bool isSolvable = total % 2 == 0;
            Console.WriteLine(isSolvable ? "The puzzle is solvable." : "The puzzle is unsolvable.");
            return isSolvable;
        }


    }

    public class Pair<T1, T2>
    {
        public T1 First { get; set; }
        public T2 Second { get; set; }

        public Pair() { }

        public Pair(T1 first, T2 second)
        {
            First = first;
            Second = second;
        }

        public override string ToString()
        {
            return $"Pair{{First={First}, Second={Second}}}";
        }
    }
}