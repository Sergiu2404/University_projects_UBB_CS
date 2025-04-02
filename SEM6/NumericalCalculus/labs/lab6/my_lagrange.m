function fi = my_lagrange(nodes, f_nodes, xi)
  fi = zeros(size(xi));
  n = length(nodes);
  m = length(xi);
  l_i = ones(size(nodes));

  for j = 1 : m
    for i = 1 : n
        l_i(i) = prod(xi(j) - nodes([1 : i - 1, i + 1 : n])) ./ prod(nodes(i) - nodes([1 : i - 1, i + 1 : n]));
    endfor
        fi(j) = l_i * f_nodes';
  endfor
end
