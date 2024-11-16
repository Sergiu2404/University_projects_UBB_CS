import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    private static void printToFile(String filePath, Object object) {
        File outputDir = new File("output");
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        try (PrintStream printStream = new PrintStream(filePath)) {
            printStream.println(object);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void run(String filePath) {
        MyScanner scanner = new MyScanner(filePath);
        scanner.scan();

        String fileName = filePath.substring(filePath.lastIndexOf('/') + 1).replace(".txt", "");
        String outputBasePath = "output/" + fileName;

        printToFile(outputBasePath + "ST.txt", scanner.getSymbolTable());
        printToFile(outputBasePath + "PIF.txt", scanner.getPif());
    }

    private static void printMenu() {
        System.out.println("============================================================");
        System.out.println("1. States.");
        System.out.println("2. Alphabet.");
        System.out.println("3. Final states.");
        System.out.println("4. Transitions.");
        System.out.println("5. Initial state.");
        System.out.println("6. Is it deterministic?");
        System.out.println("7. Give a sequence to check if it is accepted by the FA.");
        System.out.println("8. Generate BNF representation.");
        System.out.println("0. Return to the main menu.");
        System.out.println("============================================================");
    }


    private static void optionsForDFA() {
        FiniteAutomaton finiteAutomaton = new FiniteAutomaton("input/FA.txt");
        Scanner scanner = new Scanner(System.in);
        int option = -1;

        do {
            printMenu();
            System.out.print("Your option: ");

            try {
                option = scanner.nextInt();
                scanner.nextLine(); // clear buffer after integer input

                switch (option) {
                    case 1:
                        System.out.println("States: ");
                        System.out.println(finiteAutomaton.getStates());
                        break;

                    case 2:
                        System.out.println("Alphabet: ");
                        System.out.println(finiteAutomaton.getAlphabet());
                        break;

                    case 3:
                        System.out.println("Final states: ");
                        System.out.println(finiteAutomaton.getFinalStates());
                        break;

                    case 4:
                        System.out.println("Transitions: ");
                        System.out.println(finiteAutomaton.writeTransitions());
                        break;

                    case 5:
                        System.out.println("Initial state: ");
                        System.out.println(finiteAutomaton.getInitialState());
                        break;

                    case 6:
                        System.out.println("Is deterministic? ");
                        System.out.println(finiteAutomaton.isDeterministic());
                        break;

                    case 7:
                        System.out.print("Enter your sequence: ");
                        String sequence = scanner.nextLine().trim();

                        if (sequence.isEmpty()) {
                            System.out.println("Error: Sequence cannot be empty!");
                        } else if (finiteAutomaton.acceptsSequence(sequence)) {
                            System.out.println("Sequence is valid");
                        } else {
                            System.out.println("Sequence is not valid.");
                        }
                        break;

                    case 8: // New case for generating BNF
                        System.out.println("BNF Representation of the Finite Automaton: ");
                        System.out.println(finiteAutomaton.toBNF());
                        break;

                    case 0:
                        System.out.println("Returning to the main menu...");
                        break;

                    default:
                        System.out.println("Invalid option! Please choose a valid menu item.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number corresponding to a menu option.");
                scanner.nextLine(); // clear the invalid input
            }
        } while (option != 0);
    }


    public static void runScanner() {
        run("input/p1.txt");
        run("input/p2.txt");
        run("input/p3.txt");
        run("input/p1err.txt");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int option = -1;

        do {
            System.out.println("=============");
            System.out.println("1. FA");
            System.out.println("2. Scanner");
            System.out.println("0. Exit");
            System.out.println("=============");
            System.out.print("Your option: ");

            try {
                option = scanner.nextInt();

                switch (option) {
                    case 1:
                        optionsForDFA();
                        break;

                    case 2:
                        runScanner();
                        break;

                    case 0:
                        System.out.println("Closing program...");
                        break;

                    default:
                        System.out.println("Invalid option! Please choose 0, 1, or 2.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number (0, 1, or 2).");
                scanner.nextLine();
            }
        } while (option != 0);
    }
}
