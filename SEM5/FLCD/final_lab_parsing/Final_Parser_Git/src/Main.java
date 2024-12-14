import controller.Program;
import ui.UI;

public class Main {

    public static void main(String[] args) {
        Program program = new Program();

        UI ui = new UI(program);
        ui.start();

    }
}