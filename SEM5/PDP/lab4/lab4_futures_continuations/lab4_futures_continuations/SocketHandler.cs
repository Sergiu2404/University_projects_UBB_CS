using System.Net.Sockets;
using System.Net;
using System.Text;

public class SocketHandler
{
    private readonly Socket _socket;
    private readonly string _url;
    public int Id { get; }
    public string BaseUrl { get; private set; }
    public string UrlPath { get; private set; }
    public bool Connected => _socket.Connected;
    public string GetResponseContent { get; private set; }

    private const int BufferSize = 1024;

    public SocketHandler(string url, int index)
    {
        _url = url;
        Id = index;
        _socket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
        ParseUrl();
    }

    public static SocketHandler Create(string url, int index)
    {
        return new SocketHandler(url, index);
    }

    private void ParseUrl()
    {
        var uri = new Uri(_url);
        BaseUrl = uri.Host;
        UrlPath = uri.PathAndQuery;
    }

    public async Task BeginConnectAsync()
    {
        var ipAddress = (await Dns.GetHostAddressesAsync(BaseUrl)).FirstOrDefault();
        var remoteEP = new IPEndPoint(ipAddress, 80); // Use 443 for HTTPS if needed

        // Asynchronously connect to the server
        await Task.Factory.FromAsync(
            _socket.BeginConnect,
            _socket.EndConnect,
            remoteEP,
            null);
    }

    public async Task<int> BeginSendAsync(string request)
    {
        var requestBytes = Encoding.ASCII.GetBytes(request);
        return await Task.Factory.FromAsync(
            (callback, state) => _socket.BeginSend(requestBytes, 0, requestBytes.Length, SocketFlags.None, callback, state),
            _socket.EndSend,
            null);
    }

    public async Task<string> BeginReceiveAsync()
    {
        var buffer = new byte[BufferSize];
        var responseBuilder = new StringBuilder();

        // Read until the socket is closed
        while (true)
        {
            int bytesRead = await Task.Factory.FromAsync(
                (callback, state) => _socket.BeginReceive(buffer, 0, buffer.Length, SocketFlags.None, callback, state),
                _socket.EndReceive,
                null);

            if (bytesRead == 0) break; // Socket is closed

            responseBuilder.Append(Encoding.ASCII.GetString(buffer, 0, bytesRead));
        }

        GetResponseContent = responseBuilder.ToString();
        return GetResponseContent;
    }

    public void ShutdownAndClose()
    {
        if (_socket.Connected)
        {
            _socket.Shutdown(SocketShutdown.Both);
            _socket.Close();
        }
    }
}
