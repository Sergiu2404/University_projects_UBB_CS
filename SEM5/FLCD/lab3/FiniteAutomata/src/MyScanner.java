import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MyScanner {

    private ArrayList<String> operatorsList = new ArrayList<>(
            List.of("*", "/", "+", "-", "%", "smallerEq", "smaller", "greater", "greaterEq", "is", "isNot", "takes")
    );
    private ArrayList<String> separatorsList = new ArrayList<>(
            List.of("{", "}", "(", ")", "[", "]", ":", ";", " ", ",", "\t", "\n", "'", "\"")
    );
    private ArrayList<String> reservedWordsList = new ArrayList<>(
            List.of("sqrt", "read", "write", "if", "elif", "else", "while", "for", "int", "float", "string", "char", "return", "array", "start", "end")
    );

    private SymbolTable symbolTable;
    private ProgramInternalForm programInternalForm;
    private String inputFilePath;

    public MyScanner(String inputFilePath) {
        this.inputFilePath = inputFilePath;
        this.symbolTable = new SymbolTable(10);
        this.programInternalForm = new ProgramInternalForm();
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public ProgramInternalForm getProgramInternalForm() {
        return programInternalForm;
    }

    public void scan(String inputFile) {
        AtomicBoolean isLexicalError = new AtomicBoolean(false);
        List<Pair<String, Pair<Integer, Integer>>> tokens = createListProgramElements();

        if (tokens == null) {
            return;
        }

        tokens.forEach(pairToken -> {
            String token = pairToken.getFirst();
            Pair<Integer, Integer> lineAndColumn = pairToken.getSecond();

            try {
                if (reservedWordsList.contains(token)) {
                    programInternalForm.add(new Pair<>(token, new Pair<>(-1, -1)), 2);
                }
                else if (Pattern.compile("^\".*\"$").matcher(token).matches()) {
                    symbolTable.addTerm(token, true);
                    programInternalForm.add(new Pair<>("CONSTANT", symbolTable.findPositionOfTerm(token, true)), 0);
                }
                else if (Pattern.compile("^[a-zA-Z_][a-zA-Z_0-9]*$").matcher(token).matches()) {
                    symbolTable.addTerm(token, false);
                    programInternalForm.add(new Pair<>("IDENTIFIER", symbolTable.findPositionOfTerm(token, false)), 1);
                }
                else if (operatorsList.contains(token)) {
                    programInternalForm.add(new Pair<>(token, new Pair<>(-1, -1)), 3);
                } else if (separatorsList.contains(token)) {
                    programInternalForm.add(new Pair<>(token, new Pair<>(-1, -1)), 4);
                } else if (Pattern.compile("^(0|[1-9][0-9]*|[-+]?[0-9]*\\.[0-9]+|[-+]?[0-9]+)$").matcher(token).matches()) {
                    // invalid leading zeros
                    if (token.matches("^0[0-9]+$")) {
                        System.err.println("Lexical Error in " + inputFile + " -> line: " + lineAndColumn.getFirst() +
                                " column: " + lineAndColumn.getSecond() + ": Invalid number with leading zero: " + token);
                        isLexicalError.set(true);
                    } else {
                        symbolTable.addTerm(token, true);
                        programInternalForm.add(new Pair<>("CONSTANT", symbolTable.findPositionOfTerm(token, true)), 0);
                    }
                } else {
                    System.err.println("Lexical Error in " + inputFile + " -> line: " + lineAndColumn.getFirst() +
                            " column: " + lineAndColumn.getSecond() + ": Unrecognized token: " + token);
                    isLexicalError.set(true);
                }
            } catch (Exception e) {
                System.err.println("Error processing token at line " + lineAndColumn.getFirst() + ", column " +
                        lineAndColumn.getSecond() + ": " + e.getMessage());
                isLexicalError.set(true);
            }
        });

        if (!isLexicalError.get()) {
            System.out.println(String.format("Program inside %s was scanned successfully", inputFile));
        } else {
            System.out.println(String.format("Program inside %s has lexical errors", inputFile));
        }
    }


    private String readFile() {
        StringBuilder content = new StringBuilder();
        try (Scanner scanner = new Scanner(new File(inputFilePath))) {
            while (scanner.hasNextLine()) {
                content.append(scanner.nextLine()).append("\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return content.toString().replace("\t", "");  // Replace tabs with spaces for uniformity
    }

    private List<Pair<String, Pair<Integer, Integer>>> tokenize(List<String> tokens) {
        List<Pair<String, Pair<Integer, Integer>>> resultedTokens = new ArrayList<>();
        boolean isStringConstant = false;
        boolean isCharConstant = false;
        int line = 1, column = 1;
        boolean isComment = false;

        StringBuilder stringBuilder = new StringBuilder();
        for (String token : tokens) {
            if (token.equals("#")) {
                isComment = true;  // start of the comment
                continue;          // skip the comment token #
            }

            if (isComment) {
                if (token.equals("\n")) {
                    isComment = false;  // comment ends when end line
                }
                continue;  // pass over the comment content
            }

            switch (token) {
                case "'":
                    if (isCharConstant) {
                        stringBuilder.append(token);
                        resultedTokens.add(new Pair<>(stringBuilder.toString(), new Pair<>(line, column)));
                        stringBuilder = new StringBuilder();
                    } else {
                        stringBuilder.append(token);
                    }
                    isCharConstant = !isCharConstant;
                    break;
                case "\n":
                    line++;
                    column = 1;
                    break;
                case "\"":
                    if (isStringConstant) {
                        stringBuilder.append(token);
                        resultedTokens.add(new Pair<>(stringBuilder.toString(), new Pair<>(line, column)));
                        stringBuilder = new StringBuilder();
                    } else {
                        stringBuilder.append(token);
                    }
                    isStringConstant = !isStringConstant;
                    break;
                default:
                    if (isStringConstant || isCharConstant) {
                        stringBuilder.append(token);  // kkeep appending inside string or char literals
                    } else if (!token.equals(" ")) {
                        resultedTokens.add(new Pair<>(token, new Pair<>(line, column)));
                        column++;
                    }
                    break;
            }
        }

        return resultedTokens;
    }

    private List<Pair<String, Pair<Integer, Integer>>> createListProgramElements() {
        String content = readFile(); //read the content of the given file
        String concatenatedSeparators = this.separatorsList.stream().reduce("", (a, b) -> (a + b));
        StringTokenizer stringTokenizer = new StringTokenizer(content, concatenatedSeparators, true);

        List<String> tokens = Collections.list(stringTokenizer)
                .stream()
                .map(token -> (String) token)
                .collect(Collectors.toList());

        return tokenize(tokens);
    }
}
