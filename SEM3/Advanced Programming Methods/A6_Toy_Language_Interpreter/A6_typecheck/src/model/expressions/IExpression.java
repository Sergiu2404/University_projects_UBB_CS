package model.expressions;

import exceptions.DivisionByZero;
import exceptions.ExpressionEvaluationException;
import exceptions.HeapException;
import exceptions.InvalidTypeException;
import model.programState.ProgramState;
import model.types.IType;
import model.utils.MyIDictionary;
import model.utils.MyIHeap;
import model.values.IValue;

public interface IExpression{

    IType typecheck(MyIDictionary<String, IType> typeEnv) throws InvalidTypeException;
    IValue eval(MyIDictionary<String, IValue> symTbl, MyIHeap heap) throws ExpressionEvaluationException, DivisionByZero, HeapException;
    IExpression deepcopy();
}
