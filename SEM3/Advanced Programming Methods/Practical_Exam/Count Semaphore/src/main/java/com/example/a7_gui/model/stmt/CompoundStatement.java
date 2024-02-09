package com.example.a7_gui.model.stmt;

import com.example.a7_gui.exceptions.DataStructureException;
import com.example.a7_gui.exceptions.EvaluationException;
import com.example.a7_gui.exceptions.ExecutionException;
import com.example.a7_gui.model.programState.ProgramState;
import com.example.a7_gui.model.types.Type;
import com.example.a7_gui.model.utils.MyIDictionary;
import com.example.a7_gui.model.utils.MyIStack;


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
        state.setExeStack(stack);
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws ExecutionException, EvaluationException, DataStructureException {
        return secondStatement.typeCheck(firstStatement.typeCheck(typeEnv));
    }

    @Override
    public IStatement deepCopy() {
        return new CompoundStatement(firstStatement.deepCopy(), secondStatement.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("%s  |  %s", firstStatement.toString(), secondStatement.toString());
    }
}