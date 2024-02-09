package com.example.a7_gui.model.program_statements;

import com.example.a7_gui.exceptions.ADTException;
import com.example.a7_gui.exceptions.DivisionByZero;
import com.example.a7_gui.exceptions.ExpressionEvaluationException;
import com.example.a7_gui.exceptions.StatementExecutionException;
import com.example.a7_gui.model.program_expressions.IExpression;
import com.example.a7_gui.model.programState.ProgramState;
import com.example.a7_gui.model.program_types.Type;
import com.example.a7_gui.model.my_utils.MyIDictionary;
import com.example.a7_gui.model.my_utils.MyIList;
import com.example.a7_gui.model.values.Value;

public class PrintStatement implements IStatement {
    IExpression expression;

    public PrintStatement(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws ExpressionEvaluationException, ADTException, DivisionByZero {
        MyIList<Value> out = state.getOutput();
        out.add(expression.evaluateExpression(state.getSymbolTable(), state.getHeapMemory()));
        state.setOut(out);
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
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