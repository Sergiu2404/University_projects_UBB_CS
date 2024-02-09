package com.example.a7_gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage1) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("alternate_controllersFXML.fxml"));
        Parent programListRoot = fxmlLoader.load();

        Scene programListScene = new Scene(programListRoot, 700, 700);
        AlternateControllers prgSwitchCtrl = fxmlLoader.getController();
        stage1.setScene(programListScene);


        FXMLLoader executorLoader = new FXMLLoader(Application.class.getResource("current_program_executorFXML.fxml"));
        Parent executorRoot = executorLoader.load();

        Scene executorScene = new Scene(executorRoot, 900, 600);
        CurrentProgramExecutor executorCtrl = executorLoader.getController();
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