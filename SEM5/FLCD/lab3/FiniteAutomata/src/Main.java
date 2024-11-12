import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

public class Main {
    private static void writeToFile(String outputFilePath, Object object) {
        try (PrintStream printStream = new PrintStream(outputFilePath)) {
            printStream.println(object);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void printFAMenu() {
        System.out.println("\nFA Menu:");
        System.out.println("1. Print states.");
        System.out.println("2. Print alphabet.");
        System.out.println("3. Print final states.");
        System.out.println("4. Print transitions.");
        System.out.println("5. Print initial state.");
        System.out.println("6. Check if FA is deterministic.");
        System.out.println("7. Check if a sequence is accepted.");
        System.out.println("0. Exit to main menu.");
        System.out.print("Choose an option: ");
    }

    private static void runFA() {
        FiniteAutomation fa = new FiniteAutomation("input/FA.in");
        Scanner scanner = new Scanner(System.in);
        int option;

        printFAMenu();
        while ((option = scanner.nextInt()) != 0) {
            scanner.nextLine(); // Consume newline
                switch (option) {
                    case 1 -> {
                        System.out.println("States: " + fa.getStates());
                        writeToFile("output/FA_states.txt", "States: " + fa.getStates());
                    }
                    case 2 -> {
                        System.out.println("Alphabet: " + fa.getAlphabet());
                        writeToFile("output/FA_alphabet.txt", "Alphabet: " + fa.getAlphabet());
                    }
                    case 3 -> {
                        System.out.println("Final States: " + fa.getFinalStates());
                        writeToFile("output/FA_final_states.txt", "Final States: " + fa.getFinalStates());
                    }
                    case 4 -> {
                        System.out.println(fa.writeTransitions());
                        writeToFile("output/FA_transitions.txt", fa.writeTransitions());
                    }
                    case 5 -> {
                        System.out.println("Initial State: " + fa.getInitialState());
                        writeToFile("output/FA_initial_state.txt", "Initial State: " + fa.getInitialState());
                    }
                    case 6 -> {
                        boolean isDeterministic = fa.checkIfDeterministic();
                        System.out.println("Is deterministic? " + isDeterministic);
                        writeToFile("output/FA_is_deterministic.txt", "Is deterministic? " + isDeterministic);
                    }
                    case 7 -> {
                        System.out.print("Enter a sequence to check: ");
                        String sequence = scanner.nextLine();
                        boolean accepted = fa.acceptsSequence(sequence);
                        String result = accepted ? "Sequence is accepted." : "Sequence is not accepted.";
                        System.out.println(result);
                        writeToFile("output/FA_sequence_check.txt", result);
                    }
                    default -> System.out.println("Invalid option.");
                }
            printFAMenu();
        }
    }

    private static void runLexicalAnalyzer(String inputFilePath) {
        MyScanner scanner = new MyScanner(inputFilePath);
        scanner.scan(inputFilePath);
        writeToFile("output/" + inputFilePath.replace("input/", "").replace(".txt", "_IdentifiersSymbolTable.txt"),
                scanner.getSymbolTable().getIdentifierTable());
        writeToFile("output/" + inputFilePath.replace("input/", "").replace(".txt", "_ConstantsSymbolTable.txt"),
                scanner.getSymbolTable().getConstantTable());
        writeToFile("output/" + inputFilePath.replace("input/", "").replace(".txt", "_PIF.txt"),
                scanner.getProgramInternalForm());
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int option;

        do {
            System.out.println("Main Menu:");
            System.out.println("1. Run FA");
            System.out.println("2. Run Scanner");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            option = scanner.nextInt();

            switch (option) {
                case 1 -> runFA();
                case 2 -> {
                    runLexicalAnalyzer("input/p1.txt");
                    runLexicalAnalyzer("input/p2.txt");
                    runLexicalAnalyzer("input/p3.txt");
                    runLexicalAnalyzer("input/p1err.txt");
                }
                case 0 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid command!");
            }
        } while (option != 0);
    }
}
