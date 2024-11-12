import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class FiniteAutomation {
    private final String ELEM_SEPARATOR = ";";
    private boolean isDeterministic;
    private String initialState;
    private List<String> states;
    private List<String> alphabet;
    private List<String> finalStates;
    private final Map<Pair<String, String>, Set<String>> transitions;

    public FiniteAutomation(String filePath) {
        this.transitions = new HashMap<>();
        readFromFile(filePath);
    }

    private void readFromFile(String filePath) {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            // Read states
            String line = scanner.nextLine();
            this.states = new ArrayList<>(List.of(line.split(this.ELEM_SEPARATOR)));

            // Read initial state
            this.initialState = scanner.nextLine().trim();

            // Read alphabet
            line = scanner.nextLine();
            this.alphabet = new ArrayList<>(List.of(line.split(this.ELEM_SEPARATOR)));

            // Read final states
            line = scanner.nextLine();
            this.finalStates = new ArrayList<>(List.of(line.split(this.ELEM_SEPARATOR)));

            // Read transitions
            while (scanner.hasNextLine()) {
                String transitionLine = scanner.nextLine().trim();
                String[] transitionComponents = transitionLine.split(" ");

                if (transitionComponents.length != 3) {
                    System.err.println("Invalid transition format: " + transitionLine);
                    continue;
                }

                String fromState = transitionComponents[0];
                String symbol = transitionComponents[1];
                String toState = transitionComponents[2];

                // Check validity of the transition
                if (states.contains(fromState) && states.contains(toState) && alphabet.contains(symbol)) {
                    Pair<String, String> transitionKey = new Pair<>(fromState, symbol);
                    transitions.putIfAbsent(transitionKey, new HashSet<>());
                    transitions.get(transitionKey).add(toState);
                }
            }

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
        }

        this.isDeterministic = checkIfDeterministic();
    }

    public boolean checkIfDeterministic() {
        return this.transitions.values().stream().allMatch(list -> list.size() <= 1);
    }

    public List<String> getStates() {
        return this.states;
    }

    public String getInitialState() {
        return this.initialState;
    }

    public List<String> getAlphabet() {
        return this.alphabet;
    }

    public List<String> getFinalStates() {
        return this.finalStates;
    }

    public Map<Pair<String, String>, Set<String>> getTransitions() {
        return this.transitions;
    }

    public String writeTransitions() {
        StringBuilder builder = new StringBuilder();
        builder.append("Transitions:\n");
        transitions.forEach((key, value) -> {
            builder.append("<").append(key.getFirst()).append(", ").append(key.getSecond()).append("> -> ").append(value).append("\n");
        });
        return builder.toString();
    }

    public boolean acceptsSequence(String sequence) {
        if (!this.isDeterministic) {
            return false;
        }

        String currentState = this.initialState;
        System.out.println("Starting at initial state: " + currentState);

        for (char symbol : sequence.toCharArray()) {
            Pair<String, String> transition = new Pair<>(currentState, String.valueOf(symbol));
            System.out.println("Current symbol: " + symbol + ", Transition key: " + transition);
            if (!this.transitions.containsKey(transition)) {
                System.out.println("No valid transition exists for: " + transition);
                return false; // No valid transition exists
            } else {
                currentState = this.transitions.get(transition).iterator().next();
                System.out.println("Moved to state: " + currentState);
            }
        }

        return this.finalStates.contains(currentState);
    }

}
