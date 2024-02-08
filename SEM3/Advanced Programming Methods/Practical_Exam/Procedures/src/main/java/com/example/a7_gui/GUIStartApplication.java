package com.example.a7_gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
public class GUIStartApplication extends Application {
    @Override
    public void start(Stage stage1) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GUIStartApplication.class.getResource("choose_controller.fxml"));
        Parent programListRoot = fxmlLoader.load();

        Scene programListScene = new Scene(programListRoot, 750, 750);
        ChooseController chooseController = fxmlLoader.getController();
        stage1.setScene(programListScene);


        FXMLLoader executorLoader = new FXMLLoader(GUIStartApplication.class.getResource("program_execution_tables.fxml"));
        Parent executorRoot = executorLoader.load();

        Scene executorScene = new Scene(executorRoot, 1100, 600);
        ProgramExecutionTables executorCtrl = executorLoader.getController();
        chooseController.setProgramExecutor(executorCtrl);

        Stage stage2 = new Stage();
        stage2.setScene(executorScene);

        stage2.show();
        stage1.show();
    }

    public static void main(String[] args) {
        launch();
    }



    //public class MainApp extends Application {
//    @Override
//    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("choose_controller.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
//        stage.setTitle("Hello!");
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    public static void main(String[] args) {
//        launch();
//    }
//}
}