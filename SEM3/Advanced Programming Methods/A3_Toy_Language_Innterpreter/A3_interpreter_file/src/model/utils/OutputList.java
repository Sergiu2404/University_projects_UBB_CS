package model.utils;

import java.util.ArrayList;
import java.util.List;

public class OutputList<T> implements MyIList<T>{

    List<T> outputList;

    public OutputList()
    {
        this.outputList=new ArrayList<>();
    }

    public void add(T elem)
    {
        this.outputList.add(elem);
    }

    public String toString()
    {
        return ""+this.outputList;
    }

    public List<T> getList(){return outputList;}
}
