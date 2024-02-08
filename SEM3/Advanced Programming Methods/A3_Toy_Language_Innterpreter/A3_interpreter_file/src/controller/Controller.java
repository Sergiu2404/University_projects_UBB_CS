package controller;

import exceptions.*;
import model.programState.ProgramState;
import model.stmt.IStmt;
import model.utils.MyIStack;
import repo.IRepository;

import java.io.IOException;

public class Controller {
    private IRepository repositoryPrograms;

    public Controller(IRepository repo)
    {
        this.repositoryPrograms=repo;
    }

    public boolean isEmpty()
    {
        return this.repositoryPrograms.getCurrentProgram().getExeStack().isEmpty();
    }

    public void addProgram(ProgramState newProgram)
    {
        repositoryPrograms.addProgram(newProgram);
    }

    public ProgramState getCurrentProgram()
    {
        return this.repositoryPrograms.getCurrentProgram();
    }

    public ProgramState executeOneStep(ProgramState currentState) throws IOException, StatementExecutionException, ExpressionEvaluationException, DivisionByZero, StackException, DictionaryException {
        MyIStack<IStmt> exeStack = currentState.getExeStack();

        if(exeStack.isEmpty())
            throw new StackException("Stack is empty, no more statements!");

        IStmt currentStmt = exeStack.pop();
        repositoryPrograms.logPrgStateExec();
        return currentStmt.execute(currentState);
    }

    public void executeAllSteps() throws StatementExecutionException, ExpressionEvaluationException, StackException, DivisionByZero, IOException, DictionaryException {
        ProgramState currentState = repositoryPrograms.getCurrentProgram();
        repositoryPrograms.logPrgStateExec();

        while(!currentState.getExeStack().isEmpty()) {
            executeOneStep(currentState);
        }
    }
}
