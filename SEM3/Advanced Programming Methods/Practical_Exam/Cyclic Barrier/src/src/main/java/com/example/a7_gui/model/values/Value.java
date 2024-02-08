package com.example.a7_gui.model.values;

import com.example.a7_gui.model.types.Type;

public interface Value {
    Type getType();
    Value deepCopy();
}