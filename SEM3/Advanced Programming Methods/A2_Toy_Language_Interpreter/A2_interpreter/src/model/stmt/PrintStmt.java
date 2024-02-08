package model.stmt;

import exceptions.DivisionByZero;
import exceptions.ExpressionEvaluationException;
import exceptions.MyException;
import model.expr.IExpression;
import model.programState.ProgramState;
import model.utils.MyIList;
import model.value.IValue;

public class PrintStmt implements IStmt{
    private IExpression exp;

    public PrintStmt(IExpression e)
    {
        this.exp=e;
    }

    public IExpression getExpression(){return this.exp;}

    @Override
    public ProgramState execute(ProgramState state) throws ExpressionEvaluationException, DivisionByZero {
        MyIList<IValue> output = state.getOutput();
        output.add(exp.eval(state.getSymbolTable()));

        return state;
    }

    public IStmt deepcopy()
    {
        return new PrintStmt(exp.deepcopy());
    }

    @Override
    public String toString() {
        return "PrintStmt{" +
                "exp=" + exp +
                '}';
    }
}
