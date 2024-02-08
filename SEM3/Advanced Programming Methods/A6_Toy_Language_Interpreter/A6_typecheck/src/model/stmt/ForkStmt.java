package model.stmt;

import exceptions.ExpressionEvaluationException;
import exceptions.InvalidTypeException;
import exceptions.StatementExecutionException;
import model.programState.ProgramState;
import model.types.IType;
import model.utils.ExeStack;
import model.utils.MyIDictionary;
import model.utils.MyIStack;
import model.utils.SymbolTable;
import model.values.IValue;

import java.util.Map;

public class ForkStmt implements IStmt{
    private final IStmt innerStmt;

    public ForkStmt(IStmt stmt)
    {
        innerStmt = stmt;
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws InvalidTypeException, StatementExecutionException {
        innerStmt.typecheck(typeEnv.deepcopy());
        return typeEnv;
    }

    public ProgramState execute(ProgramState state) throws StatementExecutionException, ExpressionEvaluationException{
        MyIStack<IStmt> newStack = new ExeStack<>();
        MyIDictionary<String, IValue> newSymTbl = new SymbolTable<>();

        newStack.push(innerStmt);
        for(Map.Entry<String,IValue> entry: state.getSymbolTable().getContent().entrySet())
        {
            newSymTbl.put(entry.getKey(), entry.getValue().deepcopy());
        }

        return new ProgramState(newStack, newSymTbl, state.getOutput(), state.getFileTable(), state.getHeapTable());
    }

    @Override
    public IStmt deepcopy() {
        return new ForkStmt(innerStmt.deepcopy());
    }

    @Override
    public String toString() {
        return "ForkStmt{" +
                "innerStmt=" + innerStmt +
                '}';
    }
}
