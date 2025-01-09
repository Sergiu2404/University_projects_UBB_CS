class Program
{
    static void Main(string[] args)
    {
        DistributedSharedMemory dsm = new DistributedSharedMemory();

        dsm.CreateVariable("varUnaffected", 10);
        dsm.CreateVariable("varAffected1", 20);
        //dsm.CreateVariable("varAffected2", 100);

        string process1Id = "Process1";
        string process2Id = "Process2";
        //string process3Id = "Process3";

        dsm.Subscribe("varUnaffected", process1Id);
        dsm.Subscribe("varUnaffected", process2Id);
        //dsm.Subscribe("varUnaffected", process3Id);
        dsm.Subscribe("varAffected1", process1Id);
        dsm.Subscribe("varAffected1", process2Id);
        //dsm.Subscribe("varAffected1", process3Id);
        //dsm.Subscribe("varAffected2", process1Id);
        //dsm.Subscribe("varAffected2", process2Id);
        //dsm.Subscribe("varAffected2", process3Id);

        Thread process1 = new Thread(() =>
        {
            for (int i = 1; i < 6; i++)
            {
                Thread.Sleep(500);
                dsm.Write("varUnaffected", i * 10, process1Id);
              
            }
        });

        Thread process2 = new Thread(() =>
        {
            for (int i = 1; i < 6; i++)
            {
                Thread.Sleep(700);
                int currentVal = dsm.GetValue("varUnaffected");
                dsm.Write("varAffected1", currentVal - i, process2Id);
                dsm.Write("varAffected1", currentVal - i * 2, process1Id);
            }
        });

        //Thread process3 = new Thread(() =>
        //{
        //    for (int i = 0; i < 6; i++)
        //    {
        //        Thread.Sleep(800);
        //        int currentVal = dsm.GetValue("varAffected1");
        //        dsm.Write("varAffected2", currentVal * 2, process3Id);
        //    }
        //});

        process1.Start();
        process2.Start();
        //process3.Start();

        process1.Join();
        process2.Join();
        //process3.Join();

        Console.WriteLine("Final Values:");
        Console.WriteLine($"varUnaffected: {dsm.GetValue("varUnaffected")}");
        Console.WriteLine($"varAffected1: {dsm.GetValue("varAffected1")}");
        //Console.WriteLine($"varAffected2: {dsm.GetValue("varAffected2")}");
    }
}
