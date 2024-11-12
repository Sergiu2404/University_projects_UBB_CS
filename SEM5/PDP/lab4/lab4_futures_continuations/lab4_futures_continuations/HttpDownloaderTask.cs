using lab4_futures_continuations;

public class HttpDownloaderTask : HttpDownloader
{
    protected override string ParserType => "Task";

    public HttpDownloaderTask(List<string> urls) : base(urls)
    {
    }

    protected override void Run()
    {
        var tasks = Map((index, url) => Start(SocketHandler.Create(url, index)));
        Task.WhenAll(tasks).GetAwaiter().GetResult(); // Avoid blocking call
    }

    private async Task Start(SocketHandler socket)
    {
        await socket.BeginConnectAsync();
        LogConnected(socket);

        var numberOfSentBytes = await socket.BeginSendAsync("GET " + socket.UrlPath + " HTTP/1.1\r\nHost: " + socket.BaseUrl + "\r\nConnection: close\r\n\r\n");
        LogSent(socket, numberOfSentBytes);

        var response = await socket.BeginReceiveAsync();
        LogReceived(socket);

        socket.ShutdownAndClose();
    }
}
