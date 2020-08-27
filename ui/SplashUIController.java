/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bucksimulator.ui;


import bucksimulator.controller.SplashScreenController;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;


/**
 * FXML Controller class
 *
 * @author balaram
 */
public class SplashUIController extends Application{

    SplashScreenController controller;
    public static void main(String[] args) {
        // TODO code application logic here
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(SplashUIController.class.getResource("SplashScreen.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(getClass().getResource("/bucksimulator/css/SplashStyle.css").toExternalForm());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.getIcons().add(0,new Image(SplashScreenController.class.getResourceAsStream("icon_black_buck.jpeg")));
        controller = loader.<SplashScreenController>getController();
        stage.setOnShowing((WindowEvent event) -> {
	});
        stage.setOnShown((WindowEvent event) -> {
	});
        stage.setOnCloseRequest((WindowEvent event) -> {
            //event.consume(); this consumes the event and prevents the window from closing if used here.
	});
        stage.show();
    }
}