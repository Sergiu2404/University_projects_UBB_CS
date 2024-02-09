package com.example.a7_gui;

import com.example.a7_gui.controller.Controller;
import com.example.a7_gui.exceptions.InterpreterException;
import com.example.a7_gui.model.programState.ProgramState;
import com.example.a7_gui.model.program_statements.IStatement;
import com.example.a7_gui.model.my_utils.*;
import com.example.a7_gui.model.values.Value;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

class AditionalStructureGUI<T,S>{
    T first;
    S second;

    public AditionalStructureGUI(T f, S s)
    {
        this.first=f;
        this.second=s;
    }

    public T getFirst(){return first;}
    public S getSecond(){return second;}
}

public class CurrentProgramExecutor {
    public Button runOneStepButton;
    private Controller controller;

    @FXML
    private TextField numberOfProgramStatesField;
    @FXML
    private ListView<String> outputListView;
    @FXML
    private ListView<String> fileTableListView;
    @FXML
    private ListView<Integer> programIdsListView;
    @FXML
    private ListView<String> executionStackListView;
    @FXML
    private TableView<AditionalStructureGUI<String,Value>> symbolTableView;
    @FXML
    private TableColumn<AditionalStructureGUI<String,Value>,String> variableName;
    @FXML
    private TableColumn<AditionalStructureGUI<String,Value>,String> variableValue;
    @FXML
    private TableView<AditionalStructureGUI<Integer, Value>> heapTableView;
    @FXML
    private TableColumn<AditionalStructureGUI<Integer,Value>, Integer> heapAddress;
    @FXML
    private TableColumn<AditionalStructureGUI<Integer,Value>, String> heapValue;


    @FXML
    private TableView<Map.Entry<Integer, AditionalStructureTuple<Integer, List<Integer>, Integer>>> newTableView;

    @FXML
    private TableColumn<Map.Entry<Integer, AditionalStructureTuple<Integer, List<Integer>, Integer>>, Integer> semaphoreAddressColumn;

    @FXML
    private TableColumn<Map.Entry<Integer, AditionalStructureTuple<Integer, List<Integer>, Integer>>, Integer> semaphoreValueColumn;

    @FXML
    private TableColumn<Map.Entry<Integer, AditionalStructureTuple<Integer, List<Integer>, Integer>>, List<Integer>> semaphoreListColumn;

    public void setController(Controller ctrl)
    {
        this.controller=ctrl;
        populateAllItems();
    }

    @FXML
    public void initialize()
    {
        programIdsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        heapAddress.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().first).asObject());
        heapValue.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().second.toString()));

        variableName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().first));
        variableValue.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().second.toString()));

        semaphoreAddressColumn.setCellValueFactory(val -> new SimpleIntegerProperty(val.getValue().getKey()).asObject());
        semaphoreValueColumn.setCellValueFactory(val -> new SimpleIntegerProperty(val.getValue().getValue().getElement1() - val.getValue().getValue().getElement3()).asObject());
        semaphoreListColumn.setCellValueFactory(val -> new SimpleObjectProperty<>(val.getValue().getValue().getElement2()));
    }

    private ProgramState getCurrentProgramState()
    {
        if(controller.getProgramStates().size() == 0)
            return null;
        else{
            int currentId = programIdsListView.getSelectionModel().getSelectedIndex();
            if(currentId == -1)
                return controller.getProgramStates().get(0);
            else return controller.getProgramStates().get(currentId);
        }
    }

    private void populateAllItems()
    {
        populateProgramIdentifiersListView();
        populateExecutionStackListView();
        populateSymbolTableView();
        populateHeapTableView();
        populateFileTableListView();
        populateOutputListView();
        populateNewTableView();
    }

    private void populateNewTableView() {
        ProgramState programState = getCurrentProgramState();
        MyISemaphore semaphoreTable = Objects.requireNonNull(programState).getSemaphore();

        List<Map.Entry<Integer, AditionalStructureTuple<Integer, List<Integer>, Integer>>> semaphoresList = new ArrayList<>();

        for (Map.Entry<Integer, AditionalStructureTuple<Integer, List<Integer>, Integer>> entry: semaphoreTable.getSemaphoreTable().entrySet()) {
            semaphoresList.add(entry);
        }

        newTableView.setItems(FXCollections.observableArrayList(semaphoresList));
        newTableView.refresh();
    }

    @FXML
    private void switchProgramState(MouseEvent mouseEvent)
    {
        populateExecutionStackListView();
        populateSymbolTableView();
    }

    private void populateNumberOfProgramStatesField()
    {
        List<ProgramState> programStates = controller.getProgramStates();
        numberOfProgramStatesField.setText(String.valueOf(programStates.size()));
    }

    private void populateHeapTableView()
    {
        ProgramState programState = getCurrentProgramState();
        MyIHeap heap = Objects.requireNonNull(programState).getHeapMemory();
        ArrayList<AditionalStructureGUI<Integer,Value>> heapEntries = new ArrayList<>();

        for(Map.Entry<Integer,Value> entry : heap.getContent().entrySet())
            heapEntries.add(new AditionalStructureGUI<>(entry.getKey(), entry.getValue()));

        heapTableView.setItems(FXCollections.observableArrayList(heapEntries));
    }

    private void populateOutputListView()
    {
        ProgramState programState = getCurrentProgramState();
        List<String> output = new ArrayList<>();
        List<Value> outputList = Objects.requireNonNull(programState).getOutput().getList();

        for (Value value : outputList) output.add(value.toString());

        outputListView.setItems(FXCollections.observableArrayList(output));
    }

    private void populateFileTableListView()
    {
        ProgramState programState = getCurrentProgramState();
        List<String> fileNames = new ArrayList<>(Objects.requireNonNull(programState).getFileTable().getContent().keySet());
        fileTableListView.setItems(FXCollections.observableArrayList(fileNames));
    }

    private void populateProgramIdentifiersListView()
    {
        List<ProgramState> programStates = controller.getProgramStates();
        List<Integer> idList = programStates.stream()
                .map(ProgramState::getId)
                .collect(Collectors.toList());
        programIdsListView.setItems(FXCollections.observableList(idList));
        populateNumberOfProgramStatesField();
    }

    private void populateSymbolTableView()
    {
        ProgramState programState = getCurrentProgramState();
        MyIDictionary<String,Value> symbolTable = Objects.requireNonNull(programState).getSymbolTable();
        ArrayList<AditionalStructureGUI<String,Value>> symbolTableEntries = new ArrayList<>();

        for(Map.Entry<String,Value> entry : symbolTable.getContent().entrySet())
            symbolTableEntries.add(new AditionalStructureGUI<>(entry.getKey(),entry.getValue()));

        symbolTableView.setItems(FXCollections.observableArrayList(symbolTableEntries));
    }

    private void populateExecutionStackListView()
    {
        ProgramState programState = getCurrentProgramState();
        List<String> executionStackString = new ArrayList<>();

        if(programState != null)
            for (IStatement stmt : programState.getExecutionStack().getReversed())
                executionStackString.add(stmt.toString());

        executionStackListView.setItems(FXCollections.observableArrayList(executionStackString));
    }

    @FXML
    private void oneStep(MouseEvent event)
    {
        if(controller != null)
        {
            try{
                List<ProgramState> programStates = Objects.requireNonNull(controller.getProgramStates());

                if(programStates.size() > 0)
                {
                    controller.oneStep();
                    populateAllItems();
                    programStates = controller.removeCompletedPrg(controller.getProgramStates());
                    controller.setProgramStates(programStates);
                    populateProgramIdentifiersListView();
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("No statements left in the current program");
                    alert.showAndWait();
                }
            }catch(InterruptedException | InterpreterException e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Select a program");
            alert.showAndWait();
        }
    }
}
