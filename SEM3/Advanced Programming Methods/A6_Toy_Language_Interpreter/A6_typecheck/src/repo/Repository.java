package repo;

import exceptions.HeapException;
import model.programState.ProgramState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository{
    private List<ProgramState> programStates;
    private final String logFilePath;

//    public Repository(String logFilePath)
//    {
//        this.logFilePath = logFilePath;
//        programStates=new ArrayList<>();
//        //this.logFilePath="";
//    }
    public Repository(ProgramState program, String logFilePath)
    {
        this.logFilePath=logFilePath;
        programStates=new ArrayList<>();
        this.addProgram(program);
    }

    @Override
    public List<ProgramState> getProgramList() {
        return this.programStates;
    }

    public void addProgram(ProgramState newProgram)
    {
        programStates.add(newProgram);
//        if(!programStates.isEmpty())
//            programStates.set(0,newProgram);
//        else programStates.add(newProgram);
    }

    @Override
    public void setProgramStates(List<ProgramState> programStates) {
        this.programStates = programStates;
    }

    public void logPrgStateExec(ProgramState programState) throws IOException, HeapException {
        PrintWriter logFile;
        logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath,true)));
        //logFile.println(this.programs.get(programs.size()-1).programStateToStringFile());
        logFile.println(programState.programStateToStringFile());
        logFile.close();
    }

}
