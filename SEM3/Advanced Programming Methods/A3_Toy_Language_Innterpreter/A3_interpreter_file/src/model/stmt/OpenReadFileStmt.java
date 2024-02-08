package model.stmt;

import exceptions.DivisionByZero;
import exceptions.ExpressionEvaluationException;
import exceptions.StatementExecutionException;
import model.expressions.IExpression;
import model.programState.ProgramState;
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

    public ProgramState execute(ProgramState state) throws FileNotFoundException, StatementExecutionException, ExpressionEvaluationException, DivisionByZero
    {
        IValue val = exp.eval(state.getSymbolTable());

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
