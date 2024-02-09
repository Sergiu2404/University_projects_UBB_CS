package com.example.a7_gui.model.stmt;

import com.example.a7_gui.exceptions.DataStructureException;
import com.example.a7_gui.exceptions.ArithmeticException;
import com.example.a7_gui.exceptions.EvaluationException;
import com.example.a7_gui.exceptions.ExecutionException;
import com.example.a7_gui.model.expressions.IExpression;
import com.example.a7_gui.model.programState.ProgramState;
import com.example.a7_gui.model.types.RefType;
import com.example.a7_gui.model.types.Type;
import com.example.a7_gui.model.utils.MyIDictionary;
import com.example.a7_gui.model.utils.MyIHeap;
import com.example.a7_gui.model.values.RefValue;
import com.example.a7_gui.model.values.Value;

public class WriteHeapStatement implements IStatement{
    private final String varName;
    private final IExpression expression;

    public WriteHeapStatement(String varName, IExpression expression) {
        this.varName = varName;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws ExecutionException, EvaluationException, DataStructureException, ArithmeticException {

        MyIDictionary<String, Value> symbolTable = state.getSymbolTable();
        MyIHeap heap = state.getHeapTable();

        if (symbolTable.isDefined(varName)) {

            Value value = symbolTable.lookUp(varName);

            if (value.getType() instanceof RefType) {

                RefValue refValue = (RefValue) value;

                if (heap.containsKey(refValue.getAddress())) {

                    Value evaluated = expression.evaluateExpression(symbolTable, heap);

                    if (evaluated.getType().equals(refValue.getLocationType())) {

                        heap.update(refValue.getAddress(), evaluated);
                        state.setHeapTable(heap);

                    } else
                        throw new ExecutionException(String.format("WriteHeap: %s not of %s", evaluated, refValue.getLocationType()));
                } else
                    throw new ExecutionException(String.format("WriteHeap: The RefValue %s is not defined in the heap", value));
            } else
                throw new ExecutionException(String.format("WriteHeap: %s not of RefType", value));
        } else
            throw new ExecutionException(String.format("WriteHeap: %s not present in the symbolTable", varName));
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws ExecutionException, EvaluationException, DataStructureException {
        if (typeEnv.lookUp(varName).equals(new RefType(expression.typeCheck(typeEnv))))
            return typeEnv;
        else
            throw new ExecutionException("WriteHeap: right hand side and left hand side have different types");
    }

    @Override
    public IStatement deepCopy() {
        return new WriteHeapStatement(varName, expression.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("WriteHeap(%s, %s)", varName, expression);
    }
}