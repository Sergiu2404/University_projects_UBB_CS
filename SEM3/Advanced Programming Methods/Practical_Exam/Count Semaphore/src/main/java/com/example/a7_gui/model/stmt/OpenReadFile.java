package com.example.a7_gui.model.stmt;

import com.example.a7_gui.exceptions.DataStructureException;
import com.example.a7_gui.exceptions.ArithmeticException;
import com.example.a7_gui.exceptions.EvaluationException;
import com.example.a7_gui.exceptions.ExecutionException;
import com.example.a7_gui.model.expressions.IExpression;
import com.example.a7_gui.model.programState.ProgramState;
import com.example.a7_gui.model.types.StringType;
import com.example.a7_gui.model.types.Type;
import com.example.a7_gui.model.utils.MyIDictionary;
import com.example.a7_gui.model.values.StringValue;
import com.example.a7_gui.model.values.Value;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class OpenReadFile implements IStatement{
    private final IExpression expression;

    public OpenReadFile(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws ExecutionException, EvaluationException, DataStructureException, ArithmeticException {
        Value value = expression.evaluateExpression(state.getSymbolTable(), state.getHeapTable());

        if (value.getType().equals(new StringType())) {

            StringValue fileName = (StringValue) value;
            MyIDictionary<String, BufferedReader> fileTable = state.getFileTable();

            if (!fileTable.isDefined(fileName.getValue())) {

                BufferedReader bufferedReader;
                try {
                    bufferedReader = new BufferedReader(new FileReader(fileName.getValue()));
                } catch (FileNotFoundException e) {
                    throw new ExecutionException(String.format("OpenFile: %s could not be opened", fileName.getValue()));
                }
                fileTable.put(fileName.getValue(), bufferedReader);
                state.setFileTable(fileTable);

            } else {
                throw new ExecutionException(String.format("OpenFile: %s is already opened", fileName.getValue()));
            }
        } else {
            throw new ExecutionException(String.format("OpenFile: %s does not evaluate to StringType", expression.toString()));
        }
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws ExecutionException, EvaluationException, DataStructureException {
        if (expression.typeCheck(typeEnv).equals(new StringType()))
            return typeEnv;
        else
            throw new ExecutionException("OpenReadFile requires a string expression.");
    }

    @Override
    public IStatement deepCopy() {
        return new OpenReadFile(expression.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("OpenReadFile(%s)", expression.toString());
    }
}