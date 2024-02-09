package com.example.a7_gui.model.stmt;

import com.example.a7_gui.exceptions.DataStructureException;
import com.example.a7_gui.exceptions.ArithmeticException;
import com.example.a7_gui.exceptions.EvaluationException;
import com.example.a7_gui.exceptions.ExecutionException;
import com.example.a7_gui.model.expressions.IExpression;
import com.example.a7_gui.model.programState.ProgramState;
import com.example.a7_gui.model.types.IntType;
import com.example.a7_gui.model.types.StringType;
import com.example.a7_gui.model.types.Type;
import com.example.a7_gui.model.utils.MyIDictionary;
import com.example.a7_gui.model.values.IntValue;
import com.example.a7_gui.model.values.StringValue;
import com.example.a7_gui.model.values.Value;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadFile implements IStatement{
    private final IExpression expression;
    private final String varName;

    public ReadFile(IExpression expression, String varName) {
        this.expression = expression;
        this.varName = varName;
    }
    @Override
    public ProgramState execute(ProgramState state) throws ExecutionException, EvaluationException, DataStructureException, ArithmeticException {

        MyIDictionary<String, Value> symTable = state.getSymbolTable();
        MyIDictionary<String, BufferedReader> fileTable = state.getFileTable();

        if (symTable.isDefined(varName)) {

            Value value = symTable.lookUp(varName);

            if (value.getType().equals(new IntType())) {

                Value fileNameValue = expression.evaluateExpression(symTable, state.getHeapTable());

                if (fileNameValue.getType().equals(new StringType())) {

                    StringValue castValue = (StringValue)fileNameValue;

                    if (fileTable.isDefined(castValue.getValue())) {
                        BufferedReader bufferedReader = fileTable.lookUp(castValue.getValue());
                        try {
                            String line = bufferedReader.readLine();
                            if (line == null)
                                line = "0";
                            symTable.put(varName, new IntValue(Integer.parseInt(line)));
                        } catch (IOException e) {
                            throw new ExecutionException(String.format("ReadFile: Could not read from file %s", castValue));
                        }
                    } else {
                        throw new ExecutionException(String.format("ReadFile: The file table does not contain %s", castValue));
                    }
                } else {
                    throw new ExecutionException(String.format("ReadFile: %s does not evaluate to StringType", value));
                }
            } else {
                throw new ExecutionException(String.format("ReadFile: %s is not of type IntType", value));
            }
        } else {
            throw new ExecutionException(String.format("ReadFile: %s is not present in the symTable.", varName));
        }
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws ExecutionException, EvaluationException, DataStructureException {
        if (expression.typeCheck(typeEnv).equals(new StringType()))
            if (typeEnv.lookUp(varName).equals(new IntType()))
                return typeEnv;
            else
                throw new ExecutionException("ReadFile requires an int as its variable parameter");
        else
            throw new ExecutionException("ReadFile requires a string as es expression parameter");
    }

    @Override
    public IStatement deepCopy() {
        return new ReadFile(expression.deepCopy(), varName);
    }

    @Override
    public String toString() {
        return String.format("ReadF(%s, %s)", expression.toString(), varName);
    }
}