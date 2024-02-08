package com.example.a7_gui;

import com.example.a7_gui.controller.Controller;
import com.example.a7_gui.exceptions.ADTException;
import com.example.a7_gui.exceptions.ExpressionEvaluationException;
import com.example.a7_gui.exceptions.InterpreterException;
import com.example.a7_gui.exceptions.StatementExecutionException;
import com.example.a7_gui.model.expressions.*;
import com.example.a7_gui.model.programState.ProgramState;
import com.example.a7_gui.model.stmt.*;
import com.example.a7_gui.model.types.BoolType;
import com.example.a7_gui.model.types.IntType;
import com.example.a7_gui.model.types.RefType;
import com.example.a7_gui.model.types.StringType;
import com.example.a7_gui.model.utils.*;
import com.example.a7_gui.model.values.BoolValue;
import com.example.a7_gui.model.values.IntValue;
import com.example.a7_gui.model.values.StringValue;
import com.example.a7_gui.model.values.Value;
import com.example.a7_gui.repo.IRepository;
import com.example.a7_gui.repo.Repository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProgramSwitchController {
    private ExecutorController executorController;
    List<IStatement> programsList;

    public void setExecutorController(ExecutorController exeCtrl)
    {
        this.executorController = exeCtrl;
    }
    @FXML
    private ListView<IStatement> programsListView;
    @FXML
    private Button displayButton;
    @FXML
    public void initialize()
    {
        programsListView.setItems(this.getAllPrograms());
        programsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        //for only one program selecte dat a time
    }

    @FXML
    private void displayProgram(ActionEvent event)
    {
        IStatement selectedStatement = programsListView.getSelectionModel().getSelectedItem();

        if(selectedStatement == null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            //alert.setTitle("Error");
            alert.setContentText("No program was selected!");
            alert.showAndWait();
        }
        else
        {
            int id = programsListView.getSelectionModel().getSelectedIndex();
            try{
                selectedStatement.typeCheck(new MyDictionary<>()); //precompile check

                ProgramState prg = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<Value>(), new MyDictionary<>(), new MyHeap(), selectedStatement);
                IRepository repo = new Repository(prg, "C:\\Users\\Sergiu\\Desktop\\SEM3\\metode_avansate_de_programare\\practicMAP\\doWhile\\A7_gui\\src\\main\\java\\com\\example\\a7_gui\\logFile" + (id+1) + ".txt");
                Controller ctrl = new Controller(repo);

                executorController.setController(ctrl);
            }catch(IOException | ExpressionEvaluationException | ADTException | StatementExecutionException e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }

        }
    }

    @FXML
    private ObservableList<IStatement> getAllPrograms()
    {
        programsList = new ArrayList<>();

        IStatement prg1 = new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                new CompoundStatement(new AssignStatement("v", new ValueExpression(new IntValue(2))),
                        new PrintStatement(new VariableExpression("v"))));

        IStatement prg2 = new CompoundStatement(new VariableDeclarationStatement("a",new IntType()),
                new CompoundStatement(new VariableDeclarationStatement("b",new IntType()),
                        new CompoundStatement(new AssignStatement("a", new ArithmeticExpression('+',new ValueExpression(new IntValue(2)),new ArithmeticExpression('*',new ValueExpression(new IntValue(3)), new ValueExpression(new IntValue(5))))),
                                new CompoundStatement(new AssignStatement("b",new ArithmeticExpression('+',new VariableExpression("a"), new ValueExpression(new
                                        IntValue(1)))), new PrintStatement(new VariableExpression("b"))))));

        IStatement prg3 = new CompoundStatement(new VariableDeclarationStatement("a", new BoolType()),
                new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                        new CompoundStatement(new AssignStatement("a", new ValueExpression(new BoolValue(true))),
                                new CompoundStatement(new IfStatement(
                                        new VariableExpression("a"),
                                        new AssignStatement("v", new ValueExpression(new IntValue(2))),
                                        new AssignStatement("v", new ValueExpression(new IntValue(3)))),
                                        new PrintStatement(new VariableExpression("v"))))));

        IStatement prg4 = new CompoundStatement(
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

        IStatement prg5 = new CompoundStatement(new VariableDeclarationStatement("varf", new StringType()),
                new CompoundStatement(new AssignStatement("varf", new ValueExpression(new StringValue("C:\\Users\\Sergiu\\Desktop\\SEM3\\metode_avansate_de_programare\\A7\\A7_gui\\src\\main\\java\\com\\example\\a7_gui\\test.in"))),
                        new CompoundStatement(new OpenReadFile(new VariableExpression("varf")),
                                new CompoundStatement(new VariableDeclarationStatement("varc", new IntType()),
                                        new CompoundStatement(new ReadFile(new VariableExpression("varf"), "varc"),
                                                new CompoundStatement(new PrintStatement(new VariableExpression("varc")),
                                                        new CompoundStatement(new ReadFile(new VariableExpression("varf"), "varc"),
                                                                new CompoundStatement(new PrintStatement(new VariableExpression("varc")),
                                                                        new CloseReadFile(new VariableExpression("varf"))))))))));

        IStatement prg6 = new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                new CompoundStatement(new AssignStatement("v", new ValueExpression(new IntValue(4))),
                        new CompoundStatement(new WhileStatement(new RelationalExpression(">", new VariableExpression("v"), new ValueExpression(new IntValue(0))),
                                new CompoundStatement(new PrintStatement(new VariableExpression("v")), new AssignStatement("v",new ArithmeticExpression('-', new VariableExpression("v"), new ValueExpression(new IntValue(1)))))),
                                new PrintStatement(new VariableExpression("v")))));

        IStatement prg7 = new CompoundStatement(new VariableDeclarationStatement("v", new RefType(new IntType())),
                new CompoundStatement(new NewStatement("v", new ValueExpression(new IntValue(20))),
                        new CompoundStatement(new VariableDeclarationStatement("a", new RefType(new RefType(new IntType()))),
                                new CompoundStatement(new NewStatement("a", new VariableExpression("v")),
                                        new CompoundStatement(new PrintStatement(new ReadHeapExpression(new VariableExpression("v"))),
                                                new PrintStatement(new ArithmeticExpression('+',new ReadHeapExpression(new ReadHeapExpression(new VariableExpression("a"))), new ValueExpression(new IntValue(5)))))))));

        IStatement prg8 = new CompoundStatement(new VariableDeclarationStatement("v",new IntType()),
                new CompoundStatement(new VariableDeclarationStatement("a",new RefType(new IntType())),
                        new CompoundStatement(new AssignStatement("v",new ValueExpression(new IntValue(10))),
                                new CompoundStatement(
                                        new NewStatement("a",new ValueExpression(new IntValue(22))),
                                        new CompoundStatement(new ForkStatement(
                                                new CompoundStatement(new WriteHeapStatement("a", new ValueExpression(new IntValue(30))),
                                                        new CompoundStatement(new AssignStatement("v",new ValueExpression(new IntValue(32))),
                                                                new CompoundStatement(new PrintStatement(new VariableExpression("v")),new PrintStatement(new ReadHeapExpression(new VariableExpression("a")))
                                                                )
                                                        )
                                                )),
                                                new CompoundStatement(new PrintStatement(new VariableExpression("v")),new PrintStatement(new ReadHeapExpression(new VariableExpression("a"))))
                                        )
                                )
                        )
                )
        );
//        IStatement prg8 = new CompoundStatement(
//                new VariableDeclarationStatement("v", new IntType()),
//                new CompoundStatement(
//                        new VariableDeclarationStatement("a", new RefType(new IntType())),
//                        new CompoundStatement(
//                                new AssignStatement("v", new ValueExpression(new IntValue(10))),
//                                new CompoundStatement(
//                                        new NewStatement("a", new ValueExpression(new IntValue(22))),
//                                        new CompoundStatement(
//                                                new ForkStatement(new CompoundStatement(
//                                                        new WriteHeapStatement("a", new ValueExpression(new IntValue(30))),
//                                                        new CompoundStatement(
//                                                                new AssignStatement("v", new ValueExpression(new IntValue(32))),
//                                                                new CompoundStatement(
//                                                                        new PrintStatement(new VariableExpression("v")),
//                                                                        new PrintStatement(new ReadHeapExpression(new VariableExpression("a")))
//                                                                )
//                                                        )
//                                                )
//                                                ),
//                                                new NopStatement()
//                                        )
//                                )
//                        )
//                )
//        );

        IStatement prg9 = new CompoundStatement(new VariableDeclarationStatement("counter", new IntType()),
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

        //Conditional Assignment
        IStatement prg10 = new CompoundStatement(new VariableDeclarationStatement("b", new BoolType()),
                new CompoundStatement(new VariableDeclarationStatement("c", new IntType()),
                        new CompoundStatement(new AssignStatement("b", new ValueExpression(new BoolValue(true))),
                                new CompoundStatement(new CondAssignStatement(
                                        new VariableExpression("b"),
                                        new ValueExpression(new IntValue(100)),
                                        new ValueExpression(new IntValue(200)),
                                        "c"),
                                        new CompoundStatement(new PrintStatement(new VariableExpression("c")),
                                                new CompoundStatement(new CondAssignStatement(
                                                        new ValueExpression(new BoolValue(false)),
                                                        new ValueExpression(new IntValue(100)),
                                                        new ValueExpression(new IntValue(200)),
                                                        "c"),
                                                        new PrintStatement(new VariableExpression("c"))))))));

        //do while
        IStatement prg11 = new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                new CompoundStatement(new AssignStatement("v", new ValueExpression(new IntValue(4))),
                        new CompoundStatement(new DoWhileStatement(new RelationalExpression(">", new VariableExpression("v"), new ValueExpression(new IntValue(0))),
                                new CompoundStatement(new PrintStatement(new VariableExpression("v")), new AssignStatement("v",new ArithmeticExpression('-', new VariableExpression("v"), new ValueExpression(new IntValue(1)))))),
                                new PrintStatement(new VariableExpression("v")))));

        //for
        IStatement prg12 = new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                new CompoundStatement(new AssignStatement("v", new ValueExpression(new IntValue(20))),
                        new CompoundStatement(new ForStatement("v", new ValueExpression(new IntValue(0)),
                                new ValueExpression(new IntValue(3)),
                                new ArithmeticExpression('+', new VariableExpression("v"), new ValueExpression(new IntValue(1))),
                                new ForkStatement(new CompoundStatement(new PrintStatement(new VariableExpression("v")),
                                        new AssignStatement("v", new ArithmeticExpression('+', new VariableExpression("v"), new ValueExpression(new IntValue(1))))))),
                                new PrintStatement(new ArithmeticExpression('*', new VariableExpression("v"), new ValueExpression(new IntValue(10)))))));

        //MUL
        IStatement prg13 = new CompoundStatement(new VariableDeclarationStatement("v1", new IntType()),
                new CompoundStatement(new VariableDeclarationStatement("v2", new IntType()),
                        new CompoundStatement(new AssignStatement("v1", new ValueExpression(new IntValue(2))),
                                new CompoundStatement(new AssignStatement("v2", new ValueExpression(new IntValue(3))),
                                        new IfStatement(new RelationalExpression("!=", new VariableExpression("v1"), new ValueExpression(new IntValue(0))),
                                                new PrintStatement(new MULExpression(new VariableExpression("v1"), new VariableExpression("v2"))),
                                                new PrintStatement(new VariableExpression("v1")))))));

        //repeat until
        IStatement prg14 = new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                new CompoundStatement(new AssignStatement("v", new ValueExpression(new IntValue(0))),
                        new CompoundStatement(new RepeatUntilStatement(
                                new RelationalExpression("==", new VariableExpression("v"), new ValueExpression(new IntValue(3))),
                                new CompoundStatement(new ForkStatement(new CompoundStatement(new PrintStatement(new VariableExpression("v")),
                                        new AssignStatement("v", new ArithmeticExpression('-', new VariableExpression("v"), new ValueExpression(new IntValue(1)))))),
                                        new AssignStatement("v", new ArithmeticExpression('+', new VariableExpression("v"), new ValueExpression(new IntValue(1)))))
                        ),
                                new CompoundStatement(new VariableDeclarationStatement("x", new IntType()),
                                        new CompoundStatement(new VariableDeclarationStatement("y", new IntType()),
                                                new CompoundStatement(new VariableDeclarationStatement("z", new IntType()),
                                                        new CompoundStatement(new VariableDeclarationStatement("w", new IntType()),
                                                                new CompoundStatement(new AssignStatement("x", new ValueExpression(new IntValue(1))),
                                                                        new CompoundStatement(new AssignStatement("y", new ValueExpression(new IntValue(2))),
                                                                                new CompoundStatement(new AssignStatement("z", new ValueExpression(new IntValue(3))),
                                                                                        new CompoundStatement(new AssignStatement("w", new ValueExpression(new IntValue(4))),
                                                                                                new PrintStatement(new ArithmeticExpression('*', new VariableExpression("v"), new ValueExpression(new IntValue(10)))))))))))))));

        //sleep
        IStatement prg15 = new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                new CompoundStatement(new AssignStatement("v", new ValueExpression(new IntValue(0))),
                        new CompoundStatement(new WhileStatement(new RelationalExpression("<", new VariableExpression("v"), new ValueExpression(new IntValue(3))),
                                new CompoundStatement(new ForkStatement(new CompoundStatement(new PrintStatement(new VariableExpression("v")),
                                        new AssignStatement("v", new ArithmeticExpression('+', new VariableExpression("v"), new ValueExpression(new IntValue(1)))))),
                                        new AssignStatement("v", new ArithmeticExpression('+', new VariableExpression("v"), new ValueExpression(new IntValue(1)))))),
                                new CompoundStatement(new SleepStatement(5), new PrintStatement(new ArithmeticExpression('*', new VariableExpression("v"), new ValueExpression(new IntValue(10))))))));


        IStatement prg16 = new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                new CompoundStatement(new AssignStatement("v", new ValueExpression(new IntValue(10))),
                        new CompoundStatement(
                                new CompoundStatement(
                                        new ForkStatement(
                                                new CompoundStatement(
                                                        new AssignStatement("v", new ArithmeticExpression('-', new VariableExpression("v"), new ValueExpression(new IntValue(1)))),
                                                        new CompoundStatement(
                                                                new AssignStatement("v", new ArithmeticExpression('-', new VariableExpression("v"), new ValueExpression(new IntValue(1)))),
                                                                new PrintStatement(new VariableExpression("v"))
                                                        )
                                                        )),
                                        new NopStatement()
                                        ),
                                new CompoundStatement(new SleepStatement(10), new PrintStatement(new ArithmeticExpression('*', new VariableExpression("v"), new ValueExpression(new IntValue(10))))))));




        //switch
        IStatement prg17 = new CompoundStatement(new VariableDeclarationStatement("a", new IntType()),
                new CompoundStatement(new VariableDeclarationStatement("b", new IntType()),
                        new CompoundStatement(new VariableDeclarationStatement("c", new IntType()),
                                new CompoundStatement(new AssignStatement("a", new ValueExpression(new IntValue(1))),
                                        new CompoundStatement(new AssignStatement("b", new ValueExpression(new IntValue(2))),
                                                new CompoundStatement(new AssignStatement("c", new ValueExpression(new IntValue(5))),
                                                        new CompoundStatement(new SwitchStatement(
                                                                new ArithmeticExpression('*', new VariableExpression("a"), new ValueExpression(new IntValue(10))),
                                                                new ArithmeticExpression('*', new VariableExpression("b"), new VariableExpression("c")),
                                                                new CompoundStatement(new PrintStatement(new VariableExpression("a")), new PrintStatement(new VariableExpression("b"))),
                                                                new ValueExpression(new IntValue(10)),
                                                                new CompoundStatement(new PrintStatement(new ValueExpression(new IntValue(100))), new PrintStatement(new ValueExpression(new IntValue(200)))),
                                                                new PrintStatement(new ValueExpression(new IntValue(300)))
                                                        ), new PrintStatement(new ValueExpression(new IntValue(300))))))))));
        //wait
        IStatement prg18 = new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                new CompoundStatement(new AssignStatement("v", new ValueExpression(new IntValue(20))),
                        new CompoundStatement(new WaitStatement(10),
                                new PrintStatement(new ArithmeticExpression('*', new VariableExpression("v"), new ValueExpression(new IntValue(10)))))));

        programsList.add(prg1);
        programsList.add(prg2);
        programsList.add(prg3);
        programsList.add(prg4);
        programsList.add(prg5);
        programsList.add(prg6);
        programsList.add(prg7);
        programsList.add(prg8);
        programsList.add(prg9);
        programsList.add(prg10);
        programsList.add(prg11);
        programsList.add(prg12);
        programsList.add(prg13);
        programsList.add(prg14);
        programsList.add(prg15);
        programsList.add(prg16);
        programsList.add(prg17);
        programsList.add(prg18);

        return FXCollections.observableArrayList(programsList);
    }


    //protected void onHelloButtonClick() {
    //    welcomeText.setText("Welcome to JavaFX Application!");
    //}
}