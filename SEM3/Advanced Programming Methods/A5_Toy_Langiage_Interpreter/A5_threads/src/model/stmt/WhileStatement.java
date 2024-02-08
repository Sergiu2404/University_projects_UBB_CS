package model.stmt;

import exceptions.ADTException;
import exceptions.DivisionByZero;
import exceptions.ExpressionEvaluationException;
import exceptions.StatementExecutionException;
import model.expressions.IExpression;
import model.programState.ProgramState;
import model.types.BoolType;
import model.types.Type;
import model.utils.MyIDictionary;
import model.utils.MyIStack;
import model.values.BoolValue;
import model.values.Value;

public class WhileStatement implements IStatement{
    private final IExpression expression;
    private final IStatement statement;

    public WhileStatement(IExpression expression, IStatement statement) {
        this.expression = expression;
        this.statement = statement;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementExecutionException, ExpressionEvaluationException, ADTException, DivisionByZero {
        Value value = expression.eval(state.getSymTable(), state.getHeap());
        MyIStack<IStatement> stack = state.getExeStack();
        if (!value.getType().equals(new BoolType()))
            throw new StatementExecutionException(String.format("%s is not of BoolType", value));
        if (!(value instanceof BoolValue))
            throw new StatementExecutionException(String.format("%s is not a BoolValue", value));
        BoolValue boolValue = (BoolValue) value;
        if (boolValue.getValue()) {
            stack.push(this.deepCopy());
            stack.push(statement);
        }
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        Type typeExpr = expression.typeCheck(typeEnv);
        if (typeExpr.equals(new BoolType())) {
            statement.typeCheck(typeEnv.deepCopy());
            return typeEnv;
        } else
            throw new StatementExecutionException("The condition of WHILE does not have the type Bool.");
    }

    @Override
    public IStatement deepCopy() {
        return new WhileStatement(expression.deepCopy(), statement.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("while(%s){%s}", expression, statement);
    }
}