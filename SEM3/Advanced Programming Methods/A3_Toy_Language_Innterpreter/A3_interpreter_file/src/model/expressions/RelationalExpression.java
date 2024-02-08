package model.expressions;

import exceptions.DivisionByZero;
import exceptions.ExpressionEvaluationException;
import model.types.IntType;
import model.utils.MyIDictionary;
import model.values.BoolValue;
import model.values.IValue;
import model.values.IntValue;

public class RelationalExpression implements IExpression{
    private final IExpression exp1;
    private final IExpression exp2;
    private final String op;

    RelationalExpression(String op,IExpression e1,IExpression e2)
    {
        this.op=op;
        this.exp1=e1;
        this.exp2=e2;
    }

    public IValue eval(MyIDictionary<String,IValue> symTbl) throws ExpressionEvaluationException, DivisionByZero
    {
        IValue val1,val2;
        val1=exp1.eval(symTbl);
        val2=exp2.eval(symTbl);

        if(val1.getType().equals(new IntType()) && val2.getType().equals(new IntType()))
        {
            IntValue int1 = (IntValue) val1;
            IntValue int2 = (IntValue) val2;

            int number1,number2;
            number1=int1.getValue();
            number2=int2.getValue();

            return switch (this.op) {
                case "<" -> new BoolValue(number1 < number2);
                case "<=" -> new BoolValue(number1 <= number2);
                case "==" -> new BoolValue(number1 == number2);
                case "!=" -> new BoolValue(number1 != number2);
                case ">" -> new BoolValue(number1 > number2);
                case ">=" -> new BoolValue(number1 >= number2);
                default -> throw new ExpressionEvaluationException("Operation does not exist");
            };
        }
        else throw new ExpressionEvaluationException("Both operands must be of type int");
    }

    public IExpression deepcopy()
    {
        return new RelationalExpression(op,exp1.deepcopy(),exp2.deepcopy());
    }

    @Override
    public String toString() {
        return "RelationalExpression{" + "exp1=" + exp1 + " " + op + " exp2=" + exp2 + '\'' + '}';
    }
}
