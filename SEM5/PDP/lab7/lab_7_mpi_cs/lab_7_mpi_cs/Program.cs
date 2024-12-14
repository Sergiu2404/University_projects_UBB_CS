using System;
using System.Collections.Generic;
using System.Linq;
using System.Diagnostics;
using System.Threading.Tasks;
using MPI;

class Polynomial
{
    public List<double> coefficients;

    public Polynomial(List<double> coeffs)
    {
        coefficients = new List<double>(coeffs);
    }

    // Simple sequential multiplication
    //public Polynomial SimpleMultiply(Polynomial other)
    //{
    //    var result = new List<double>(new double[coefficients.Count + other.coefficients.Count - 1]);

    //    for (int i = 0; i < coefficients.Count; i++)
    //    {
    //        for (int j = 0; j < other.coefficients.Count; j++)
    //        {
    //            result[i + j] += coefficients[i] * other.coefficients[j];
    //        }
    //    }

    //    return new Polynomial(result);
    //}

    //// Parallel multiplication
    //public Polynomial ParallelMultiply(Polynomial other)
    //{
    //    var result = new List<double>(new double[coefficients.Count + other.coefficients.Count - 1]);

    //    Parallel.For(0, coefficients.Count, i =>
    //    {
    //        for (int j = 0; j < other.coefficients.Count; j++)
    //        {
    //            lock (result)
    //            {
    //                result[i + j] += coefficients[i] * other.coefficients[j];
    //            }
    //        }
    //    });

    //    return new Polynomial(result);
    //}

    // Karatsuba multiplication
    public Polynomial Karatsuba(Polynomial other)
    {
        int maxSize = Math.Max(coefficients.Count, other.coefficients.Count);

        // Straight forward multiplication if one coefficient for each term
        if (maxSize == 1)
            return new Polynomial(new List<double> { coefficients[0] * other.coefficients[0] });

        // Pad to next power of 2
        int newSize = (int)Math.Pow(2, Math.Ceiling(Math.Log(maxSize, 2)));

        // Pad coefficients with zeros
        var aCoeffs = coefficients.Concat(Enumerable.Repeat(0.0, newSize - coefficients.Count)).ToList();
        var bCoeffs = other.coefficients.Concat(Enumerable.Repeat(0.0, newSize - other.coefficients.Count)).ToList();

        int half = newSize / 2;

        var aLow = new Polynomial(aCoeffs.Take(half).ToList());
        var aHigh = new Polynomial(aCoeffs.Skip(half).ToList());
        var bLow = new Polynomial(bCoeffs.Take(half).ToList());
        var bHigh = new Polynomial(bCoeffs.Skip(half).ToList());

        var ac = aHigh.Karatsuba(bHigh);           // Product of high degree polynomials
        var bd = aLow.Karatsuba(bLow);             // Product of low degree polynomials
        var ab_cd = aHigh.Plus(aLow)
            .Karatsuba(bHigh.Plus(bLow))
            .Minus(ac)
            .Minus(bd);                            // (a+b)(c+d) - ac - bd

        // Combine result with shifting
        var result = ac.Shift(2 * half)
            .Plus(ab_cd.Shift(half))
            .Plus(bd);

        // Trim to original polynomial length
        return new Polynomial(result.coefficients.Take(2 * maxSize - 1).ToList());
    }

    // Parallel Karatsuba multiplication
    public Polynomial ParallelKaratsuba(Polynomial other)
    {
        int maxSize = Math.Max(coefficients.Count, other.coefficients.Count);

        // Base case: single-term polynomial multiplication
        if (maxSize == 1)
            return new Polynomial(new List<double> { coefficients[0] * other.coefficients[0] });

        // Pad to next power of 2
        int newSize = (int)Math.Pow(2, Math.Ceiling(Math.Log(maxSize, 2)));

        // Pad coefficients with zeros
        var aCoeffs = coefficients.Concat(Enumerable.Repeat(0.0, newSize - coefficients.Count)).ToList();
        var bCoeffs = other.coefficients.Concat(Enumerable.Repeat(0.0, newSize - other.coefficients.Count)).ToList();

        int half = newSize / 2;

        var aLow = new Polynomial(aCoeffs.Take(half).ToList());
        var aHigh = new Polynomial(aCoeffs.Skip(half).ToList());
        var bLow = new Polynomial(bCoeffs.Take(half).ToList());
        var bHigh = new Polynomial(bCoeffs.Skip(half).ToList());

        // Parallel tasks
        var taskAC = Task.Run(() => aHigh.Karatsuba(bHigh));
        var taskBD = Task.Run(() => aLow.Karatsuba(bLow));
        var taskABCD = Task.Run(() =>
            aHigh.Plus(aLow)
                .Karatsuba(bHigh.Plus(bLow))
                .Minus(taskAC.Result)
                .Minus(taskBD.Result)
        );

        // Wait for all tasks
        Task.WaitAll(taskAC, taskBD, taskABCD);

        // Combine results
        var result = taskAC.Result.Shift(2 * half)
            .Plus(taskABCD.Result.Shift(half))
            .Plus(taskBD.Result);

        // Trim to original polynomial length
        return new Polynomial(result.coefficients.Take(2 * maxSize - 1).ToList());
    }

    // MPI-based Karatsuba multiplication
    public Polynomial MPIKaratsuba(Polynomial other, Intracommunicator comm, int rank)
    {
        int maxSize = Math.Max(coefficients.Count, other.coefficients.Count);

        // Base case: single-term polynomial multiplication
        if (maxSize == 1)
            return new Polynomial(new List<double> { coefficients[0] * other.coefficients[0] });

        // Pad to next power of 2
        int newSize = (int)Math.Pow(2, Math.Ceiling(Math.Log(maxSize, 2)));

        // Pad coefficients with zeros
        var aCoeffs = coefficients.Concat(Enumerable.Repeat(0.0, newSize - coefficients.Count)).ToList();
        var bCoeffs = other.coefficients.Concat(Enumerable.Repeat(0.0, newSize - other.coefficients.Count)).ToList();

        int half = newSize / 2;

        var aLow = new Polynomial(aCoeffs.Take(half).ToList());
        var aHigh = new Polynomial(aCoeffs.Skip(half).ToList());
        var bLow = new Polynomial(bCoeffs.Take(half).ToList());
        var bHigh = new Polynomial(bCoeffs.Skip(half).ToList());

        Polynomial ac, bd, ab_cd;

        // Distribute computation across processes
        if (rank == 0)
        {
            ac = aHigh.Karatsuba(bHigh);
            bd = aLow.Karatsuba(bLow);
            ab_cd = aHigh.Plus(aLow)
                .Karatsuba(bHigh.Plus(bLow))
                .Minus(ac)
                .Minus(bd);
        }
        else
        {
            ac = new Polynomial(new List<double>());
            bd = new Polynomial(new List<double>());
            ab_cd = new Polynomial(new List<double>());
        }

        // Convert List<double> to double[] for broadcasting
        double[] acArray = ac.coefficients.ToArray();
        double[] bdArray = bd.coefficients.ToArray();
        double[] abCdArray = ab_cd.coefficients.ToArray();

        // Broadcast results
        comm.Broadcast(ref acArray, 0);
        comm.Broadcast(ref bdArray, 0);
        comm.Broadcast(ref abCdArray, 0);

        // Convert back to List<double>
        ac.coefficients = acArray.ToList();
        bd.coefficients = bdArray.ToList();
        ab_cd.coefficients = abCdArray.ToList();

        // Combine results
        var result = ac.Shift(2 * half)
            .Plus(ab_cd.Shift(half))
            .Plus(bd);

        // Trim to original polynomial length
        return new Polynomial(result.coefficients.Take(2 * maxSize - 1).ToList());
    }


    // Helper method to add two polynomials
    public Polynomial Plus(Polynomial other)
    {
        var result = new List<double>(
            new double[Math.Max(coefficients.Count, other.coefficients.Count)]
        );

        for (int i = 0; i < coefficients.Count; i++)
            result[i] += coefficients[i];

        for (int i = 0; i < other.coefficients.Count; i++)
            result[i] += other.coefficients[i];

        return new Polynomial(result);
    }

    // Helper method to subtract two polynomials
    public Polynomial Minus(Polynomial other)
    {
        var result = new List<double>(
            new double[Math.Max(coefficients.Count, other.coefficients.Count)]
        );

        for (int i = 0; i < coefficients.Count; i++)
            result[i] += coefficients[i];

        for (int i = 0; i < other.coefficients.Count; i++)
            result[i] -= other.coefficients[i];

        return new Polynomial(result);
    }

    // Helper method to shift polynomial (multiply by x^k)
    public Polynomial Shift(int k)
    {
        if (k == 0) return this;

        var shiftedCoeffs = Enumerable.Repeat(0.0, k)
            .Concat(coefficients)
            .ToList();

        return new Polynomial(shiftedCoeffs);
    }

    // Override ToString for easy printing
    public override string ToString()
    {
        return string.Join(" + ",
            coefficients.Select((coeff, power) =>
                coeff != 0 ?
                (power == 0 ? coeff.ToString() : $"{coeff}x^{power}")
                : "")
            .Where(s => !string.IsNullOrEmpty(s))
        );
    }
}

class Program
{
    static void Main(string[] args)
    {
        // Initialize MPI only once here
        using (new MPI.Environment(ref args))  // This should be done only once
        {
            Intracommunicator comm = Communicator.world;
            int rank = comm.Rank;
            int size = comm.Size;

            // Define polynomials
            var poly1 = new Polynomial(new List<double> { 1, 2, 3 }); // 1 + 2x + 3x^2
            var poly2 = new Polynomial(new List<double> { 4, 5 });    // 4 + 5x

            // Sequential Multiplication
            var watch = Stopwatch.StartNew();
            var resultParallelKaratsuba = poly1.ParallelKaratsuba(poly2);
            watch.Stop();
            Console.WriteLine($"Parallel Karatsuba Multiply Result: {resultParallelKaratsuba} (Time: {watch.ElapsedMilliseconds} ms)");

            // MPI Karatsuba Multiplication
            try
            {
                watch.Restart();
                var resultMPIKaratsuba = poly1.MPIKaratsuba(poly2, comm, rank);
                watch.Stop();
                Console.WriteLine($"MPI Karatsuba Multiply Result: {resultMPIKaratsuba} (Time: {watch.ElapsedMilliseconds} ms)");
            }
            catch (Exception ex)
            {
                Console.WriteLine($"MPI Karatsuba failed: {ex.Message}");
            }
        }
    }
}




//class Program
//{
//    static void Main(string[] args)
//    {
//        var poly1 = new Polynomial(new List<double> { 1, 2, 3 }); // 1 + 2x + 3x^2
//        var poly2 = new Polynomial(new List<double> { 4, 5 });    // 4 + 5x

//        //// Sequential Multiplication
//        var watch = Stopwatch.StartNew();
//        //var resultSimple = poly1.SimpleMultiply(poly2);
//        //watch.Stop();
//        //Console.WriteLine($"Simple Multiply Result: {resultSimple} (Time: {watch.ElapsedMilliseconds} ms)");

//        //// Parallel Multiplication
//        //watch.Restart();
//        //var resultParallel = poly1.ParallelMultiply(poly2);
//        //watch.Stop();
//        //Console.WriteLine($"Parallel Multiply Result: {resultParallel} (Time: {watch.ElapsedMilliseconds} ms)");

//        //// Karatsuba Multiplication
//        //watch.Restart();
//        //var resultKaratsuba = poly1.Karatsuba(poly2);
//        //watch.Stop();
//        //Console.WriteLine($"Karatsuba Multiply Result: {resultKaratsuba} (Time: {watch.ElapsedMilliseconds} ms)");

//        // Parallel Karatsuba Multiplication
//        watch.Restart();
//        var resultParallelKaratsuba = poly1.ParallelKaratsuba(poly2);
//        watch.Stop();
//        Console.WriteLine($"Parallel Karatsuba Multiply Result: {resultParallelKaratsuba} (Time: {watch.ElapsedMilliseconds} ms)");

//        // MPI Karatsuba Multiplication
//        try
//        {
//            watch.Restart();
//            var resultMPIKaratsuba = poly1.MPIKaratsuba(poly2);
//            watch.Stop();
//            Console.WriteLine($"MPI Karatsuba Multiply Result: {resultMPIKaratsuba} (Time: {watch.ElapsedMilliseconds} ms)");
//        }
//        catch (Exception ex)
//        {
//            Console.WriteLine($"MPI Karatsuba failed: {ex.Message}");
//        }
//    }

//}