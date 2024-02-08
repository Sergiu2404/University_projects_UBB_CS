package model.stmt;

import exceptions.*;
import model.programState.ProgramState;
import model.types.IType;
import model.utils.MyIDictionary;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface IStmt {
    MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws InvalidTypeException, StatementExecutionException;
    ProgramState execute(ProgramState state) throws ExpressionEvaluationException, DivisionByZero, StatementExecutionException, IOException, HeapException;

    IStmt deepcopy();
}
