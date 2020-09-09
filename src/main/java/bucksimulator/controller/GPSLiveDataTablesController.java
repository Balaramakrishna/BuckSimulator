/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bucksimulator.controller;

import bucksimulator.model.GPSLog;
import bucksimulator.model.Simulation;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class.
 * This is used when the popout charts feature is used.
 * @author balaram
 */
public class GPSLiveDataTablesController implements Initializable
{

    @FXML
    private AnchorPane GPSLiveDataAnchorPane;
    @FXML
    private Label tableTitle;
    @FXML
    private TableView<GPSLog> w1DataTable;
    @FXML
    private TableColumn<?, ?> w1xColumn;
    @FXML
    private TableColumn<?, ?> w1yColumn;
    @FXML
    private TableView<GPSLog> nfDataTable;
    @FXML
    private TableColumn<?, ?> nfxColumn;
    @FXML
    private TableColumn<?, ?> nfyColumn;
    @FXML
    private TableView<GPSLog> fhDataTable;
    @FXML
    private TableColumn<?, ?> fhxColumn;
    @FXML
    private TableColumn<?, ?> fhyColumn;
    @FXML
    private TableView<GPSLog> mhDataTable;
    @FXML
    private TableColumn<?, ?> mhxColumn;
    @FXML
    private TableColumn<?, ?> mhyColumn;
    @FXML
    private TableView<GPSLog> bhDataTable;
    @FXML
    private TableColumn<?, ?> bhxColumn;
    @FXML
    private TableColumn<?, ?> bhyColumn;
    @FXML
    private TableView<GPSLog> t3DataTable;
    @FXML
    private TableColumn<?, ?> t3xColumn;
    @FXML
    private TableColumn<?, ?> t3yColumn;
    @FXML
    private TableView<GPSLog> t2DataTable;
    @FXML
    private TableColumn<?, ?> t2xColumn;
    @FXML
    private TableColumn<?, ?> t2yColumn;
    @FXML
    private TableView<GPSLog> t1DataTable;
    @FXML
    private TableColumn<?, ?> t1xColumn;
    @FXML
    private TableColumn<?, ?> t1yColumn;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        t1xColumn.setCellValueFactory(new PropertyValueFactory<>("xCord"));
        t1yColumn.setCellValueFactory(new PropertyValueFactory<>("yCord"));
        t2xColumn.setCellValueFactory(new PropertyValueFactory<>("xCord"));
        t2yColumn.setCellValueFactory(new PropertyValueFactory<>("yCord"));
        t3xColumn.setCellValueFactory(new PropertyValueFactory<>("xCord"));
        t3yColumn.setCellValueFactory(new PropertyValueFactory<>("yCord"));
        bhxColumn.setCellValueFactory(new PropertyValueFactory<>("xCord"));
        bhyColumn.setCellValueFactory(new PropertyValueFactory<>("yCord"));
        mhxColumn.setCellValueFactory(new PropertyValueFactory<>("xCord"));
        mhyColumn.setCellValueFactory(new PropertyValueFactory<>("yCord"));
        fhxColumn.setCellValueFactory(new PropertyValueFactory<>("xCord"));
        fhyColumn.setCellValueFactory(new PropertyValueFactory<>("yCord"));
        nfxColumn.setCellValueFactory(new PropertyValueFactory<>("xCord"));
        nfyColumn.setCellValueFactory(new PropertyValueFactory<>("yCord"));
        w1xColumn.setCellValueFactory(new PropertyValueFactory<>("xCord"));
        w1yColumn.setCellValueFactory(new PropertyValueFactory<>("yCord"));
    }    
    
    /**
     * Binds the JavaFx tables with the simulation data. Table will be updated
     * dynamically to show the live data.
     * @param sim 
     */
    void setData(Simulation sim)
    {
        tableTitle.setText(sim.simName());
        new Thread(() ->
        {
            Platform.runLater(() -> {
            t1DataTable.setItems(sim.getT1GrazeLog());
            t2DataTable.setItems(sim.getT2GrazeLog());
            t3DataTable.setItems(sim.getT3GrazeLog());
            bhDataTable.setItems(sim.getBHGrazeLog());
            mhDataTable.setItems(sim.getMHGrazeLog());
            fhDataTable.setItems(sim.getFHGrazeLog());
            nfDataTable.setItems(sim.getNFGrazeLog());
            w1DataTable.setItems(sim.getW1GrazeLog());
            });
        }).start();
    }
}
