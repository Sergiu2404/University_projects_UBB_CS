import controller.Controller;
import repo.IRepository;
import repo.Repository;
import view.View;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {

        IRepository repo = new Repository();
        Controller ctrl = new Controller(repo);
        View UI = new View(ctrl);

        UI.runMenu();
    }
}