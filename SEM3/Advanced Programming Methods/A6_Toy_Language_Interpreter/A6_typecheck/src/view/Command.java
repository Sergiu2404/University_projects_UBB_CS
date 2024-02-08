package view;

import exceptions.*;

import java.io.IOException;

public abstract class Command {
    private String key,description;

    public Command(String k, String d)
    {
        this.key=k;
        this.description=d;
    }

    public abstract void execute() throws ExpressionEvaluationException, DivisionByZero, HeapException, IOException, StackException, StatementExecutionException, InterruptedException;

    public String getKey(){return this.key;}
    public String getDescription(){return this.description;}
}
