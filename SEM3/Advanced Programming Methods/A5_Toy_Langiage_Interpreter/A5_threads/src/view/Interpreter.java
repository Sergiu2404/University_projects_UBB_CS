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
import model.values.IntValue;
import model.values.StringValue;
import repo.IRepository;
import repo.Repository;

import java.io.IOException;

public class Interpreter {
    public static void main(String[] args){
        TextMenu menu = new TextMenu();
        menu.addCommand(new ExitCommand("0", "exit"));

        IStatement ex1 = new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                new CompoundStatement(new AssignStatement("v", new ValueExpression(new IntValue(2))),
                        new PrintStatement(new VariableExpression("v"))));
        try {
            ex1.typeCheck(new MyDictionary<>());
            System.out.println("program 1 compiled");
            ProgramState prg1 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), ex1);
            IRepository repo1;
            repo1 = new Repository(prg1, "logFile1.txt");
            Controller controller1 = new Controller(repo1);
            menu.addCommand(new RunExampleCommand("1", "example 1", controller1));
        } catch (IOException | ExpressionEvaluationException | ADTException | StatementExecutionException e) {
            System.out.println(e.getMessage());
        }

        // int a
        // int b
        // a = 2 + 3 * 5
        // b = a + 1
        // print b
        IStatement ex2 = new CompoundStatement(new VariableDeclarationStatement("a",new IntType()),
                new CompoundStatement(new VariableDeclarationStatement("b",new IntType()),
                        new CompoundStatement(new AssignStatement("a", new ArithmeticExpression('+',new ValueExpression(new IntValue(2)),new ArithmeticExpression('*',new ValueExpression(new IntValue(3)), new ValueExpression(new IntValue(5))))),
                                new CompoundStatement(new AssignStatement("b",new ArithmeticExpression('+',new VariableExpression("a"), new ValueExpression(new
                                        IntValue(1)))), new PrintStatement(new VariableExpression("b"))))));
        try {
            ex2.typeCheck(new MyDictionary<>());
            System.out.println("program 2 compiled");
            ProgramState prg2 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), ex2);
            IRepository repo2;
            repo2 = new Repository(prg2, "logFile2.txt");
            Controller controller2 = new Controller(repo2);
            menu.addCommand(new RunExampleCommand("2", "example 2", controller2));
        } catch (IOException | ExpressionEvaluationException | ADTException | StatementExecutionException e) {
            System.out.println(e.getMessage());
        }

        // bool a
        // int v
        // a = true
        // if a then v = 2 else v = 3
        // print v
        IStatement ex3 = new CompoundStatement(new VariableDeclarationStatement("a", new BoolType()),
                new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                        new CompoundStatement(new AssignStatement("a", new ValueExpression(new BoolValue(true))),
                                new CompoundStatement(new IfStatement(
                                            new VariableExpression("a"),
                                            new AssignStatement("v", new ValueExpression(new IntValue(2))),
                                            new AssignStatement("v", new ValueExpression(new IntValue(3)))),
                                        new PrintStatement(new VariableExpression("v"))))));
        try {
            ex3.typeCheck(new MyDictionary<>());
            System.out.println("program 3 compiled");
            ProgramState prg3 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), ex3);
            IRepository repo3;
            repo3 = new Repository(prg3, "logFile3.txt");
            Controller controller3 = new Controller(repo3);
            menu.addCommand(new RunExampleCommand("3", "example 3", controller3));
        } catch (IOException | ExpressionEvaluationException | ADTException | StatementExecutionException e) {
            System.out.println(e.getMessage());
        }

        IStatement ex4 = new CompoundStatement(
                new VariableDeclarationStatement("a",new IntType()),
                new CompoundStatement(
                        new VariableDeclarationStatement("b", new IntType()),
                        new CompoundStatement(
                                new VariableDeclarationStatement("bool1", new BoolType()),
                                new CompoundStatement(
                                        new VariableDeclarationStatement("bool2", new BoolType()),
                                        new CompoundStatement(
                                                new AssignStatement("a", new ValueExpression(new IntValue(100))),
                                                new CompoundStatement(
                                                        new AssignStatement("b", new ValueExpression(new IntValue(100))),
                                                        new CompoundStatement(
                                                                new AssignStatement("bool1", new ValueExpression(new BoolValue(true))),
                                                                new CompoundStatement(
                                                                        new AssignStatement("bool2", new ValueExpression(new BoolValue(false))),
                                                                        new CompoundStatement(
                                                                                new IfStatement(
                                                                                        new LogicExpression("&&", new VariableExpression("bool1"), new VariableExpression("bool2")),
                                                                                        new AssignStatement("a", new ArithmeticExpression('+', new VariableExpression("a"), new ValueExpression(new IntValue(77)))),
                                                                                        new AssignStatement("b", new ArithmeticExpression('-', new VariableExpression("b"), new ValueExpression(new IntValue(77))))
                                                                                ),
                                                                                new CompoundStatement(
                                                                                        new PrintStatement(new VariableExpression("a")),
                                                                                        new PrintStatement(new VariableExpression("b"))
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


        try {
            ex4.typeCheck(new MyDictionary<>());
            System.out.println("program 4 compiled");
            ProgramState prg4 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), ex4);
            IRepository repo4;
            repo4 = new Repository(prg4, "logFile4.txt");
            Controller controller4 = new Controller(repo4);
            menu.addCommand(new RunExampleCommand("4", "example 4 (my example)", controller4));
        } catch (IOException | ExpressionEvaluationException | ADTException | StatementExecutionException e) {
            System.out.println(e.getMessage());
        }

        //String varf
        //varf = "test.in"
        //openFile(varf)
        //int varc
        //varc = readFile(varf)
        //print(varc)
        //varc = readFile(varf)
        //print(varc)
        //closeFile(varf)
        IStatement ex5 =
                new CompoundStatement(new VariableDeclarationStatement("varf", new StringType()),
                    new CompoundStatement(new AssignStatement("varf", new ValueExpression(new StringValue("test.in"))),
                            new CompoundStatement(new OpenReadFile(new VariableExpression("varf")),
                                    new CompoundStatement(new VariableDeclarationStatement("varc", new IntType()),
                                            new CompoundStatement(new ReadFile(new VariableExpression("varf"), "varc"),
                                                    new CompoundStatement(new PrintStatement(new VariableExpression("varc")),
                                                            new CompoundStatement(new ReadFile(new VariableExpression("varf"), "varc"),
                                                                    new CompoundStatement(new PrintStatement(new VariableExpression("varc")),
                                                                            new CloseReadFile(new VariableExpression("varf"))))))))));
//                new CompoundStatement(new VariableDeclarationStatement("a", new IntType()),
//                new CompoundStatement(new VariableDeclarationStatement("b", new IntType()),
//                        new CompoundStatement(new AssignStatement("a", new ValueExpression(new IntValue(5))),
//                                new CompoundStatement(new AssignStatement("b", new ValueExpression(new IntValue(7))),
//                                        new IfStatement(new RelationalExpression(">", new VariableExpression("a"),
//                                                new VariableExpression("b")),new PrintStatement(new VariableExpression("a")),
//                                                new PrintStatement(new VariableExpression("b")))))));
        try {
            ex5.typeCheck(new MyDictionary<>());
            System.out.println("program 5 compiled");
            ProgramState prg5 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), ex5);
            IRepository repo5;
            repo5 = new Repository(prg5, "logFile5.txt");
            Controller controller5 = new Controller(repo5);
            menu.addCommand(new RunExampleCommand("5", "example 5 with file reading", controller5));
        } catch (IOException | ExpressionEvaluationException | ADTException | StatementExecutionException e) {
            System.out.println(e.getMessage());
        }

        //relational expr + while
        IStatement ex6 = new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                new CompoundStatement(new AssignStatement("v", new ValueExpression(new IntValue(4))),
                        new CompoundStatement(new WhileStatement(new RelationalExpression(">", new VariableExpression("v"), new ValueExpression(new IntValue(0))),
                                new CompoundStatement(new PrintStatement(new VariableExpression("v")), new AssignStatement("v",new ArithmeticExpression('-', new VariableExpression("v"), new ValueExpression(new IntValue(1)))))),
                                new PrintStatement(new VariableExpression("v")))));
//                new CompoundStatement(new VariableDeclarationStatement("varf", new StringType()),
//                new CompoundStatement(new AssignStatement("varf", new ValueExpression(new StringValue("test.in"))),
//                        new CompoundStatement(new OpenReadFile(new VariableExpression("varf")),
//                                new CompoundStatement(new VariableDeclarationStatement("varc", new IntType()),
//                                        new CompoundStatement(new ReadFile(new VariableExpression("varf"), "varc"),
//                                                new CompoundStatement(new PrintStatement(new VariableExpression("varc")),
//                                                        new CompoundStatement(new ReadFile(new VariableExpression("varf"), "varc"),
//                                                                new CompoundStatement(new PrintStatement(new VariableExpression("varc")),
//                                                                        new CompoundStatement(new CloseReadFile(new VariableExpression("varf")), new CloseReadFile(new VariableExpression("varf")))))))))));
        try {
            ex6.typeCheck(new MyDictionary<>());
            System.out.println("program 6 compiled");
            ProgramState prg6 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), ex6);
            IRepository repo6;
            repo6 = new Repository(prg6, "logFile6.txt");
            Controller controller6 = new Controller(repo6);
            menu.addCommand(new RunExampleCommand("6", "program 6 with relational expression and while statement", controller6));
        } catch (IOException | ExpressionEvaluationException | ADTException | StatementExecutionException e) {
            System.out.println(e.getMessage());
        }

        //refference variables
        IStatement ex7 = new CompoundStatement(new VariableDeclarationStatement("v", new RefType(new IntType())),
                new CompoundStatement(new NewStatement("v", new ValueExpression(new IntValue(20))),
                        new CompoundStatement(new VariableDeclarationStatement("a", new RefType(new RefType(new IntType()))),
                                new CompoundStatement(new NewStatement("a", new VariableExpression("v")),
                                        new CompoundStatement(new PrintStatement(new ReadHeapExpression(new VariableExpression("v"))),
                                                new PrintStatement(new ArithmeticExpression('+',new ReadHeapExpression(new ReadHeapExpression(new VariableExpression("a"))), new ValueExpression(new IntValue(5)))))))));
        try {
            ex7.typeCheck(new MyDictionary<>());
            System.out.println("program 7 compiled");
            ProgramState prg7 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), ex7);
            IRepository repo7;
            repo7 = new Repository(prg7, "logFile7.txt");
            Controller controller7 = new Controller(repo7);
            menu.addCommand(new RunExampleCommand("7", "example 7 with refference variables", controller7));
        } catch (IOException | ExpressionEvaluationException | ADTException | StatementExecutionException e) {
            System.out.println(e.getMessage());
        }


        //FORK EXAMPLE
        // int v
        // Ref(int) a
        // v = 10
        // a = new(22)
        // fork(
        // writeHeap(a, 30)
        // v = 32
        // print(v)
        // print(readHeap(a))
        // )
        IStatement ex8 = new CompoundStatement(
                new VariableDeclarationStatement("v", new IntType()),
                new CompoundStatement(
                        new VariableDeclarationStatement("a", new RefType(new IntType())),
                        new CompoundStatement(
                                new AssignStatement("v", new ValueExpression(new IntValue(10))),
                                new CompoundStatement(
                                        new NewStatement("a", new ValueExpression(new IntValue(22))),
                                        new CompoundStatement(
                                                new ForkStatement(new CompoundStatement(
                                                        new WriteHeapStatement("a", new ValueExpression(new IntValue(30))),
                                                        new CompoundStatement(
                                                                new AssignStatement("v", new ValueExpression(new IntValue(32))),
                                                                new CompoundStatement(
                                                                    new PrintStatement(new VariableExpression("v")),
                                                                    new PrintStatement(new ReadHeapExpression(new VariableExpression("a")))
                                                                )
                                                        )
                                                )
                                                ),
                                                new NopStatement()
                                        )
                                )
                        )
                )
        );
        try {
            ex8.typeCheck(new MyDictionary<>());
            System.out.println("program 8 compiled");
            ProgramState prg8 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), ex8);
            IRepository repo8;
            repo8 = new Repository(prg8, "logFile8.txt");
            Controller controller8 = new Controller(repo8);
            menu.addCommand(new RunExampleCommand("8", "example 8 with fork (from Teams)", controller8));
        } catch (IOException | ExpressionEvaluationException | ADTException | StatementExecutionException e) {
            System.out.println(e.getMessage());
        }

        // Exemplu clasa fork + while
        // int counter
        // while(counter < 10){
        //      fork(
        //        fork(
        //          int a
        //          a = counter
        //          print(a)
        //        )
        //      )
        //      counter = counter +1
        // }
        IStatement ex9 = new CompoundStatement(new VariableDeclarationStatement("counter", new IntType()),
                new WhileStatement(
                        new RelationalExpression("<", new VariableExpression("counter"), new ValueExpression(new IntValue(10))),
                        new CompoundStatement(new ForkStatement(new ForkStatement(new CompoundStatement(new VariableDeclarationStatement("a", new RefType(new IntType())),
                                new CompoundStatement(new NewStatement("a", new VariableExpression("counter")),
                                        new PrintStatement(new ReadHeapExpression(new VariableExpression("a")))
                                )
                        ))),
                                new AssignStatement("counter", new ArithmeticExpression('+', new VariableExpression("counter"), new ValueExpression(new IntValue(1))))
                        )
                )
        );
        try {
            ex9.typeCheck(new MyDictionary<>());
            ProgramState prg9 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), ex9);
            IRepository repo9;
            repo9 = new Repository(prg9, "logFile9.txt");
            Controller controller9 = new Controller(repo9);
            menu.addCommand(new RunExampleCommand("9", "example 9 (from last lab)", controller9));
        } catch (IOException | ExpressionEvaluationException | ADTException | StatementExecutionException e) {
            System.out.println(e.getMessage());
        }

        menu.show();
    }
}