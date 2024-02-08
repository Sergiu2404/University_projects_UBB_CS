package view;

import exceptions.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TextMenu {
    private Map<String, Command> commands;

    public TextMenu()
    {
        this.commands = new HashMap<>();
    }

    public void addCommand(Command c)
    {
        commands.put(c.getKey(), c);
    }

    private void printMenu()
    {
        for(Command com: commands.values())
        {
            String line = String .format("%s : %s", com.getKey(), com.getDescription());
            System.out.println(line);
        }
    }

    public void show() throws ExpressionEvaluationException, DivisionByZero, HeapException, IOException, StackException, StatementExecutionException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        while (true)
        {
            this.printMenu();

            System.out.println("input option: ");
            String key = scanner.nextLine();

            Command com = commands.get(key);
            if(com == null) {
                System.out.println("invalid command");
                continue;
            }
            com.execute();
        }
    }
}
