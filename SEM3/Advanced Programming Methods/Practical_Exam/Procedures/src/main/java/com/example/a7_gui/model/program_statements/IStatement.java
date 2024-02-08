package com.example.a7_gui.model.program_statements;

import com.example.a7_gui.exceptions.DataStructureException;
import com.example.a7_gui.exceptions.ArithmeticException;
import com.example.a7_gui.exceptions.EvaluationException;
import com.example.a7_gui.exceptions.ExecutionException;
import com.example.a7_gui.model.programState.ProgramState;
import com.example.a7_gui.model.program_types.Type;
import com.example.a7_gui.model.my_utils.MyIDictionary;


public interface IStatement {
    ProgramState execute(ProgramState state) throws ExecutionException, EvaluationException, DataStructureException, ArithmeticException;
    MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws ExecutionException, EvaluationException, DataStructureException;
    IStatement deepCopy();
}
