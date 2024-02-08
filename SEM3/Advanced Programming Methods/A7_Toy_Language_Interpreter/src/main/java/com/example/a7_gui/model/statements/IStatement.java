package com.example.a7_gui.model.statements;

import com.example.a7_gui.my_exceptions.DataStructureException;
import com.example.a7_gui.my_exceptions.DivisionByZero;
import com.example.a7_gui.my_exceptions.ExpressionEvaluationException;
import com.example.a7_gui.my_exceptions.StatementExecutionException;
import com.example.a7_gui.model.program_state.ProgramState;
import com.example.a7_gui.model.types.Type;
import com.example.a7_gui.model.my_data_structures.MyIDictionary;


public interface IStatement {
    ProgramState execute(ProgramState state) throws StatementExecutionException, ExpressionEvaluationException, DataStructureException, DivisionByZero;
    MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, DataStructureException;
    IStatement deepCopy();
}
