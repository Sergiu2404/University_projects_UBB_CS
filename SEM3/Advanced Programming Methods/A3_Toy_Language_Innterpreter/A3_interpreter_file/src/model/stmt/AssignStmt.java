package model.stmt;

import exceptions.DivisionByZero;
import exceptions.ExpressionEvaluationException;
import model.expressions.IExpression;
import model.programState.ProgramState;
import model.types.IType;
import model.utils.MyIDictionary;
import model.utils.MyIStack;
import model.values.IValue;

public class AssignStmt implements IStmt{
    private final String varId;
    private final IExpression expToAssign;

    public AssignStmt(String id, IExpression e)
    {
        this.varId=id;
        this.expToAssign=e;
    }
    public String getVarId(){return this.varId;}
    public IExpression getExpToAssign(){return this.expToAssign;}


    public ProgramState execute(ProgramState state) throws ExpressionEvaluationException, DivisionByZero {
        //MyIStack<IStmt> exeStack = state.getExeStack();
        MyIDictionary<String, IValue> symTbl = state.getSymbolTable();

        if(!symTbl.isDefined(varId))
            throw new ExpressionEvaluationException("Var "+varId+" never defined");
        else{
            IValue valToAssign = expToAssign.eval(symTbl);
            IType typeId = (symTbl.lookup(varId)).getType();

            if (valToAssign.getType().equals(typeId))
                symTbl.put(varId, valToAssign);
            else
                throw new ExpressionEvaluationException("declared type of variable" + varId + " and type of the assigned value do not match");
        }
        state.setSymTable(symTbl);
        return state;
    }

    public IStmt deepcopy()
    {
        return new AssignStmt(varId, expToAssign.deepcopy());
    }


    @Override
    public String toString() {
        return "AssignStmt{" +
                "var name='" + varId + '\'' +
                ", expToAssign=" + expToAssign +
                '}';
    }
}
