package com.example.a7_gui.controller;

import com.example.a7_gui.exceptions.ADTException;
import com.example.a7_gui.exceptions.ExpressionEvaluationException;
import com.example.a7_gui.exceptions.InterpreterException;
import com.example.a7_gui.exceptions.StatementExecutionException;
import com.example.a7_gui.model.programState.ProgramState;
import com.example.a7_gui.model.values.RefValue;
import com.example.a7_gui.model.values.Value;
import com.example.a7_gui.repo.IRepository;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;


class AditionalStructure {
    final ProgramState programState;
    final InterpreterException programException;

    public AditionalStructure(ProgramState f, InterpreterException s)
    {
        this.programState =f;
        this.programException=s;
    }

}

public class Controller {
    IRepository repository;
    boolean displayFlag = false;
    ExecutorService executorService;

    public void setDisplayFlag(boolean value) {
        this.displayFlag = value;
    }

    public Controller(IRepository repository) {
        this.repository = repository;
    }

    public List<ProgramState> getProgramStates()
    {
        return this.repository.getProgramList();
    }

    public void setProgramStates(List<ProgramState> newProgramStatesList)
    {
        this.repository.setProgramStates(newProgramStatesList);
    }


    public List<Integer> getAddrFromSymTable(Collection<Value> symTableValues, HashMap<Integer, Value> heapTable) {
        List<Integer> allAddresses = new ArrayList<>();
        symTableValues.stream()
                .filter(val -> val instanceof RefValue)
                .forEach(val ->{
                    while (val instanceof RefValue)
                    {
                        allAddresses.add(((RefValue) val).getAddress());
                        val = heapTable.get(((RefValue) val).getAddress());
                    }
                });
        return allAddresses;
//        return symTableValues.stream()
//                .filter(v -> v instanceof RefValue)
//                .map(v -> {RefValue v1 = (RefValue) v; return v1.getAddress();})
//                .collect(Collectors.toList());
    }

    public List<Integer> getAddrFromHeap(Collection<Value> heapValues) {
        return heapValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> {RefValue v1 = (RefValue) v; return v1.getAddress();})
                .collect(Collectors.toList());
    }

//    public Map<Integer, Value> unsafeGarbageCollector(List<Integer> symTableAddr, Map<Integer,IValue> heap)
//    {
//        return heap.entrySet().stream()
//                .filter(e -> symTableAddr.contains(e.getKey()))
//                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//    }

    public Map<Integer, Value> safeGarbageCollector(List<Integer> symTableAddresses, List<Integer> heapAddresses, Map<Integer, Value> heap) {
        return heap.entrySet().stream()
                .filter(e -> ( symTableAddresses.contains(e.getKey()) || heapAddresses.contains(e.getKey())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void oneStepForAllPrograms(List<ProgramState> programStates) throws InterruptedException, InterpreterException {
        programStates.forEach(programState -> {
            try {
                repository.logPrgStateExec(programState);
                display(programState);
            } catch (IOException | ADTException e) {
                System.out.println(e.getMessage());
            }
        });
        List<Callable<ProgramState>> callList = programStates.stream()
                .map((ProgramState p) -> (Callable<ProgramState>) (p::oneStep))
                .collect(Collectors.toList());

        List<AditionalStructure> newProgramList;
        newProgramList = executorService.invokeAll(callList).stream()
                .map(future -> {
                    try{
                        return new AditionalStructure(future.get(),null);
                    }catch (ExecutionException | InterruptedException e) {
                        if(e.getCause() instanceof InterpreterException)
                            return new AditionalStructure(null, (InterpreterException) e.getCause());
                        System.out.println(e.getMessage());
                        return null;
                    }
                }).filter(Objects::nonNull)
                .filter(aditionalStructure -> aditionalStructure.programState != null || aditionalStructure.programException != null)
                .collect(Collectors.toList());

        for(AditionalStructure err : newProgramList)
            if(err.programException != null)
                throw err.programException;

        programStates.addAll(newProgramList.stream().map(aditionalStructure -> aditionalStructure.programState).collect(Collectors.toList()));
        programStates.forEach(prg ->{
            try{
                repository.logPrgStateExec(prg);
            } catch (IOException | ADTException e) {
                System.out.println(e.getMessage());
            }
        });
        repository.setProgramStates(programStates);

//        List<ProgramState> newProgramList = executorService.invokeAll(callList).stream()
//                .map(future -> {
//                    try {
//                        return future.get();
//                    } catch (ExecutionException | InterruptedException e) {
//                        System.out.println(e.getMessage());
//                    }
//                    return null;
//                })
//                .filter(Objects::nonNull)
//                .toList();
//
//        programStates.addAll(newProgramList);
//        conservativeGarbageCollector(programStates);
//
//        programStates.forEach(programState -> {
//            try {
//                repository.logPrgStateExec(programState);
//            } catch (IOException | ADTException e) {
//                System.out.println(e.getMessage());
//            }
//        });
//        repository.setProgramStates(programStates);
    }

    public void oneStep() throws InterruptedException, InterpreterException {
        executorService = Executors.newFixedThreadPool(2);
        List<ProgramState> programStates = removeCompletedPrg(repository.getProgramList());
        oneStepForAllPrograms(programStates);
        conservativeGarbageCollector(programStates);

        executorService.shutdownNow();
    }

    public void allStep() throws InterruptedException, ExpressionEvaluationException, ADTException, StatementExecutionException, IOException, InterpreterException {
        executorService = Executors.newFixedThreadPool(2);
        List<ProgramState> programStates = removeCompletedPrg(repository.getProgramList());
        while (!programStates.isEmpty()) {
            oneStepForAllPrograms(programStates);
            programStates = removeCompletedPrg(repository.getProgramList());
        }
        executorService.shutdownNow();
        repository.setProgramStates(programStates);
    }

    public void conservativeGarbageCollector(List<ProgramState> programStates) {
        List<Integer> symTableAddresses = Objects.requireNonNull(programStates.stream()
                        .map(p -> getAddrFromSymTable(p.getSymTable().values(), p.getHeap().getContent()))
                        .map(Collection::stream)
                        .reduce(Stream::concat).orElse(null))
                .collect(Collectors.toList());
        programStates.forEach(p -> {
            p.getHeap().setContent((HashMap<Integer, Value>) safeGarbageCollector(symTableAddresses, getAddrFromHeap(p.getHeap().getContent().values()), p.getHeap().getContent()));
        });
    }

    private void display(ProgramState programState) {
        if (displayFlag) {
            System.out.println(programState.toString());
        }
    }

    public List<ProgramState> removeCompletedPrg(List<ProgramState> inPrgList) {
        return inPrgList.stream().filter(p -> !p.isNotCompleted()).collect(Collectors.toList());
    }
}