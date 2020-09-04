/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bucksimulator.controller;

import bucksimulator.model.Simulation;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author balaram
 */
public class GPSLogDataController implements Initializable
{
    Simulation currentSim;
    ArrayList<BooleanProperty> glCBDisPropList = new ArrayList<>();
    ArrayList<CheckBox> gpsLogCBList = new ArrayList<>();
    @FXML
    private AnchorPane rclAnchorPane;
    @FXML
    private ScatterChart<Number, Number> gpsLogChart;
    @FXML
    private CheckBox t1CBGL;
    @FXML
    private CheckBox t2CBGL;
    @FXML
    private CheckBox t3CBGL;
    @FXML
    private CheckBox bhCBGL;
    @FXML
    private CheckBox mhCBGL;
    @FXML
    private CheckBox fhCBGL;
    @FXML
    private CheckBox nfCBGL;
    @FXML
    private CheckBox w1CBGL;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        gpsLogChart.getStylesheets().add("/bucksimulator/css/GpsLiveChartBackground.css");
        gpsLogCBList.add(t1CBGL);
        gpsLogCBList.add(t2CBGL);
        gpsLogCBList.add(t3CBGL);
        gpsLogCBList.add(bhCBGL);
        gpsLogCBList.add(mhCBGL);
        gpsLogCBList.add(fhCBGL);
        gpsLogCBList.add(nfCBGL);
        gpsLogCBList.add(w1CBGL);
        for (int i = 0; i < 8; i++)
        {
            SimpleBooleanProperty sbp = new SimpleBooleanProperty();
            glCBDisPropList.add(sbp);
            gpsLogCBList.get(i).disableProperty().bind(sbp);
        }
    }    

    public ScatterChart<?,?> getChart()
    {
        return gpsLogChart;
    }
    
    public void setData(Simulation sim)
    {
        currentSim = sim;
        for (int i = 0; i < 8; i++) {
            glCBDisPropList.get(i).set(sim.getGPSLog().get(i).getData().isEmpty());
        }
        gpsLogChart.getData().addAll(sim.getGPSLog());
        currentSim.getSimCompletedProperty().addListener(e -> 
        {
            for (int i = 0; i < 8; i++)
            {
                glCBDisPropList.get(i).set(currentSim.getGPSLog().get(i).getData().isEmpty());
            }
        });
    }

    @FXML
    private void w1GLChecked(ActionEvent event) {
        if (w1CBGL.isSelected())
        {
            gpsLogChart.getData().add(currentSim.getGPSLog().get(7));
        }
        else
        {
            gpsLogChart.getData().remove(currentSim.getGPSLog().get(7));
        }
        currentSim.getGLCheckBoxTrack().set(7, w1CBGL.isSelected());
    }
    
    @FXML
    private void t1GLChecked(ActionEvent event) {
        if (t1CBGL.isSelected())
        {
            gpsLogChart.getData().add(currentSim.getGPSLog().get(0));
        }
        else
        {
            gpsLogChart.getData().remove(currentSim.getGPSLog().get(0));
        }
        currentSim.getGLCheckBoxTrack().set(0, t1CBGL.isSelected());
    }

    @FXML
    private void t2GLChecked(ActionEvent event) {
        if (t2CBGL.isSelected())
        {
            gpsLogChart.getData().add(currentSim.getGPSLog().get(1));
        }
        else
        {
            gpsLogChart.getData().remove(currentSim.getGPSLog().get(1));
        }
        currentSim.getGLCheckBoxTrack().set(1, t2CBGL.isSelected());
    }

    @FXML
    private void t3GLChecked(ActionEvent event) {
        if (t3CBGL.isSelected())
        {
            gpsLogChart.getData().add(currentSim.getGPSLog().get(2));
        }
        else
        {
            gpsLogChart.getData().remove(currentSim.getGPSLog().get(2));
        }
        currentSim.getGLCheckBoxTrack().set(2, t3CBGL.isSelected());
    }

    @FXML
    private void bhGLChecked(ActionEvent event) {
        if (bhCBGL.isSelected())
        {
            gpsLogChart.getData().add(currentSim.getGPSLog().get(3));
        }
        else
        {
            gpsLogChart.getData().remove(currentSim.getGPSLog().get(3));
        }
        currentSim.getGLCheckBoxTrack().set(3, bhCBGL.isSelected());
    }

    @FXML
    private void mhGLChecked(ActionEvent event) {
        if (mhCBGL.isSelected())
        {
            gpsLogChart.getData().add(currentSim.getGPSLog().get(4));
        }
        else
        {
            gpsLogChart.getData().remove(currentSim.getGPSLog().get(4));
        }
        currentSim.getGLCheckBoxTrack().set(4, mhCBGL.isSelected());
    }

    @FXML
    private void fhGLChecked(ActionEvent event) {
        if (fhCBGL.isSelected())
        {
            gpsLogChart.getData().add(currentSim.getGPSLog().get(5));
        }
        else
        {
            gpsLogChart.getData().remove(currentSim.getGPSLog().get(5));
        }
        currentSim.getGLCheckBoxTrack().set(5, fhCBGL.isSelected());
    }

    @FXML
    private void nfGLChecked(ActionEvent event) {
        if (nfCBGL.isSelected())
        {
            gpsLogChart.getData().add(currentSim.getGPSLog().get(6));
        }
        else
        {
            gpsLogChart.getData().remove(currentSim.getGPSLog().get(6));
        }
        currentSim.getGLCheckBoxTrack().set(6, nfCBGL.isSelected());
    }
    
}
