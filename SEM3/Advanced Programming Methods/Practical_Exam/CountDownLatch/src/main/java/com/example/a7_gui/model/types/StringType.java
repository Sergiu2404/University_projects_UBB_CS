package com.example.a7_gui.model.types;

import com.example.a7_gui.model.values.StringValue;
import com.example.a7_gui.model.values.Value;

public class StringType implements Type{
    @Override
    public boolean equals(Type anotherType) {
        return anotherType instanceof StringType;
    }

    @Override
    public Value defaultValue() {
        return new StringValue("");
    }

    @Override
    public Type deepCopy() {
        return new StringType();
    }

    @Override
    public String toString() {
        return "string";
    }
}