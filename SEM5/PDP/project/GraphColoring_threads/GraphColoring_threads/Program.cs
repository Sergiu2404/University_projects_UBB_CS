using System;
using System.Diagnostics;

namespace GraphColoring
{
    class Program
    {
        static void Main(string[] args)
        {
            // Check for command-line arguments to choose which program to run
            if (args.Length == 0)
            {
                Console.WriteLine("Please specify which program to run: 'future' or 'mpi'.");
                return;
            }

            string mode = args[0].ToLower();

            switch (mode)
            {
                case "future":
                    // Run ProgramFuture
                    ProgramFuture.Main(args).Wait(); // Call the Main method of ProgramFuture and wait for completion
                    break;

                case "mpi":
                    // Run MPI using mpiexec with 4 processes
                    RunMpiExecution();
                    break;

                default:
                    Console.WriteLine("Invalid option. Use 'future' or 'mpi'.");
                    break;
            }
        }

        private static void RunMpiExecution()
        {
            // Specify the command to run using mpiexec
            string command = "mpiexec";
            string arguments = "-n 4 dotnet run mpi";
            //string arguments = "-n 4 GraphColoring_threads.exe";

            try
            {
                // Start the process
                ProcessStartInfo startInfo = new ProcessStartInfo
                {
                    FileName = command,
                    Arguments = arguments,
                    CreateNoWindow = true,
                    UseShellExecute = false
                };

                Process process = Process.Start(startInfo);
                process.WaitForExit(); // Wait for the process to complete
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error executing mpiexec: " + ex.Message);
            }
        }
    }
}
