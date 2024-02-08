package model.expressions;

import exceptions.DivisionByZero;
import exceptions.ExpressionEvaluationException;
import model.programState.ProgramState;
import model.types.IntType;
import model.utils.MyIDictionary;
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

    public IValue eval(MyIDictionary<String,IValue> symTbl) throws ExpressionEvaluationException,DivisionByZero
    {
        IValue val1,val2;

        val1=exp1.eval(symTbl);
        val2=exp2.eval(symTbl);

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
