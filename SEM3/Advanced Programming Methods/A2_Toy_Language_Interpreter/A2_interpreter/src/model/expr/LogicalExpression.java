package model.expr;

import exceptions.DivisionByZero;
import exceptions.ExpressionEvaluationException;
import exceptions.MyException;
import model.type.BoolType;
import model.utils.MyIDictionary;
import model.value.BoolValue;
import model.value.IValue;
import model.value.IntValue;

public class LogicalExpression implements IExpression {

    private final IExpression exp1;
    private final IExpression exp2;
    private final int op; //1-AND | 2-OR

    public LogicalExpression(int op,IExpression e1, IExpression e2)
    {
        this.exp1=e1;
        this.exp2=e2;
        this.op=op;
    }

    public IExpression getExp1() {
        return exp1;
    }

    public IExpression getExp2() {
        return exp2;
    }

    public int getOp() {
        return op;
    }

    public IValue eval(MyIDictionary<String,IValue> symTbl) throws ExpressionEvaluationException, DivisionByZero
    {
        IValue val1 = exp1.eval(symTbl);
        IValue val2 = exp2.eval(symTbl);

        if(val1.getType().equals(new BoolType()) && val2.getType().equals(new BoolType()))
        {
            BoolValue operand1 = (BoolValue) val1;
            BoolValue operand2 = (BoolValue) val2;

            if(this.op == 1) //AND
            {
                return new BoolValue(operand1.getValue() && operand2.getValue());
            }
            else //OR
            {
                return new BoolValue(operand1.getValue() || operand2.getValue());
            }

        }
        else if (!val1.getType().equals(new BoolType())){
            throw new ExpressionEvaluationException("Variable 1 must be boolean!");
        }
        else {
            throw new ExpressionEvaluationException("Variable 2 must be boolean!");
        }

        //return null;
    }

    public IExpression deepcopy()
    {
        return new LogicalExpression(op,exp1.deepcopy(),exp2.deepcopy());
    }

    @Override
    public String toString() {
        if (this.op == 1)
            return "LogicalExpression{" +
                    "e1=" + exp1 +
                    " && " +
                    " e2=" + exp2 +
                    '}';
        else
            return "LogicalExpression{" +
                    "e1=" + exp1 +
                    " || " +
                    " e2=" + exp2 +
                    '}';
    }
}
