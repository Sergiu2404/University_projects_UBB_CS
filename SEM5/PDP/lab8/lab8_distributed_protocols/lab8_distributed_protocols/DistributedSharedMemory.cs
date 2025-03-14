﻿using lab8_distributed_protocols;
using System.Collections.Concurrent;

public class DistributedSharedMemory
{
    private ConcurrentDictionary<string, Variable> variables = new ConcurrentDictionary<string, Variable>();

    public void CreateVariable(string name, int initialValue)
    {
        variables[name] = new Variable(initialValue);
    }

    public void Write(string name, int value, string processId)
    {
        if (variables.ContainsKey(name))
        {
            variables[name].SetValue(value, processId);
        }
    }

    public void Subscribe(string name, string processId)
    {
        if (variables.ContainsKey(name))
        {
            var variable = variables[name];
            if (!variable.Subscribers.Contains(processId))
            {
                variable.Subscribe(processId);
                variable.OnChange += (subscriber, newValue) =>
                    Console.WriteLine($"Process notified: {name} changed to {newValue}");
            }
        }
    }


    public int GetValue(string name)
    {
        if (variables.ContainsKey(name))
        {
            return variables[name].Value;
        }
        throw new Exception($"Variable {name} does not exist.");
    }
}