package exceptions;

import model.types.IType;

public class StatementExecutionException extends Exception {
    public StatementExecutionException(String m)
    {
        super(m);
    }
}
