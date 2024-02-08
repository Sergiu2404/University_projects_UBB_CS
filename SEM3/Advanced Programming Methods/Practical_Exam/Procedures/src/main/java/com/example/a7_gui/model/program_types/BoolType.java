package com.example.a7_gui.model.program_types;

import com.example.a7_gui.model.program_values.BoolValue;
import com.example.a7_gui.model.program_values.Value;


public class BoolType implements Type{
    @Override
    public boolean equals(Type anotherType) {
        return anotherType instanceof BoolType;
    }

    @Override
    public Value defaultValue() {
        return new BoolValue(false);
    }

    @Override
    public Type deepCopy() {
        return new BoolType();
    }

    @Override
    public String toString() {
        return "bool";
    }
}