package com.example.a7_gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
public class MainApp extends Application {
    @Override
    public void start(Stage stage1) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("alternate_controller.fxml"));
        Parent programListRoot = fxmlLoader.load();

        Scene programListScene = new Scene(programListRoot, 750, 750);
        AlternateController prgSwitchCtrl = fxmlLoader.getController();
        stage1.setScene(programListScene);


        FXMLLoader executorLoader = new FXMLLoader();
        executorLoader.setLocation(MainApp.class.getResource("program_executor.fxml"));
        Parent executorRoot = executorLoader.load();

        Scene executorScene = new Scene(executorRoot, 1000, 600);
        ProgramExecutor executorCtrl = executorLoader.getController();
        prgSwitchCtrl.setExecutorController(executorCtrl);

        Stage stage2 = new Stage();
        stage2.setScene(executorScene);

        stage2.show();
        stage1.show();
    }

    public static void main(String[] args) {
        launch();
    }
}