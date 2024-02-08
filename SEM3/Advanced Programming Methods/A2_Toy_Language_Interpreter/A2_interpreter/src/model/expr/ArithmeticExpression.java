package model.expr;

import exceptions.DivisionByZero;
import exceptions.ExpressionEvaluationException;
import exceptions.MyException;
import model.type.IntType;
import model.utils.MyIDictionary;
import model.value.IValue;
import model.value.IntValue;

public class ArithmeticExpression implements IExpression {
    private final IExpression exp1;
    private final IExpression exp2;
    int op; //1-plus, 2-minus, 3-star, 4-divide

    public ArithmeticExpression(int op,IExpression e1,IExpression e2)
    {
        this.op=op;
        this.exp1=e1;
        this.exp2=e2;
    }

    public IExpression getExp1(){return this.exp1;}
    public IExpression getExp2(){return this.exp2;}
    public int getOp(){return this.op;}

    public IValue eval(MyIDictionary<String,IValue> symTbl) throws ExpressionEvaluationException, DivisionByZero {
        IValue val1, val2;
        val1 = exp1.eval(symTbl);
        val2 = exp2.eval(symTbl);

        if (val1.getType().equals(new IntType())) {

            if (val2.getType().equals(new IntType())) {
                IntValue integer1 = (IntValue) val1;
                IntValue integer2 = (IntValue) val2;
                int number1, number2;

                number1 = integer1.getValue();
                number2 = integer2.getValue();
                if (op == 1) return new IntValue(number1 + number2);
                if (op == 2) return new IntValue(number1 - number2);
                if (op == 3) return new IntValue(number1 * number2);
                if (op == 4)
                    if (number2 == 0) throw new DivisionByZero("Division by zero not allowed");
                    else return new IntValue(number1 / number2);
            } else
                throw new ExpressionEvaluationException("Variable 1 must be an integer");
        } else
            throw new ExpressionEvaluationException("Variable 2 must be an integer");

        return null;
    }

    public IExpression deepcopy()
    {
        return new ArithmeticExpression(op,exp1.deepcopy(),exp2.deepcopy());
    }


    @Override
    public String toString() {
        if (this.op == 1) {
            return "ArithmeticExpression{" +
                    "exp1=" + exp1 +
                    " + " +
                    ", exp2=" + exp2 +
                    '}';
        } else if (this.op == 2) {
            return "ArithmeticExpression{" +
                    "exp1=" + exp1 +
                    " - " +
                    ", exp2=" + exp2 +
                    '}';
        } else if (this.op == 3) {
            return "ArithmeticExpression{" +
                    "exp1=" + exp1 +
                    " * " +
                    ", exp2=" + exp2 +
                    '}';
        } else{
            return "ArithmeticExpression{" +
                    "exp1=" + exp1 +
                    " / " +
                    ", exp2=" + exp2 +
                    '}';
        }
    }
}