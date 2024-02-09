package com.example.a7_gui.model.statements;

import com.example.a7_gui.my_exceptions.DataStructureException;
import com.example.a7_gui.my_exceptions.ArithmeticException;
import com.example.a7_gui.my_exceptions.ExpressionEvaluationException;
import com.example.a7_gui.my_exceptions.StatementExecutionException;
import com.example.a7_gui.model.expressions.IExpression;
import com.example.a7_gui.model.program_state.ProgramState;
import com.example.a7_gui.model.types.StringType;
import com.example.a7_gui.model.types.Type;
import com.example.a7_gui.model.my_data_structures.MyIDictionary;
import com.example.a7_gui.model.my_data_structures.MyIFileTable;
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
    public ProgramState executeStatement(ProgramState state) throws StatementExecutionException, ExpressionEvaluationException, DataStructureException, ArithmeticException {
        Value value = expression.expressionEvaluation(state.getSymbolTable(), state.getHeapTable());

        if (value.getType().equals(new StringType())) {

            StringValue fileName = (StringValue) value;
            MyIFileTable<String, BufferedReader> fileTable = state.getFileTable();

            if (!fileTable.isDefined(fileName.getValue())) {

                BufferedReader bufferedReader;
                try {
                    bufferedReader = new BufferedReader(new FileReader(fileName.getValue()));
                } catch (FileNotFoundException e) {
                    throw new StatementExecutionException(String.format("OpenFIle:%s could not be opened", fileName.getValue()));
                }
                fileTable.put(fileName.getValue(), bufferedReader);
                state.setFileTable(fileTable);
            } else {
                throw new StatementExecutionException(String.format("OpenFIle:%s is already opened", fileName.getValue()));
            }
        } else {
            throw new StatementExecutionException(String.format("OpenFIle: %s does not evaluate to StringType", expression.toString()));
        }
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, DataStructureException {
        if (expression.typeCheck(typeEnv).equals(new StringType()))
            return typeEnv;
        else
            throw new StatementExecutionException("OpenReadFile requires a string expression.");
    }

    @Override
    public IStatement deepCopy() {
        return new OpenReadFile(expression.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("openFile(%s)", expression.toString());
    }
}