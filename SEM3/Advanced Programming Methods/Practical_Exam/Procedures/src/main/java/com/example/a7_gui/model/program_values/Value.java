package com.example.a7_gui.model.program_values;

import com.example.a7_gui.model.program_types.Type;

public interface Value {
    Type getType();
    Value deepCopy();
}