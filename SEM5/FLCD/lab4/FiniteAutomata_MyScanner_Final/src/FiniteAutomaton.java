import utils.Pair;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class FiniteAutomaton {

    private boolean deterministic;
    private String startState;
    private List<String> allStates;
    private List<String> symbols;
    private List<String> acceptStates;
    private final Map<Pair<String, String>, Set<String>> transitionMap;

    public FiniteAutomaton(String filePath) {
        this.transitionMap = new HashMap<>();
        loadFromFile(filePath);
        this.deterministic = isDeterministic();
    }

    public List<String> getStates() {
        return allStates;
    }

    public String getInitialState() {
        return startState;
    }

    public List<String> getAlphabet() {
        return symbols;
    }

    public List<String> getFinalStates() {
        return acceptStates;
    }

    public Map<Pair<String, String>, Set<String>> getTransitions() {
        return transitionMap;
    }

    public boolean isDeterministic() {
        return checkDeterminism();
    }

    public boolean acceptsSequence(String sequence) {
        if (!this.deterministic) return false;
        return checkAcceptance(sequence);
    }

    public String writeTransitions() {
        return formatTransitions();
    }

    @Override
    public String toString() {
        return formatAutomatonDetails();
    }

    private void loadFromFile(String filePath) {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            parseStates(scanner.nextLine());
            parseStartState(scanner.nextLine());
            parseAlphabet(scanner.nextLine());
            parseAcceptStates(scanner.nextLine());
            parseTransitions(scanner);
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }
    }

    private void parseStates(String line) {
        this.allStates = Arrays.asList(line.split(";"));
    }

    private void parseStartState(String line) {
        this.startState = line;
    }

    private void parseAlphabet(String line) {
        this.symbols = Arrays.asList(line.split(";"));
    }

    private void parseAcceptStates(String line) {
        this.acceptStates = Arrays.asList(line.split(";"));
    }

    private void parseTransitions(Scanner scanner) {
        while (scanner.hasNextLine()) {
            String transitionLine = scanner.nextLine();
            processTransition(transitionLine);
        }
    }

    private void processTransition(String transitionLine) {
        String[] parts = transitionLine.split(" ");
        if (isValidTransition(parts)) {
            Pair<String, String> stateSymbolPair = new Pair<>(parts[0], parts[1]);
            transitionMap.putIfAbsent(stateSymbolPair, new HashSet<>());
            transitionMap.get(stateSymbolPair).add(parts[2]);
        }
    }

    private boolean isValidTransition(String[] parts) {
        return allStates.contains(parts[0]) && symbols.contains(parts[1]) && allStates.contains(parts[2]);
    }

    private boolean checkDeterminism() {
        for (Set<String> transitions : transitionMap.values()) {
            if (transitions.size() > 1) return false;
        }
        return true;
    }

    private boolean checkAcceptance(String sequence) {
        String current = startState;
        for (char symbol : sequence.toCharArray()) {
            Pair<String, String> stateSymbol = new Pair<>(current, String.valueOf(symbol));
            Set<String> nextStates = transitionMap.get(stateSymbol);
            if (nextStates == null || nextStates.size() != 1) return false;
            current = nextStates.iterator().next();
        }
        return acceptStates.contains(current);
    }

    private String formatTransitions() {
        StringBuilder transitions = new StringBuilder("Transitions: \n");
        transitionMap.forEach((stateSymbolPair, destinationSet) ->
                transitions.append("<")
                        .append(stateSymbolPair.getKey())
                        .append(", ")
                        .append(stateSymbolPair.getValue())
                        .append("> to ")
                        .append(destinationSet)
                        .append("\n"));
        return transitions.toString();
    }

    private String formatAutomatonDetails() {
        return "FiniteAutomaton{" +
                "allStates=" + allStates +
                ", startState='" + startState + '\'' +
                ", symbols=" + symbols +
                ", acceptStates=" + acceptStates +
                ", transitionMap=" + transitionMap +
                ", deterministic=" + deterministic +
                '}';
    }
}
