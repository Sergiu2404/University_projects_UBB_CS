package view;

import controller.Controller;
import exceptions.*;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class RunExample1 extends Command{
    private Controller ctrl;
    public RunExample1(String key, String description, Controller ctrl)
    {
        super(key,description);
        this.ctrl=ctrl;
    }

    public void execute() throws ExpressionEvaluationException, DivisionByZero, HeapException, IOException, StackException, StatementExecutionException, InterruptedException {
        System.out.println("1-step by step");
        System.out.println("2-see results after all-steps-execution");
        Scanner sc = new Scanner(System.in);
        String option = sc.nextLine();

        ctrl.setDisplayOption(Objects.equals(option, "1"));
        ctrl.allStep();
    }
}
