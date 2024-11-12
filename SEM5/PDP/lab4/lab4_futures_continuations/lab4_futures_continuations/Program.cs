using lab4_futures_continuations;

class Program
{
    private static readonly List<string> Urls = new()
    {
        "http://www.serbanology.com/",
        "http://www.dspcluj.ro/HTML/CORONAVIRUS/incidenta.html",
        "http://www.apache.org/"
    };

    static void Main()
    {
        Console.WriteLine("1. Callback Parser");
        Console.WriteLine("2. Task Parser");
        Console.WriteLine("3. Async Await Parser");
        string choice = Console.ReadLine();
        switch (choice)
        {
            case "1":
                new HttpDownloaderCallback(Urls);
                break;
            case "2":
                new HttpDownloaderTask(Urls);
                break;
            case "3":
                new HttpDownloaderAsyncAwait(Urls);
                break;
            default:
                Console.WriteLine("Invalid choice");
                break;
        }
    }
}
