package com.example.a7_gui.model.statements;

import com.example.a7_gui.my_exceptions.DataStructureException;
import com.example.a7_gui.my_exceptions.ExpressionEvaluationException;
import com.example.a7_gui.my_exceptions.StatementExecutionException;
import com.example.a7_gui.model.program_state.ProgramState;
import com.example.a7_gui.model.types.Type;
import com.example.a7_gui.model.my_data_structures.*;
import com.example.a7_gui.model.values.Value;

import java.util.Map;

public class ForkStatement implements IStatement{
    private final IStatement statement;

    public ForkStatement(IStatement statement) {
        this.statement = statement;
    }
    @Override
    public ProgramState executeStatement(ProgramState state) throws StatementExecutionException, ExpressionEvaluationException, DataStructureException {

        MyIStack<IStatement> newStack = new MyStack<>();
        newStack.push(statement);
        MyISymbolTable<String, Value> newSymTable = new MySymbolTable<>();

        for (Map.Entry<String, Value> entry: state.getSymbolTable().getContent().entrySet()) {
            newSymTable.put(entry.getKey(), entry.getValue().deepCopy());
        }

        return new ProgramState(newStack, newSymTable, state.getOutputList(), state.getFileTable(), state.getHeapTable(), state.getSemaphoreTable());
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, DataStructureException {
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