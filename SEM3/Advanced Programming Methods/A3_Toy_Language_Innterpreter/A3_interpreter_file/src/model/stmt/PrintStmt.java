package model.stmt;

import exceptions.DivisionByZero;
import exceptions.ExpressionEvaluationException;
import model.expressions.IExpression;
import model.programState.ProgramState;
import model.utils.MyIList;
import model.values.IValue;

public class PrintStmt implements IStmt{
    private final IExpression expToPrint;

    public PrintStmt(IExpression e)
    {
        this.expToPrint=e;
    }


    public ProgramState execute(ProgramState state) throws ExpressionEvaluationException, DivisionByZero {
        MyIList<IValue> output = state.getOutput();
        output.add(expToPrint.eval(state.getSymbolTable()));

        state.setOutput(output);
        return state;
    }

    public IStmt deepcopy()
    {
        return new PrintStmt(expToPrint.deepcopy());
    }

    @Override
    public String toString() {
        return "PrintStmt{" +
                "exp=" + expToPrint +
                '}';
    }


}
