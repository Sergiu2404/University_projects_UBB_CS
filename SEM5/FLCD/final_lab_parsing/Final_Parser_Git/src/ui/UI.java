package ui;

import controller.Program;
import model.Production;

import java.util.*;

public class UI {
    private Program program;

    public UI(Program program) {
        this.program = program;
    }

    public void start() {
        System.out.println();
        System.out.println("0 - Exit");
        System.out.println("1 - Grammar");
        System.out.println("2 - Parser");

        Scanner inScanner = new Scanner(System.in);
        int option = Integer.parseInt(inScanner.next().trim());
        switch (option) {
            case 0:
                System.exit(0);
            case 1:
                fileMenuGrammar();
                break;
            case 2:
                fileMenuParser();
                break;
            default:
                start();
        }
    }

    private void fileMenuParser() {
        System.out.println();
        System.out.println("0 - Back");
        System.out.println("1 - Get FIRST set");
        System.out.println("2 - Get FOLLOW set");
        System.out.println("3 - Create parse table");
        System.out.println("4 - Parse sequence");
        System.out.println("5 - Parse source code");
        System.out.println("6 - Parse pif");

        Scanner inScanner = new Scanner(System.in);
        int option = Integer.parseInt(inScanner.next().trim());
        switch (option) {
            case 0:
                start();
                break;
            case 1:
                program.getFirstSet().forEach(this::displaySet);
                System.out.println();
                fileMenuParser();
                break;
            case 2:
                program.getFollowSet().forEach(this::displaySet);
                System.out.println();
                fileMenuParser();
                break;
            case 3:
                System.out.println(program.getParseTable());
                System.out.println();
                fileMenuParser();
                break;
            case 4:
                program.parse(promptForSequence());
                System.out.println();
                fileMenuParser();
                break;
            case 5:
                program.scanSourceCode();
                System.out.println();
                fileMenuParser();
                break;
            case 6:
                program.parsePIF();
                System.out.println();
                fileMenuParser();
                break;
            default:
                start();
        }
    }

    private void displaySet(String key, Set<String> value) {
        StringBuilder sb = new StringBuilder(key + " = { ");
        for (String symbol : value)
            sb.append(symbol).append(", ");
        sb.append("}");
        sb.replace(sb.length() - 3, sb.length() - 2, "");
        System.out.println(sb);
    }

    private void fileMenuGrammar() {
        System.out.println();
        System.out.println("0 - Back");
        System.out.println("1 - Non-terminals");
        System.out.println("2 - Terminals");
        System.out.println("3 - Productions");
        System.out.println("4 - Productions of a non-terminal");
        System.out.println("5 - Starting Symbol");

        Scanner inScanner = new Scanner(System.in);
        int option = Integer.parseInt(inScanner.next().trim());
        switch (option) {
            case 0:
                start();
                break;
            case 1:
                System.out.println(program.getNonTerminals());
                System.out.println();
                fileMenuGrammar();
                break;
            case 2:
                System.out.println(program.getTerminals());
                System.out.println();
                fileMenuGrammar();
                break;
            case 3:
                System.out.println("P: {");
                for (Production production: program.getProductions())
                    System.out.println("    " + production);
                System.out.println("}");
                System.out.println();
                fileMenuGrammar();
                break;
            case 4:
                System.out.println(program.getProductionsForNonterminal(promptForNonTerminal()));
                System.out.println();
                fileMenuGrammar();
                break;
            case 5:
                System.out.println(program.getStartingSymbol());
                System.out.println();
                fileMenuGrammar();
                break;
            default:
                start();
        }
    }

    private List<String> promptForSequence() {
        Scanner inScanner = new Scanner(System.in);
        return Arrays.asList(inScanner.nextLine().replace("\n", "").split(" "));
    }

    private String promptForNonTerminal() {
        Scanner inScanner = new Scanner(System.in);
        return inScanner.next().trim();
    }
}
