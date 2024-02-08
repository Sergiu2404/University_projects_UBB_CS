package com.example.a7_gui.controller;

import com.example.a7_gui.my_exceptions.DataStructureException;
import com.example.a7_gui.my_exceptions.ExpressionEvaluationException;
import com.example.a7_gui.my_exceptions.ProgramExecutionException;
import com.example.a7_gui.my_exceptions.StatementExecutionException;
import com.example.a7_gui.model.program_state.ProgramState;
import com.example.a7_gui.model.values.RefValue;
import com.example.a7_gui.model.values.Value;
import com.example.a7_gui.repository.IRepository;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;


class AditionalStructure {
    final ProgramState currentProgram;
    final ProgramExecutionException currentProgramExecutionException;

    public AditionalStructure(ProgramState p, ProgramExecutionException e)
    {
        this.currentProgram =p;
        this.currentProgramExecutionException =e;
    }

}

public class Controller {
    IRepository repository;
    ExecutorService executorService;

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

    public List<Integer> getAddressesFromHeap(Collection<Value> heapValues) {
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

    public void oneStepForAllPrograms(List<ProgramState> programStates) throws InterruptedException, ProgramExecutionException {
        List<Callable<ProgramState>> callList = programStates.stream()
                .map((ProgramState p) -> (Callable<ProgramState>) (p::oneStep))
                .collect(Collectors.toList());

        List<AditionalStructure> newProgramList;
        newProgramList = executorService.invokeAll(callList).stream()
                .map(future -> {
                    try{
                        return new AditionalStructure(future.get(),null);
                    }catch (ExecutionException | InterruptedException e) {
                        if(e.getCause() instanceof ProgramExecutionException)
                            return new AditionalStructure(null, (ProgramExecutionException) e.getCause());
                        System.out.println(e.getMessage());
                        return null;
                    }
                }).filter(Objects::nonNull)
                .filter(aditionalStructure -> aditionalStructure.currentProgram != null || aditionalStructure.currentProgramExecutionException != null)
                .collect(Collectors.toList());

        for(AditionalStructure currentProgramError : newProgramList)
            if(currentProgramError.currentProgramExecutionException != null)
                throw currentProgramError.currentProgramExecutionException;

        programStates.addAll(newProgramList.stream().map(aditionalStructure -> aditionalStructure.currentProgram).collect(Collectors.toList()));
        programStates.forEach(prg ->{
            try{
                repository.logPrgStateExec(prg);
            } catch (IOException | DataStructureException e) {
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
    }
    public List<Integer> getAddressesFromSymbolTable(Collection<Value> symbolTableValues, HashMap<Integer, Value> heapTable) {
        List<Integer> allAddresses = new ArrayList<>();

        symbolTableValues.stream()
                .filter(val -> val instanceof RefValue)
                .forEach(val ->{
                    while (val instanceof RefValue)
                    {
                        allAddresses.add(((RefValue) val).getAddress());
                        val = heapTable.get(((RefValue) val).getAddress());
                    }
                });

        return allAddresses;
//        return symbolTableValues.stream()
//                .filter(v -> v instanceof RefValue)
//                .map(v -> {RefValue v1 = (RefValue) v; return v1.getAddress();})
//                .collect(Collectors.toList());
    }


    public void oneStep() throws InterruptedException, ProgramExecutionException {
        executorService = Executors.newFixedThreadPool(2);
        List<ProgramState> programStates = removeCompletedPrograms(repository.getProgramList());

        oneStepForAllPrograms(programStates);
        conservativeGarbageCollector(programStates);

        executorService.shutdownNow();
    }

    public void conservativeGarbageCollector(List<ProgramState> programStates) {
        List<Integer> symTableAddresses = Objects.requireNonNull(programStates.stream()
                        .map(p -> getAddressesFromSymbolTable(p.getSymbolTable().values(), p.getHeapTable().getContent()))
                        .map(Collection::stream)
                        .reduce(Stream::concat).orElse(null))
                .collect(Collectors.toList());
        programStates.forEach(p -> {
            p.getHeapTable().setContent((HashMap<Integer, Value>) safeGarbageCollector(symTableAddresses, getAddressesFromHeap(p.getHeapTable().getContent().values()), p.getHeapTable().getContent()));
        });
    }

    public List<ProgramState> removeCompletedPrograms(List<ProgramState> inPrgList) {
        return inPrgList.stream().filter(p -> !p.isNotCompleted()).collect(Collectors.toList());
    }

    public void executeAllSteps() throws InterruptedException, ExpressionEvaluationException, DataStructureException, StatementExecutionException, IOException, ProgramExecutionException {

        executorService = Executors.newFixedThreadPool(2);
        List<ProgramState> programStates = removeCompletedPrograms(repository.getProgramList());

        while (!programStates.isEmpty()) {
            oneStepForAllPrograms(programStates);
            programStates = removeCompletedPrograms(repository.getProgramList());
        }

        executorService.shutdownNow();
        repository.setProgramStates(programStates);
    }
}