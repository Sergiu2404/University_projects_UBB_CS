package model.stmt;

import exceptions.DivisionByZero;
import exceptions.ExpressionEvaluationException;
import exceptions.MyException;
import exceptions.StatementExecutionException;
import model.programState.ProgramState;

public interface IStmt {
    ProgramState execute(ProgramState state) throws ExpressionEvaluationException, StatementExecutionException, DivisionByZero;
    IStmt deepcopy();
}
