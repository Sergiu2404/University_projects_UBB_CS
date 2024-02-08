import controller.Controller;
import model.programState.ProgramState;
import repo.IRepository;
import repo.Repository;
import view.View;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {

        IRepository repo = new Repository("logFile1.txt"); //"C:\\Users\\Sergiu\\Desktop\\SEM3\\metode_avansate_de_programare\\A3\\A3_interpreter_file\\logFile.txt"
        Controller ctrl = new Controller(repo);
        View view = new View(ctrl);

        view.runMenu();
    }
}