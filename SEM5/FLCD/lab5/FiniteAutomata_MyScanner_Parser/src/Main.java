import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Main {

    private static void printMenu() {
        System.out.println("=============");
        System.out.println("1. Finite Automaton (FA)");
        System.out.println("2. Grammar");
        System.out.println("0. Exit");
        System.out.println("=============");
    }

    private static void grammarMenu() {
        Scanner scanner = new Scanner(System.in);
        int option = -1;
        Grammar grammar = new Grammar();

        do {
            System.out.println("============ Grammar Menu ============");
            System.out.println("1. Load Grammar (g1.txt)");
            System.out.println("2. Load Grammar (g2.txt)");
            System.out.println("3. Print Nonterminals");
            System.out.println("4. Print Terminals");
            System.out.println("5. Print All Productions");
            System.out.println("6. Print Productions for a Nonterminal");
            System.out.println("7. Check if CFG");
            System.out.println("8. Compute first");
            System.out.println("9. Compute follow");
            System.out.println("10. Print Tree Representation");
            System.out.println("0. Exit");
            System.out.println("======================================");
            System.out.print("Your option: ");

            try {
                option = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                switch (option) {
                    case 1:
                        grammar.readFromFile("input/g1.txt");
                        System.out.println("Loaded grammar from g1.txt");
                        break;

                    case 2:
                        grammar.readFromFile("input/g2.txt");
                        System.out.println("Loaded grammar from g2.txt");
                        break;

                    case 3:
                        grammar.printNonterminals();
                        break;

                    case 4:
                        grammar.printTerminals();
                        break;

                    case 5:
                        grammar.printProductions();
                        break;

                    case 6:
                        System.out.print("Enter a nonterminal: ");
                        String nonterminal = scanner.nextLine();
                        grammar.printProductionsForNonterminal(nonterminal);
                        break;

                    case 7:
                        System.out.println("Is CFG: " + grammar.isCFG());
                        break;

                    case 8:
                        grammar.computeFirst(); // This already prints FIRST sets
                        break;

                    case 9:
                        Map<String, Set<String>> firstSets = grammar.computeFirst();
                        Map<String, Set<String>> followSets = grammar.computeFollow(firstSets);
                        System.out.println("\nFollow Sets:");
                        for (Map.Entry<String, Set<String>> entry : followSets.entrySet()) {
                            System.out.println("Follow(" + entry.getKey() + ") = " + entry.getValue());
                        }
                        break;

                    case 10:
//                        Node root = grammar.getRootNode();
//                        grammar.printGraphicalTree(root);
                        grammar.printTree();
                        break;

                    case 0:
                        System.out.println("Exiting...");
                        break;

                    default:
                        System.out.println("Invalid option. Please choose a valid menu item.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                scanner.nextLine(); // Clear invalid input
            }
        } while (option != 0);
    }



    private static void finiteAutomatonMenu() {
        FiniteAutomaton finiteAutomaton = new FiniteAutomaton("input/FA.txt");
        Scanner scanner = new Scanner(System.in);
        int option = -1;

        do {
            System.out.println("============ FA Menu ============");
            System.out.println("1. States");
            System.out.println("2. Alphabet");
            System.out.println("3. Final States");
            System.out.println("4. Transitions");
            System.out.println("5. Initial State");
            System.out.println("6. Is Deterministic?");
            System.out.println("7. Validate Sequence");
            System.out.println("8. Generate BNF Representation");
            System.out.println("0. Return to Main Menu");
            System.out.println("=================================");
            System.out.print("Your option: ");

            try {
                option = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                switch (option) {
                    case 1:
                        System.out.println("States: " + finiteAutomaton.getStates());
                        break;

                    case 2:
                        System.out.println("Alphabet: " + finiteAutomaton.getAlphabet());
                        break;

                    case 3:
                        System.out.println("Final States: " + finiteAutomaton.getFinalStates());
                        break;

                    case 4:
                        System.out.println("Transitions: " + finiteAutomaton.writeTransitions());
                        break;

                    case 5:
                        System.out.println("Initial State: " + finiteAutomaton.getInitialState());
                        break;

                    case 6:
                        System.out.println("Is Deterministic? " + finiteAutomaton.isDeterministic());
                        break;

                    case 7:
                        System.out.print("Enter your sequence: ");
                        String sequence = scanner.nextLine();
                        if (finiteAutomaton.acceptsSequence(sequence)) {
                            System.out.println("Sequence is valid.");
                        } else {
                            System.out.println("Sequence is not valid.");
                        }
                        break;

                    case 8:
                        System.out.println("BNF Representation of the Finite Automaton:");
                        System.out.println(finiteAutomaton.toBNF());
                        break;

                    case 0:
                        System.out.println("Returning to main menu...");
                        break;

                    default:
                        System.out.println("Invalid option. Please choose a valid menu item.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                scanner.nextLine(); // Clear invalid input
            }
        } while (option != 0);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int option = -1;

        do {
            printMenu();
            System.out.print("Your option: ");

            try {
                option = scanner.nextInt();

                switch (option) {
                    case 1:
                        finiteAutomatonMenu();
                        break;

                    case 2:
                        grammarMenu();
                        break;

                    case 0:
                        System.out.println("Exiting the program...");
                        break;

                    default:
                        System.out.println("Invalid option! Please choose 0, 1, or 2.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                scanner.nextLine(); // Clear invalid input
            }
        } while (option != 0);
    }
}
