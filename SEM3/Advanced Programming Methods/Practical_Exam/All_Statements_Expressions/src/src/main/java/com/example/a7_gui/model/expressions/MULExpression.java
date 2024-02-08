package com.example.a7_gui.model.expressions;

import com.example.a7_gui.exceptions.ADTException;
import com.example.a7_gui.exceptions.DivisionByZero;
import com.example.a7_gui.exceptions.ExpressionEvaluationException;
import com.example.a7_gui.model.types.IntType;
import com.example.a7_gui.model.types.Type;
import com.example.a7_gui.model.utils.MyIDictionary;
import com.example.a7_gui.model.utils.MyIHeap;
import com.example.a7_gui.model.values.Value;

public class MULExpression implements IExpression{
    private final IExpression expr1;
    private final IExpression expr2;

    public MULExpression(IExpression e1, IExpression e2)
    {
        this.expr1=e1;
        this.expr2=e2;
    }
    @Override
    public Type typeCheck(MyIDictionary<String, Type> typeEnv) throws ExpressionEvaluationException, ADTException {
        Type type1 = expr1.typeCheck(typeEnv);
        Type type2 = expr2.typeCheck(typeEnv);

        if(type1.equals(new IntType()) && type2.equals(new IntType()))
            return new IntType();
        else throw new ExpressionEvaluationException("Expression should be int");
    }

    @Override
    public Value evaluate(MyIDictionary<String, Value> table, MyIHeap heap) throws ExpressionEvaluationException, ADTException, DivisionByZero {
        IExpression arithmeticExpr =
                new ArithmeticExpression('-',
                    new ArithmeticExpression('*', expr1, expr2),
                    new ArithmeticExpression('+', expr1, expr2)
                );

        return arithmeticExpr.evaluate(table, heap);
    }

    @Override
    public IExpression deepCopy() {
        return new MULExpression(expr1.deepCopy(), expr2.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("MUL(%s, %s)", expr1, expr2);
    }
}
