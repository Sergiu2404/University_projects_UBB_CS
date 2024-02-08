package model.stmt;

import exceptions.DivisionByZero;
import exceptions.ExpressionEvaluationException;
import exceptions.MyException;
import model.expr.IExpression;
import model.programState.ProgramState;
import model.type.BoolType;
import model.utils.MyIStack;
import model.value.BoolValue;
import model.value.IValue;

public class IfStmt implements IStmt{
    private IExpression expToEval;
    private IStmt thenStmt, elseStmt;

    public IfStmt(IExpression e, IStmt then, IStmt els)
    {
        this.expToEval=e;
        this.thenStmt=then;
        this.elseStmt=els;
    }

    public ProgramState execute(ProgramState state) throws ExpressionEvaluationException, DivisionByZero {
        MyIStack<IStmt> exeStack= state.getExeStack();
        IValue condToEval=expToEval.eval(state.getSymbolTable());
        BoolValue condVal;

        if(!condToEval.getType().equals(new BoolType()))
            throw new ExpressionEvaluationException("Conditional expression must be boolean!");

        condVal=(BoolValue) condToEval;

        if(condVal.getValue())
            exeStack.push(thenStmt);
        else
            exeStack.push(elseStmt);

        return state;
    }

    public IStmt deepcopy()
    {
        return new IfStmt(expToEval.deepcopy(), thenStmt.deepcopy(), elseStmt.deepcopy());
    }

    @Override
    public String toString() {
        return "IfStmt{" +
                "expToEval=" + expToEval +
                ", thenStmt=" + thenStmt +
                ", elseStmt=" + elseStmt +
                '}';
    }
}
