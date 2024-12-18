using System.Net.Sockets;
using System.Net;
using System.Text;

public class DSMClient
{
    private Socket clientSocket;
    private string host;
    private int port;

    public DSMClient(string host, int port)
    {
        this.host = host;
        this.port = port;
        clientSocket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
    }

    // Connect to the server
    public void Connect()
    {
        clientSocket.Connect(new IPEndPoint(IPAddress.Parse(host), port));
        Console.WriteLine("Connected to server...");
    }

    // Subscribe to a variable
    public void Subscribe(string variableName)
    {
        SendMessage($"subscribe {variableName}");
    }

    // Write a value to a variable
    public void Write(string variableName, int value)
    {
        SendMessage($"write {variableName} {value}");
    }

    // Perform a compare and exchange operation
    public void CompareAndExchange(string variableName, int expectedValue, int newValue)
    {
        SendMessage($"compare_exchange {variableName} {expectedValue} {newValue}");
    }

    // Send a message to the server
    private void SendMessage(string message)
    {
        byte[] data = Encoding.UTF8.GetBytes(message);
        clientSocket.Send(data);
    }

    // Listen for notifications
    public void ListenForNotifications()
    {
        ThreadPool.QueueUserWorkItem(HandleNotifications);
    }

    // Handle notifications from the server
    private void HandleNotifications(object obj)
    {
        byte[] buffer = new byte[1024];
        NetworkStream stream = new NetworkStream(clientSocket);

        while (true)
        {
            int bytesRead = stream.Read(buffer, 0, buffer.Length);
            if (bytesRead == 0) break;

            string notification = Encoding.UTF8.GetString(buffer, 0, bytesRead);
            Console.WriteLine($"Notification: {notification}");
        }
    }
}
