package com.example.a7_gui.model.program_statements;

import com.example.a7_gui.exceptions.DataStructureException;
import com.example.a7_gui.exceptions.EvaluationException;
import com.example.a7_gui.exceptions.ExecutionException;
import com.example.a7_gui.model.programState.ProgramState;
import com.example.a7_gui.model.program_types.Type;
import com.example.a7_gui.model.my_utils.MyIDictionary;
import com.example.a7_gui.model.my_utils.MyIStack;
import com.example.a7_gui.model.my_utils.MyStack;
import com.example.a7_gui.model.program_values.Value;

public class ForkStatement implements IStatement{
    private final IStatement statement;

    public ForkStatement(IStatement statement) {
        this.statement = statement;
    }
    @Override
    public ProgramState execute(ProgramState state) throws ExecutionException, EvaluationException, DataStructureException {
        MyIStack<IStatement> newStack = new MyStack<>();
        newStack.push(statement);
        MyIStack<MyIDictionary<String, Value>> newSymTable = state.getSymbolTablw().clone();
        return new ProgramState(newStack, newSymTable, state.getOut(), state.getFileTable(), state.getHeap(), state.getProcedureTable());
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws ExecutionException, EvaluationException, DataStructureException {
        statement.typeCheck(typeEnv.deepCopy());
        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new ForkStatement(statement.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("fork(%s)", statement.toString());
    }
}