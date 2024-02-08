package com.example.a7_gui.model.program_statements;

import com.example.a7_gui.exceptions.DataStructureException;
import com.example.a7_gui.exceptions.ArithmeticException;
import com.example.a7_gui.exceptions.EvaluationException;
import com.example.a7_gui.exceptions.ExecutionException;
import com.example.a7_gui.model.program_expressions.IExpression;
import com.example.a7_gui.model.programState.ProgramState;
import com.example.a7_gui.model.program_types.RefType;
import com.example.a7_gui.model.program_types.Type;
import com.example.a7_gui.model.my_utils.MyIDictionary;
import com.example.a7_gui.model.my_utils.MyIHeap;
import com.example.a7_gui.model.my_utils.MyIStack;
import com.example.a7_gui.model.program_values.RefValue;
import com.example.a7_gui.model.program_values.Value;

public class NewStatement implements IStatement{
    private final String varName;
    private final IExpression expression;

    public NewStatement(String varName, IExpression expression) {
        this.varName = varName;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws ExecutionException, EvaluationException, DataStructureException, ArithmeticException {

        MyIStack<MyIDictionary<String, Value>> symTable = state.getSymbolTablw();
        MyIDictionary<String, Value> currentSymbolTable = symTable.peek();
        MyIHeap heap = state.getHeap();

        if (currentSymbolTable.isDefined(varName)) {

            Value varValue = currentSymbolTable.lookUp(varName);
            if ((varValue.getType() instanceof RefType)) {

                Value evaluated = expression.evaluateExpression(currentSymbolTable, heap);
                Type locationType = ((RefValue) varValue).getLocationType();

                if (locationType.equals(evaluated.getType())) {

                    int newPosition = heap.add(evaluated);

                    currentSymbolTable.put(varName, new RefValue(newPosition, locationType));
                    state.setSymTable(symTable);
                    state.setHeap(heap);

                } else
                    throw new ExecutionException(String.format("%s not of %s", varName, evaluated.getType()));
            } else {
                throw new ExecutionException(String.format("%s in not of RefType", varName));
            }
        } else {
            throw new ExecutionException(String.format("%s not in symTable", varName));
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
        return String.format("New(%s, %s)", varName, expression);
    }
}