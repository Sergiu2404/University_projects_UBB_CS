package repo;

import model.programState.ProgramState;

public interface IRepository {
    void addProgram(ProgramState newProgramToAdd);

    ProgramState getCurrentProgram();
}
