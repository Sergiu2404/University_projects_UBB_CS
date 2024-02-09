package com.example.a7_gui.model.stmt;

import com.example.a7_gui.exceptions.DataStructureException;
import com.example.a7_gui.exceptions.ArithmeticException;
import com.example.a7_gui.exceptions.EvaluationException;
import com.example.a7_gui.exceptions.ExecutionException;
import com.example.a7_gui.model.expressions.IExpression;
import com.example.a7_gui.model.programState.ProgramState;
import com.example.a7_gui.model.types.Type;
import com.example.a7_gui.model.utils.MyIDictionary;
import com.example.a7_gui.model.values.Value;


public class AssignStatement implements IStatement {
    private final String variable;
    private final IExpression expression;

    public AssignStatement(String variable, IExpression expression) {
        this.variable = variable;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws ExecutionException, EvaluationException, DataStructureException, ArithmeticException {
        MyIDictionary<String, Value> symbolTable = state.getSymbolTable();

        if (symbolTable.isDefined(variable)) {
            Value value = expression.evaluateExpression(symbolTable, state.getHeapTable());
            Type typeId = (symbolTable.lookUp(variable)).getType();
            if (value.getType().equals(typeId)) {
                symbolTable.put(variable, value);
            } else {
                throw new ExecutionException("Assign: Declared type of variable " + variable + " and type of the assigned expression do not match.");
            }
        } else {
            throw new ExecutionException("Assign: The used variable " + variable + " was not declared before.");
        }
        state.setSymbolTable(symbolTable);
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws ExecutionException, EvaluationException, DataStructureException {
        Type typeVar = typeEnv.lookUp(variable);
        Type typeExpr = expression.typeCheck(typeEnv);
        if (typeVar.equals(typeExpr))
            return typeEnv;
        else
            throw new ExecutionException("Assignment: right hand side and left hand side have different types.");
    }

    @Override
    public IStatement deepCopy() {
        return new AssignStatement(variable, expression.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("%s = %s", variable, expression.toString());
    }
}