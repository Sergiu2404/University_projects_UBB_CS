package model.expressions;

import exceptions.DivisionByZero;
import exceptions.ExpressionEvaluationException;
import exceptions.HeapException;
import exceptions.InvalidTypeException;
import model.types.BoolType;
import model.types.IType;
import model.types.IntType;
import model.utils.MyIDictionary;
import model.utils.MyIHeap;
import model.values.BoolValue;
import model.values.IValue;
import model.values.IntValue;

public class RelationalExpression implements IExpression{
    private final IExpression exp1;
    private final IExpression exp2;
    private final String op;

    public RelationalExpression(String op,IExpression e1,IExpression e2)
    {
        this.op=op;
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
            else throw new InvalidTypeException("Second operand must be integer");
        }
        else throw new InvalidTypeException("First operand must be integer");

    }

    public IValue eval(MyIDictionary<String,IValue> symTbl, MyIHeap heap) throws ExpressionEvaluationException, DivisionByZero, HeapException {
        IValue val1,val2;
        val1=exp1.eval(symTbl, heap);
        val2=exp2.eval(symTbl, heap);

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
