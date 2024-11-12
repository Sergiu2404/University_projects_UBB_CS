using lab1_pb1_supermarket.entities;
using System.Diagnostics;

public class Program
{
    public static void Main(string[] args)
    {
        List<Product> products = LoadProductsFromFile(@"..\..\..\products.txt");
        Supermarket supermarket = new Supermarket(products);
        List<Bill> bills = Bill.GenerateRandomBills(products, products.Count / 2);

        List<Thread> threads = new List<Thread>();
        int threadCount = 6;
        int iterations = (int)Math.Ceiling((double)bills.Count / threadCount);

        Stopwatch stopwatch = new Stopwatch();
        stopwatch.Start();

        for (int i = 0; i < threadCount; ++i)
        {
            int start = i * iterations;
            int end = Math.Min((i + 1) * iterations, bills.Count);
            int count = end - start;

            if (count > 0)
            {
                List<Bill> sublist = bills.GetRange(start, count);
                Console.WriteLine($"Starting thread {i + 1} to process {count} bills from index {start} to {end - 1}.");


                Thread t = new Thread(() => ProcessBills(sublist, supermarket));
                threads.Add(t);
                t.Start();
            }
        }


        foreach (Thread thread in threads)
        {
            thread.Join();
        }

        stopwatch.Stop();


        Console.WriteLine(supermarket.CheckEverything() ? "Data is consistent!" : "Data inconsistency found!");
        

        Console.WriteLine($"{stopwatch} ms");

    }

    private static void ProcessBills(List<Bill> bills, Supermarket supermarket)
    {
        foreach (Bill bill in bills)
        {
            Console.WriteLine($"Thread {Thread.CurrentThread.ManagedThreadId} is processing bill with {bill.Products.Count} products.");
            supermarket.ProcessBill(bill);
            Console.WriteLine($"Thread {Thread.CurrentThread.ManagedThreadId} finished processing the bill.");
        }
    }

    private static List<Product> LoadProductsFromFile(string relativeFilePath)
    {
        List<Product> products = new List<Product>();
        string baseDirectory = AppDomain.CurrentDomain.BaseDirectory;
        string fullPath = Path.Combine(baseDirectory, relativeFilePath);

        using (StreamReader reader = new StreamReader(fullPath))
        {
            int n = int.Parse(reader.ReadLine());

            for (int i = 0; i < n; ++i)
            {
                string[] parts = reader.ReadLine().Split(' ');
                string name = parts[0];
                int quantity = int.Parse(parts[1]);
                int price = int.Parse(parts[2]);

                products.Add(new Product(name, quantity, price));
            }
        }

        return products;
    }
}




///////////////////////