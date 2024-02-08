package view;

import controller.Controller;
import exceptions.*;
import model.expressions.*;
import model.programState.ProgramState;
import model.stmt.*;
import model.types.BoolType;
import model.types.IntType;
import model.types.RefType;
import model.types.StringType;
import model.utils.*;
import model.values.BoolValue;
import model.values.IValue;
import model.values.IntValue;
import model.values.StringValue;
import repo.IRepository;
import repo.Repository;

import java.io.BufferedReader;
import java.io.IOException;

public class Interpreter {
    public static void main(String[] args){
        TextMenu textMenu = new TextMenu();
        textMenu.addCommand(new ExitCommand("0","exit"));

        // int v
        // v = 2
        // print v
        IStmt firstProgram = new CompoundStmt(new VarDeclStmt("v", new IntType()),
                new CompoundStmt(new AssignStmt("v",new ValueExpression(new IntValue(2))), new PrintStmt(new VariableExpression("v"))
                )
        );
        try {
            System.out.println("compile program 1");
            firstProgram.typecheck(new MyDictionary<>());
            ProgramState prg1 = new ProgramState(
                    new ExeStack<IStmt>(),
                    new SymbolTable<String, IValue>(),
                    new OutputList<IValue>(),
                    new FileTable<String, BufferedReader>(),
                    new HeapTable(),
                    firstProgram
            );
            IRepository repo1 = new Repository(prg1, "C:\\Users\\Sergiu\\Desktop\\SEM3\\metode_avansate_de_programare\\A6\\A6_typecheck\\log1.txt");
            Controller ctrl1 = new Controller(repo1);
            textMenu.addCommand(new RunExample1("1", "example1", ctrl1));
            // int a
            // int b
            // a = 2 + 3 * 5
            // b = a + 1
            // print b
            IStmt secondProgram =
                    new CompoundStmt(new VarDeclStmt("a", new IntType()),
                            new CompoundStmt(new VarDeclStmt("b", new IntType()),
                                    new CompoundStmt(new AssignStmt("a", new ArithmeticExpression("+", new ValueExpression(new IntValue(2)), new ArithmeticExpression("*", new ValueExpression(new IntValue(3)), new ValueExpression(new IntValue(5))))),
                                            new CompoundStmt(new AssignStmt("b", new ArithmeticExpression("+", new VariableExpression("a"), new ValueExpression(new IntValue(1)))), new PrintStmt(new VariableExpression("b"))))
                            )
                    );
            System.out.println("compile program 2");
            secondProgram.typecheck(new MyDictionary<>());
            ProgramState prg2 = new ProgramState(new ExeStack<IStmt>(), new SymbolTable<String, IValue>(), new OutputList<IValue>(), new FileTable<String, BufferedReader>(), new HeapTable(), secondProgram);
            IRepository repo2 = new Repository(prg2, "log2.txt");
            Controller ctrl2 = new Controller(repo2);
            textMenu.addCommand(new RunExample1("2", "example2", ctrl2));


            // bool a
            // int v
            // a = true
            // if a then v = 2 else v = 3
            // print v
            IStmt thirdProgram =
                    new CompoundStmt(new VarDeclStmt("a", new BoolType()),
                            new CompoundStmt(new VarDeclStmt("v", new IntType()),
                                    new CompoundStmt(new AssignStmt("a", new ValueExpression(new BoolValue(true))),
                                            new CompoundStmt(
                                                    new IfStmt(
                                                            new VariableExpression("a"),
                                                            new AssignStmt("v", new ValueExpression(new IntValue(2))),
                                                            new AssignStmt("v", new ValueExpression(new IntValue(3)))
                                                    ),
                                                    new PrintStmt(new VariableExpression("v"))
                                            )
                                    )
                            )
                    );
            System.out.println("compile program 3");
            thirdProgram.typecheck(new MyDictionary<>());
            ProgramState prg3 = new ProgramState(
                    new ExeStack<IStmt>(),
                    new SymbolTable<String, IValue>(),
                    new OutputList<IValue>(),
                    new FileTable<String, BufferedReader>(),
                    new HeapTable(),
                    thirdProgram
            );
            IRepository repo3 = new Repository(prg3, "log3.txt");
            Controller ctrl3 = new Controller(repo3);
            textMenu.addCommand(new RunExample1("3", "example3", ctrl3));

            IStmt fourthProgram =
                    new CompoundStmt(
                            new VarDeclStmt("a", new IntType()),
                            new CompoundStmt(
                                    new VarDeclStmt("b", new IntType()),
                                    new CompoundStmt(
                                            new VarDeclStmt("bool1", new BoolType()),
                                            new CompoundStmt(
                                                    new VarDeclStmt("bool2", new BoolType()),
                                                    new CompoundStmt(
                                                            new AssignStmt("a", new ValueExpression(new IntValue(100))),
                                                            new CompoundStmt(
                                                                    new AssignStmt("b", new ValueExpression(new IntValue(100))),
                                                                    new CompoundStmt(
                                                                            new AssignStmt("bool1", new ValueExpression(new BoolValue(true))),
                                                                            new CompoundStmt(
                                                                                    new AssignStmt("bool2", new ValueExpression(new BoolValue(false))),
                                                                                    new CompoundStmt(
                                                                                            new IfStmt(
                                                                                                    new LogicalExpression("&&", new VariableExpression("bool1"), new VariableExpression("bool2")),
                                                                                                    new AssignStmt("a", new ArithmeticExpression("+", new VariableExpression("a"), new ValueExpression(new IntValue(77)))),
                                                                                                    new AssignStmt("b", new ArithmeticExpression("-", new VariableExpression("b"), new ValueExpression(new IntValue(77))))
                                                                                            ),
                                                                                            new CompoundStmt(
                                                                                                    new PrintStmt(new VariableExpression("a")),
                                                                                                    new PrintStmt(new VariableExpression("b"))
                                                                                            )
                                                                                    )
                                                                            )
                                                                    )
                                                            )
                                                    )
                                            )
                                    )
                            )
                    );
            System.out.println("compile program 4");
            fourthProgram.typecheck(new MyDictionary<>());
            ProgramState prg4 = new ProgramState(
                    new ExeStack<IStmt>(),
                    new SymbolTable<String, IValue>(),
                    new OutputList<IValue>(),
                    new FileTable<String, BufferedReader>(),
                    new HeapTable(),
                    fourthProgram
            );
            IRepository repo4 = new Repository(prg4, "log4.txt");
            Controller ctrl4 = new Controller(repo4);
            textMenu.addCommand(new RunExample1("4", "example4", ctrl4));

            IStmt fifthProgram =
                    new CompoundStmt(
                            new VarDeclStmt("varf", new StringType()),
                            new CompoundStmt(
                                    new AssignStmt("varf", new ValueExpression(new StringValue("test.in"))),
                                    new CompoundStmt(
                                            new OpenReadFileStmt(new VariableExpression("varf")),
                                            new CompoundStmt(
                                                    new VarDeclStmt("varc", new IntType()),
                                                    new CompoundStmt(
                                                            new ReadFileStmt(new VariableExpression("varf"), "varc"),
                                                            new CompoundStmt(
                                                                    new PrintStmt(new VariableExpression("varc")),
                                                                    new CompoundStmt(
                                                                            new ReadFileStmt(new VariableExpression("varf"), "varc"),
                                                                            new CompoundStmt(
                                                                                    new PrintStmt(new VariableExpression("varc")),
                                                                                    new CloseReadFileStmt(new VariableExpression("varf"))
                                                                            )
                                                                    )
                                                            )
                                                    )
                                            )
                                    )
                            )
                    );
            System.out.println("compile program 5");
            fifthProgram.typecheck(new MyDictionary<>());
            ProgramState prg5 = new ProgramState(
                    new ExeStack<IStmt>(),
                    new SymbolTable<String, IValue>(),
                    new OutputList<IValue>(),
                    new FileTable<String, BufferedReader>(),
                    new HeapTable(),
                    fifthProgram
            );
            IRepository repo5 = new Repository(prg5, "log5.txt");
            Controller ctrl5 = new Controller(repo5);
            textMenu.addCommand(new RunExample1("5", "example5 with reading from file", ctrl5));


            //refference variables
            IStmt sixthProgram = new CompoundStmt(new VarDeclStmt("v", new RefType(new IntType())),
                    new CompoundStmt(new NewStmt("v", new ValueExpression(new IntValue(20))),
                            new CompoundStmt(new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                    new CompoundStmt(new NewStmt("a", new VariableExpression("v")),
                                            new CompoundStmt(new PrintStmt(new ReadHeapExpression(new VariableExpression("v"))),
                                                    new PrintStmt(
                                                            new ArithmeticExpression(
                                                                    "+",
                                                                    new ReadHeapExpression(new ReadHeapExpression(new VariableExpression("a"))), new ValueExpression(new IntValue(5)))))))));
            System.out.println("compile program 6");
            sixthProgram.typecheck(new MyDictionary<>());
            ProgramState prg6 = new ProgramState(
                    new ExeStack<IStmt>(),
                    new SymbolTable<String, IValue>(),
                    new OutputList<IValue>(),
                    new FileTable<String, BufferedReader>(),
                    new HeapTable(),
                    sixthProgram
            );
            IRepository repo6 = new Repository(prg6, "log6.txt");
            Controller ctrl6 = new Controller(repo6);
            textMenu.addCommand(new RunExample1("6", "example6 including refference variables", ctrl6));


            //While example
            IStmt seventhProgram = new CompoundStmt(new VarDeclStmt("v", new IntType()),
                    new CompoundStmt(new AssignStmt("v", new ValueExpression(new IntValue(4))),
                            new CompoundStmt(new WhileStmt(new RelationalExpression(">", new VariableExpression("v"), new ValueExpression(new IntValue(0))),
                                    new CompoundStmt(new PrintStmt(new VariableExpression("v")), new AssignStmt("v", new ArithmeticExpression("-", new VariableExpression("v"), new ValueExpression(new IntValue(1)))))),
                                    new PrintStmt(new VariableExpression("v")))));
            System.out.println("compile program 7");
            //seventhProgram.typecheck(new MyDictionary<>());
            ProgramState prg7 = new ProgramState(
                    new ExeStack<IStmt>(),
                    new SymbolTable<String, IValue>(),
                    new OutputList<IValue>(),
                    new FileTable<String, BufferedReader>(),
                    new HeapTable(),
                    seventhProgram
            );
            IRepository repo7 = new Repository(prg7, "log7.txt");
            Controller ctrl7 = new Controller(repo7);
            textMenu.addCommand(new RunExample1("7", "example7 including while statement", ctrl7));


            IStmt eighthProgram = new CompoundStmt(new VarDeclStmt("v", new IntType()),
                    new CompoundStmt(new VarDeclStmt("a", new RefType(new IntType())),
                            new CompoundStmt(new AssignStmt("v", new ValueExpression(new IntValue(10))),
                                    new CompoundStmt(new NewStmt("a", new ValueExpression(new IntValue(22))),
                                            new CompoundStmt(new ForkStmt(new CompoundStmt(new WriteHeapStmt("a", new ValueExpression(new IntValue(30))),
                                                    new CompoundStmt(new AssignStmt("v", new ValueExpression(new IntValue(32))),
                                                            new CompoundStmt(new PrintStmt(new VariableExpression("v")), new PrintStmt(new ReadHeapExpression(new VariableExpression("a"))))))),
                                                    new CompoundStmt(new PrintStmt(new VariableExpression("v")), new PrintStmt(new ReadHeapExpression(new VariableExpression("a")))))))));
            System.out.println("compile program 8");
            eighthProgram.typecheck(new MyDictionary<>());
            ProgramState prg8 = new ProgramState(
                    new ExeStack<IStmt>(),
                    new SymbolTable<String, IValue>(),
                    new OutputList<IValue>(),
                    new FileTable<String, BufferedReader>(),
                    new HeapTable(),
                    eighthProgram
            );
            IRepository repo8 = new Repository(prg8, "log8.txt");
            Controller ctrl8 = new Controller(repo8);
            textMenu.addCommand(new RunExample1("8", "example8 (including the fork statement)", ctrl8));

            textMenu.show();
        }catch(StatementExecutionException | InvalidTypeException | ExpressionEvaluationException | DivisionByZero |
               HeapException | IOException | StackException | InterruptedException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
