using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Numerics;

class Program
{
    static void Main(string[] args)
    {
        // Example polynomials
        var poly1 = new Polynomial(new List<BigInteger> { 1, 2, 3 }); // Represents 1 + 2x + 3x^2
        var poly2 = new Polynomial(new List<BigInteger> { 4, 5 });    // Represents 4 + 5x

        // Sequential Multiplication
        var watch = Stopwatch.StartNew();
        var resultSimple = poly1.SimpleMultiply(poly2);
        watch.Stop();
        Console.WriteLine($"Simple Multiply Result: {resultSimple} (Time: {watch.ElapsedMilliseconds} ms)");

        // Karatsuba Multiplication
        watch.Restart();
        var resultKaratsuba = poly1.Karatsuba(poly2);
        watch.Stop();
        Console.WriteLine($"Karatsuba Multiply Result: {resultKaratsuba} (Time: {watch.ElapsedMilliseconds} ms)");

        // Bonus: Example with large numbers
        var largePoly1 = new Polynomial(new List<BigInteger> { BigInteger.Parse("123456789123456789"),
                                                               BigInteger.Parse("987654321987654321"),
                                                               BigInteger.Parse("111111111111111111") });
        var largePoly2 = new Polynomial(new List<BigInteger> { BigInteger.Parse("222222222222222222"),
                                                               BigInteger.Parse("333333333333333333") });

        // Sequential Multiplication of large numbers
        watch.Restart();
        var resultLargeSimple = largePoly1.SimpleMultiply(largePoly2);
        watch.Stop();
        Console.WriteLine($"Large Simple Multiply Result: {resultLargeSimple} (Time: {watch.ElapsedMilliseconds} ms)");

        // Karatsuba Multiplication of large numbers
        watch.Restart();
        var resultLargeKaratsuba = largePoly1.Karatsuba(largePoly2);
        watch.Stop();
        Console.WriteLine($"Large Karatsuba Multiply Result: {resultLargeKaratsuba} (Time: {watch.ElapsedMilliseconds} ms)");
    }
}
