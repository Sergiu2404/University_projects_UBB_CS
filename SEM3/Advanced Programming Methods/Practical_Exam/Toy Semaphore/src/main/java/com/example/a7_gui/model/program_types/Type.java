package com.example.a7_gui.model.program_types;

import com.example.a7_gui.model.values.Value;

public interface Type {
    boolean equals(Type anotherType);
    Value defaultValue();
    Type deepCopy();
}
