public interface SymbolTableInterface {
    boolean add(String term);
    boolean containsTerm(String term);
    String findByPos(Pair pos);
    Pair findPositionOfTerm(String term);
    Integer getSize();
    String toString();
}
