package controller;

import exceptions.ADTException;
import exceptions.ExpressionEvaluationException;
import exceptions.StatementExecutionException;
import model.programState.ProgramState;
import model.values.RefValue;
import model.values.Value;
import repo.IRepository;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public List<Integer> getAddrFromSymTable(Collection<Value> symTableValues, HashMap<Integer, Value> heapTable) {
        List<Integer> allAddresses = new ArrayList<>();
        symTableValues.stream().
                filter(val -> val instanceof RefValue)
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

    public void oneStepForAllPrograms(List<ProgramState> programStates) throws InterruptedException {
//        programStates.forEach(programState -> {
//            try {
//                repository.logPrgStateExec(programState);
//                display(programState);
//            } catch (IOException | ADTException e) {
//                System.out.println(e.getMessage());
//            }
//        });
        List<Callable<ProgramState>> callList = programStates.stream()
                .map((ProgramState p) -> (Callable<ProgramState>) (p::oneStep))
                .collect(Collectors.toList());

        List<ProgramState> newProgramList = executorService.invokeAll(callList).stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (ExecutionException | InterruptedException e) {
                        System.out.println(e.getMessage());
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .toList();

        programStates.addAll(newProgramList);
        conservativeGarbageCollector(programStates);

        programStates.forEach(programState -> {
            try {
                repository.logPrgStateExec(programState);
            } catch (IOException | ADTException e) {
                System.out.println(e.getMessage());
            }
        });
        repository.setProgramStates(programStates);
    }

    public void allStep() throws InterruptedException, ExpressionEvaluationException, ADTException, StatementExecutionException, IOException {
        executorService = Executors.newFixedThreadPool(2);
        List<ProgramState> programStates = removeCompletedPrg(repository.getProgramList());
        while (!programStates.isEmpty()) {
            oneStepForAllPrograms(programStates);
            System.out.println(programStates.get(0).getHeap());
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