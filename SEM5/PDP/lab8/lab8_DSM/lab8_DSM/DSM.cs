using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;

public class DSM
{
    // Variables stored in the DSM system
    private Dictionary<string, int> variables = new Dictionary<string, int>();

    // Subscribers for each variable
    private Dictionary<string, List<Socket>> subscriptions = new Dictionary<string, List<Socket>>();

    // Lock object for thread synchronization
    private readonly object lockObj = new object();

    // Initialize DSM with variables
    public DSM(string[] variableNames)
    {
        foreach (var name in variableNames)
        {
            variables[name] = 0; // Initialize each variable to 0
            subscriptions[name] = new List<Socket>();
        }
    }

    // Subscribe to a variable
    public void Subscribe(string variableName, Socket subscriberSocket)
    {
        lock (lockObj)
        {
            if (subscriptions.ContainsKey(variableName))
            {
                subscriptions[variableName].Add(subscriberSocket);
            }
        }
    }

    // Write a value to a variable (local or remote)
    public void Write(string variableName, int value, Socket senderSocket)
    {
        lock (lockObj)
        {
            if (variables.ContainsKey(variableName))
            {
                variables[variableName] = value;
                NotifySubscribers(variableName);
            }
        }
    }

    // Compare and exchange operation
    public bool CompareAndExchange(string variableName, int expectedValue, int newValue, Socket senderSocket)
    {
        lock (lockObj)
        {
            if (variables.ContainsKey(variableName) && variables[variableName] == expectedValue)
            {
                variables[variableName] = newValue;
                NotifySubscribers(variableName);
                return true;
            }
            return false;
        }
    }

    // Notify subscribers about a variable change
    private void NotifySubscribers(string variableName)
    {
        if (subscriptions.ContainsKey(variableName))
        {
            foreach (var subscriber in subscriptions[variableName])
            {
                SendNotification(subscriber, variableName, variables[variableName]);
            }
        }
    }

    // Send a change notification to a subscriber
    private void SendNotification(Socket subscriber, string variableName, int newValue)
    {
        try
        {
            string message = $"Variable {variableName} changed to {newValue}";
            byte[] data = Encoding.UTF8.GetBytes(message);
            subscriber.Send(data);
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Error sending notification: {ex.Message}");
        }
    }
}
