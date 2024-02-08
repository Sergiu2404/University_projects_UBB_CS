package model.expressions;

import exceptions.DivisionByZero;
import exceptions.ExpressionEvaluationException;
import exceptions.HeapException;
import exceptions.InvalidTypeException;
import model.types.IType;
import model.types.RefType;
import model.utils.MyIDictionary;
import model.utils.MyIHeap;
import model.values.IValue;
import model.values.RefValue;

public class ReadHeapExpression implements IExpression{
    private final IExpression expression;

    public ReadHeapExpression(IExpression e)
    {
        this.expression = e;
    }

    @Override
    public IType typecheck(MyIDictionary<String, IType> typeEnv) throws InvalidTypeException {
        IType type = expression.typecheck(typeEnv);

        if(type instanceof RefType)
        {
            RefType refT = (RefType) type;
            return refT.getInnerType();
        }
        else throw new InvalidTypeException("Type of expression must be Ref");
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> symTbl, MyIHeap heap) throws ExpressionEvaluationException, DivisionByZero, HeapException {

        IValue value = expression.eval(symTbl, heap);
        if (value instanceof RefValue) {
            RefValue refValue = (RefValue) value;
            if (heap.containsKey(refValue.getAddress()))
                return heap.get(refValue.getAddress());
            else
                throw new ExpressionEvaluationException("The address is not defined on the heap!");
        } else
            throw new ExpressionEvaluationException(String.format("%s not of RefType", value));
    }

    @Override
    public IExpression deepcopy() {
        return new ReadHeapExpression(expression.deepcopy());
    }

    @Override
    public String toString() {
        return "ReadHeapExpression{" +
                "expression=" + expression +
                '}';
    }
}
