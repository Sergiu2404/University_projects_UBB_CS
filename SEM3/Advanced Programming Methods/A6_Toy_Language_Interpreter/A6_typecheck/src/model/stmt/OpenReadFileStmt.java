package model.stmt;

import exceptions.*;
import model.expressions.IExpression;
import model.programState.ProgramState;
import model.types.IType;
import model.types.StringType;
import model.utils.MyIDictionary;
import model.values.IValue;
import model.values.StringValue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class OpenReadFileStmt implements IStmt{
    private final IExpression exp;

    public OpenReadFileStmt(IExpression e){
        this.exp=e;
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws InvalidTypeException {
        if(exp.typecheck(typeEnv).equals(new StringType()))
            return typeEnv;
        else throw new InvalidTypeException("Inner expression must be a string");
    }

    public ProgramState execute(ProgramState state) throws FileNotFoundException, StatementExecutionException, ExpressionEvaluationException, DivisionByZero, HeapException {
        IValue val = exp.eval(state.getSymbolTable(), state.getHeapTable());

        if(val.getType().equals(new StringType()))
        {
            StringValue fileName = (StringValue) val;
            MyIDictionary<String, BufferedReader> fileTable = state.getFileTable();
            if(!fileTable.isDefined(fileName.getValue()))
            {
                BufferedReader bufR;
                bufR = new BufferedReader(new FileReader(fileName.getValue()));

                fileTable.put(fileName.getValue(), bufR);
                state.setFileTable(fileTable);
            }
            else throw new StatementExecutionException("File already opened");
        }
        else throw new StatementExecutionException("File name should be of type string");
        return state;
    }

    public IStmt deepcopy()
    {
        return new OpenReadFileStmt(exp.deepcopy());
    }

    @Override
    public String toString() {
        return "OpenReadFileStmt{" +
                "exp=" + exp +
                '}';
    }
}
