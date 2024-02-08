package com.example.a7_gui;

import com.example.a7_gui.controller.Controller;
import com.example.a7_gui.exceptions.CurrentProgramExecutionException;
import com.example.a7_gui.model.programState.ProgramState;
import com.example.a7_gui.model.program_statements.IStatement;
import com.example.a7_gui.model.my_utils.MyIDictionary;
import com.example.a7_gui.model.my_utils.MyIHeap;
import com.example.a7_gui.model.my_utils.MyIProcedureTable;
import com.example.a7_gui.model.program_values.Value;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.util.*;
import java.util.stream.Collectors;

class AditionalStructure<T,S>{
    T first;
    S second;

    public AditionalStructure(T f, S s)
    {
        this.first=f;
        this.second=s;
    }
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
    private TableView<Map.Entry<String, javafx.util.Pair<List<String>, IStatement>>> newTableView;

    @FXML
    private TableColumn<Map.Entry<String, javafx.util.Pair<List<String>, IStatement>>, javafx.util.Pair<String, List<String>>> procedureNameColumn;

    @FXML
    private TableColumn<Map.Entry<String, javafx.util.Pair<List<String>, IStatement>>, String> procedureBody;
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
        procedureNameColumn.setCellValueFactory(p -> new SimpleObjectProperty<>(new javafx.util.Pair<String, List<String>>(p.getValue().getKey(), p.getValue().getValue().getKey())));
        procedureBody.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().getValue().toString()));
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
        populateProcTableView();
    }

    private void populateProcTableView() {
        MyIProcedureTable procTable = Objects.requireNonNull(getCurrentProgramState()).getProcedureTable();
        List<Map.Entry<String, javafx.util.Pair<List<String>, IStatement>>> procTableList = new ArrayList<>();
        for (Map.Entry<String, javafx.util.Pair<List<String>, IStatement>> entry: procTable.getContent().entrySet()) {
            Map.Entry<String, javafx.util.Pair<List<String>, IStatement>> entry1 = new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue());
            procTableList.add(entry1);
        }
        newTableView.setItems(FXCollections.observableList(procTableList));
        newTableView.refresh();
    }


    private void populateNumberOfProgramStatesField()
    {
        List<ProgramState> programStates = controller.getProgramStates();
        numberOfProgramStatesField.setText(String.valueOf(programStates.size()));
    }

    private void populateHeapTableView()
    {
        ProgramState programState = getCurrentProgramState();
        MyIHeap heap = Objects.requireNonNull(programState).getHeap();
        ArrayList<AditionalStructure<Integer,Value>> heapEntries = new ArrayList<>();

        for(Map.Entry<Integer,Value> entry : heap.getContent().entrySet())
            heapEntries.add(new AditionalStructure<>(entry.getKey(), entry.getValue()));

        heapTableView.setItems(FXCollections.observableArrayList(heapEntries));
    }

    private void populateOutputListView()
    {
        ProgramState programState = getCurrentProgramState();
        List<String> output = new ArrayList<>();
        List<Value> outputList = Objects.requireNonNull(programState).getOut().getList();

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
        MyIDictionary<String,Value> symbolTable = Objects.requireNonNull(programState).getTopFromStackSymbolTable();
        ArrayList<AditionalStructure<String,Value>> symbolTableEntries = new ArrayList<>();

        if (symbolTable != null) {
            for (Map.Entry<String, Value> entry : symbolTable.getContent().entrySet()) {
                symbolTableEntries.add(new AditionalStructure<>(entry.getKey(), entry.getValue()));
            }
        }
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
                    alert.setTitle("error");
                    alert.setContentText("Nothing left to execute");
                    alert.showAndWait();
                }
            }catch(InterruptedException | CurrentProgramExecutionException e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("No program was selected");
            alert.showAndWait();
        }
    }


    @FXML
    private void switchProgramState(MouseEvent mouseEvent)
    {
        populateExecutionStackListView();
        populateSymbolTableView();
    }
}
