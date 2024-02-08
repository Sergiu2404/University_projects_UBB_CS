package model.expressions;

import exceptions.DivisionByZero;
import exceptions.ExpressionEvaluationException;
import exceptions.HeapException;
import exceptions.InvalidTypeException;
import model.programState.ProgramState;
import model.types.IType;
import model.types.IntType;
import model.utils.MyIDictionary;
import model.utils.MyIHeap;
import model.values.IValue;
import model.values.IntValue;

public class ArithmeticExpression implements IExpression {
    private final IExpression exp1;
    private final IExpression exp2;
    private final String op;

    public ArithmeticExpression(String o, IExpression e1, IExpression e2)
    {
        this.op=o;
        this.exp1=e1;
        this.exp2=e2;
    }

    @Override
    public IType typecheck(MyIDictionary<String, IType> typeEnv) throws InvalidTypeException {
        IType type1, type2;
        type1 = exp1.typecheck(typeEnv);
        type2 = exp2.typecheck(typeEnv);

        if(type1.equals(new IntType()))
        {
            if(type2.equals(new IntType()))
                return new IntType();
            else
                throw new InvalidTypeException("Type of second operand must integer");
        }
        else throw new InvalidTypeException("Type of first operand must be integer");
    }

    public IValue eval(MyIDictionary<String,IValue> symTbl, MyIHeap heap) throws ExpressionEvaluationException, DivisionByZero, HeapException {
        IValue val1,val2;

        val1=exp1.eval(symTbl, heap);
        val2=exp2.eval(symTbl, heap);

        if(val1.getType().equals(new IntType()))
        {
            if(val2.getType().equals(new IntType()))
            {
                IntValue integer1 = (IntValue) val1;
                IntValue integer2 = (IntValue) val2;
                int nr1,nr2;
                nr1=integer1.getValue();
                nr2= integer2.getValue();

                switch(this.op)
                {
                    case "+":
                        return new IntValue(nr1+nr2);
                    case "-":
                        return new IntValue(nr1-nr2);

                    case "*":
                        return new IntValue(nr1*nr2);

                    case "/":
                        if(nr2 == 0)
                            throw new DivisionByZero("Division by zero not possible");
                        return new IntValue(nr1/nr2);



                }
            }
            else throw new ExpressionEvaluationException("variable 1 must be an integer");
        }
        else throw new ExpressionEvaluationException("variable 2 must be an integer");

        return null;
    }

    public IExpression deepcopy()
    {
        return new ArithmeticExpression(op, exp1.deepcopy(),exp2.deepcopy());
    }

    public String toString()
    {
        return switch (this.op) {
            case "+" -> "ArithmeticExpression{" +
                    "exp1=" + exp1 +
                    " + " +
                    ", exp2=" + exp2 +
                    '}';
            case "-" -> "ArithmeticExpression{" +
                    "exp1=" + exp1 +
                    " - " +
                    ", exp2=" + exp2 +
                    '}';
            case "*" -> "ArithmeticExpression{" +
                    "exp1=" + exp1 +
                    " * " +
                    ", exp2=" + exp2 +
                    '}';
            default -> "ArithmeticExpression{" +
                    "exp1=" + exp1 +
                    " / " +
                    ", exp2=" + exp2 +
                    '}';
        };
    }
}
