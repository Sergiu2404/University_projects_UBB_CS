package com.example.a7_gui.model.statements;

import com.example.a7_gui.my_exceptions.DataStructureException;
import com.example.a7_gui.my_exceptions.ExpressionEvaluationException;
import com.example.a7_gui.my_exceptions.StatementExecutionException;
import com.example.a7_gui.model.program_state.ProgramState;
import com.example.a7_gui.model.types.Type;
import com.example.a7_gui.model.my_data_structures.MyIDictionary;


public class NoStatement implements IStatement {
    @Override
    public ProgramState executeStatement(ProgramState state) {
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, DataStructureException {
        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new NoStatement();
    }

    @Override
    public String toString() {
        return "NopStatement";
    }
}