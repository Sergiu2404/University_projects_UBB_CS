using System.Net.Sockets;
using System.Net;
using System.Text;

public class DSMServer
{
    private DSM dsm;
    private Socket listenerSocket;
    private int port;

    public DSMServer(int port, string[] variables)
    {
        this.port = port;
        dsm = new DSM(variables);
    }

    // Start the server and listen for connections
    public void Start()
    {
        listenerSocket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
        listenerSocket.Bind(new IPEndPoint(IPAddress.Any, port));
        listenerSocket.Listen(10);

        Console.WriteLine($"Server started on port {port}...");
        while (true)
        {
            Socket clientSocket = listenerSocket.Accept();
            Console.WriteLine("Client connected...");
            ThreadPool.QueueUserWorkItem(HandleClient, clientSocket);
        }
    }

    // Handle incoming client requests
    private void HandleClient(object clientObj)
    {
        Socket clientSocket = (Socket)clientObj;
        NetworkStream stream = new NetworkStream(clientSocket);
        byte[] buffer = new byte[1024];

        while (true)
        {
            int bytesRead = stream.Read(buffer, 0, buffer.Length);
            if (bytesRead == 0) break;

            string receivedMessage = Encoding.UTF8.GetString(buffer, 0, bytesRead);
            string[] messageParts = receivedMessage.Split(' ');

            if (messageParts[0] == "subscribe")
            {
                string variableName = messageParts[1];
                dsm.Subscribe(variableName, clientSocket);
            }
            else if (messageParts[0] == "write")
            {
                string variableName = messageParts[1];
                int value = int.Parse(messageParts[2]);
                dsm.Write(variableName, value, clientSocket);
            }
            else if (messageParts[0] == "compare_exchange")
            {
                string variableName = messageParts[1];
                int expectedValue = int.Parse(messageParts[2]);
                int newValue = int.Parse(messageParts[3]);
                bool success = dsm.CompareAndExchange(variableName, expectedValue, newValue, clientSocket);
                if (success)
                {
                    Console.WriteLine($"CAS operation succeeded for {variableName}");
                }
                else
                {
                    Console.WriteLine($"CAS operation failed for {variableName}");
                }
            }
        }

        clientSocket.Close();
    }
}
