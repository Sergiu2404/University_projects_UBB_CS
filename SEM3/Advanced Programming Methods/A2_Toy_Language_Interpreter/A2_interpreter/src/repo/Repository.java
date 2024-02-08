package repo;

import model.programState.ProgramState;

import java.util.ArrayList;

public class Repository implements IRepository{
    private ArrayList<ProgramState> programs;

    public Repository()
    {
        programs=new ArrayList<ProgramState>();
    }

    public void addProgram(ProgramState newProgramToAdd)
    {
        programs.add(newProgramToAdd);
    }

    public ProgramState getCurrentProgram()
    {
        return programs.get(programs.size()-1);
    }
}
