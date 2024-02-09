package com.example.a7_gui.model.stmt;

import com.example.a7_gui.exceptions.DataStructureException;
import com.example.a7_gui.exceptions.ArithmeticException;
import com.example.a7_gui.exceptions.EvaluationException;
import com.example.a7_gui.exceptions.ExecutionException;
import com.example.a7_gui.model.expressions.IExpression;
import com.example.a7_gui.model.programState.ProgramState;
import com.example.a7_gui.model.types.BoolType;
import com.example.a7_gui.model.types.Type;
import com.example.a7_gui.model.utils.MyIDictionary;
import com.example.a7_gui.model.utils.MyIStack;
import com.example.a7_gui.model.values.BoolValue;
import com.example.a7_gui.model.values.Value;

public class WhileStatement implements IStatement{
    private final IExpression expression;
    private final IStatement statement;

    public WhileStatement(IExpression expression, IStatement statement) {
        this.expression = expression;
        this.statement = statement;
    }

    @Override
    public ProgramState execute(ProgramState state) throws ExecutionException, EvaluationException, DataStructureException, ArithmeticException {

        Value value = expression.evaluateExpression(state.getSymbolTable(), state.getHeapTable());
        MyIStack<IStatement> stack = state.getExecutionStack();

        if (!value.getType().equals(new BoolType()))
            throw new ExecutionException(String.format("While: %s is not of BoolType", value));
        if (!(value instanceof BoolValue))
            throw new ExecutionException(String.format("While: %s is not a BoolValue", value));
        BoolValue boolValue = (BoolValue) value;
        if (boolValue.getValue()) {

            stack.push(this.deepCopy());
            stack.push(statement);

        }
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws ExecutionException, EvaluationException, DataStructureException {
        Type typeExpr = expression.typeCheck(typeEnv);
        if (typeExpr.equals(new BoolType())) {
            statement.typeCheck(typeEnv.deepCopy());
            return typeEnv;
        } else
            throw new ExecutionException("While: The condition of WHILE does not have the type Bool.");
    }

    @Override
    public IStatement deepCopy() {
        return new WhileStatement(expression.deepCopy(), statement.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("while(%s){ %s }", expression, statement);
    }
}