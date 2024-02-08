package com.example.a7_gui.model.statements;

import com.example.a7_gui.my_exceptions.DataStructureException;
import com.example.a7_gui.my_exceptions.ExpressionEvaluationException;
import com.example.a7_gui.my_exceptions.StatementExecutionException;
import com.example.a7_gui.model.program_state.ProgramState;
import com.example.a7_gui.model.types.Type;
import com.example.a7_gui.model.my_data_structures.MyIDictionary;
import com.example.a7_gui.model.my_data_structures.MyIStack;


public class CompoundStatement implements IStatement {
    private final IStatement firstStatement;
    private final IStatement secondStatement;

    public CompoundStatement(IStatement firstStatement, IStatement secondStatement) {
        this.firstStatement = firstStatement;
        this.secondStatement = secondStatement;
    }

    @Override
    public ProgramState execute(ProgramState state){
        MyIStack<IStatement> stack = state.getExecutionStack();
        stack.push(secondStatement);
        stack.push(firstStatement);
        state.setExecutionStack(stack);
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, DataStructureException {
        return secondStatement.typeCheck(firstStatement.typeCheck(typeEnv));
    }

    @Override
    public IStatement deepCopy() {
        return new CompoundStatement(firstStatement.deepCopy(), secondStatement.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("(%s|%s)", firstStatement.toString(), secondStatement.toString());
    }
}