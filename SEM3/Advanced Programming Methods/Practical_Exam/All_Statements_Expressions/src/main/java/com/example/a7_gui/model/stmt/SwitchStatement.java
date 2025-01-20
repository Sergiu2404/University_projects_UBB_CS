package com.example.a7_gui.model.stmt;

import com.example.a7_gui.exceptions.ADTException;
import com.example.a7_gui.exceptions.DivisionByZero;
import com.example.a7_gui.exceptions.ExpressionEvaluationException;
import com.example.a7_gui.exceptions.StatementExecutionException;
import com.example.a7_gui.model.expressions.IExpression;
import com.example.a7_gui.model.expressions.RelationalExpression;
import com.example.a7_gui.model.programState.ProgramState;
import com.example.a7_gui.model.types.Type;
import com.example.a7_gui.model.utils.MyIDictionary;
import com.example.a7_gui.model.utils.MyIStack;

public class SwitchStatement implements IStatement{
    private final IExpression mainExpression;
    private final IExpression case1;
    private final IStatement stmtCase1;
    private final IExpression case2;
    private final IStatement stmtCase2;
    private final IStatement defaultStmt;

    public SwitchStatement(IExpression main, IExpression case1, IStatement stmtCase1, IExpression case2, IStatement stmtCase2, IStatement defaultStmt)
    {
        this.mainExpression = main;
        this.case1 = case1;
        this.stmtCase1 = stmtCase1;
        this.case2 = case2;
        this.stmtCase2 = stmtCase2;
        this.defaultStmt = defaultStmt;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementExecutionException, ExpressionEvaluationException, ADTException, DivisionByZero, InterruptedException {
        MyIStack<IStatement> exeStack = state.getExeStack();
        IStatement ifStmt = new IfStatement(
                new RelationalExpression("==", mainExpression, case1),
                stmtCase1, new IfStatement(new RelationalExpression("==", mainExpression, case2), stmtCase2, defaultStmt)
        );

        exeStack.push(ifStmt);
        state.setExeStack(exeStack);

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        Type mainType = mainExpression.typeCheck(typeEnv);
        Type type1 = case1.typeCheck(typeEnv);
        Type type2 = case2.typeCheck(typeEnv);

        if(mainType.equals(type1) && mainType.equals(type2))
        {
            stmtCase1.typeCheck(typeEnv.deepCopy());
            stmtCase2.typeCheck(typeEnv.deepCopy());
            defaultStmt.typeCheck(typeEnv.deepCopy());
            return typeEnv;
        }
        else throw new StatementExecutionException("Expression types must match in switch");
    }

    @Override
    public IStatement deepCopy() {
        return new SwitchStatement(mainExpression.deepCopy(), case1.deepCopy(), stmtCase1.deepCopy(), case2.deepCopy(), stmtCase2.deepCopy(), defaultStmt.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("switch(%s){ (case %s: %s)(case %s: %s)(default: %s) }", mainExpression, case1, stmtCase1, case2, stmtCase2, defaultStmt);
    }
}
