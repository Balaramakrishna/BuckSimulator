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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author balaram
 */
public class DataCenterLogController implements Initializable
{
    Simulation currentSim;
    ArrayList<CheckBox> dcCBList = new ArrayList<>();
    ArrayList<BooleanProperty> dcCBDisPropList = new ArrayList<>();
    @FXML
    private AnchorPane rclAnchorPane;
    @FXML
    private ScatterChart<Number, Number> dataCenterChart;
    @FXML
    private NumberAxis yAxisDC;
    @FXML
    private NumberAxis xAxisDC;
    @FXML
    private CheckBox t1CBDC;
    @FXML
    private CheckBox t2CBDC;
    @FXML
    private CheckBox t3CBDC;
    @FXML
    private CheckBox bhCBDC;
    @FXML
    private CheckBox mhCBDC;
    @FXML
    private CheckBox fhCBDC;
    @FXML
    private CheckBox nfCBDC;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        dataCenterChart.getStylesheets().add("/bucksimulator/css/GpsLiveChartBackground.css");
        dcCBList.add(t1CBDC);
        dcCBList.add(t2CBDC);
        dcCBList.add(t3CBDC);
        dcCBList.add(bhCBDC);
        dcCBList.add(mhCBDC);
        dcCBList.add(fhCBDC);
        dcCBList.add(nfCBDC);
        for (int i = 0; i < 7; i++)
        {
            SimpleBooleanProperty sbp1 = new SimpleBooleanProperty();
            dcCBDisPropList.add(sbp1);
            dcCBList.get(i).disableProperty().bind(sbp1);
        }
    }    

    public void setData(Simulation sim)
    {
        currentSim = sim;
        for (int i = 0; i < 7; i++)
        {
            dcCBDisPropList.get(i).set(sim.getDTNLog().get(i).getData().isEmpty());
        }
        dataCenterChart.getData().addAll(sim.getDTNLog());
        currentSim.getSimCompletedProperty().addListener(e -> 
        {
            for (int i = 0; i < 7; i++)
            {
                dcCBDisPropList.get(i).set(currentSim.getDTNLog().get(i).getData().isEmpty());
            }
        });
    }
    
    public ScatterChart<?,?> getChart()
    {
        return dataCenterChart;
    }
    
    @FXML
    private void t1DCChecked(ActionEvent event) {
        if (t1CBDC.isSelected())
        {
            dataCenterChart.getData().add(currentSim.getDTNLog().get(0));
        }
        else
        {
            dataCenterChart.getData().remove(currentSim.getDTNLog().get(0));
        }
        currentSim.getDCCheckBoxTrack().set(0, t1CBDC.isSelected());
    }

    @FXML
    private void t2DCChecked(ActionEvent event) {
        if (t2CBDC.isSelected())
        {
            dataCenterChart.getData().add(currentSim.getDTNLog().get(1));
        }
        else
        {
            dataCenterChart.getData().remove(currentSim.getDTNLog().get(1));
        }
        currentSim.getDCCheckBoxTrack().set(1, t2CBDC.isSelected());
    }

    @FXML
    private void t3DCChecked(ActionEvent event) {
        if (t3CBDC.isSelected())
        {
            dataCenterChart.getData().add(currentSim.getDTNLog().get(2));
        }
        else
        {
            dataCenterChart.getData().remove(currentSim.getDTNLog().get(2));
        }
        currentSim.getDCCheckBoxTrack().set(2, t3CBDC.isSelected());
    }

    @FXML
    private void bhDCChecked(ActionEvent event) {
        if (bhCBDC.isSelected())
        {
            dataCenterChart.getData().add(currentSim.getDTNLog().get(3));
        }
        else
        {
            dataCenterChart.getData().remove(currentSim.getDTNLog().get(3));
        }
        currentSim.getDCCheckBoxTrack().set(3, bhCBDC.isSelected());
    }

    @FXML
    private void mhDCChecked(ActionEvent event) {
        if (mhCBDC.isSelected())
        {
            dataCenterChart.getData().add(currentSim.getDTNLog().get(4));
        }
        else
        {
            dataCenterChart.getData().remove(currentSim.getDTNLog().get(4));
        }
        currentSim.getDCCheckBoxTrack().set(4, mhCBDC.isSelected());
    }

    @FXML
    private void fhDCChecked(ActionEvent event) {
        if (fhCBDC.isSelected())
        {
            dataCenterChart.getData().add(currentSim.getDTNLog().get(5));
        }
        else
        {
            dataCenterChart.getData().remove(currentSim.getDTNLog().get(5));
        }
        currentSim.getDCCheckBoxTrack().set(5, fhCBDC.isSelected());
    }

    @FXML
    private void nfDCChecked(ActionEvent event) {
        if (nfCBDC.isSelected())
        {
            dataCenterChart.getData().add(currentSim.getDTNLog().get(6));
        }
        else
        {
            dataCenterChart.getData().remove(currentSim.getDTNLog().get(6));
        }
        currentSim.getDCCheckBoxTrack().set(6, nfCBDC.isSelected());
    }

    private void validateCheckBoxes(MouseEvent event)
    {
        for (int i = 0; i < 7; i++)
        {
            dcCBDisPropList.get(i).set(currentSim.getDTNLog().get(i).getData().isEmpty());
        }
    }
    
}
