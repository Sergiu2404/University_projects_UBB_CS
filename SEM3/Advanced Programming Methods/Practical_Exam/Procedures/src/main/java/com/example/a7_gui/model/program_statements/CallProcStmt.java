package com.example.a7_gui.model.program_statements;

import com.example.a7_gui.exceptions.DataStructureException;
import com.example.a7_gui.exceptions.ArithmeticException;
import com.example.a7_gui.exceptions.EvaluationException;
import com.example.a7_gui.exceptions.ExecutionException;
import com.example.a7_gui.model.program_expressions.IExpression;
import com.example.a7_gui.model.programState.ProgramState;
import com.example.a7_gui.model.program_types.Type;
import com.example.a7_gui.model.my_utils.MyDictionary;
import com.example.a7_gui.model.my_utils.MyIDictionary;
import com.example.a7_gui.model.my_utils.MyIHeap;
import com.example.a7_gui.model.my_utils.MyIProcedureTable;
import com.example.a7_gui.model.program_values.Value;

import java.util.List;

public class CallProcStmt implements IStatement{

    private final String procedureName;
    private final List<IExpression> expressions;

    public CallProcStmt(String pn, List<IExpression> exp){
        this.procedureName=pn;
        this.expressions=exp;
    }
    @Override
    public ProgramState execute(ProgramState state) throws ExecutionException, EvaluationException, DataStructureException, ArithmeticException {
        MyIDictionary<String, Value> symbolTable = state.getTopFromStackSymbolTable();
        MyIHeap heap = state.getHeap();
        MyIProcedureTable procedureTable = state.getProcedureTable();

        if (procedureTable.isDefined(procedureName)) {

            List<String> variables = procedureTable.lookUp(procedureName).getKey();
            IStatement innerStatement = procedureTable.lookUp(procedureName).getValue();
            MyIDictionary<String, Value> newSymTable = new MyDictionary<>();

            for(String var: variables) {
                int position = variables.indexOf(var);
                newSymTable.put(var, expressions.get(position).evaluateExpression(symbolTable, heap));
            }

            state.getSymbolTablw().push(newSymTable);
            state.getExecutionStack().push(new ReturnStmt());
            state.getExecutionStack().push(innerStatement);

        } else {
            throw new ExecutionException("CallProcedure: Procedure does not exist");
        }

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws ExecutionException, EvaluationException, DataStructureException {
        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new CallProcStmt(procedureName,expressions);
    }

    @Override
    public String toString() {
        return String.format("%s{ %s }", procedureName, expressions.toString());
    }
}
