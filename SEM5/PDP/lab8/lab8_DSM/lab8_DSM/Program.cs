public class Program
{
    public static void Main(string[] args)
    {
        int port = 5000; // Port for communication

        // Start DSM server
        DSMServer server = new DSMServer(port, new string[] { "var1", "var2", "var3" });
        Thread serverThread = new Thread(() => server.Start());
        serverThread.Start();

        // Create client and connect
        DSMClient client = new DSMClient("127.0.0.1", port);
        client.Connect();
        client.Subscribe("var1");
        client.Subscribe("var2");
        client.ListenForNotifications();

        // Simulate some operations
        client.Write("var1", 42);
        client.CompareAndExchange("var2", 0, 100);
    }
}
