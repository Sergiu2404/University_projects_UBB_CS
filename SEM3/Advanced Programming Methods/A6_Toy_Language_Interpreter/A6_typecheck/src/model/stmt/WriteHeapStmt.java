package model.stmt;

import exceptions.*;
import model.expressions.IExpression;
import model.programState.ProgramState;
import model.types.IType;
import model.types.RefType;
import model.utils.MyIDictionary;
import model.utils.MyIHeap;
import model.values.IValue;
import model.values.RefValue;

public class WriteHeapStmt implements IStmt{
    private final String varName;
    private final IExpression expr;

    public WriteHeapStmt(String v, IExpression e)
    {
        this.varName = v;
        this.expr = e;
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws InvalidTypeException {
        if(typeEnv.lookup(varName).equals(new RefType(expr.typecheck(typeEnv))))
            return typeEnv;
        else throw new InvalidTypeException("Variable must be Ref type");
    }

    public ProgramState execute(ProgramState state) throws StatementExecutionException, ExpressionEvaluationException, DivisionByZero, HeapException {
        MyIDictionary<String, IValue> symTbl = state.getSymbolTable();
        MyIHeap heapTbl = state.getHeapTable();

        if(!symTbl.isDefined(varName))
            throw new StatementExecutionException(String.format("%s not defined in symbol table", varName));

        IValue value = symTbl.lookup(varName);
        if(!(value instanceof RefValue))
            throw new StatementExecutionException(String.format("%s should be of type ref", value));

        RefValue refValue = (RefValue) value;
        IValue evaluatedExpValue = expr.eval(symTbl, heapTbl);

        if(!(evaluatedExpValue.getType().equals(refValue.getLocationType())))
            throw new StatementExecutionException(String.format("evaluated expression should be of type %s", evaluatedExpValue, refValue.getLocationType()));

        heapTbl.update(refValue.getAddress(), evaluatedExpValue);
        state.setHeapTable(heapTbl);
        return state;
    }

    public IStmt deepcopy()
    {
        return new WriteHeapStmt(varName, expr.deepcopy());
    }

    @Override
    public String toString() {
        return "WriteHeapStmt{" +
                "varName='" + varName + '\'' +
                ", expr=" + expr +
                '}';
    }
}
