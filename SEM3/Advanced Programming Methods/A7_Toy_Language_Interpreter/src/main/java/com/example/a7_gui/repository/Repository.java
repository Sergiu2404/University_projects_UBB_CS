package com.example.a7_gui.repository;

import com.example.a7_gui.my_exceptions.DataStructureException;
import com.example.a7_gui.model.program_state.ProgramState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository{
    private List<ProgramState> programStates;
    private final String logFilePath;

    public Repository(ProgramState programState, String logFilePath) throws IOException {
        this.logFilePath = logFilePath;
        this.programStates = new ArrayList<>();
        this.addProgram(programState);
    }

    @Override
    public List<ProgramState> getProgramList() {
        return this.programStates;
    }

    @Override
    public void setProgramStates(List<ProgramState> programStates) {
        this.programStates = programStates;
    }

    @Override
    public void addProgram(ProgramState program) {
        this.programStates.add(program);
    }

    @Override
    public void logPrgStateExec(ProgramState programState) throws IOException, DataStructureException {
        PrintWriter logFile;
        logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)));
        logFile.println(programState.fileToString());
        logFile.close();
    }
}