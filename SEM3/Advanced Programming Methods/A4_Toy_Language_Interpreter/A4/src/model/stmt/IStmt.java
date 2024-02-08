package model.stmt;

import exceptions.*;
import model.programState.ProgramState;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface IStmt {
    ProgramState execute(ProgramState state) throws ExpressionEvaluationException, DivisionByZero, StatementExecutionException, IOException, DictionaryException, HeapException;

    IStmt deepcopy();
}
