package model.expressions;

import exceptions.DivisionByZero;
import exceptions.ExpressionEvaluationException;
import exceptions.HeapException;
import exceptions.InvalidTypeException;
import model.types.BoolType;
import model.types.IType;
import model.utils.MyIDictionary;
import model.utils.MyIHeap;
import model.values.BoolValue;
import model.values.IValue;

public class LogicalExpression implements IExpression{
    private final IExpression exp1;
    private final IExpression exp2;
    private final String op;

    public LogicalExpression(String op,IExpression e1, IExpression e2)
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

    public String getOp() {
        return op;
    }

    @Override
    public IType typecheck(MyIDictionary<String, IType> typeEnv) throws InvalidTypeException {
        IType type1, type2;
        type1 = exp1.typecheck(typeEnv);
        type2 = exp2.typecheck(typeEnv);

        if(type1.equals(new BoolType()))
        {
            if(type2.equals(new BoolType()))
                return new BoolType();
            else throw new InvalidTypeException("Type of second operand must be bool");
        }
        else throw new InvalidTypeException("Type of first operand must be bool");
    }

    public IValue eval(MyIDictionary<String,IValue> symTbl, MyIHeap heap) throws ExpressionEvaluationException, DivisionByZero, HeapException {
        IValue val1 = exp1.eval(symTbl, heap);
        IValue val2 = exp2.eval(symTbl, heap);

        if(val1.getType().equals(new BoolType()) && val2.getType().equals(new BoolType()))
        {
            BoolValue operand1 = (BoolValue) val1;
            BoolValue operand2 = (BoolValue) val2;

            if(this.op.equals("&&")) //AND
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
    }

    public IExpression deepcopy()
    {
        return new LogicalExpression(op,exp1.deepcopy(),exp2.deepcopy());
    }

    @Override
    public String toString() {
        if (this.op.equals("&&"))
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
