/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bucksimulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

/**
 *
 * @author balaram
 */
public interface Simulation extends Runnable{

    IntegerProperty getSimprogressProperty();
    BooleanProperty getSimStartedProperty();
    BooleanProperty getSimNotStartedProperty();
    BooleanProperty getSimCompletedProperty();
    BooleanProperty getSimInProgressProperty();
    BooleanProperty getSimNotInProgressProperty();
    BooleanProperty getSimPausedProperty();
    BooleanProperty getSimSavedProperty();
    ObservableList getGpsLiveDataProperty();
    XYChart.Series getRClXYSeries();
    XYChart.Series getRCLXGTSeries();
    XYChart.Series getRCLYGTSeries();
    XYChart.Series getThroughput();
    XYChart.Series getThroughputLC();
    XYChart.Data getRadioContactRatio();
    void exportSimulationData(String folderPath);
    ObservableList<GPSLog> getT1GrazeLog();
    ObservableList<GPSLog> getT2GrazeLog();
    ObservableList<GPSLog> getT3GrazeLog();
    ObservableList<GPSLog> getBHGrazeLog();
    ObservableList<GPSLog> getMHGrazeLog();
    ObservableList<GPSLog> getFHGrazeLog();
    ObservableList<GPSLog> getNFGrazeLog();
    ObservableList<GPSLog> getW1GrazeLog();
    void pauseSim();
    void resumeSim();
    void signalTerminate();
    String simName();
    ObservableList<XYChart.Series<Number,Number>> getGPSLog();
    ObservableList<XYChart.Series<Number,Number>> getDTNLog();
    ArrayList<Boolean> getGLCheckBoxTrack();
    ArrayList<Boolean> getDCCheckBoxTrack();
    int saveSimData();
    ObservableList<PacktTallyModel> getPacktTallyData();
    HashMap<Integer,HashMap<Integer,ArrayList<Integer>>>  getDataTranferMapXY();
    HashMap<Integer,HashMap<Integer,Integer>>  getDataTranferMapXT();
    HashMap<Integer,HashMap<Integer,Integer>>  getDataTranferMapYT();
}
