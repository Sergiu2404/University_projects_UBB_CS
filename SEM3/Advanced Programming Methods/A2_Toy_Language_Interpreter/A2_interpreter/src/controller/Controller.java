package controller;

import exceptions.DivisionByZero;
import exceptions.ExpressionEvaluationException;
import exceptions.StackException;
import exceptions.StatementExecutionException;
import model.programState.ProgramState;
import model.stmt.IStmt;
import model.utils.MyIStack;
import repo.IRepository;

public class Controller {
    private IRepository repositoryPrograms;

    public Controller(IRepository p)
    {
        this.repositoryPrograms=p;
    }

    public boolean isEmpty()
    {
        return this.repositoryPrograms.getCurrentProgram().getExeStack().isEmpty();
    }

    public void addProgram(ProgramState newProgramToAdd)
    {
        repositoryPrograms.addProgram(newProgramToAdd);
    }

    public ProgramState getCurrentProgram()
    {
        return this.repositoryPrograms.getCurrentProgram();
    }

    public ProgramState executeOneStep(ProgramState currentState) throws StatementExecutionException, ExpressionEvaluationException, DivisionByZero, StackException
    {
        MyIStack<IStmt> exeStack = currentState.getExeStack();

        if(exeStack.isEmpty())
            throw new StackException("Stack is empty, no more statements!");

        IStmt currentStmt = exeStack.pop();
        return currentStmt.execute(currentState);
    }

    public void executeAllSteps() throws StatementExecutionException, ExpressionEvaluationException, StackException, DivisionByZero {
        ProgramState currentState = repositoryPrograms.getCurrentProgram();

        while(!currentState.getExeStack().isEmpty())
            executeOneStep(currentState);
    }
}
