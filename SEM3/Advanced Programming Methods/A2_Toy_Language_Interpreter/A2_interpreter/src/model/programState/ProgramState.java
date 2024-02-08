package model.programState;

import model.stmt.IStmt;
import model.utils.MyIDictionary;
import model.utils.MyIList;
import model.utils.MyIStack;
import model.value.IValue;

public class ProgramState {
    private MyIStack<IStmt> exeStack;
    private MyIDictionary<String, IValue> symTable;
    private MyIList<IValue> output;
    private IStmt originalProgram;

    public ProgramState(MyIStack<IStmt> s, MyIDictionary<String, IValue> symT, MyIList<IValue> l, IStmt prg)
    {
        this.exeStack=s;
        this.symTable=symT;
        this.output=l;

        //this.originalProgram=deepCopy(prg);
        this.exeStack.push(prg);
    }

    public MyIStack<IStmt> getExeStack(){return this.exeStack;}

    public void setExeStack(MyIStack<IStmt> st){this.exeStack=st;}
    public MyIDictionary<String,IValue> getSymbolTable(){return this.symTable;}
    public void setSymTable(MyIDictionary<String,IValue> d){this.symTable=d;}
    public MyIList<IValue> getOutput(){return this.output;}
    public void setOutput(MyIList<IValue> l){this.output=l;}
    public IStmt getOriginalProgram(){return this.originalProgram;}
    public void setOriginalProgram(IStmt newPrg){this.originalProgram=newPrg;}

    @Override
    public String toString() {
        return "ProgramState{" +
                "exeStack=" + exeStack +
                ", symTable=" + symTable +
                ", output=" + output +
                ", originalProgram=" + originalProgram +
                '}';
    }
}
