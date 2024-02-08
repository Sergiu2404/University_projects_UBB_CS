package model.utils;

import java.util.ArrayList;
import java.util.List;

public class MyList<T> implements MyIList<T>{
    List<T> items;

    public MyList(){items=new ArrayList<>();}

    @Override
    public void add(T elem){items.add(elem);}

    @Override
    public String toString()
    {
        return ""+items;
    }
}
