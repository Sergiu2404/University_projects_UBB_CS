using System;
using System.Collections.Generic;
using System.ComponentModel.Design;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace lab4_futures_continuations
{
    public static class DirectCallBack
    {
        private static List<string> HOSTS;
        private static string response = String.Empty;

        public static void Run(List<string> hostnames)
        {
            HOSTS = hostnames;

            for (var i = 0; i < HOSTS.Count; i++)
            {
                DoStart(i); //call this for each hostname
            }
        }
        
        private static void DoStart(object idObject)
        {
            var id = (int)idObject; // convert object to an ID
            StartClient(HOSTS[id], id); //start client for host with given ID

            Thread.Sleep(2000); // pause 2 seconds before continuing
        }

        //initialize and communicate with a remote server
        private static void StartClient(string host, int id)
        {
            // assign an IP to hostname
            var hostInfo = Dns.GetHostEntry(host.Split('/')[0]);
            var ipAddress = hostInfo.AddressList[0];
            var remoteEndpoint = new IPEndPoint(ipAddress, 80);

            // create a TCP socket for communication
            var client = new Socket(ipAddress.AddressFamily, SocketType.Stream, ProtocolType.Tcp);

            // state obj for holding conneciton and request details
            var state = new StateObject
            {
                workSocket = client,
                hostname = host.Split('/')[0], //base hostname
                endpointPath = host.Contains("/") ? host.Substring(host.IndexOf("/")) : "/", //get path or set to default path "/"
                remoteEndpoint = remoteEndpoint,
                clientID = id
            };

            // async conn to server
            state.workSocket.BeginConnect(state.remoteEndpoint, OnConnect, state);
            state.connectDone.WaitOne(); //wait conn to complete

            // parse URL to split hostname and endpoint path
            var url = HttpUtils.parseURL(host);

            // http get request to server
            Send(state, string.Format("GET /{0} HTTP/1.1\r\nHOST: {1}\r\n\r\n", url.Item2, url.Item1));
            state.sendDone.WaitOne();

            // response from the server
            Receive(state);
            state.receiveDone.WaitOne();

            Console.WriteLine("{0} - Response received : \n{1}", state.clientID, response);

            client.Shutdown(SocketShutdown.Both);
            client.Close();
        }

        // callback triggered when conn established
        private static void OnConnect(IAsyncResult ar)
        {
            // get state object containing conn details
            var state = (StateObject)ar.AsyncState;
            var clientSocket = state.workSocket;

            // end conn for socket for resulted socket
            clientSocket.EndConnect(ar);

            Console.WriteLine("Socket connected to {0}",
                clientSocket.RemoteEndPoint.ToString());

            // signal that the connection has been established
            state.connectDone.Set();
        }

        // initiate receiving data from the server
        private static void Receive(StateObject state)
        {
            try
            {
                // begin async data reception
                state.workSocket.BeginReceive(state.receiveBuffer, 0, StateObject.BUFFER_SIZE, 0,
                    new AsyncCallback(ReceiveCallback), state);
            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }
        }

        // callback to handle received data
        private static void ReceiveCallback(IAsyncResult ar)
        {
            try
            {
                // retrieve the state object and socket
                StateObject state = (StateObject)ar.AsyncState;
                Socket client = state.workSocket;

                // read data from server
                int bytesRead = client.EndReceive(ar);

                if (bytesRead > 0)
                {
                    // append received data to response content
                    state.responseContent.Append(Encoding.ASCII.GetString(state.receiveBuffer, 0, bytesRead));

                    // check if http response header was fully received
                    if (!HttpUtils.responseHeaderFullyObtained(state.responseContent.ToString()))
                    {
                        // continue receiving more data if necessary
                        client.BeginReceive(state.receiveBuffer, 0, StateObject.BUFFER_SIZE, 0,
                            new AsyncCallback(ReceiveCallback), state);
                    }
                    else
                    {
                        // get body from the http response
                        var responseBody = HttpUtils.getResponseBody(state.responseContent.ToString());

                        // check if all content received
                        var contentLengthHeaderValue = HttpUtils.getContentLength(state.responseContent.ToString());

                        if (contentLengthHeaderValue > responseBody.Length)
                        {
                            // continue receiving data if content incomplete
                            client.BeginReceive(state.receiveBuffer, 0, StateObject.BUFFER_SIZE, 0,
                                new AsyncCallback(ReceiveCallback), state);
                        }
                        else
                        {
                            // Store complete response and signal completion
                            if (state.responseContent.Length > 1)
                            {
                                response = state.responseContent.ToString();
                            }
                            state.receiveDone.Set();
                        }
                    }
                }
                else
                {
                    //all data has been received
                    if (state.responseContent.Length > 1)
                    {
                        response = state.responseContent.ToString();
                    }
                    state.receiveDone.Set();
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }
        }

        // Method to send data to the server
        private static void Send(StateObject state, String data)
        {
            // Convert the data string to bytes
            byte[] byteData = Encoding.ASCII.GetBytes(data);

            // Begin sending data asynchronously
            state.workSocket.BeginSend(byteData, 0, byteData.Length, 0,
                new AsyncCallback(SendCallback), state);
        }

        // callback triggered when data was sent
        private static void SendCallback(IAsyncResult ar)
        {
            try
            {
                // Retrieve state object and complete send operation
                StateObject state = (StateObject)ar.AsyncState;
                int bytesSent = state.workSocket.EndSend(ar);

                Console.WriteLine("{0} - Sent {1} bytes to server.", state.clientID, bytesSent);

                // signal that the send operation is complete
                state.sendDone.Set();
            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }
        }
    }
}