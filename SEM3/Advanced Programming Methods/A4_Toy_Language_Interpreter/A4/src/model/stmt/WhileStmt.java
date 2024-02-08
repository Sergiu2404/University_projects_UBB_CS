package model.stmt;

import exceptions.*;
import model.expressions.IExpression;
import model.programState.ProgramState;
import model.types.BoolType;
import model.utils.MyIStack;
import model.values.BoolValue;
import model.values.IValue;

import java.io.IOException;

public class WhileStmt implements IStmt {
    private final IExpression condToEval;
    private final IStmt statement;

    public WhileStmt(IExpression e, IStmt s)
    {
        condToEval = e;
        statement = s;
    }


    @Override
    public ProgramState execute(ProgramState state) throws ExpressionEvaluationException, DivisionByZero, StatementExecutionException, IOException, DictionaryException, HeapException {
        IValue val = condToEval.eval(state.getSymbolTable(), state.getHeapTable());
        MyIStack<IStmt> stack = state.getExeStack();

        if(!val.getType().equals(new BoolType()))
            throw new StatementExecutionException("Expression should be of type bool");
        BoolValue boolVal = (BoolValue) val;

        if(boolVal.getValue())
        {
            stack.push(this);
            stack.push(statement);
        }
        return state;
    }

    @Override
    public IStmt deepcopy() {
        return null;
    }

    @Override
    public String toString() {
        return "WhileStmt{" +
                "condToEVal=" + condToEval +
                ", statement=" + statement +
                '}';
    }
}
