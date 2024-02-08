package com.example.a7_gui.model.program_statements;

import com.example.a7_gui.exceptions.DataStructureException;
import com.example.a7_gui.exceptions.ArithmeticException;
import com.example.a7_gui.exceptions.EvaluationException;
import com.example.a7_gui.exceptions.ExecutionException;
import com.example.a7_gui.model.programState.ProgramState;
import com.example.a7_gui.model.program_types.Type;
import com.example.a7_gui.model.my_utils.MyIDictionary;
import com.example.a7_gui.model.my_utils.MyIStack;

public class ReturnStmt implements IStatement{
    @Override
    public ProgramState execute(ProgramState state) throws ExecutionException, EvaluationException, DataStructureException, ArithmeticException {
        MyIStack executionStack = state.getExecutionStack();

        executionStack.push(this);
        executionStack.pop();

        state.getSymbolTablw().pop();

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws ExecutionException, EvaluationException, DataStructureException {
        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new ReturnStmt();
    }

    @Override
    public String toString() {
        return "return";
    }
}
