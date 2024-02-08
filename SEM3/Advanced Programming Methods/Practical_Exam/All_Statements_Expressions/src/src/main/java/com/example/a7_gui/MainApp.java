package com.example.a7_gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
//ORIGINAL VERSION
//public class MainApp extends Application {
//    @Override
//    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("program-switch-controller.fxml"));
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
public class MainApp extends Application {
    @Override
    public void start(Stage stage1) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(MainApp.class.getResource("program-switch-controller.fxml"));
        Parent programListRoot = fxmlLoader.load();

        Scene programListScene = new Scene(programListRoot, 600, 600);
        ProgramSwitchController prgSwitchCtrl = fxmlLoader.getController();
        stage1.setTitle("Choose a program");
        stage1.setScene(programListScene);
        stage1.show();


        FXMLLoader executorLoader = new FXMLLoader();
        executorLoader.setLocation(MainApp.class.getResource("executor-controller.fxml"));
        Parent executorRoot = executorLoader.load();

        Scene executorScene = new Scene(executorRoot, 1000, 500);
        ExecutorController executorCtrl = executorLoader.getController();
        prgSwitchCtrl.setExecutorController(executorCtrl);

        Stage stage2 = new Stage();
        stage2.setTitle("Toy language interpreter");
        stage2.setScene(executorScene);
        stage2.show();
    }

    public static void main(String[] args) {
        launch();
    }
}