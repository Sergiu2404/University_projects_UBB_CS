package model.programState;

import model.stmt.IStmt;
import model.utils.ExeStack;
import model.utils.MyIDictionary;
import model.utils.MyIList;
import model.utils.MyIStack;
import model.values.IValue;

import java.io.BufferedReader;
import java.nio.Buffer;
import java.util.List;

public class ProgramState {
    private MyIStack<IStmt> exeStack;
    private MyIDictionary<String, IValue> symTable;
    private MyIList<IValue> output;
    private MyIDictionary<String, BufferedReader> fileTable;
    private IStmt originalProgram;

    public ProgramState(MyIStack<IStmt> s, MyIDictionary<String, IValue> symT, MyIList<IValue> l, MyIDictionary<String, BufferedReader> fileT, IStmt prg)
    {
        this.exeStack=s;
        this.symTable=symT;
        this.output=l;
        this.fileTable=fileT;

        //this.originalProgram=deepCopy(prg);
        this.exeStack.push(prg);
    }

    public MyIStack<IStmt> getExeStack(){return this.exeStack;}

    public void setExeStack(MyIStack<IStmt> st){this.exeStack=st;}
    public MyIDictionary<String,IValue> getSymbolTable(){return this.symTable;}
    public void setSymTable(MyIDictionary<String,IValue> d){this.symTable=d;}
    public MyIList<IValue> getOutput(){return this.output;}
    public void setOutput(MyIList<IValue> l){this.output=l;}
    public MyIDictionary<String,BufferedReader> getFileTable(){return this.fileTable;}
    public void setFileTable(MyIDictionary<String,BufferedReader> f){this.fileTable=f;}
    public IStmt getOriginalProgram(){return this.originalProgram;}
    public void setOriginalProgram(IStmt newPrg){this.originalProgram=newPrg;}

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


    public String programStateToStringFile()
    {
        return "ExeStack: \n" + this.stackToString() + "\nSymbol Table: \n" + this.dictionarySymTblToString() + "\nOutput List: \n" + this.listToString()+"\nFile Table: \n" + this.dictionaryFileTableToString()+"\nEND\n";
    }

    public String toString() {
        return "ProgramState{" +
                "exeStack=" + exeStack +
                ", symTable=" + symTable +
                ", output=" + output +
                ", fileTable=" + fileTable +
                ", originalProgram=" + originalProgram +
                '}';
    }
}
