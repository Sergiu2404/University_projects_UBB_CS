// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
//========================================================
//1. Intr-o parcare exista masini, motociclete
//        si biciclete. Sa se afiseze toate vehiculele
//        de culoare rosie.
//========================================================
import controller.Controller;
import repo.Repository;
import repo.RepositoryInterface;
import view.View;

public class Main {
    public static void main(String[] args) {

//        ArrayList<model.Vehicle> vehicles=new ArrayList<model.Vehicle>();
//        model.Vehicle bike1=new Bike(1,"bycicle","red");
//        model.Vehicle car1=new Car(2,"car","blue");
//        model.Vehicle motorcycle1=new model.Motorcycle(2,"motorcycle","green");

        RepositoryInterface repository=new Repository();
        Controller controller=new Controller(repository);
        View ui=new View(controller);

        ui.run_menu();


    }
}