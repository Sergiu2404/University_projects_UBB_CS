package com.example.a7_gui;

import com.example.a7_gui.controller.Controller;
import com.example.a7_gui.model.my_data_structures.MyISemaphore;
import com.example.a7_gui.my_exceptions.ProgramExecutionException;
import com.example.a7_gui.model.program_state.ProgramState;
import com.example.a7_gui.model.statements.IStatement;
import com.example.a7_gui.model.my_data_structures.MyIHeap;
import com.example.a7_gui.model.my_data_structures.MyISymbolTable;
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

class AditionalStructure<T,S>{
    T first;
    S second;

    public AditionalStructure(T f, S s)
    {
        this.first=f;
        this.second=s;
    }

    public T getFirst(){return this.first;};
    public S getSecond(){return this.second;};
}

public class ProgramExecutionTables {
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
    private TableView<AditionalStructure<String,Value>> symbolTableView;
    @FXML
    private TableColumn<AditionalStructure<String,Value>,String> variableName;
    @FXML
    private TableColumn<AditionalStructure<String,Value>,String> variableValue;


    @FXML
    private TableView<AditionalStructure<Integer, Value>> heapTableView;
    @FXML
    private TableColumn<AditionalStructure<Integer,Value>, Integer> heapAddress;
    @FXML
    private TableColumn<AditionalStructure<Integer,Value>, String> heapValue;


    @FXML
    private TableView<Map.Entry<Integer, javafx.util.Pair<Integer, List<Integer>>>> semaphoreTableView;
    @FXML
    private TableColumn<Map.Entry<Integer, javafx.util.Pair<Integer, List<Integer>>>, Integer> semaphoreAddressColumn;
    @FXML
    private TableColumn<Map.Entry<Integer, javafx.util.Pair<Integer, List<Integer>>>, Integer> semaphoreValueColumn;
    @FXML
    private TableColumn<Map.Entry<Integer, javafx.util.Pair<Integer, List<Integer>>>, List<Integer>> semaphoreListColumn;

    public void setController(Controller ctrl)
    {
        this.controller=ctrl;
        populateAllItems();
    }

    @FXML
    public void initialize()
    {
        programIdsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        heapAddress.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getFirst()).asObject());
        heapValue.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getSecond().toString()));

        variableName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getFirst()));
        variableValue.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getSecond().toString()));

        semaphoreAddressColumn.setCellValueFactory(val -> new SimpleIntegerProperty(val.getValue().getKey()).asObject());
        semaphoreValueColumn.setCellValueFactory(val -> new SimpleIntegerProperty(val.getValue().getValue().getKey()).asObject());
        semaphoreListColumn.setCellValueFactory(val -> new SimpleObjectProperty<>(val.getValue().getValue().getValue()));

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
        MyISemaphore semaphoreTable = Objects.requireNonNull(programState).getSemaphoreTable();
        List<Map.Entry<Integer, javafx.util.Pair<Integer, List<Integer>>>> semaphoreList = new ArrayList<>();

        semaphoreList.addAll(semaphoreTable.getSemaphoreTable().entrySet());

        semaphoreTableView.setItems(FXCollections.observableArrayList(semaphoreList));
        semaphoreTableView.refresh();
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
        MyIHeap heap = Objects.requireNonNull(programState).getHeapTable();
        ArrayList<AditionalStructure<Integer,Value>> heapEntries = new ArrayList<>();

        for(Map.Entry<Integer,Value> entry : heap.getContent().entrySet())
            heapEntries.add(new AditionalStructure<>(entry.getKey(), entry.getValue()));

        heapTableView.setItems(FXCollections.observableArrayList(heapEntries));
    }

    private void populateOutputListView()
    {
        ProgramState programState = getCurrentProgramState();
        List<String> output = new ArrayList<>();
        List<Value> outputList = Objects.requireNonNull(programState).getOutputList().getList();

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
        MyISymbolTable<String,Value> symbolTable = Objects.requireNonNull(programState).getSymbolTable();
        ArrayList<AditionalStructure<String,Value>> symbolTableEntries = new ArrayList<>();

        for(Map.Entry<String,Value> entry : symbolTable.getContent().entrySet())
            symbolTableEntries.add(new AditionalStructure<>(entry.getKey(),entry.getValue()));

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
                    programStates = controller.removeCompletedPrograms(controller.getProgramStates());
                    controller.setProgramStates(programStates);
                    populateProgramIdentifiersListView();
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("error");
                    alert.setContentText("No statements left for this program");
                    alert.showAndWait();
                }
            }catch(InterruptedException | ProgramExecutionException e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Select one program");
            alert.showAndWait();
        }
    }
}
