using System.Diagnostics;

class Program
{
    static void Main(string[] args)
    {
        var poly1 = new Polynomial(new List<double> { 1, 2, 3 }); // 1 + 2x + 3x^2
        var poly2 = new Polynomial(new List<double> { 4, 5 });    // 4 + 5x

        // Sequential Multiplication
        var watch = Stopwatch.StartNew();
        var resultSimple = poly1.SimpleMultiply(poly2);
        watch.Stop();
        Console.WriteLine($"Simple Multiply Result: {resultSimple} (Time: {watch.ElapsedMilliseconds} ms)");

        // Parallel Multiplication
        watch.Restart();
        var resultParallel = poly1.ParallelMultiply(poly2);
        watch.Stop();
        Console.WriteLine($"Parallel Multiply Result: {resultParallel} (Time: {watch.ElapsedMilliseconds} ms)");

        // Karatsuba Multiplication
        watch.Restart();
        var resultKaratsuba = poly1.Karatsuba(poly2);
        watch.Stop();
        Console.WriteLine($"Karatsuba Multiply Result: {resultKaratsuba} (Time: {watch.ElapsedMilliseconds} ms)");

        // Parallel Karatsuba Multiplication
        watch.Restart();
        var resultParallelKaratsuba = poly1.ParallelKaratsuba(poly2);
        watch.Stop();
        Console.WriteLine($"Parallel Karatsuba Multiply Result: {resultParallelKaratsuba} (Time: {watch.ElapsedMilliseconds} ms)");
    }
}