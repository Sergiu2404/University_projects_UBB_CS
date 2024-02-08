package com.example.a7_gui.model.stmt;

import com.example.a7_gui.exceptions.ADTException;
import com.example.a7_gui.exceptions.DivisionByZero;
import com.example.a7_gui.exceptions.ExpressionEvaluationException;
import com.example.a7_gui.exceptions.StatementExecutionException;
import com.example.a7_gui.model.expressions.IExpression;
import com.example.a7_gui.model.expressions.OppositeExpression;
import com.example.a7_gui.model.programState.ProgramState;
import com.example.a7_gui.model.types.BoolType;
import com.example.a7_gui.model.types.Type;
import com.example.a7_gui.model.utils.MyIDictionary;
import com.example.a7_gui.model.utils.MyIStack;

public class RepeatUntilStatement implements IStatement{
    private final IStatement innerStmt;
    private final IExpression condExpr;

    public RepeatUntilStatement(IExpression condExpr, IStatement innerStmt)
    {
        this.condExpr=condExpr;
        this.innerStmt=innerStmt;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementExecutionException, ExpressionEvaluationException, ADTException, DivisionByZero {
        MyIStack<IStatement> exeStack = state.getExeStack();
        IStatement whileStmt = new CompoundStatement(innerStmt, new WhileStatement(new OppositeExpression(condExpr), innerStmt));

        exeStack.push(whileStmt);
        state.setExeStack(exeStack);

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        Type type = condExpr.typeCheck(typeEnv);
        if(type.equals(new BoolType()))
        {
            innerStmt.typeCheck(typeEnv.deepCopy());
            return typeEnv;
        }
        else throw new StatementExecutionException("Expression from >>repeat until<< must be of type bool");
    }

    @Override
    public IStatement deepCopy() {
        return new RepeatUntilStatement(condExpr.deepCopy(), innerStmt.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("repeat(%s) until (%s)", innerStmt, condExpr);
    }
}
