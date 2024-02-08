package view;

import controller.Controller;
import custom_exceptions.ArraySizeException;
import custom_exceptions.InvalidInputException;
import custom_exceptions.InvalidObject;
import custom_exceptions.InvalidType;
import model.Vehicle;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class View {
    private Controller controller;

    public View(Controller ctrl)
    {
        this.controller=ctrl;
    }

    public void print_menu()
    {
        System.out.println("MENU========================================");
        System.out.println("1-display all vehicles");
        System.out.println("2-display all vehicles of a given color");
        System.out.println("3-add vehicle to the list");
        System.out.println("4-remove vehicle from the list");
        System.out.println("5-exit program");
        System.out.println("MENU========================================");
    }

    public void run_menu(){
        this.controller.initController();

        Scanner scanner=new Scanner(System.in);
        String command="";

        while(true)
        {
            print_menu();
            System.out.println("Give command number: ");


            try {
                command=scanner.nextLine();

                if (command.equals("1"))
                    printAllVehicles();
                else if (command.equals("2"))
                    printColorVehicles();
                else if (command.equals("3"))
                    addVehcle();
                else if (command.equals("4"))
                    deleteVehicle();
                else if (command.equals("5"))
                    System.exit(0);
                else
                    throw new InvalidInputException("Invalid option, try again");

            }catch(InvalidInputException | InvalidType | InvalidObject | ArraySizeException ie){
                System.out.println(ie.getMessage());
            }
        }
    }

    public void printAllVehicles()
    {
        for(Vehicle v: this.controller.getAllVehicles())
            System.out.println(v.representation());
    }

    public void printColorVehicles()
    {
        Scanner scanner=new Scanner(System.in);
        System.out.println("Give vehicle color: ");
        String color=scanner.nextLine();

        for(Vehicle v:this.controller.getAllGivenColorVehicles(color))
            System.out.println(v.representation());
    }

    public void addVehcle() throws InvalidType,InvalidObject,ArraySizeException {

        Scanner scanner=new Scanner(System.in);
        String type,color,idString;
        int id;
        String regex="[0-9]+";
        Pattern p= Pattern.compile(regex);

        try {
            System.out.println("Give id: ");

            idString = scanner.nextLine();
            Matcher m= p.matcher(idString);
            if(!m.matches())
                throw new InvalidInputException("ID must be an integer.");
            else id=Integer.parseInt(idString);

            System.out.println("Give type (car/bike/motorcycle): ");
            type = scanner.nextLine();
            System.out.println("Give color: ");
            color = scanner.nextLine();

            if (!this.controller.addVehicle(id, type, color))
                System.out.println("model.Vehicle already exists");
            else {
                System.out.println("model.Vehicle added");
            }
        }catch(InvalidInputException e1){
            System.out.println(e1.getMessage());
        } //catch (InvalidType | InvalidObject | ArraySizeException e2) {
        //    throw new InvalidType(e2.getMessage());
        //}


    }

    public void deleteVehicle()
    {
        Scanner scanner=new Scanner(System.in);
        int id=-1;
        String type="",color="",idString="";
        String regex="[0-9]+";
        Pattern p= Pattern.compile(regex);

        try {
            System.out.println("Give id: ");
            idString = scanner.nextLine();
            Matcher m = p.matcher(idString);
            if (!m.matches())
                throw new InvalidInputException("ID must be an integer.");
            else {
                id = Integer.parseInt(idString);
                if (!this.controller.deleteVehicle(id,type, color))
                    System.out.println("model.Vehicle does not exist exists");
                else {
                    //this.controller.deleteVehicle(id,type,color);
                    System.out.println("model.Vehicle deleted");
                }
            }
        }catch(InvalidInputException iie){
            System.out.println("ID must be an integer.");
        }

//        System.out.println("Give type (car/bike/motorcycle): ");
//        type = scanner.nextLine();
//        System.out.println("Give color: ");
//        color = scanner.nextLine();

    }


}
