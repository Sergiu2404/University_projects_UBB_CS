package view;

import controller.Controller;
import exceptions.*;

import java.io.IOException;
import java.util.Scanner;

public class RunExample1 extends Command{
    private Controller ctrl;
    public RunExample1(String key, String description, Controller ctrl)
    {
        super(key,description);
        this.ctrl=ctrl;
    }

    public void execute()
    {
        try{
            System.out.println("1-step by step");
            System.out.println("2-see results after all-steps-execution");
            Scanner sc = new Scanner(System.in);
            String displayOption = sc.nextLine();

            if(displayOption.equals("2")) {
                ctrl.executeAllSteps();
                System.out.println(ctrl.getCurrentProgram());
            }
            else{
                while(!ctrl.isEmpty())
                {
                    ctrl.executeOneStep(ctrl.getCurrentProgram());
                }
                this.ctrl.logPrgStateExecController();
            }
        }catch(DictionaryException | ExpressionEvaluationException | StatementExecutionException | StackException | DivisionByZero | IOException | HeapException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
