package repo;

import exceptions.HeapException;
import model.programState.ProgramState;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public interface IRepository {
    List<ProgramState> getProgramList();
    void addProgram(ProgramState newProgram);
    void setProgramStates(List<ProgramState> programStates);

    void logPrgStateExec(ProgramState state) throws IOException, HeapException;
}
