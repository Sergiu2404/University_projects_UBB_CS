package com.example.a7_gui;

import com.example.a7_gui.controller.Controller;
import com.example.a7_gui.my_exceptions.DataStructureException;
import com.example.a7_gui.my_exceptions.ExpressionEvaluationException;
import com.example.a7_gui.my_exceptions.StatementExecutionException;
import com.example.a7_gui.model.expressions.*;
import com.example.a7_gui.model.program_state.ProgramState;
import com.example.a7_gui.model.statements.*;
import com.example.a7_gui.model.types.BoolType;
import com.example.a7_gui.model.types.IntType;
import com.example.a7_gui.model.types.RefType;
import com.example.a7_gui.model.types.StringType;
import com.example.a7_gui.model.my_data_structures.*;
import com.example.a7_gui.model.values.BoolValue;
import com.example.a7_gui.model.values.IntValue;
import com.example.a7_gui.model.values.StringValue;
import com.example.a7_gui.model.values.Value;
import com.example.a7_gui.repository.IRepository;
import com.example.a7_gui.repository.Repository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AlternateControllers {
    private ProgramExecutionTables programExecutionTables;
    List<IStatement> programsList;

    public void setExecutorController(ProgramExecutionTables exeCtrl)
    {
        this.programExecutionTables = exeCtrl;
    }
    @FXML
    private ListView<IStatement> programsListView;
    @FXML
    public void initialize()
    {
        programsListView.setItems(this.getAllPrograms());
        programsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    @FXML
    private void displayProgram(ActionEvent event)
    {
        IStatement selectedStatement = programsListView.getSelectionModel().getSelectedItem();

        if(selectedStatement == null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Seleect a program");
            alert.showAndWait();
        }
        else
        {
            int id = programsListView.getSelectionModel().getSelectedIndex();
            try{
                selectedStatement.typeCheck(new MyDictionary<>()); //precompile check

                ProgramState prg = new ProgramState(new MyStack<>(), new MySymbolTable<>(), new MyList<>(), new MyFileTable<>(), new MyHeap(),new MySemaphore(), selectedStatement);
                IRepository repo = new Repository(prg, "C:\\Users\\Sergiu\\Desktop\\examen_practicMAP\\semaphore\\A7_gui\\src\\main\\java\\com\\example\\a7_gui\\logFile" + (id+1) + ".txt");
                Controller ctrl = new Controller(repo);

                programExecutionTables.setController(ctrl);
            }catch(IOException | ExpressionEvaluationException | DataStructureException | StatementExecutionException e)
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
                new CompoundStatement(new AssignStatement("varf", new ValueExpression(new StringValue("C:\\Users\\Sergiu\\Desktop\\examen_practicMAP\\semaphore\\A7_gui\\src\\main\\java\\com\\example\\a7_gui\\test.in"))),
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

        IStatement prg10 = new CompoundStatement(new VariableDeclarationStatement("a", new IntType()),
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


        IStatement prg11 = new CompoundStatement(
                new VariableDeclarationStatement("v1", new RefType(new IntType())),
                new CompoundStatement(
                        new VariableDeclarationStatement("cnt", new IntType()),
                        new CompoundStatement(
                                new NewStatement("v1", new ValueExpression(new IntValue(1))),
                                new CompoundStatement(
                                        new CreateCountSemaphore("cnt", new ReadHeapExpression(new VariableExpression("v1"))),
                                        new CompoundStatement(
                                                new ForkStatement(
                                                        new CompoundStatement(
                                                                new AcquirePermitsStatement("cnt"),
                                                                new CompoundStatement(
                                                                        new WriteHeapStatement("v1", new ArithmeticExpression('*', new ReadHeapExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(10)))),
                                                                        new CompoundStatement(
                                                                                new PrintStatement(new ReadHeapExpression(new VariableExpression("v1"))),
                                                                                new ReleasePermitsStatement("cnt")
                                                                        )
                                                                )
                                                        )
                                                ),
                                                new CompoundStatement(
                                                        new ForkStatement(
                                                                new CompoundStatement(
                                                                        new AcquirePermitsStatement("cnt"),
                                                                        new CompoundStatement(
                                                                                new WriteHeapStatement("v1", new ArithmeticExpression('*',  new ReadHeapExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(10)))),
                                                                                new CompoundStatement(
                                                                                        new WriteHeapStatement("v1", new ArithmeticExpression('*',  new ReadHeapExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(2)))),
                                                                                        new CompoundStatement(
                                                                                                new PrintStatement(new ReadHeapExpression(new VariableExpression("v1"))),
                                                                                                new ReleasePermitsStatement("cnt")
                                                                                        )
                                                                                )
                                                                        )
                                                                )
                                                        ),
                                                        new CompoundStatement(
                                                                new AcquirePermitsStatement("cnt"),
                                                                new CompoundStatement(
                                                                        new PrintStatement(new ArithmeticExpression('-', new ReadHeapExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(1)))),
                                                                        new ReleasePermitsStatement("cnt")
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );

        programsList.add(prg1);
        programsList.add(prg2);
        programsList.add(prg3);
        programsList.add(prg4);
        programsList.add(prg5);
        programsList.add(prg6);
        programsList.add(prg7);
        programsList.add(prg8);
        //programsList.add(prg8);
        programsList.add(prg9);
        programsList.add(prg10);
        programsList.add(prg11);
        //programsList.add(prg12);

        return FXCollections.observableArrayList(programsList);
    }


    //protected void onHelloButtonClick() {
    //    welcomeText.setText("Welcome to JavaFX Application!");
    //}
}