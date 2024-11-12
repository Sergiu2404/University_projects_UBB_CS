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

        int totalTasks = this.coefficients.Count * other.coefficients.Count;
        int taskSize = (int)Math.Ceiling(totalTasks / (double)Environment.ProcessorCount);
        var tasks = new List<Task>();

        for (int i = 0; i < totalTasks; i += taskSize)
        {
            tasks.Add(Task.Run(() =>
            {
                for (int index = i; index < Math.Min(i + taskSize, totalTasks); index++)
                {
                    int firstCoord = index / other.coefficients.Count;
                    int secondCoord = index % other.coefficients.Count;
                    result[firstCoord + secondCoord] += this.coefficients[firstCoord] * other.coefficients[secondCoord];
                }
            }));
        }

        Task.WaitAll(tasks.ToArray());
        return new Polynomial(result.ToList());
    }

    public Polynomial Karatsuba(Polynomial other)
    {
        // Check for empty polynomials
        if (this.coefficients.Count == 0 || other.coefficients.Count == 0)
            return new Polynomial(new List<double> { 0 });

        int n = this.coefficients.Count + other.coefficients.Count - 1;

        // Handle base case for single coefficient
        if (n == 1)
            return new Polynomial(new List<double> { this.coefficients[0] * other.coefficients[0] });

        // Calculate the midpoint for splitting
        int halfSize = (int)Math.Ceiling(Math.Max(this.coefficients.Count, other.coefficients.Count) / 2.0);

        // Split the coefficients
        var a = new Polynomial(this.coefficients.Take(halfSize).ToList());
        var b = new Polynomial(this.coefficients.Skip(halfSize).ToList());
        var c = new Polynomial(other.coefficients.Take(halfSize).ToList());
        var d = new Polynomial(other.coefficients.Skip(halfSize).ToList());

        // Recursively apply the Karatsuba algorithm
        var ac = a.Karatsuba(c);
        var bd = b.Karatsuba(d);
        var ab_cd = a.Plus(b).Karatsuba(c.Plus(d)).Minus(ac).Minus(bd);

        // Combine the results
        return ac.Shift(2 * (halfSize - 1)).Plus(ab_cd.Shift(halfSize - 1)).Plus(bd);
    }



    public Polynomial ParallelKaratsuba(Polynomial other)
    {
        int n = this.coefficients.Count + other.coefficients.Count - 1;
        if (n <= 1) return new Polynomial(new List<double> { this.coefficients[0] * other.coefficients[0] });

        int halfSize = n / 2;
        var a = new Polynomial(this.coefficients.Take(halfSize).ToList());
        var b = new Polynomial(this.coefficients.Skip(halfSize).ToList());
        var c = new Polynomial(other.coefficients.Take(halfSize).ToList());
        var d = new Polynomial(other.coefficients.Skip(halfSize).ToList());

        var ac = Task.Run(() => a.Karatsuba(c));
        var bd = Task.Run(() => b.Karatsuba(d));
        var ab_cd = Task.Run(() => a.Plus(b).Karatsuba(c.Plus(d)).Minus(ac.Result).Minus(bd.Result));

        Task.WaitAll(ac, bd, ab_cd);
        return ac.Result.Shift(2 * halfSize).Plus(ab_cd.Result.Shift(halfSize)).Plus(bd.Result);
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
        return string.Join(", ", coefficients);
    }
}