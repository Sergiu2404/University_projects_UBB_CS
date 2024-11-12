using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace lab4_futures_continuations
{
    public class HttpDownloaderCallback : HttpDownloader
    {
        protected override string ParserType => "Callback";

        public HttpDownloaderCallback(List<string> urls) : base(urls)
        {
        }

        protected override void Run()
        {
            ForEach((index, url) => Start(SocketHandler.Create(url, index)));
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
}