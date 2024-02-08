package com.example.a7_gui.model.programState;



import com.example.a7_gui.exceptions.*;
import com.example.a7_gui.exceptions.ArithmeticException;
import com.example.a7_gui.model.program_statements.IStatement;
import com.example.a7_gui.model.my_utils.*;
import com.example.a7_gui.model.program_values.Value;

import java.io.BufferedReader;
import java.util.List;

public class ProgramState {
    private MyIStack<IStatement> exeStack;
    private MyIStack<MyIDictionary<String, Value>> symTable;
    private MyIList<Value> out;
    private MyIDictionary<String, BufferedReader> fileTable;
    private MyIHeap heap;
    private MyIProcedureTable procTable;
    private IStatement originalProgram;
    private int id;
    private static int lastId = 0;

    public ProgramState(MyIStack<IStatement> stack, MyIStack<MyIDictionary<String,Value>> symTable, MyIList<Value> out, MyIDictionary<String, BufferedReader> fileTable, MyIHeap heap, MyIProcedureTable pr, IStatement program) {
        this.exeStack = stack;
        this.symTable = symTable;
        this.out = out;
        this.fileTable = fileTable;
        this.heap = heap;
        this.procTable=pr;
        this.originalProgram = program.deepCopy();
        this.exeStack.push(this.originalProgram);
        this.id = setId();
    }

    public ProgramState(MyIStack<IStatement> stack, MyIStack<MyIDictionary<String,Value>> symTable, MyIList<Value> out, MyIDictionary<String, BufferedReader> fileTable, MyIHeap heap, MyIProcedureTable pr) {
        this.exeStack = stack;
        this.symTable = symTable;
        this.out = out;
        this.fileTable = fileTable;
        this.heap = heap;
        this.procTable=pr;
        this.id = setId();
    }

    public int getId(){return this.id;}

    public synchronized int setId() {
        lastId++;
        return lastId;
    }



    public MyIStack<IStatement> getExecutionStack() {
        return exeStack;
    }

    public MyIStack<MyIDictionary<String,Value>> getSymbolTablw() {
        return symTable;
    }

    public MyIList<Value> getOut() {
        return out;
    }

    public MyIDictionary<String, BufferedReader> getFileTable() {
        return fileTable;
    }

    public MyIHeap getHeap() {
        return heap;
    }

    public MyIProcedureTable getProcedureTable() {
        return procTable;
    }
    public MyIDictionary<String, Value> getTopFromStackSymbolTable() {
        return symTable.peek();
    }



    public void setExeStack(MyIStack<IStatement> newStack) {
        this.exeStack = newStack;
    }

    public void setSymTable(MyIStack<MyIDictionary<String,Value>> newSymTable) {
        this.symTable = newSymTable;
    }

    public void setOut(MyIList<Value> newOut) {
        this.out = newOut;
    }

    public void setFileTable(MyIDictionary<String, BufferedReader> newFileTable) {
        this.fileTable = newFileTable;
    }

    public void setHeap(MyIHeap newHeap) {
        this.heap = newHeap;
    }

    public void setProcTable(MyIProcedureTable procTable) {
        this.procTable = procTable;
    }
    public boolean isNotCompleted() {
        return exeStack.isEmpty();
    }

    public ProgramState oneStep() throws ExecutionException, DataStructureException, EvaluationException, StackException, ArithmeticException {
        if (exeStack.isEmpty())
            throw new StackException("Program state stack is empty!");
        IStatement currentStatement = exeStack.pop();
        return currentStatement.execute(this);
    }

    public String exeStackToString() {
        StringBuilder exeStackStringBuilder = new StringBuilder();
        List<IStatement> stack = exeStack.getReversed();
        for (IStatement statement: stack) {
            exeStackStringBuilder.append(statement.toString()).append("\n");
        }
        return exeStackStringBuilder.toString();
    }

    public String symTableToString() throws DataStructureException {
        StringBuilder symTableStringBuilder = new StringBuilder();
        List<MyIDictionary<String, Value>> stack = symTable.getReversed();
        for (MyIDictionary<String, Value> table: stack) {
            for (String key: table.keySet()) {
                symTableStringBuilder.append(String.format("%s -> %s\n", key, table.lookUp(key).toString()));
            }
            symTableStringBuilder.append("\n");
        }
        return symTableStringBuilder.toString();
    }

    public String outToString() {
        StringBuilder outStringBuilder = new StringBuilder();
        for (Value elem: out.getList()) {
            outStringBuilder.append(String.format("%s\n", elem.toString()));
        }
        return outStringBuilder.toString();
    }

    public String fileTableToString() {
        StringBuilder fileTableStringBuilder = new StringBuilder();
        for (String key: fileTable.keySet()) {
            fileTableStringBuilder.append(String.format("%s\n", key));
        }
        return fileTableStringBuilder.toString();
    }

    public String heapToString() throws DataStructureException {
        StringBuilder heapStringBuilder = new StringBuilder();
        for (int key: heap.keySet()) {
            heapStringBuilder.append(String.format("%d -> %s\n", key, heap.get(key)));
        }
        return heapStringBuilder.toString();
    }
    public String procTableToString() throws DataStructureException {
        StringBuilder procTableStringBuilder = new StringBuilder();
        for (String key: procTable.keySet()) {
            procTableStringBuilder.append(String.format("%s -> Params: %s, Statement: %s\n", key, procTable.lookUp(key).getKey(), procTable.lookUp(key).getValue()));
        }
        return procTableStringBuilder.toString();
    }

    @Override
    public String toString() {
        return "[PROCESS ID]: " + id + "\n[EXE STACK]: \n" + exeStack.getReversed() + "\n[SYMBOL TABLE]: \n" + symTable.toString() + "\n[OUTPUT LIST]: \n" + out.toString() + "\n[FILE TABLE]:\n" + fileTable.toString() + "\n[HEAP STORAGE]:\n" + heap.toString()  + "\n[PROCEDURES STORAGE]:\n" + procTable.toString() + "\n";
    }

    public String programStateToString() throws DataStructureException {
        return "[PROCESS ID]: " + id + "\n[EXE STACK]: \n" + exeStackToString() + "[SYMBOL TABLE]: \n" + symTableToString() + "[OUTPUT LIST]: \n" + outToString() + "[FILE TABLE]:\n" + fileTableToString() + "[HEAP STORAGE]:\n" + heapToString() + "[PROCEDURE STORAGE]:\n" + procTableToString();
    }
}