package model.stmt;

import exceptions.InvalidTypeException;
import model.programState.ProgramState;
import model.types.IType;
import model.utils.MyIDictionary;

public class NOPStmt implements IStmt{
    public NOPStmt() {}

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws InvalidTypeException {
        return typeEnv;
    }

    public ProgramState execute(ProgramState state)
    {
        return state;
    }

    public NOPStmt deepcopy()
    {
        return new NOPStmt();
    }

    public String toString()
    {
        return "No operation";
    }
}
