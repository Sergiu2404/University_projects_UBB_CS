import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Main {
    private static void writeToFile(String outputFilePath, Object object) {
        try (PrintStream printStream = new PrintStream(outputFilePath)) {
            printStream.println(object.toString());
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    //run the lexical analyzer, scan the file and write symbol table, identifier table, and PIF to files
    private static void runLexicalAnalyzer(String inputFilePath) {
        MyScanner scanner = new MyScanner(inputFilePath);
        scanner.scan(inputFilePath);

        //write identifierTable to file
        writeToFile("output/" + inputFilePath.replace("input/", "").replace(".txt", "IdentifiersSymbolTable.txt"),
                scanner.getSymbolTable().getIdentifierTable());

        //write symbolTable to file
        writeToFile("output/" + inputFilePath.replace("input/", "").replace(".txt", "constantsSymbolTable.txt"),
                scanner.getSymbolTable().getConstantTable());

        //write ProgramInternalForm (PIF) to file (overwriting any previous content)
        writeToFile("output/" + inputFilePath.replace("input/", "").replace(".txt", "PIF.txt"),
                scanner.getProgramInternalForm());
    }

    public static void main(String[] args) {
        runLexicalAnalyzer("input/p1.txt");
        runLexicalAnalyzer("input/p2.txt");
        runLexicalAnalyzer("input/p3.txt");
        runLexicalAnalyzer("input/p1err.txt");
    }
}
