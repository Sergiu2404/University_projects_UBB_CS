package utils;

import java.util.Objects;

public class Pair<Key,Value> {
    private final Key Key;
    private final Value Value;

    public Key getKey() {
        return this.Key;
    }

    public Value getValue() {
        return this.Value;
    }

    public Pair(Key Key, Value Value) {
        this.Key = Key;
        this.Value = Value;
    }

    @Override
    public String toString() {
        return "<" + this.Key.toString() + "," + this.Value.toString() + ">";
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) object;
        return Key.equals(pair.Key) && Value.equals(pair.Value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Key, Value);
    }

}
