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

public class NewStatement implements IStatement{
    private final String varName;
    private final IExpression expression;

    public NewStatement(String varName, IExpression expression) {
        this.varName = varName;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws ExecutionException, EvaluationException, DataStructureException, ArithmeticException {

        MyIDictionary<String, Value> symTable = state.getSymbolTable();
        MyIHeap heap = state.getHeapTable();

        if (symTable.isDefined(varName)) {

            Value varValue = symTable.lookUp(varName);

            if ((varValue.getType() instanceof RefType)) {

                Value evaluated = expression.evaluateExpression(symTable, heap);
                Type locationType = ((RefValue) varValue).getLocationType();

                if (locationType.equals(evaluated.getType())) {

                    int newPosition = heap.add(evaluated);
                    symTable.put(varName, new RefValue(newPosition, locationType));
                    state.setSymbolTable(symTable);
                    state.setHeapTable(heap);

                } else
                    throw new ExecutionException(String.format("NEW statement: %s not of %s", varName, evaluated.getType()));
            } else {
                throw new ExecutionException(String.format("NEW statement: %s in not of RefType", varName));
            }
        } else {
            throw new ExecutionException(String.format("NEW statement: %s not in symTable", varName));
        }
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws ExecutionException, EvaluationException, DataStructureException {
        Type typeVar = typeEnv.lookUp(varName);
        Type typeExpr = expression.typeCheck(typeEnv);
        if (typeVar.equals(new RefType(typeExpr)))
            return typeEnv;
        else
            throw new ExecutionException("NEW statement: right hand side and left hand side have different types.");
    }

    @Override
    public IStatement deepCopy() {
        return new NewStatement(varName, expression.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("NewReference(%s, %s)", varName, expression);
    }
}