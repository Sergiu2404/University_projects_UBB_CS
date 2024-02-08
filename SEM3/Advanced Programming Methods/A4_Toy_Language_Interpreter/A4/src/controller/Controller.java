package controller;

import exceptions.*;
import model.programState.ProgramState;
import model.stmt.IStmt;
import model.utils.MyIStack;
import model.values.IValue;
import model.values.RefValue;
import repo.IRepository;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Controller {
    private IRepository repositoryPrograms;

    public Controller(IRepository repo)
    {
        this.repositoryPrograms=repo;
    }

    public boolean isEmpty()
    {
        return this.repositoryPrograms.getCurrentProgram().getExeStack().isEmpty();
    }

    public void addProgram(ProgramState newProgram)
    {
        repositoryPrograms.addProgram(newProgram);
    }
    public ProgramState getCurrentProgram()
    {
        return this.repositoryPrograms.getCurrentProgram();
    }

    public List<Integer> getAddrFromSymTable(Collection<IValue> symTableValues, Map<Integer, IValue> heapTbl)
    {
        List<Integer> allAddresses = new ArrayList<>();

        symTableValues.stream()
                .filter(val -> val instanceof RefValue)
                .forEach( val -> {
                            while(val instanceof RefValue)
                            {
                                allAddresses.add(((RefValue) val).getAddress());
                                val = heapTbl.get(((RefValue) val).getAddress());
                            }
                        }
                );
        return allAddresses;
//        return symTableValues.stream()
//                .filter(v -> v instanceof RefValue)
//                .map(v -> {
//                    RefValue v1 = (RefValue)v;
//                    return v1.getAddress();
//                })
//                .collect(Collectors.toList());
    }
    public List<Integer> getAddrFromHeapTable(Collection<IValue> heapTableValues)
    {
        return heapTableValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> {
                    RefValue v1 = (RefValue)v;
                    return v1.getAddress();
                })
                .collect(Collectors.toList());
    }


    public Map<Integer, IValue> unsafeGarbageCollector(List<Integer> symTableAddr, Map<Integer,IValue> heap)
    {
        return heap.entrySet().stream()
                .filter(e -> symTableAddr.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    //trebuie verificat daca valorea se gaseste in hep si nu se gaseste in symTbl => elimina din heapS
    public Map<Integer, IValue> safeGarbageCollector(List<Integer> symTableAddr, List<Integer> heapAddr, Map<Integer,IValue> heap)
    {
        return heap.entrySet().stream()
                .filter(e -> (symTableAddr.contains(e.getKey()) || heapAddr.contains(e.getKey())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }



    public ProgramState executeOneStep(ProgramState currentState) throws IOException, StatementExecutionException, ExpressionEvaluationException, DivisionByZero, StackException, DictionaryException, HeapException {
        MyIStack<IStmt> exeStack = currentState.getExeStack();

        if(exeStack.isEmpty())
            throw new StackException("Stack is empty, no more statements!");

        IStmt currentStmt = exeStack.pop();
        repositoryPrograms.logPrgStateExec();
        currentState.getHeapTable().setContent(
                (HashMap<Integer,IValue>)safeGarbageCollector(
                        getAddrFromSymTable(currentState.getSymbolTable().getContent().values(), currentState.getHeapTable().getContent()),
                        getAddrFromHeapTable(currentState.getHeapTable().getContent().values()),
                        currentState.getHeapTable().getContent()
                )
        );
        currentState.setExeStack(exeStack);

        return currentStmt.execute(currentState);
    }

    public void executeAllSteps() throws StatementExecutionException, ExpressionEvaluationException, StackException, DivisionByZero, IOException, DictionaryException, HeapException {
        ProgramState currentState = repositoryPrograms.getCurrentProgram();
        //repositoryPrograms.logPrgStateExec();

        while(!currentState.getExeStack().isEmpty()) {
            executeOneStep(currentState);
        }
    }

    public void logPrgStateExecController() throws HeapException, IOException {
        this.repositoryPrograms.logPrgStateExec();
    }
}
