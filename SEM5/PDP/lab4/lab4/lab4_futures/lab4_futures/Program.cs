﻿using System;
using System.Collections.Generic;
using System.Reflection;
using lab4_futures_continuations;

class Program
{
    static void Main()
    {
        var hosts = new List<string> {
                "www.cs.ubbcluj.ro/~rlupsa/edu/pdp/lab-4-futures-continuations.html",
                "www.cs.ubbcluj.ro/~rlupsa/edu/pdp/index.html"
        };

        //DirectCallBack.Run(hosts);
        //TaskMechanism.Run(hosts);
        //AsyncAwaitTasksMechanism.Run(hosts);
        Console.ReadLine();
    }
}
