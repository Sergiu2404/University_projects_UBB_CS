package com.example.a7_gui.model.types;

import com.example.a7_gui.model.values.BoolValue;
import com.example.a7_gui.model.values.IntValue;
import com.example.a7_gui.model.values.Value;


public class IntType implements Type{
    @Override
    public boolean equals(Type anotherType) {
        return anotherType instanceof IntType;
    }

    @Override
    public Value defaultValue() {
        return new IntValue(0);
    }

    @Override
    public Type deepCopy() {
        return new IntType();
    }

    @Override
    public String toString() {
        return "int";
    }
}