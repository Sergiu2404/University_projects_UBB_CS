package model.stmt;

import exceptions.ADTException;
import exceptions.DivisionByZero;
import exceptions.ExpressionEvaluationException;
import exceptions.StatementExecutionException;
import model.expressions.IExpression;
import model.programState.ProgramState;
import model.types.StringType;
import model.types.Type;
import model.utils.MyIDictionary;
import model.values.StringValue;
import model.values.Value;

import java.io.BufferedReader;
import java.io.IOException;

public class CloseReadFile implements IStatement{
    private final IExpression expression;

    public CloseReadFile(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementExecutionException, ExpressionEvaluationException, ADTException, DivisionByZero {
        Value value = expression.eval(state.getSymTable(), state.getHeap());
        if (value.getType().equals(new StringType())) {
            StringValue fileName = (StringValue) value;
            MyIDictionary<String, BufferedReader> fileTable = state.getFileTable();
            if (fileTable.isDefined(fileName.getValue())) {
                BufferedReader br = fileTable.lookUp(fileName.getValue());
                try {
                    br.close();
                } catch (IOException e) {
                    throw new StatementExecutionException(String.format("Unexpected error in closing %s", value));
                }
                fileTable.remove(fileName.getValue());
                state.setFileTable(fileTable);
            } else
                throw new StatementExecutionException(String.format("%s is not present in the FileTable", value));
        } else
            throw new StatementExecutionException(String.format("%s does not evaluate to StringValue", expression));
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        if (expression.typeCheck(typeEnv).equals(new StringType()))
            return typeEnv;
        else
            throw new StatementExecutionException("CloseReadFile requires a string expression.");
    }

    @Override
    public IStatement deepCopy() {
        return new CloseReadFile(expression.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("CloseReadFile(%s)", expression.toString());
    }
}