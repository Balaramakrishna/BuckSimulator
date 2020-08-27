/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bucksimulator.controller;

import bucksimulator.model.Simulation;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.ScatterChart;

/**
 * FXML Controller class
 *
 * @author balaram
 */
public class GPSLiveController implements Initializable {

    @FXML
    private ScatterChart<Number, Number> gpsLiveChart;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO 
    }    

    public void setData(Simulation sim)
    {
        gpsLiveChart.getData().addAll(sim.getGpsLiveDataProperty());
        gpsLiveChart.layout();
    }    
}
