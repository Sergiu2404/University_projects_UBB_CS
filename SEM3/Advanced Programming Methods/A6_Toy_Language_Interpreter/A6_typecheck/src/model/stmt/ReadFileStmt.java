package model.stmt;

import exceptions.*;
import model.expressions.IExpression;
import model.programState.ProgramState;
import model.types.IType;
import model.types.IntType;
import model.types.StringType;
import model.utils.MyIDictionary;
import model.values.IValue;
import model.values.IntValue;
import model.values.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadFileStmt implements  IStmt{
    private final IExpression exp;
    private final String varName;

    public ReadFileStmt(IExpression e, String s)
    {
        this.exp=e;
        this.varName=s;
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws InvalidTypeException {
        if(exp.typecheck(typeEnv).equals(new StringType()))
            if(typeEnv.lookup(varName).equals(new IntType()))
                return typeEnv;
            else throw new InvalidTypeException("Variable must be int type");
        else throw new InvalidTypeException("Inner expression must be a string");
    }

    public ProgramState execute(ProgramState state) throws ExpressionEvaluationException, DivisionByZero, IOException, StatementExecutionException, HeapException {
        MyIDictionary<String, IValue> symTable = state.getSymbolTable();
        MyIDictionary<String, BufferedReader> fileTable = state.getFileTable();

        if(symTable.isDefined(varName))
        {
            IValue val = symTable.lookup(varName); //get value of variable inside we read the nr from the file

            if(val.getType().equals(new IntType()))
            {
                IValue val2 = exp.eval(symTable, state.getHeapTable()); //get name of the file

                if(val2.getType().equals(new StringType()))
                {
                    StringValue fileName = (StringValue) val2;

                    if(fileTable.isDefined(fileName.getValue()))
                    {
                        BufferedReader bufR = fileTable.lookup(fileName.getValue());

                        String line = bufR.readLine();

                        if(line == null)
                            line="0";
                        symTable.put(varName , new IntValue(Integer.parseInt(line)));
                    }
                    else throw new StatementExecutionException("File table doesn't contain " + fileName.toString());
                }
                else throw new StatementExecutionException(String.format("%s shpuld be of type string", val2));
            }
            else throw new StatementExecutionException(String.format("%s should be of type int", val));
        }
        else throw new StatementExecutionException(String.format("%s is not defined in symbol table", varName));
        return state;
    }

    public IStmt deepcopy()
    {
        return new ReadFileStmt(exp.deepcopy(), varName);
    }

    @Override
    public String toString() {
        return "ReadFileStmt{" +
                "exp=" + exp +
                ", varName='" + varName + '\'' +
                '}';
    }
}
