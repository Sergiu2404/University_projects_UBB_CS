package model.stmt;

import exceptions.ADTException;
import exceptions.DivisionByZero;
import exceptions.ExpressionEvaluationException;
import exceptions.StatementExecutionException;
import model.programState.ProgramState;
import model.types.Type;
import model.utils.MyIDictionary;


public interface IStatement {
    ProgramState execute(ProgramState state) throws StatementExecutionException, ExpressionEvaluationException, ADTException, DivisionByZero;
    MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException;
    IStatement deepCopy();
}
