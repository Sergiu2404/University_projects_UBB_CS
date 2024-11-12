using System;
using System.Collections.Generic;
using System.Linq;
using System.Numerics;

public class Polynomial
{
    private readonly List<BigInteger> coefficients;

    public Polynomial(List<BigInteger> coefficients)
    {
        this.coefficients = coefficients;
    }

    public Polynomial SimpleMultiply(Polynomial other)
    {
        var result = new BigInteger[this.coefficients.Count + other.coefficients.Count - 1];

        for (int i = 0; i < this.coefficients.Count; i++)
            for (int j = 0; j < other.coefficients.Count; j++)
                result[i + j] += this.coefficients[i] * other.coefficients[j];

        return new Polynomial(result.ToList());
    }

    public Polynomial Karatsuba(Polynomial other)
    {
        // Check for empty polynomials
        if (this.coefficients.Count == 0 || other.coefficients.Count == 0)
            return new Polynomial(new List<BigInteger> { 0 });

        int n = Math.Max(this.coefficients.Count, other.coefficients.Count);

        // Handle base case for single coefficient
        if (n == 1)
            return new Polynomial(new List<BigInteger> { this.coefficients[0] * other.coefficients[0] });

        // Calculate the midpoint for splitting
        int halfSize = (n + 1) / 2; // Ensure we handle odd sizes correctly

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

    private Polynomial Shift(int positions)
    {
        var shifted = new BigInteger[this.coefficients.Count + positions];
        for (int i = 0; i < this.coefficients.Count; i++)
            shifted[i + positions] = this.coefficients[i];

        return new Polynomial(shifted.ToList());
    }

    public Polynomial Plus(Polynomial other)
    {
        var maxSize = Math.Max(this.coefficients.Count, other.coefficients.Count);
        var result = new BigInteger[maxSize];

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
        var result = new BigInteger[maxSize];

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
