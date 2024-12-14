import utils.Pair;
import utils.SymbolTable;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MyScanner {
    private static final Set<String> OPERATORS = Set.of("+", "-", "*", "/", "%", "greaterEq", "smallerEq", "is", "isNot", "smaller", "greater", "takes");
    private static final Set<String> SEPARATORS = Set.of("{", "}", "(", ")", "[", "]", ":", ";", " ", ",", "\t", "\n", "'", "\"");
    private static final Set<String> RESERVED_WORDS = Set.of("read", "write", "if", "else", "elif", "for", "while", "int", "string", "char", "return", "start", "array");

    private final String filePath;
    private final SymbolTable symbolTable;
    private final ProgramInternalForm pif;

    public MyScanner(String filePath) {
        this.filePath = filePath;
        this.symbolTable = new SymbolTable(10);
        this.pif = new ProgramInternalForm();
    }

    public void scan() {
        List<Pair<String, Pair<Integer, Integer>>> tokens = extractTokens();
        if (tokens == null) return;

        AtomicBoolean hasErrors = new AtomicBoolean(false);
        tokens.forEach(tokenPair -> processToken(tokenPair, hasErrors));

        if (!hasErrors.get()) {
            System.out.println("Program is lexically correct!");
        }
    }

    private List<Pair<String, Pair<Integer, Integer>>> extractTokens() {
        try {
            String content = readFileContent();
            List<String> rawTokens = tokenizeContent(content);
            return categorizeTokens(rawTokens);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String readFileContent() throws FileNotFoundException {
        StringBuilder content = new StringBuilder();
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                content.append(scanner.nextLine()).append("\n");
            }
        }
        return content.toString().replace("\t", "");
    }

    private List<String> tokenizeContent(String content) {
        String separatorPattern = SEPARATORS.stream().reduce("", String::concat);
        StringTokenizer tokenizer = new StringTokenizer(content, separatorPattern, true);
        return Collections.list(tokenizer).stream()
                .map(token -> (String) token)
                .collect(Collectors.toList());
    }

    private List<Pair<String, Pair<Integer, Integer>>> categorizeTokens(List<String> tokens) {
        List<Pair<String, Pair<Integer, Integer>>> categorizedTokens = new ArrayList<>();
        StringBuilder tokenBuilder = new StringBuilder();
        boolean isStringConst = false, isCharConst = false;
        int line = 1, col = 1;

        for (String token : tokens) {
            if (isQuote(token)) {
                handleQuoteToken(token, tokenBuilder, categorizedTokens, line, col, isStringConst, isCharConst);
                isStringConst = !isStringConst;
                isCharConst = !isCharConst;
            } else if ("\n".equals(token)) {
                line++;
                col = 1;
            } else {
                if (isStringConst || isCharConst) {
                    tokenBuilder.append(token);
                } else if (!" ".equals(token)) {
                    categorizedTokens.add(new Pair<>(token, new Pair<>(line, col++)));
                }
            }
        }
        return categorizedTokens;
    }

    private void handleQuoteToken(String token, StringBuilder builder, List<Pair<String, Pair<Integer, Integer>>> tokens, int line, int col, boolean isString, boolean isChar) {
        if (isString || isChar) {
            builder.append(token);
            tokens.add(new Pair<>(builder.toString(), new Pair<>(line, col)));
            builder.setLength(0);
        } else {
            builder.append(token);
        }
    }

    private boolean isQuote(String token) {
        return "\"".equals(token) || "'".equals(token);
    }

    private void processToken(Pair<String, Pair<Integer, Integer>> tokenPair, AtomicBoolean hasErrors) {
        String token = tokenPair.getKey();
        if (isReservedWord(token) || isOperator(token) || isSeparator(token)) {
            pif.add(new Pair<>(token, new Pair<>(-1, -1)));
        } else if (isSingleCharConstant(token) || isStringConstant(token) || isIntegerConstant(token)) {
            addTokenToSymbolTable(token, true); // add constant to symbol table
        } else if(isIdentifier(token)) {
            addTokenToSymbolTable(token, false); // add identifier

        } else {
            logError(tokenPair);
            hasErrors.set(true);
        }
    }

    private boolean isReservedWord(String token) {
        return RESERVED_WORDS.contains(token);
    }

    private boolean isOperator(String token) {
        return OPERATORS.contains(token);
    }

    private boolean isSeparator(String token) {
        return SEPARATORS.contains(token);
    }

    private boolean isSingleCharConstant(String token) {
        return Pattern.matches("^'[a-zA-Z]'$", token);
    }

    private boolean isStringConstant(String token) {
        return Pattern.matches("^\"[^\"]*\"$", token);
    }

    private boolean isIntegerConstant(String token) {
        return new FiniteAutomaton("input/FA_integer_constant.txt").acceptsSequence(token);
    }

    private boolean isIdentifier(String token) {
        return new FiniteAutomaton("input/FA_identifier.txt").acceptsSequence(token);
    }

    private void addTokenToSymbolTable(String token, boolean isConstant) {
//        symbolTable.add(token);
//        pif.add(new Pair<>(token, symbolTable.findPositionOfTerm(token)));

        if(isConstant) {
            symbolTable.addConstant(token);
            pif.add(new Pair<>(token, symbolTable.findPositionOfConstant(token))); }
        else{
            symbolTable.addIdentifier(token);
            pif.add(new Pair<>(token, symbolTable.findPositionOfIdentifier(token))); }
    }

    private void logError(Pair<String, Pair<Integer, Integer>> tokenPair) {
        Pair<Integer, Integer> pos = tokenPair.getValue();
        System.out.printf("\u001B[31mError in file %s at line %d, column %d: invalid token '%s'%n\u001B[0m",
                filePath, pos.getKey(), pos.getValue(), tokenPair.getKey());
    }

    public ProgramInternalForm getPif() {
        return pif;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }
}
