function I = simpsons(f, a, b, m)
  h = (b - a) / (2 * m);
  n = m;
  I = h / 3 * (f(a) + f(b) + 4 * sum(f(a + ([1:n] * 2 - 1) * h)) + 2 * sum(f(a + ([1:n - 1] * 2) * h)));
