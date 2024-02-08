package com.example.a7_gui.model.my_utils;

import com.example.a7_gui.exceptions.DataStructureException;
import com.example.a7_gui.model.program_statements.IStatement;
import javafx.util.Pair;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class MyProcedureTable implements MyIProcedureTable {
    private HashMap<String,Pair<List<String>,IStatement>> procedureTable;
    public MyProcedureTable(){
        this.procedureTable=new HashMap<>();
    }
    @Override
    public boolean isDefined(String key) {
        return this.procedureTable.containsKey(key);
    }

    @Override
    public void put(String key, Pair<List<String>, IStatement> value) {
        this.procedureTable.put(key,value);
    }

    @Override
    public Pair<List<String>, IStatement> lookUp(String key) throws DataStructureException {
        if(!isDefined(key)){
            throw new DataStructureException("key is not defined");
        }
        return this.procedureTable.get(key);
    }

    @Override
    public void update(String key, Pair<List<String>, IStatement> value) throws DataStructureException {
        if(!isDefined(key)){
            throw new DataStructureException("key is not defined");
        }
        this.procedureTable.put(key,value);
    }

    @Override
    public Collection<Pair<List<String>, IStatement>> values() {
        return this.procedureTable.values();
    }

    @Override
    public void remove(String key) throws DataStructureException {
        if(! isDefined(key)){
            throw new DataStructureException("key is not defined");
        }
        this.procedureTable.remove(key);
    }

    @Override
    public Set<String> keySet() {
        return procedureTable.keySet();
    }

    @Override
    public HashMap<String, Pair<List<String>, IStatement>> getContent() {
        return procedureTable;
    }

    @Override
    public MyIDictionary<String, Pair<List<String>, IStatement>> deepCopy() throws DataStructureException {
        MyIDictionary<String, Pair<List<String>, IStatement>> toReturn = new MyDictionary<>();
        for (String key: keySet())
            try {
                toReturn.put(key, lookUp(key));
            } catch (DataStructureException e) {
                throw new DataStructureException(e.getMessage());
            }
        return toReturn;
    }

    @Override
    public String toString() {
        return "ProceduresTable{" +
                "procedureTable=" + procedureTable +
                '}';
    }
}
