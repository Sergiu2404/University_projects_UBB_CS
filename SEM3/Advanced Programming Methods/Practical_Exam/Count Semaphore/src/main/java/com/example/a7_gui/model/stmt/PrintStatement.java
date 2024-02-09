package com.example.a7_gui.model.stmt;

import com.example.a7_gui.exceptions.DataStructureException;
import com.example.a7_gui.exceptions.ArithmeticException;
import com.example.a7_gui.exceptions.EvaluationException;
import com.example.a7_gui.exceptions.ExecutionException;
import com.example.a7_gui.model.expressions.IExpression;
import com.example.a7_gui.model.programState.ProgramState;
import com.example.a7_gui.model.types.Type;
import com.example.a7_gui.model.utils.MyIDictionary;
import com.example.a7_gui.model.utils.MyIList;
import com.example.a7_gui.model.values.Value;

public class PrintStatement implements IStatement {
    IExpression innerExpression;

    public PrintStatement(IExpression innerExpression) {
        this.innerExpression = innerExpression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws EvaluationException, DataStructureException, ArithmeticException {
        MyIList<Value> out = state.getOut();
        out.add(innerExpression.evaluateExpression(state.getSymbolTable(), state.getHeapTable()));
        state.setOut(out);
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws ExecutionException, EvaluationException, DataStructureException {
        innerExpression.typeCheck(typeEnv);
        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new PrintStatement(innerExpression.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("Print(%s)", innerExpression.toString());
    }
}