package model.stmt;

import model.programState.ProgramState;

public class NOPStmt implements IStmt{

    public NOPStmt(){}

    public ProgramState execute(ProgramState state) {
        //nothing
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
