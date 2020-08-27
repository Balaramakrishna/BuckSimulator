/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bucksimulator.controller;

import bucksimulator.model.DBConnect;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author balaram
 */
public class SplashScreenController implements Initializable
{
    volatile Stage mainStage;
    Thread splashThread;
    SimController controller;
    @FXML
    private Label appName;
    @FXML
    private Pane splashScreen;
    @FXML
    private AnchorPane splash;
    @FXML
    private Label developer;
    @FXML
    private Label algorithmdesigner;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        try
        {
            launchMainStage();
        }
        catch (IOException ex)
        {
            Logger.getLogger(SplashScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        splashScreen.getStyleClass().add("pane-background");
        Task splashTask = new Task() {
            @Override
            protected String call() throws InterruptedException {
                String s = "Buck Simulator";
                int k = 1;
                for (int i = 1; i <= s.length(); i++) {
                    updateMessage(s.substring(0,i));
                    if(i%2 == 0)
                    {
                        splashScreen.getStyleClass().add("pane-background"+k);
                        k = k+1;
                    }
                    Thread.sleep(200);
                }
                return s;
            };
        };
        appName.textProperty().bind(splashTask.messageProperty());
        showSplash(splashTask, () ->
        {
           mainStage.show();
        });
        splashThread= new Thread(splashTask);
        splashThread.start();
    }
    
    void showSplash(Task<?> splashTask, InitCompletionHandler initCompletionHandler)
    {
        splashTask.stateProperty().addListener((observableValue, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                final Stage stage = (Stage)splash.getScene().getWindow();
                FadeTransition fadeSplash = new FadeTransition(Duration.seconds(1.2), splash);
                fadeSplash.setFromValue(1.0);
                fadeSplash.setToValue(0.0);
                fadeSplash.setOnFinished(e -> 
                {
                    stage.close();
                });
                fadeSplash.play();
                initCompletionHandler.complete();
            }
        });
    }
    
    public interface InitCompletionHandler 
    {
        void complete();
    }
    
    void launchMainStage() throws IOException
    {
        FXMLLoader loader = new FXMLLoader(SplashScreenController.class.getResource("/bucksimulator/ui/SimulationUI.fxml"));
        Scene scene = new Scene(loader.load());
        mainStage = new Stage(StageStyle.DECORATED);
        controller = loader.<SimController>getController();
        mainStage.setTitle("Buck Simulator");
        mainStage.setScene(scene);
        mainStage.getIcons().add(0,new Image(SplashScreenController.class.getResourceAsStream("icon_black_buck.jpeg")));
        mainStage.setResizable(true);
        mainStage.setOnHidden(e -> Platform.exit());
        mainStage.setOnShowing((WindowEvent event) -> {
            controller.setPreferences();
	});
        mainStage.setOnShown((WindowEvent event) -> {
            event.consume();
	});
        mainStage.setOnCloseRequest((WindowEvent event) -> {
            controller.killTasks();
            DBConnect.DBShutdown();
            //event.consume(); this consumes the event and prevents the window from closing if used here.
	});
    }
}
