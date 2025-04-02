function fi = my_lagrange_bar(nodes, f_nodes, xi)
    n = length(nodes)
    w = ones(1, n)

    for i = 1:n
        for j = [1:i-1, i+1:n]
            w(i) = w(i) / (nodes(i) - nodes(j));
        end
    end

    m = length(xi);
    fi = zeros(size(xi));

    for j = 1:m
        numerator = 0;
        denominator = 0;
        exact_match = false;

        for i = 1:n
            if xi(j) == nodes(i)
                fi(j) = f_nodes(i);
                exact_match = true;
                break;
            end
            term = w(i) / (xi(j) - nodes(i));
            numerator = numerator + term * f_nodes(i);
            denominator = denominator + term;
        end

        if ~exact_match
            fi(j) = numerator / denominator
        end
    end
end

