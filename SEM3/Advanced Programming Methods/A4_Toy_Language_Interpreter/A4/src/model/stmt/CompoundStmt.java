package model.stmt;

import model.programState.ProgramState;
import model.utils.MyIStack;

public class CompoundStmt implements IStmt{
    private final IStmt stmt1;
    private final IStmt stmt2;

    public CompoundStmt(IStmt s1, IStmt s2)
    {
        this.stmt1=s1;
        this.stmt2=s2;
    }

    public ProgramState execute(ProgramState state)
    {
        MyIStack<IStmt> exeStack=state.getExeStack();
        exeStack.push(stmt2);
        exeStack.push(stmt1);

        state.setExeStack(exeStack);
        return state;
    }

    public IStmt deepcopy(){return new CompoundStmt(stmt1,stmt2);}

    @Override
    public String toString() {
        return "CompoundStmt{" +
                "stmt1=" + stmt1 +
                ", stmt2=" + stmt2 +
                '}';
    }
}
