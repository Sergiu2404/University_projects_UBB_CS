package model.programState;

import exceptions.*;
import model.stmt.IStmt;
import model.utils.*;
import model.values.IValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgramState {
    private MyIStack<IStmt> exeStack;
    private MyIDictionary<String, IValue> symTable;
    private MyIList<IValue> output;
    private MyIDictionary<String, BufferedReader> fileTable;
    private MyIHeap heapTable;
    private IStmt originalProgram;
    private int id;
    private static int lastId = 0;

    public ProgramState(MyIStack<IStmt> s, MyIDictionary<String, IValue> symT, MyIList<IValue> l, MyIDictionary<String, BufferedReader> fileT, MyIHeap h, IStmt prg)
    {
        this.exeStack=s;
        this.symTable=symT;
        this.output=l;
        this.fileTable=fileT;
        this.heapTable = h;

        this.originalProgram = prg.deepcopy();
        this.exeStack.push(this.originalProgram);
        this.id=setId();
    }
    public ProgramState(MyIStack<IStmt> s, MyIDictionary<String, IValue> symT, MyIList<IValue> l, MyIDictionary<String, BufferedReader> fileT, MyIHeap h)
    {
        this.exeStack=s;
        this.symTable=symT;
        this.output=l;
        this.fileTable=fileT;
        this.heapTable = h;

        this.id = setId();
    }


    public synchronized int setId()
    {
        lastId++;
        return lastId;
    }

    public MyIStack<IStmt> getExeStack(){return this.exeStack;}

    public void setExeStack(MyIStack<IStmt> st){this.exeStack=st;}
    public MyIDictionary<String,IValue> getSymbolTable(){return this.symTable;}
    public void setSymTable(MyIDictionary<String,IValue> d){this.symTable=d;}
    public MyIList<IValue> getOutput(){return this.output;}
    public void setOutput(MyIList<IValue> l){this.output=l;}
    public MyIDictionary<String,BufferedReader> getFileTable(){return this.fileTable;}
    public void setFileTable(MyIDictionary<String,BufferedReader> f){this.fileTable=f;}

    public MyIHeap getHeapTable(){return this.heapTable;}
    public void  setHeapTable(MyIHeap newHeap){this.heapTable = newHeap;}


    public boolean isNotCopleted()
    {
        return exeStack.isEmpty();
    }

    public ProgramState oneStep() throws StatementExecutionException, ExpressionEvaluationException, DivisionByZero, HeapException, IOException {
        if(exeStack.isEmpty())
            throw new StatementExecutionException("Program state stack is empty");

        IStmt currentStatement = exeStack.pop();
        return currentStatement.execute(this);
    }

    //FOR PRINTING IN FILE
    public String stackToString()
    {
        StringBuilder exeStackString= new StringBuilder();
        List<IStmt> stackToList= exeStack.getReverse();
        for(IStmt elem: stackToList)
            exeStackString.append(elem.toString()).append("\n");

        return exeStackString.toString();
    }


    public String dictionarySymTblToString()
    {
        StringBuilder dictionaryToString = new StringBuilder();
        for(String key : symTable.keySet())
            dictionaryToString.append(String.format("%s -> %s\n",key, symTable.lookup(key).toString()));
        return dictionaryToString.toString();
    }

    public String listToString()
    {
        StringBuilder listToString = new StringBuilder();
        for(IValue elem: this.output.getList())
            listToString.append(String.format("%s\n",elem.toString()));

        return listToString.toString();
    }

    public String dictionaryFileTableToString()
    {
        StringBuilder dictionaryToString = new StringBuilder();
        for(String key: this.fileTable.keySet())
            dictionaryToString.append(String.format("%s\n",key));

        return dictionaryToString.toString();
    }

    public String dictionaryHeapTableToString() throws HeapException {
        StringBuilder dictionaryToString = new StringBuilder();
        for(int key: this.heapTable.keySet())
            dictionaryToString.append(String.format("%s -> %s\n", key, heapTable.get(key)));

        return dictionaryToString.toString();
    }


    public String programStateToStringFile() throws HeapException {
        return "id: " + id + "ExeStack: \n" + this.stackToString() + "\nSymbol Table: \n" + this.dictionarySymTblToString() + "\nOutput List: \n" + this.listToString()+"\nFile Table: \n" + this.dictionaryFileTableToString()+"\nHeap Table: \n" + this.dictionaryHeapTableToString() +"\nEND\n";
    }

    @Override
    public String toString() {
        return "ProgramState{" +
                ", id=" + id +
                "exeStack=" + exeStack +
                ", symTable=" + symTable +
                ", output=" + output +
                ", fileTable=" + fileTable +
                ", heapTable=" + heapTable +
                ", originalProgram=" + originalProgram +
                '}';
    }
}
