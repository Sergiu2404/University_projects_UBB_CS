package com.example.a7_gui.model.stmt;

import com.example.a7_gui.exceptions.ADTException;
import com.example.a7_gui.exceptions.DivisionByZero;
import com.example.a7_gui.exceptions.ExpressionEvaluationException;
import com.example.a7_gui.exceptions.StatementExecutionException;
import com.example.a7_gui.model.expressions.IExpression;
import com.example.a7_gui.model.programState.ProgramState;
import com.example.a7_gui.model.types.BoolType;
import com.example.a7_gui.model.types.Type;
import com.example.a7_gui.model.utils.MyIDictionary;

public class DoWhileStatement implements IStatement{
    private final IStatement innerStmt;
    private final IExpression condExpr;

    public DoWhileStatement(IExpression condExpr, IStatement stmt){
        this.innerStmt=stmt;
        this.condExpr=condExpr;
    }
    @Override
    public ProgramState execute(ProgramState state) throws StatementExecutionException, ExpressionEvaluationException, ADTException, DivisionByZero {
        IStatement whileStmt = new CompoundStatement(innerStmt, new WhileStatement(condExpr, innerStmt));
        state.getExeStack().push(whileStmt);

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        Type typeExpr = condExpr.typeCheck(typeEnv);
        if(typeExpr.equals(new BoolType())){
            innerStmt.typeCheck(typeEnv.deepCopy());
            return typeEnv;
        }
        else{
            throw new StatementExecutionException("DoWhile: Condition must be of type bool");
        }
    }

    @Override
    public IStatement deepCopy() {
        return new DoWhileStatement(condExpr.deepCopy(), innerStmt.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("do { %s } while ( %s )", innerStmt, condExpr);
    }
}
