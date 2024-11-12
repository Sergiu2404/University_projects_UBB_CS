class Program
{
    static void Main(string[] args)
    {
        DistributedSharedMemory dsm = new DistributedSharedMemory();

        // Create shared variables
        dsm.CreateVariable("var1", 10);
        dsm.CreateVariable("var2", 20);

        // Simulating processes
        string process1Id = "Process1";
        string process2Id = "Process2";

        // Subscribing processes to variables
        dsm.Subscribe("var1", process1Id);
        dsm.Subscribe("var1", process2Id);
        dsm.Subscribe("var2", process1Id);
        dsm.Subscribe("var2", process2Id);

        // Start threads to simulate concurrent operations
        Thread process1 = new Thread(() =>
        {
            for (int i = 0; i < 5; i++)
            {
                Thread.Sleep(500);
                dsm.Write("var1", i * 5, process1Id);
            }
        });

        Thread process2 = new Thread(() =>
        {
            for (int i = 0; i < 5; i++)
            {
                Thread.Sleep(700);
                dsm.CompareAndExchange("var1", i * 5, i * 5 + 1, process2Id);
                dsm.Write("var2", i * 10, process2Id);
            }
        });

        process1.Start();
        process2.Start();

        process1.Join();
        process2.Join();

        Console.WriteLine("Final Values:");
        Console.WriteLine($"var1: {dsm.GetValue("var1")}");
        Console.WriteLine($"var2: {dsm.GetValue("var2")}");
    }
}