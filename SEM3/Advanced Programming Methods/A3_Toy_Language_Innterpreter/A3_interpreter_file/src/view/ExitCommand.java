package view;

public class ExitCommand extends Command{
    public ExitCommand(String key, String description)
    {
        super(key,description);
    }

    public void execute()
    {
        System.exit(0);
    }
}
