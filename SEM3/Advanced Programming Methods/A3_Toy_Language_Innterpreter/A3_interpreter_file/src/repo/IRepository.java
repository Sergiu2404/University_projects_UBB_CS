package repo;

import model.programState.ProgramState;

import java.io.IOException;
import java.io.PrintWriter;

public interface IRepository {
    void addProgram(ProgramState newProgram);

    ProgramState getCurrentProgram();

    void logPrgStateExec() throws IOException;
}
