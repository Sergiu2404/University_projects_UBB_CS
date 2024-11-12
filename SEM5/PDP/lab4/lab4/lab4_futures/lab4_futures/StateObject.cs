using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Sockets;
using System.Net;
using System.Text;
using System.Threading.Tasks;

namespace lab4_futures_continuations
{
    public class StateObject
    {
        public Socket workSocket = null;
        public const int BUFFER_SIZE = 512;
        public byte[] receiveBuffer = new byte[BUFFER_SIZE];
        public StringBuilder responseContent = new StringBuilder();
        public int clientID;
        public string hostname;
        public string endpointPath;
        public IPEndPoint remoteEndpoint;
        
        //mutexes
        public ManualResetEvent connectDone = new ManualResetEvent(false);
        public ManualResetEvent sendDone = new ManualResetEvent(false);
        public ManualResetEvent receiveDone = new ManualResetEvent(false);
    }
}
