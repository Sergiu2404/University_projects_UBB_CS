package model.stmt;

import exceptions.*;
import model.expressions.IExpression;
import model.programState.ProgramState;
import model.types.BoolType;
import model.types.IType;
import model.utils.MyIDictionary;
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
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws InvalidTypeException, StatementExecutionException {
        IType expType = condToEval.typecheck(typeEnv);

        if(expType.equals(new BoolType()))
        {
            statement.typecheck(typeEnv.deepcopy());
            return typeEnv;
        }
        else throw new InvalidTypeException("Expression must have type Bool");
    }

    @Override
    public ProgramState execute(ProgramState state) throws ExpressionEvaluationException, DivisionByZero, StatementExecutionException, IOException, HeapException {
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
        return new WhileStmt(condToEval.deepcopy(), statement.deepcopy());
    }

    @Override
    public String toString() {
        return "WhileStmt{" +
                "condToEVal=" + condToEval +
                ", statement=" + statement +
                '}';
    }
}
