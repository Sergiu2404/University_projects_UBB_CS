package repo;

import exceptions.HeapException;
import model.programState.ProgramState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Repository implements IRepository{
    private ArrayList<ProgramState> programs;
    private final String logFilePath;

    public Repository(String logFilePath)
    {
        this.logFilePath = logFilePath;
        programs=new ArrayList<ProgramState>();
        //this.logFilePath="";
    }

//    public Repository(String logFilePath)
//    {
//        this.logFilePath=logFilePath;
//        programs=new ArrayList<ProgramState>();
//    }
    public Repository(ProgramState program, String logFilePath)
    {
        this.logFilePath=logFilePath;
        programs=new ArrayList<ProgramState>();
        programs.add(program);
    }

    public void addProgram(ProgramState newProgram)
    {
        //programs.add(newProgram);
        if(!programs.isEmpty())
            programs.set(0,newProgram);
        else programs.add(newProgram);
    }

    public ProgramState getCurrentProgram()
    {
        //return programs.get(programs.size()-1);
        return programs.get(0);
    }


    public void logPrgStateExec() throws IOException, HeapException {
        PrintWriter logFile;
        logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath,true)));
        //logFile.println(this.programs.get(programs.size()-1).programStateToStringFile());
        logFile.println(this.programs.get(0).programStateToStringFile());
        logFile.close();
    }

}
