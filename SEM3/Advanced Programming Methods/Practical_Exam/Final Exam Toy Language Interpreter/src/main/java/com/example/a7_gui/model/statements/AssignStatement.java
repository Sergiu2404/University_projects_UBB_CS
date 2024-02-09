package com.example.a7_gui.model.statements;

import com.example.a7_gui.my_exceptions.DataStructureException;
import com.example.a7_gui.my_exceptions.ArithmeticException;
import com.example.a7_gui.my_exceptions.ExpressionEvaluationException;
import com.example.a7_gui.my_exceptions.StatementExecutionException;
import com.example.a7_gui.model.expressions.IExpression;
import com.example.a7_gui.model.program_state.ProgramState;
import com.example.a7_gui.model.types.Type;
import com.example.a7_gui.model.my_data_structures.MyIDictionary;
import com.example.a7_gui.model.my_data_structures.MyISymbolTable;
import com.example.a7_gui.model.values.Value;


public class AssignStatement implements IStatement {
    private final String key;
    private final IExpression expression;

    public AssignStatement(String key, IExpression expression) {
        this.key = key;
        this.expression = expression;
    }

    @Override
    public ProgramState executeStatement(ProgramState state) throws StatementExecutionException, ExpressionEvaluationException, DataStructureException, ArithmeticException {
        MyISymbolTable<String, Value> symbolTable = state.getSymbolTable();

        if (symbolTable.isDefined(key)) {
            Value value = expression.expressionEvaluation(symbolTable, state.getHeapTable());
            Type typeId = (symbolTable.lookUp(key)).getType();
            if (value.getType().equals(typeId)) {
                symbolTable.put(key, value);
            } else {
                throw new StatementExecutionException("Declared type of variable " + key + " and type of the assigned expression do not match.");
            }
        } else {
            throw new StatementExecutionException("The used variable " + key + " was not declared before.");
        }
        state.setSymbolTable(symbolTable);
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, DataStructureException {
        Type typeVar = typeEnv.lookUp(key);
        Type typeExpr = expression.typeCheck(typeEnv);
        if (typeVar.equals(typeExpr))
            return typeEnv;
        else
            throw new StatementExecutionException("Assignment: right hand side and left hand side have different types.");
    }

    @Override
    public IStatement deepCopy() {
        return new AssignStatement(key, expression.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("%s = %s", key, expression.toString());
    }
}