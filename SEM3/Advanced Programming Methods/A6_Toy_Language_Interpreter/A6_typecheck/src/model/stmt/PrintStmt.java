package model.stmt;

import exceptions.DivisionByZero;
import exceptions.ExpressionEvaluationException;
import exceptions.HeapException;
import exceptions.InvalidTypeException;
import model.expressions.IExpression;
import model.programState.ProgramState;
import model.types.IType;
import model.utils.MyIDictionary;
import model.utils.MyIList;
import model.values.IValue;

public class PrintStmt implements IStmt{
    private final IExpression expToPrint;

    public PrintStmt(IExpression e)
    {
        this.expToPrint=e;
    }


    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws InvalidTypeException {
        expToPrint.typecheck(typeEnv);
        return typeEnv;
    }

    public ProgramState execute(ProgramState state) throws ExpressionEvaluationException, DivisionByZero, HeapException {
        MyIList<IValue> output = state.getOutput();
        output.add(expToPrint.eval(state.getSymbolTable(), state.getHeapTable()));

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
