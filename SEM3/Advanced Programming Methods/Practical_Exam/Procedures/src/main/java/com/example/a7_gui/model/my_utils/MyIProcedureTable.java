package com.example.a7_gui.model.my_utils;

import com.example.a7_gui.exceptions.DataStructureException;
import com.example.a7_gui.model.program_statements.IStatement;
import javafx.util.Pair;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public interface MyIProcedureTable {
    boolean isDefined(String key);
    void put(String key, Pair<List<String>, IStatement> value);
    Pair<List<String>, IStatement> lookUp(String key) throws DataStructureException;
    void update(String key,  Pair<List<String>, IStatement> value) throws DataStructureException;
    Collection< Pair<List<String>, IStatement>> values();
    void remove(String key) throws DataStructureException;
    Set<String> keySet();
    HashMap<String,  Pair<List<String>, IStatement>> getContent();
    MyIDictionary<String, Pair<List<String>, IStatement>> deepCopy() throws DataStructureException;
}

