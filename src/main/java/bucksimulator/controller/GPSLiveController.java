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
 * FXML Controller class.
 * This is used when the popout charts feature is used.
 * @author balaram
 */
public class GPSLiveController implements Initializable {

    @FXML
    private ScatterChart<Number, Number> gpsLiveChart;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO 
        gpsLiveChart.getStylesheets().add("/bucksimulator/css/GpsLiveChartBackground.css");
    }    

    /**
     * Chart will be associated with the simulation data.
     * @param sim 
     */
    public void setData(Simulation sim)
    {
        gpsLiveChart.getData().addAll(sim.getGpsLiveDataProperty());
        gpsLiveChart.layout();
    }
    
    /**
     * Returns the chart instance to be used during snapshot.User can save chart
     * by pressing ctrl + s.
     * @return ScatterChart
     */
    public ScatterChart<Number, Number> getChart()
    {
        return gpsLiveChart;
    }
}
