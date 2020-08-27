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
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author balaram
 */
public class RadioContactLogXYController implements Initializable
{

    @FXML
    private ScatterChart<Number, Number> radioContactLogChart;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }
    
    void setData(Simulation sim)
    {
        radioContactLogChart.getData().add(sim.getRClXYSeries());
    }

    @FXML
    private void zoomChart(MouseEvent event)
    {
    }

    @FXML
    private void setZoomSize(MouseEvent event)
    {
    }

    @FXML
    private void addTooltipsForChart(MouseEvent event)
    {
    }

    @FXML
    private void setZoomStartLocation(MouseEvent event)
    {
    }
    
}
