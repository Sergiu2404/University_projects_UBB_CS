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

public class NewStmt implements IStmt{
    private final String varName;
    private final IExpression expr;

    public NewStmt(String v, IExpression e)
    {
        varName = v;
        expr = e;
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws InvalidTypeException {
        IType varType = typeEnv.lookup(varName);
        IType expType = expr.typecheck(typeEnv);

        if(varType.equals(new RefType(expType)))
            return typeEnv;
        else throw new InvalidTypeException("Type of the variable must be Ref");
    }

    public ProgramState execute(ProgramState state) throws StatementExecutionException, ExpressionEvaluationException, DivisionByZero, HeapException {
        MyIDictionary<String, IValue> symTbl = state.getSymbolTable();
        MyIHeap heapTbl = state.getHeapTable();

        if(symTbl.isDefined(varName))
        {
            IValue varVal = symTbl.lookup(varName);
            if((varVal.getType() instanceof RefType))
            {
                IValue evaluatedExpVal = expr.eval(symTbl, heapTbl);
                IType locationType = ((RefValue) varVal).getLocationType();
                if(locationType.equals(evaluatedExpVal.getType()))
                {
                    int newPos = heapTbl.add(evaluatedExpVal);
                    symTbl.put(varName, new RefValue(newPos, locationType));
                    state.setSymTable(symTbl);
                    state.setHeapTable(heapTbl);
                }else throw new StatementExecutionException(String.format("%s not of type %s", varName, evaluatedExpVal));
            }else throw new StatementExecutionException(String.format("%s should be of type ref", varVal));
        }else throw new StatementExecutionException(String.format("variable %s not of wanted type", varName));

        return state;
    }

    public IStmt deepcopy(){return new NewStmt(varName, expr.deepcopy());}

    @Override
    public String toString() {
        return "NewStmt{" +
                "varName='" + varName + '\'' +
                ", expr=" + expr +
                '}';
    }
}
