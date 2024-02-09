package com.example.a7_gui.repository;

import com.example.a7_gui.my_exceptions.DataStructureException;
import com.example.a7_gui.model.program_state.ProgramState;

import java.io.IOException;
import java.util.List;

public interface IRepository {
    List<ProgramState> getProgramList();
    void setProgramStates(List<ProgramState> programStates);
    void addProgram(ProgramState program);
    void logPrgStateExec(ProgramState programState) throws IOException, DataStructureException;
    //void emptyLogFile() throws IOException;
}