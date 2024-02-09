package com.example.a7_gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
public class GUIStartApplication extends javafx.application.Application {
    @Override
    public void start(Stage stage1) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GUIStartApplication.class.getResource("alternate_controllers.fxml"));
        Parent programListRoot = fxmlLoader.load();

        Scene programListScene = new Scene(programListRoot, 650, 650);
        AlternateControllers alternateControlelrs = fxmlLoader.getController();
        stage1.setScene(programListScene);


        FXMLLoader executorLoader = new FXMLLoader(GUIStartApplication.class.getResource("program_execution_tables.fxml"));
        Parent executorRoot = executorLoader.load();

        Scene executorScene = new Scene(executorRoot, 1000, 650);
        ProgramExecutionTables executorCtrl = executorLoader.getController();
        alternateControlelrs.setExecutorController(executorCtrl);

        Stage stage2 = new Stage();
        stage2.setScene(executorScene);

        stage2.show();
        stage1.show();
    }

    public static void main(String[] args) {
        launch();
    }
}