package com.example.a7_gui.model.program_state;



import com.example.a7_gui.my_exceptions.*;
import com.example.a7_gui.model.statements.IStatement;
import com.example.a7_gui.model.my_data_structures.*;
import com.example.a7_gui.model.values.Value;
import com.example.a7_gui.my_exceptions.ArithmeticException;

import java.io.BufferedReader;
import java.util.List;

public class ProgramState {
    private MyIStack<IStatement> executionStack;
    private MyISymbolTable<String, Value> symbolTable;
    private MyIList<Value> outputList;
    private MyIFileTable<String, BufferedReader> fileTable;
    private MyIHeap heapTable;
    private MyISemaphore semaphoreTable;

    private IStatement originalProgram;
    private int id;
    private static int lastId = 0;

    public ProgramState(MyIStack<IStatement> stack, MyISymbolTable<String,Value> symbolTable, MyIList<Value> outputList, MyIFileTable<String, BufferedReader> fileTable, MyIHeap heapTable,MyISemaphore semaphoreTabel, IStatement program) {
        this.executionStack = stack;
        this.symbolTable = symbolTable;
        this.outputList = outputList;
        this.fileTable = fileTable;
        this.heapTable = heapTable;
        this.semaphoreTable = semaphoreTabel;

        this.originalProgram = program.deepCopy();
        this.executionStack.push(this.originalProgram);
        this.id = setId();
    }

    public ProgramState(MyIStack<IStatement> stack, MyISymbolTable<String,Value> symbolTable, MyIList<Value> outputList, MyIFileTable<String, BufferedReader> fileTable, MyIHeap heapTable, MyISemaphore semaphoreTable) {
        this.executionStack = stack;
        this.symbolTable = symbolTable;
        this.outputList = outputList;
        this.fileTable = fileTable;
        this.heapTable = heapTable;
        this.semaphoreTable = semaphoreTable;

        this.id = setId();
    }

    public int getId(){return this.id;}

    public synchronized int setId() {
        lastId++;
        return lastId;
    }



    public MyIStack<IStatement> getExecutionStack() {
        return executionStack;
    }

    public MyISymbolTable<String, Value> getSymbolTable() {
        return symbolTable;
    }

    public MyIList<Value> getOutputList() {
        return outputList;
    }

    public MyIFileTable<String, BufferedReader> getFileTable() {
        return fileTable;
    }

    public MyIHeap getHeapTable() {
        return heapTable;
    }

    public MyISemaphore getSemaphoreTable() {
        return semaphoreTable;
    }

    public void setExecutionStack(MyIStack<IStatement> newStack) {
        this.executionStack = newStack;
    }

    public void setSymbolTable(MyISymbolTable<String, Value> newSymTable) {
        this.symbolTable = newSymTable;
    }

    public void setOutputList(MyIList<Value> newOut) {
        this.outputList = newOut;
    }

    public void setFileTable(MyIFileTable<String, BufferedReader> newFileTable) {
        this.fileTable = newFileTable;
    }

    public void setHeapTable(MyIHeap newHeap) {
        this.heapTable = newHeap;
    }
    public void setSemaphoreTable(MyISemaphore newSemaphoreTable){this.semaphoreTable =newSemaphoreTable;}


    public boolean isNotCompleted() {
        return executionStack.isEmpty();
    }

    public ProgramState oneStep() throws StatementExecutionException, DataStructureException, ExpressionEvaluationException, StackException, ArithmeticException {
        if (executionStack.isEmpty())
            throw new StackException("Statements stack is empty");
        IStatement currentStatement = executionStack.pop();
        return currentStatement.executeStatement(this);
    }


    public String semaphoreTableToString() throws DataStructureException
    {
        StringBuilder semaphoreTableStringBuilder = new StringBuilder();

        for(int key : semaphoreTable.getSemaphoreTable().keySet())
            semaphoreTableStringBuilder.append(String.format("%d -> (%d, %s)\n", key, semaphoreTable.get(key).getKey(), semaphoreTable.get(key).getValue()));

        return semaphoreTableStringBuilder.toString();
    }


    public String executionStackToStringFile() {
        StringBuilder exeStackStringBuilder = new StringBuilder();
        List<IStatement> stack = executionStack.getReversed();
        for (IStatement statement: stack) {
            exeStackStringBuilder.append(statement.toString()).append("\n");
        }
        return exeStackStringBuilder.toString();
    }

    public String symbolTableToStringFile() throws DataStructureException {
        StringBuilder symTableStringBuilder = new StringBuilder();
        for (String key: symbolTable.keySet()) {
            symTableStringBuilder.append(String.format("%s -> %s\n", key, symbolTable.lookUp(key).toString()));
        }
        return symTableStringBuilder.toString();
    }
    public String fileTableToStringFile() {
        StringBuilder fileTableStringBuilder = new StringBuilder();
        for (String key: fileTable.keySet()) {
            fileTableStringBuilder.append(String.format("%s\n", key));
        }
        return fileTableStringBuilder.toString();
    }

    public String heapTableToStringFile() throws DataStructureException {
        StringBuilder heapStringBuilder = new StringBuilder();
        for (int key: heapTable.keySet()) {
            heapStringBuilder.append(String.format("%d -> %s\n", key, heapTable.get(key)));
        }
        return heapStringBuilder.toString();
    }
    public String outputListToStringFile() {
        StringBuilder outStringBuilder = new StringBuilder();
        for (Value elem: outputList.getList()) {
            outStringBuilder.append(String.format("%s\n", elem.toString()));
        }
        return outStringBuilder.toString();
    }

    @Override
    public String toString() {
        return "PROCESS ID:\n" + id + "\nEXECUTION STACK: \n" + executionStack.getReversed() + "\nSYMBOL TABLE: \n" + symbolTable.toString() + "\nOUTPUT LIST: \n" + outputList.toString() + "\nFILE TABLE:\n" + fileTable.toString() + "\nHEAP MEMORY:\n" + heapTable.toString() + "\nSSEMAPHORE TABLE:\n" + semaphoreTable.toString() + "\n";
    }

    public String fileToString() throws DataStructureException {
        return "PROCESS ID:\n" + id + "\nEXE STACK: \n" + executionStackToStringFile() + "\nSYMBOL TABLE: \n" + symbolTableToStringFile() + "\nOUTPUT LIST: \n" + outputListToStringFile() + "\nFILE TABLE:\n" + fileTableToStringFile() + "\nHEAP STORAGE:\n" + heapTableToStringFile() + "\nSEMAPHORE TABLE: \n" + semaphoreTableToString() + "\n";
    }
}