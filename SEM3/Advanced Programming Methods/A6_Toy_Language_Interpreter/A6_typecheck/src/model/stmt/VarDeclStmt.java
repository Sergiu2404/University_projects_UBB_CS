package model.stmt;

import exceptions.InvalidTypeException;
import exceptions.StatementExecutionException;
import model.programState.ProgramState;
import model.types.BoolType;
import model.types.IType;
import model.types.IntType;
import model.utils.MyIDictionary;
import model.values.BoolValue;
import model.values.IValue;
import model.values.IntValue;
import model.values.StringValue;

public class VarDeclStmt implements IStmt{
    private final String varId;
    private final IType varType;

    public VarDeclStmt(String v, IType t)
    {
        this.varId=v;
        this.varType=t;
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws InvalidTypeException {
        typeEnv.put(varId, varType);
        return typeEnv;
    }

    public ProgramState execute(ProgramState state) throws StatementExecutionException
    {
        MyIDictionary<String, IValue> symTbl= state.getSymbolTable();

        if(symTbl.isDefined(varId))
            throw new StatementExecutionException("Variable "+ varId +" was already declared!");

        symTbl.put(varId, varType.defaultValue());
        state.setSymTable(symTbl);
//        if(varType.equals(new IntType()))
//            symTbl.put(varId,new IntValue(0));
//        else if(varType.equals(new BoolType()))
//            symTbl.put(varId, new BoolValue(false));
//        else
//            symTbl.put(varId,new StringValue(""));
        return state;
    }

    public IStmt deepcopy()
    {
        return new VarDeclStmt(varId,varType);
    }

    @Override
    public String toString() {
        return "VarDeclStmt{" +
                "variable name='" + varId + '\'' +
                ", variable type=" + varType +
                '}';
    }
}
