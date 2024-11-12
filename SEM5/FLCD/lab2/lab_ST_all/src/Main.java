public class Main {
    public static void main(String[] args) {
        // HashTable Implementation
        SymbolTableInterface hashTable = new HashTable(5);
        hashTable.add("1");
        System.out.println(hashTable.containsTerm("1"));  // true
        System.out.println(hashTable.toString());

        // Unsorted Table Implementation
        SymbolTableInterface unsortedTable = new UnsortedSymbolTable();
        unsortedTable.add("a");
        unsortedTable.add("c");
        unsortedTable.add("b");
        System.out.println(unsortedTable.toString());  // Random order

        // Sorted Table Implementation
        SymbolTableInterface sortedTable = new SortedSymbolTable();
        sortedTable.add("a");
        sortedTable.add("c");
        sortedTable.add("b");
        System.out.println(sortedTable.toString());  // Sorted order

        // Binary Search Tree Implementation
        SymbolTableInterface bst = new BSTSymbolTable();
        bst.add("a");
        bst.add("c");
        bst.add("b");
        System.out.println(bst.toString());  // In-order traversal
    }
}
