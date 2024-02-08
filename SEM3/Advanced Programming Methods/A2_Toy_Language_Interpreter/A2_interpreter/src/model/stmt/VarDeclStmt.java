package model.stmt;

import exceptions.StatementExecutionException;
import model.programState.ProgramState;
import model.type.BoolType;
import model.type.IType;
import model.type.IntType;
import model.utils.MyIDictionary;
import model.value.BoolValue;
import model.value.IValue;
import model.value.IntValue;

public class VarDeclStmt implements IStmt{
    private String varName;
    private IType varType;

    public VarDeclStmt(String n, IType t)
    {
        this.varName=n;
        this.varType=t;
    }

    public String getVarName(){return this.varName;}
    public IType getVarType(){return this.varType;}

    public ProgramState execute(ProgramState state) throws StatementExecutionException
    {
        MyIDictionary<String, IValue> symTbl= state.getSymbolTable();

        if(symTbl.isDefined(varName))
            throw new StatementExecutionException("Variable "+ varName +" was already declared!");

        if(varType.equals(new IntType()))
            symTbl.put(varName,new IntValue(0));
        else
            symTbl.put(varName, new BoolValue(false));

        return state;
    }

    public IStmt deepcopy()
    {
        return new VarDeclStmt(varName,varType);
    }

    @Override
    public String toString() {
        return "VarDeclStmt{" +
                "variable name='" + varName + '\'' +
                ", variable type=" + varType +
                '}';
    }

}
