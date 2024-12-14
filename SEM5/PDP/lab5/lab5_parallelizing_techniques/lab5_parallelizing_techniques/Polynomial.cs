using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Threading.Tasks;

public class Polynomial
{
    private readonly List<double> coefficients;

    public Polynomial(List<double> coefficients)
    {
        this.coefficients = coefficients;
    }

    public Polynomial() {
        this.coefficients = new List<double> { };
    }

    public Polynomial SimpleMultiply(Polynomial other)
    {
        var result = new double[this.coefficients.Count + other.coefficients.Count - 1];

        for (int i = 0; i < this.coefficients.Count; i++)
            for (int j = 0; j < other.coefficients.Count; j++)
                result[i + j] += this.coefficients[i] * other.coefficients[j];

        return new Polynomial(result.ToList());
    }

    public Polynomial ParallelMultiply(Polynomial other)
    {
        var result = new double[this.coefficients.Count + other.coefficients.Count - 1];
        object lockObj = new object(); // Lock object for thread safety

        Parallel.For(0, this.coefficients.Count, i => //creates a thread pool
        {
            for (int j = 0; j < other.coefficients.Count; j++)
            {
                lock (lockObj)
                {
                    result[i + j] += this.coefficients[i] * other.coefficients[j];
                }
            }
        });

        return new Polynomial(result.ToList());
    }


    public Polynomial Karatsuba(Polynomial other)
    {
        int maxSize = Math.Max(this.coefficients.Count, other.coefficients.Count);

        // straight forward multiplicaiton if one coef for each term
        if (maxSize == 1)
            return new Polynomial(new List<double> { this.coefficients[0] * other.coefficients[0] });

        int newSize = (int)Math.Pow(2, Math.Ceiling(Math.Log(maxSize, 2)));
        var aCoeffs = this.coefficients.Concat(Enumerable.Repeat(0.0, newSize - this.coefficients.Count)).ToList();
        var bCoeffs = other.coefficients.Concat(Enumerable.Repeat(0.0, newSize - other.coefficients.Count)).ToList();

        int half = newSize / 2;
        var aLow = new Polynomial(aCoeffs.Take(half).ToList());
        var aHigh = new Polynomial(aCoeffs.Skip(half).ToList());
        var bLow = new Polynomial(bCoeffs.Take(half).ToList());
        var bHigh = new Polynomial(bCoeffs.Skip(half).ToList());

        var ac = aHigh.Karatsuba(bHigh); //prod of high degree polys
        var bd = aLow.Karatsuba(bLow); //prod of low degree polys
        var ab_cd = aHigh.Plus(aLow).Karatsuba(bHigh.Plus(bLow)).Minus(ac).Minus(bd); //(a+b)(c+d) - ac - bd

        // combine result shifting, shifting needed because each part of the prod corresponds to different powers of x
        return ac.Shift(2 * half).Plus(ab_cd.Shift(half)).Plus(bd);
    }




    public Polynomial ParallelKaratsuba(Polynomial other)
    {
        int maxSize = Math.Max(this.coefficients.Count, other.coefficients.Count);

        // Base case: single-term polynomial multiplication
        if (maxSize == 1)
            return new Polynomial(new List<double> { this.coefficients[0] * other.coefficients[0] });

        int newSize = (int)Math.Pow(2, Math.Ceiling(Math.Log(maxSize, 2)));
        var aCoeffs = this.coefficients.Concat(Enumerable.Repeat(0.0, newSize - this.coefficients.Count)).ToList();
        var bCoeffs = other.coefficients.Concat(Enumerable.Repeat(0.0, newSize - other.coefficients.Count)).ToList();

        int half = newSize / 2;
        var aLow = new Polynomial(aCoeffs.Take(half).ToList());
        var aHigh = new Polynomial(aCoeffs.Skip(half).ToList());
        var bLow = new Polynomial(bCoeffs.Take(half).ToList());
        var bHigh = new Polynomial(bCoeffs.Skip(half).ToList());

        var taskAC = Task.Run(() => aHigh.Karatsuba(bHigh));
        var taskBD = Task.Run(() => aLow.Karatsuba(bLow));
        var taskABCD = Task.Run(() => 
            aHigh.Plus(aLow).Karatsuba(bHigh.Plus(bLow))
                 .Minus(taskAC.Result)
                 .Minus(taskBD.Result));

        Task.WaitAll(taskAC, taskBD, taskABCD);

        // Combine results
        return taskAC.Result.Shift(2 * half)
               .Plus(taskABCD.Result.Shift(half))
               .Plus(taskBD.Result);
    }


    private Polynomial Shift(int positions)
    {
        var shifted = new double[this.coefficients.Count + positions];
        for (int i = 0; i < this.coefficients.Count; i++)
            shifted[i + positions] = this.coefficients[i];

        return new Polynomial(shifted.ToList());
    }

    public Polynomial Plus(Polynomial other)
    {
        var maxSize = Math.Max(this.coefficients.Count, other.coefficients.Count);
        var result = new double[maxSize];

        for (int i = 0; i < maxSize; i++)
        {
            result[i] = (i < this.coefficients.Count ? this.coefficients[i] : 0) +
                         (i < other.coefficients.Count ? other.coefficients[i] : 0);
        }

        return new Polynomial(result.ToList());
    }

    public Polynomial Minus(Polynomial other)
    {
        var maxSize = Math.Max(this.coefficients.Count, other.coefficients.Count);
        var result = new double[maxSize];

        for (int i = 0; i < maxSize; i++)
        {
            result[i] = (i < this.coefficients.Count ? this.coefficients[i] : 0) -
                         (i < other.coefficients.Count ? other.coefficients[i] : 0);
        }

        return new Polynomial(result.ToList());
    }

    public override string ToString()
    {
        var terms = new List<string>();

        for (int i = coefficients.Count - 1; i >= 0; i--)
        {
            if (coefficients[i] == 0)
                continue; // Skip zero coefficients

            string term = coefficients[i].ToString();

            if (i > 0)
            {
                term += " * x";

                if (i > 1)
                {
                    term += "^" + i;
                }
            }

            terms.Add(term);
        }

        return string.Join(" + ", terms);
    }


}