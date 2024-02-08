package controller;

import exceptions.*;
import model.programState.ProgramState;
import model.stmt.IStmt;
import model.utils.MyIStack;
import model.values.IValue;
import model.values.RefValue;
import repo.IRepository;

import javax.print.attribute.IntegerSyntax;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller {
    private IRepository repository;
    private ExecutorService executor;
    private boolean displayOption = false;

    public Controller(IRepository repo)
    {
        this.repository=repo;
    }

    public void setDisplayOption(boolean newVal) {
        this.displayOption = newVal;
    }

    public List<Integer> getAddrFromSymTable(Collection<IValue> symTableValues, HashMap<Integer, IValue> heapTbl)
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

    public void conservativeGarbageCollector(List<ProgramState> programStates)
    {
        List<Integer> symTblAddresses = Objects.requireNonNull(programStates.stream()
                .map(p -> getAddrFromSymTable(p.getSymbolTable().values(), p.getHeapTable().getContent()))
                .map(Collection::stream)
                .reduce(Stream::concat).orElse(null))
                .collect(Collectors.toList());

        programStates.forEach(p -> {
            p.getHeapTable().setContent((HashMap<Integer, IValue>) safeGarbageCollector(symTblAddresses, getAddrFromHeapTable(p.getHeapTable().getContent().values()), p.getHeapTable().getContent()));
        });
    }


    public void oneStepForAllPrograms(List<ProgramState> programStates)throws InterruptedException {
        programStates.forEach(pr -> {
            try {
                repository.logPrgStateExec(pr);
                display(pr);
            } catch (IOException | HeapException e) {
                System.out.println(e.getMessage());
            }
        });
        List<Callable<ProgramState>> callList = programStates.stream()
                .map((ProgramState p) -> (Callable<ProgramState>) (p::oneStep))
                .collect(Collectors.toList());

        List<ProgramState> newProgramList = executor.invokeAll(callList).stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        System.out.println(e.getMessage());
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        programStates.addAll(newProgramList);

        programStates.forEach(pr -> {
            try {
                repository.logPrgStateExec(pr);
            } catch (IOException | HeapException e) {
                System.out.println(e.getMessage());
            }
        });

        repository.setProgramStates(programStates);
    }

    public void allStep() throws InterruptedException {
        executor = Executors.newFixedThreadPool(2);
        List<ProgramState> programStates = removeCompletedProgram(repository.getProgramList());

//        oneStepForAllPrograms(programStates);
//        programStates = removeCompletedProgram(repository.getProgramList());

        while(!programStates.isEmpty())
        {
            conservativeGarbageCollector(programStates);
            oneStepForAllPrograms(programStates);
            programStates = removeCompletedProgram(repository.getProgramList());
        }

        executor.shutdownNow();
        repository.setProgramStates(programStates);
    }


    List<ProgramState> removeCompletedProgram(List<ProgramState> inPrgList)
    {
        return inPrgList.stream()
                .filter(ProgramState::isNotCopleted)
                .collect(Collectors.toList());
    }

    public void display(ProgramState state)
    {
        if(displayOption)
            System.out.println(state.toString());
    }

}
