package view;

import controller.Controller;
import exceptions.*;
import model.expr.ArithmeticExpression;
import model.expr.LogicalExpression;
import model.expr.ValueExpression;
import model.expr.VariableExpression;
import model.programState.ProgramState;
import model.stmt.*;
import model.type.BoolType;
import model.type.IntType;
import model.utils.*;
import model.value.BoolValue;
import model.value.IValue;
import model.value.IntValue;

import java.util.InputMismatchException;
import java.util.Scanner;

public class View {
    private Controller controller;
    private int executionOption;

    public View(Controller ctrl)
    {
        this.controller=ctrl;
    }

    public void printMenu()
    {
        System.out.println("1-first example");
        System.out.println("2-second example");
        System.out.println("3-third example");
        System.out.println("4-fourth example");
        System.out.println("5-exit");
    }

    public void example1()
    {
        // int v
        // v = 2
        // print v
        IStmt firstProgram = new CompStmt(new VarDeclStmt("v", new IntType()),
                new CompStmt(new AssignStmt("v",new ValueExpression(new IntValue(2))), new PrintStmt(new VariableExpression("v"))
                )
        );

        ProgramState newProgram = new ProgramState(
                new MyStack<IStmt>(),
                new MyDictionary<String, IValue>(),
                new MyList<IValue>(),
                firstProgram
        );

        controller.addProgram(newProgram);

    }

    public void example2()
    {
        // int a
        // int b
        // a = 2 + 3 * 5
        // b = a + 1
        // print b
        IStmt secondProgram =
                new CompStmt(new VarDeclStmt("a",new IntType()),
                        new CompStmt(new VarDeclStmt("b",new IntType()),
                                new CompStmt(new AssignStmt("a",new ArithmeticExpression(1,new ValueExpression(new IntValue(2)),new ArithmeticExpression(3,new ValueExpression(new IntValue(3)),new ValueExpression(new IntValue(5))))),
                                        new CompStmt(new AssignStmt("b",new ArithmeticExpression(1,new VariableExpression("a"), new ValueExpression(new IntValue(1)))), new PrintStmt(new VariableExpression("b"))))
                                )
                        );

        ProgramState program = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), secondProgram);

        controller.addProgram(program);
    }

    public void example3()
    {
        // bool a
        // int v
        // a = true
        // if a then v = 2 else v = 3
        // print v
        IStmt thirdProgram =
                new CompStmt(new VarDeclStmt("a", new BoolType()),
                new CompStmt(new VarDeclStmt("v",new IntType()),
                new CompStmt(new AssignStmt("a", new ValueExpression(new BoolValue(true))),
                new CompStmt(
                        new IfStmt(
                                new VariableExpression("a"),
                                new AssignStmt("v",new ValueExpression(new IntValue(2))),
                                new AssignStmt("v",new ValueExpression(new IntValue(3)))
                        ),
                        new PrintStmt(new VariableExpression("v"))
                )
                )
                )
                );

        ProgramState newProgram = new ProgramState(
                new MyStack<IStmt>(),
                new MyDictionary<String, IValue>(),
                new MyList<IValue>(),
                thirdProgram
        );

        controller.addProgram(newProgram);
    }

    public void example4()
    {
        IStmt fourthProgram =
                new CompStmt(
                        new VarDeclStmt("a", new IntType()),
                        new CompStmt(
                                new VarDeclStmt("b",new IntType()),
                                new CompStmt(
                                        new VarDeclStmt("bool1", new BoolType()),
                                        new CompStmt(
                                                new VarDeclStmt("bool2", new BoolType()),
                                                new CompStmt(
                                                        new AssignStmt("a", new ValueExpression(new IntValue(100))),
                                                        new CompStmt(
                                                                new AssignStmt("b", new ValueExpression(new IntValue(100))),
                                                                new CompStmt(
                                                                        new AssignStmt("bool1", new ValueExpression(new BoolValue(true))),
                                                                        new CompStmt(
                                                                                new AssignStmt("bool2", new ValueExpression(new BoolValue(false))),
                                                                                new CompStmt(
                                                                                        new IfStmt(
                                                                                            new LogicalExpression(1, new VariableExpression("bool1"), new VariableExpression("bool2")),
                                                                                            new AssignStmt("a", new ArithmeticExpression(1, new VariableExpression("a"), new ValueExpression(new IntValue(77)))),
                                                                                            new AssignStmt("b",new ArithmeticExpression(2, new VariableExpression("b"), new ValueExpression(new IntValue(77))))
                                                                                        ),
                                                                                        new CompStmt(
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


        ProgramState newProgram = new ProgramState(
                new MyStack<IStmt>(),
                new MyDictionary<String, IValue>(),
                new MyList<IValue>(),
                fourthProgram
        );

        controller.addProgram(newProgram);
    }

    public void runMenu()
    {
        int option;
        Scanner scanner=new Scanner(System.in);

        while(true)
        {
            try {
                System.out.println("1-step by step");
                System.out.println("2-all steps");
                System.out.println("give execution option: ");
                executionOption=scanner.nextInt();

                printMenu();
                System.out.println("give option number: ");
                option = scanner.nextInt();

                switch (option)
                {
                    case 1:
                        this.example1();
                        break;
                    case 2:
                        this.example2();
                        break;
                    case 3:
                        this.example3();
                        break;
                    case 4:
                        this.example4();
                        break;
                    case 5:
                        System.exit(0);
                    default:
                        throw new InvalidInput("Invalid command, use only integers 1 to 5");
                }

                ProgramState program = controller.getCurrentProgram();

                if(executionOption == 2)
                {
                    controller.executeAllSteps();
                    System.out.println(program);
                }
                else
                {
                    System.out.println(program);

                    while(!controller.isEmpty())
                    {
                        controller.executeOneStep(program);
                        System.out.println(program);
                    }
                }

            }catch(InvalidInput | ExpressionEvaluationException | StatementExecutionException | DivisionByZero | StackException |
                   InputMismatchException e) {
                System.out.println(e.getMessage());
            }

        }
    }

}
