package model.expressions;

import exceptions.DivisionByZero;
import exceptions.ExpressionEvaluationException;
import exceptions.HeapException;
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
        return null;
    }

    @Override
    public String toString() {
        return "ReadHeapExpression{" +
                "expression=" + expression +
                '}';
    }
}
