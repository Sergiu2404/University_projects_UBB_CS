package model.stmt;

import exceptions.ExpressionEvaluationException;
import exceptions.MyException;
import model.programState.ProgramState;
import model.utils.MyIStack;

public class CompStmt implements IStmt{
    private IStmt stmt1, stmt2;

    public CompStmt(IStmt s1, IStmt s2)
    {
        this.stmt1=s1;
        this.stmt2=s2;
    }

    public IStmt getStmt1(){return this.stmt1;}
    public IStmt getStmt2(){return this.stmt2;}

    @Override
    public ProgramState execute(ProgramState state) throws ExpressionEvaluationException
    {
        MyIStack<IStmt> exeStack=state.getExeStack();
        exeStack.push(stmt2);
        exeStack.push(stmt1);

        return state;
    }

    public IStmt deepcopy()
    {
        return new CompStmt(stmt1.deepcopy(), stmt2.deepcopy());
    }

    @Override
    public String toString() {
        return "CompStmt{" +
                "stmt1=" + stmt1 +
                ", stmt2=" + stmt2 +
                '}';
    }

}
