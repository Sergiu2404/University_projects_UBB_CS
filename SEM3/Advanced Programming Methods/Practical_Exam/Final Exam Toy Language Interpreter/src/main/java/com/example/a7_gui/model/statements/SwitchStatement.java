package com.example.a7_gui.model.statements;

import com.example.a7_gui.model.expressions.IExpression;
import com.example.a7_gui.model.expressions.RelationalExpression;
import com.example.a7_gui.model.my_data_structures.MyIDictionary;
import com.example.a7_gui.model.my_data_structures.MyIStack;
import com.example.a7_gui.model.program_state.ProgramState;
import com.example.a7_gui.model.types.Type;
import com.example.a7_gui.my_exceptions.DataStructureException;
import com.example.a7_gui.my_exceptions.ArithmeticException;
import com.example.a7_gui.my_exceptions.ExpressionEvaluationException;
import com.example.a7_gui.my_exceptions.StatementExecutionException;

public class SwitchStatement implements IStatement{
    private final IExpression mainExpression;
    private final IExpression firstCase;
    private final IStatement firstCaseStatement;
    private final IExpression secondCase;
    private final IStatement secondCaseStatement;
    private final IStatement defaultStatement;

    public SwitchStatement(IExpression m, IExpression c1, IStatement s1, IExpression c2, IStatement s2, IStatement d)
    {
        this.mainExpression = m;
        this.firstCase = c1;
        this.firstCaseStatement = s1;
        this.secondCase = c2;
        this.secondCaseStatement = s2;
        this.defaultStatement = d;
    }


    @Override
    public ProgramState executeStatement(ProgramState state) throws StatementExecutionException, ExpressionEvaluationException, DataStructureException, ArithmeticException {
        MyIStack<IStatement> executionStack = state.getExecutionStack();

        IStatement convertedToIf = new IfStatement(
                new RelationalExpression("==", mainExpression, firstCase),
                firstCaseStatement,
                new IfStatement(
                        new RelationalExpression("==", mainExpression, secondCase),
                        secondCaseStatement,
                        defaultStatement
                )
        );

        executionStack.push(convertedToIf);
        state.setExecutionStack(executionStack);

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, DataStructureException {
        Type mainExpressionType = mainExpression.typeCheck(typeEnv);

        Type case1ExpressionType = firstCase.typeCheck(typeEnv);
        Type case2ExpressionTYpp = secondCase.typeCheck(typeEnv);

        if(mainExpressionType.equals(case1ExpressionType) && mainExpressionType.equals(case2ExpressionTYpp))
        {
            firstCaseStatement.typeCheck(typeEnv);
            secondCaseStatement.typeCheck(typeEnv);
            defaultStatement.typeCheck(typeEnv.deepCopy());

            return typeEnv;
        }else throw new StatementExecutionException(String.format("Switch: Expressions types or statements types should mathc"));
    }

    @Override
    public IStatement deepCopy() {
        return new SwitchStatement(this.mainExpression.deepCopy(), this.firstCase.deepCopy(), this.firstCaseStatement.deepCopy(), this.secondCase.deepCopy(), this.secondCaseStatement.deepCopy(), this.defaultStatement.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("switch(%s) { (case %s: %s) (case %s: %s) (default case: %s) }", mainExpression, firstCase, firstCaseStatement, secondCase, secondCaseStatement, defaultStatement);
    }
}
