package model.stmt;

import exceptions.DictionaryException;
import exceptions.DivisionByZero;
import exceptions.ExpressionEvaluationException;
import exceptions.StatementExecutionException;
import model.programState.ProgramState;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface IStmt {
    ProgramState execute(ProgramState state) throws ExpressionEvaluationException, DivisionByZero, StatementExecutionException, IOException, DictionaryException;

    IStmt deepcopy();
}
