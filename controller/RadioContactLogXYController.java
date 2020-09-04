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
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author balaram
 */
public class RadioContactLogXYController implements Initializable
{
    Simulation currentSim;
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
        currentSim = sim;
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
        if (currentSim.getSimStartedProperty().get() || currentSim.getSimCompletedProperty().get())
        {
            for (XYChart.Series<Number, Number> s : radioContactLogChart.getData())
            {
                for (XYChart.Data<Number, Number> d : s.getData())
                {
                    if (d.getYValue().intValue() != 9)
                    {
                        int n = currentSim.getDataTranferMapXY().get(d.getXValue()).get(d.getYValue()).size();
                        Tooltip tt = new Tooltip("Node "+d.getXValue().toString() + " sent to node " + d.getYValue().toString() + ", " + n + " times");
                        tt.setShowDelay(Duration.millis(20));
                        Tooltip.install(d.getNode(), tt);
                    }
                }
            }
        }
    }

    @FXML
    private void setZoomStartLocation(MouseEvent event)
    {
    }
    
    public ScatterChart<?, ?> getChart()
    {
        return radioContactLogChart;
    }
    
}
