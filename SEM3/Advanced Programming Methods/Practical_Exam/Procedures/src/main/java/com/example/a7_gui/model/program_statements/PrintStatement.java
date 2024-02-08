package com.example.a7_gui.model.program_statements;

import com.example.a7_gui.exceptions.DataStructureException;
import com.example.a7_gui.exceptions.ArithmeticException;
import com.example.a7_gui.exceptions.EvaluationException;
import com.example.a7_gui.exceptions.ExecutionException;
import com.example.a7_gui.model.program_expressions.IExpression;
import com.example.a7_gui.model.programState.ProgramState;
import com.example.a7_gui.model.program_types.Type;
import com.example.a7_gui.model.my_utils.MyIDictionary;
import com.example.a7_gui.model.my_utils.MyIList;
import com.example.a7_gui.model.program_values.Value;

public class PrintStatement implements IStatement {
    IExpression expression;

    public PrintStatement(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws EvaluationException, DataStructureException, ArithmeticException {
        MyIList<Value> out = state.getOut();
        out.add(expression.evaluateExpression(state.getSymbolTablw().peek(), state.getHeap()));
        state.setOut(out);
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws ExecutionException, EvaluationException, DataStructureException {
        expression.typeCheck(typeEnv);
        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new PrintStatement(expression.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("Print(%s)", expression.toString());
    }
}