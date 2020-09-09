/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bucksimulator.controller;

import bucksimulator.model.CaptureTimeHide;
import bucksimulator.model.DBConnect;
import bucksimulator.model.EpidemicMulticopy;
import bucksimulator.model.EpidemicSinglecopy;
import bucksimulator.model.ExpectedMeetingtime;
import bucksimulator.model.GPSLog;
import bucksimulator.model.GameTheoryRouting;
import bucksimulator.model.NewProphetRouting;
import bucksimulator.model.PacketTallyNodeModel;
import bucksimulator.model.PacktTallyModel;
import bucksimulator.model.ProphetRouting;
import bucksimulator.model.SimDataModel;
import bucksimulator.model.Simulation;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import javax.imageio.ImageIO;

/**
 * FXML Controller class.
 * Serves as the interface between main user interface and the
 * business logic.
 * @author balaram
 */

public class SimController implements Initializable {
    DBConnect db;
    volatile HashMap<String, ArrayList<Stage>> popoutStages = new HashMap<>();
    ArrayList<Simulation> simulations = new ArrayList<>();
    private Preferences prefs;
    String normalTheme = "Theme";
    String autoSave = "AutoSave";
    Rectangle zoomArea = new Rectangle();
    final ObjectProperty<Point2D> mouseAnchor = new SimpleObjectProperty<>();
    boolean zoomStarted = false;
    double zoomEndY, zoomStartY;
    Simulation taskcth, taskemc, taskesc, taskemt, taskgtr, tasknpr, taskpr, currentSim;
    int rclSeriesType;
    Thread t1, t2, t3, t4, t5, t6, t7, currentThread;
    ArrayList<CheckBox> gpsLogCBList = new ArrayList<>();
    ArrayList<CheckBox> dcCBList = new ArrayList<>();
    ArrayList<BooleanProperty> glCBDisPropList = new ArrayList<>();
    ArrayList<BooleanProperty> dcCBDisPropList = new ArrayList<>();
    XYChart.Series radioContactRatioSeries = new XYChart.Series();
    @FXML
    private MenuBar menuBar;
    @FXML
    private TabPane masterTabPane;
    @FXML
    private ScatterChart<Number, Number> radioContactLogChart;
    @FXML
    private BarChart<String, Number> throughputChart;
    @FXML
    private RadioButton darkTheme;
    @FXML
    private Label progressTextCTH;
    @FXML
    private Label progressTextESC;
    @FXML
    private Label progressTextEMC;
    @FXML
    private Label progressTextEMT;
    @FXML
    private Label progressTextGTR;
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
    @FXML
    private ScatterChart<Number, Number> gpsLiveChart;
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
    private Pane startScreen;
    @FXML
    private Label startScreenText;
    @FXML
    private RadioMenuItem popoutCTHChartsMI;
    @FXML
    private AnchorPane topLevelAnchorPane;
    @FXML
    private BarChart<String, Number> radioContactRatioChart;
    @FXML
    private NumberAxis rclYAxis;
    @FXML
    private NumberAxis rclXAxis;
    @FXML
    private AnchorPane rclAnchorPane;
    @FXML
    private Tab rclTab;
    @FXML
    private Tab startScreenTab;
    @FXML
    private Tab gpsLiveTab;
    @FXML
    private Tab throughputTab;
    @FXML
    private Tab dataCenterTab;
    @FXML
    private Tab gpsLogTab;
    @FXML
    private Tab rcrTab;
    @FXML
    private Tab simStatusTab;
    @FXML
    private MenuItem gpsLiveCTHMI;
    @FXML
    private MenuItem gpsLogCTHMI;
    @FXML
    private MenuItem gpsLiveESCMI;
    @FXML
    private MenuItem gpsLogESCMI;
    @FXML
    private MenuItem gpsLiveEMCMI;
    @FXML
    private MenuItem gpsLogEMCMI;
    @FXML
    private MenuItem gpsLiveEMTMI;
    @FXML
    private MenuItem gpsLogEMTMI;
    @FXML
    private MenuItem gpsLiveGTRMI;
    @FXML
    private MenuItem gpsLogGTRMI;
    @FXML
    private MenuItem gpsLivePRMI;
    @FXML
    private MenuItem gpsLogPRMI;
    @FXML
    private MenuItem gpsLiveNPRMI;
    @FXML
    private MenuItem gpsLogNPRMI;
    @FXML
    private Menu cthViewMI;
    @FXML
    private Menu escViewMI;
    @FXML
    private Menu emcViewMI;
    @FXML
    private Menu emtViewMI;
    @FXML
    private Menu gtrViewMI;
    @FXML
    private Menu prViewMI;
    @FXML
    private Menu nprViewMI;
    @FXML
    private MenuItem tpCTHMI;
    @FXML
    private MenuItem dcCTHMI;
    @FXML
    private MenuItem tpESCMI;
    @FXML
    private MenuItem dcESCMI;
    @FXML
    private MenuItem tpEMCMI;
    @FXML
    private MenuItem dcEMCMI;
    @FXML
    private MenuItem tpEMTMI;
    @FXML
    private MenuItem dcEMTMI;
    @FXML
    private MenuItem tpGTRMI;
    @FXML
    private MenuItem dcGTRMI;
    @FXML
    private MenuItem cthExportMI;
    @FXML
    private MenuItem escExportMI;
    @FXML
    private MenuItem emcExportMI;
    @FXML
    private MenuItem emtExportMI;
    @FXML
    private MenuItem gtrExportMI;
    @FXML
    private Menu exportSimDataMenu;
    @FXML
    private Tab nodeDataTables;
    @FXML
    private TableColumn<GPSLog, Integer> t1xColumn;
    @FXML
    private TableColumn<GPSLog, Integer> t1yColumn;
    @FXML
    private TableColumn<GPSLog, Integer> t2xColumn;
    @FXML
    private TableColumn<GPSLog, Integer> t2yColumn;
    @FXML
    private TableColumn<GPSLog, Integer> t3xColumn;
    @FXML
    private TableColumn<GPSLog, Integer> t3yColumn;
    @FXML
    private TableColumn<GPSLog, Integer> bhxColumn;
    @FXML
    private TableColumn<GPSLog, Integer> bhyColumn;
    @FXML
    private TableColumn<GPSLog, Integer> mhxColumn;
    @FXML
    private TableColumn<GPSLog, Integer> mhyColumn;
    @FXML
    private TableColumn<GPSLog, Integer> fhxColumn;
    @FXML
    private TableColumn<GPSLog, Integer> fhyColumn;
    @FXML
    private TableColumn<GPSLog, Integer> nfxColumn;
    @FXML
    private TableColumn<GPSLog, Integer> nfyColumn;
    @FXML
    private TableColumn<GPSLog, Integer> w1xColumn;
    @FXML
    private TableView<GPSLog> t1DataTable;
    @FXML
    private TableView<GPSLog> t2DataTable;
    @FXML
    private TableView<GPSLog> t3DataTable;
    @FXML
    private TableView<GPSLog> bhDataTable;
    @FXML
    private TableView<GPSLog> mhDataTable;
    @FXML
    private TableView<GPSLog> fhDataTable;
    @FXML
    private TableView<GPSLog> nfDataTable;
    @FXML
    private TableView<GPSLog> w1DataTable;
    @FXML
    private TableColumn<?, ?> w1yColumn;
    @FXML
    private Tab throughputCompareTab;
    @FXML
    private BarChart<String, Number> throughputCompareChart;
    @FXML
    private MenuItem pauseCTHMI;
    @FXML
    private MenuItem resumeCTHMI;
    @FXML
    private MenuItem pauseESCMI;
    @FXML
    private MenuItem resumeESCMI;
    @FXML
    private MenuItem pauseEMCMI;
    @FXML
    private MenuItem resumeEMCMI;
    @FXML
    private MenuItem pauseEMTMI;
    @FXML
    private MenuItem resumeEMTMI;
    @FXML
    private MenuItem pauseGTRMI;
    @FXML
    private MenuItem resumeGTRMI;
    @FXML
    private Label tableTitle;
    @FXML
    private MenuItem startCTHMI;
    @FXML
    private MenuItem startESCMI;
    @FXML
    private MenuItem startEMCMI;
    @FXML
    private MenuItem startEMTMI;
    @FXML
    private MenuItem startGTRMI;
    @FXML
    private MenuItem startPRMI;
    @FXML
    private MenuItem startNPRMI;
    @FXML
    private MenuItem stopCTHMI;
    @FXML
    private MenuItem stopESCMI;
    @FXML
    private MenuItem stopEMCMI;
    @FXML
    private MenuItem stopEMTMI;
    @FXML
    private MenuItem stopGTRMI;
    @FXML
    private MenuItem stopPRMI;
    @FXML
    private MenuItem stopNPRMI;
    @FXML
    private MenuItem startAllSimsMI;
    @FXML
    private MenuItem pauseAllSimsMI;
    @FXML
    private MenuItem stopAllSimsMI;
    @FXML
    private MenuItem resumeAllSimsMI;
    @FXML
    private MenuItem nprExportMI;
    @FXML
    private MenuItem tpNPRMI;
    @FXML
    private MenuItem dcNPRMI;
    @FXML
    private MenuItem pauseNPRMI;
    @FXML
    private MenuItem resumeNPRMI;
    @FXML
    private Label progressTextPR;
    @FXML
    private Label progressTextNPR;
    @FXML
    private MenuItem prExportMI;
    @FXML
    private MenuItem pausePRMI;
    @FXML
    private MenuItem resumePRMI;
    @FXML
    private MenuItem tpPRMI;
    @FXML
    private MenuItem dcPRMI;
    @FXML
    private LineChart<String, Number> throughputCompareChartLC;
    @FXML
    private Tab throughputCompareTabLC;
    @FXML
    private Menu viewMenu;
    @FXML
    private Label busyScreenText;
    @FXML
    private Pane busyScreen;
    @FXML
    private Tab busyTab;
    @FXML
    private RadioMenuItem popoutESCChartsMI;
    @FXML
    private RadioMenuItem popoutEMCChartsMI;
    @FXML
    private RadioMenuItem popoutEMTChartsMI;
    @FXML
    private RadioMenuItem popoutGTRChartsMI;
    @FXML
    private RadioMenuItem popoutPRChartsMI;
    @FXML
    private RadioMenuItem popoutNPRChartsMI;
    @FXML
    private CheckMenuItem popoutAllChartsMI;
    @FXML
    private RadioMenuItem fullScreenMI;
    @FXML
    private Tab databaseReport;
    @FXML
    private TableView<SimDataModel> databaseTable;
    @FXML
    private TableColumn<?, ?> dbAlgorithmName;
    @FXML
    private TableColumn<?, ?> dbRunDate;
    @FXML
    private TableColumn<?, ?> dbRadioContactRatio;
    @FXML
    private TableColumn<?, ?> dbCumulativeThroughput;
    @FXML
    private TableColumn<?, ?> dbT1Throughput;
    @FXML
    private TableColumn<?, ?> dbT2Throughput;
    @FXML
    private TableColumn<?, ?> dbT3Throughput;
    @FXML
    private TableColumn<?, ?> dbBHThroughput;
    @FXML
    private TableColumn<?, ?> dbFHThroughput;
    @FXML
    private TableColumn<?, ?> dbMHThroughput;
    @FXML
    private TableColumn<?, ?> dbNFThroughput;
    @FXML
    private MenuItem cthDBMI;
    @FXML
    private MenuItem escDBMI;
    @FXML
    private MenuItem emcDBMI;
    @FXML
    private MenuItem emtDBMI;
    @FXML
    private MenuItem gtrDBMI;
    @FXML
    private MenuItem prDBMI;
    @FXML
    private MenuItem nprDBMI;
    @FXML
    private Tab packetTallyTab;
    @FXML
    private TableColumn<?, ?> packetTallyTableNodeColumn;
    @FXML
    private TableView<PacketTallyNodeModel> packetTallyTableNode;
    @FXML
    private TableView<PacktTallyModel> cthtallytable;
    @FXML
    private TableColumn<?, ?> cthtallytablecolumn;
    @FXML
    private TableView<PacktTallyModel> esctallytable;
    @FXML
    private TableColumn<?, ?> esctallytablecolumn;
    @FXML
    private TableView<PacktTallyModel> emctallytable;
    @FXML
    private TableColumn<?, ?> emctallytablecolumn;
    @FXML
    private TableView<PacktTallyModel> emttallytable;
    @FXML
    private TableColumn<?, ?> emttallytablecolumn;
    @FXML
    private TableView<PacktTallyModel> gtrtallytable;
    @FXML
    private TableColumn<?, ?> gtrtallytablecolumn;
    @FXML
    private TableView<PacktTallyModel> prtallytable;
    @FXML
    private TableColumn<?, ?> prtallytablecolumn;
    @FXML
    private TableView<PacktTallyModel> nprtallytable;
    @FXML
    private TableColumn<?, ?> nprtallytablecolumn;
    @FXML
    private MenuItem packetTallyMI;
    @FXML
    private MenuItem rcrMI;
    @FXML
    private Menu tpCompareMI;
    @FXML
    private TableColumn<?, ?> dbStartTime;
    @FXML
    private TableColumn<?, ?> dbEndTime;
    @FXML
    private Menu saveIndividualSimDataToDBM;
    @FXML
    private CheckBox w1CBGL;
    @FXML
    private Tab preferencesTab;
    @FXML
    private TextField chartBackgroundTF;
    @FXML
    private RadioButton autoSaveDBRB;

    /**
     * Initializes the simulation tool and performs the data binding to the
     * user interface.
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Task task = new Task() 
        {
           @Override
            protected String call() throws InterruptedException 
            {
                startScreenText.getStyleClass().add("label1");
                busyScreenText.getStyleClass().add("label1");
                startScreen.getStyleClass().add("pane-background");
                busyScreen.getStyleClass().add("pane-background");
                gpsLiveChart.getStylesheets().add("/bucksimulator/css/GpsLiveChartBackground.css");
                gpsLogChart.getStylesheets().add("/bucksimulator/css/GpsLiveChartBackground.css");
                dataCenterChart.getStylesheets().add("/bucksimulator/css/GpsLiveChartBackground.css");
                prefs = Preferences.userRoot().node(SimController.class.getName());
                String m = DBConnect.DBConnect();
                System.out.println(m);
                DBConnect.getSimulationDataTableData();
                gpsLogCBList.add(t1CBGL);
                gpsLogCBList.add(t2CBGL);
                gpsLogCBList.add(t3CBGL);
                gpsLogCBList.add(bhCBGL);
                gpsLogCBList.add(mhCBGL);
                gpsLogCBList.add(fhCBGL);
                gpsLogCBList.add(nfCBGL);
                gpsLogCBList.add(w1CBGL);
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
                for (int i = 0; i < 8; i++)
                {
                    SimpleBooleanProperty sbp = new SimpleBooleanProperty();
                    glCBDisPropList.add(sbp);
                    gpsLogCBList.get(i).disableProperty().bind(sbp);
                }
                // Show start screen
                masterTabPane.getSelectionModel().select(startScreenTab);
                Platform.runLater(() ->
                {
                    radioContactRatioChart.getData().add(radioContactRatioSeries);
                    //Chart Zoom
                    zoomArea.setManaged(false);
                    zoomArea.setFill(Color.MEDIUMTURQUOISE.deriveColor(0, 1, 1, 0.5));
                    rclAnchorPane.getChildren().add(zoomArea);

                    //Sims
                    taskcth = new CaptureTimeHide();
                    progressTextCTH.textProperty().bind(taskcth.getSimprogressProperty().asString());
                    //cthViewMI.visibleProperty().bind(taskcth.getSimStartedProperty());
                    gpsLogCTHMI.visibleProperty().bind(taskcth.getSimCompletedProperty());
                    cthExportMI.visibleProperty().bind(taskcth.getSimCompletedProperty());
                    cthDBMI.visibleProperty().bind(taskcth.getSimCompletedProperty());
                    tpCTHMI.visibleProperty().bind(taskcth.getSimCompletedProperty());
                    dcCTHMI.visibleProperty().bind(taskcth.getSimCompletedProperty());
                    radioContactRatioSeries.getData().add(taskcth.getRadioContactRatio());
                    throughputCompareChart.getData().add(taskcth.getThroughput());
                    throughputCompareChartLC.getData().add(taskcth.getThroughputLC());
                    startCTHMI.visibleProperty().bind(taskcth.getSimNotStartedProperty());
                    stopCTHMI.visibleProperty().bind(taskcth.getSimStartedProperty());
                    pauseCTHMI.visibleProperty().bind(taskcth.getSimInProgressProperty());
                    resumeCTHMI.visibleProperty().bind(taskcth.getSimPausedProperty());

                    taskemc = new EpidemicMulticopy();
                    progressTextEMC.textProperty().bind(taskemc.getSimprogressProperty().asString());
                    //emcViewMI.visibleProperty().bind(taskemc.getSimStartedProperty());
                    gpsLogEMCMI.visibleProperty().bind(taskemc.getSimCompletedProperty());
                    emcExportMI.visibleProperty().bind(taskemc.getSimCompletedProperty());
                    emcDBMI.visibleProperty().bind(taskemc.getSimCompletedProperty());
                    tpEMCMI.visibleProperty().bind(taskemc.getSimCompletedProperty());
                    dcEMCMI.visibleProperty().bind(taskemc.getSimCompletedProperty());
                    radioContactRatioSeries.getData().add(taskemc.getRadioContactRatio());
                    throughputCompareChart.getData().add(taskemc.getThroughput());
                    throughputCompareChartLC.getData().add(taskemc.getThroughputLC());
                    startEMCMI.visibleProperty().bind(taskemc.getSimNotStartedProperty());
                    stopEMCMI.visibleProperty().bind(taskemc.getSimStartedProperty());
                    pauseEMCMI.visibleProperty().bind(taskemc.getSimInProgressProperty());
                    resumeEMCMI.visibleProperty().bind(taskemc.getSimPausedProperty());

                    taskesc = new EpidemicSinglecopy();
                    progressTextESC.textProperty().bind(taskesc.getSimprogressProperty().asString());
                    //escViewMI.visibleProperty().bind(taskesc.getSimStartedProperty());
                    gpsLogESCMI.visibleProperty().bind(taskesc.getSimCompletedProperty());
                    escExportMI.visibleProperty().bind(taskesc.getSimCompletedProperty());
                    escDBMI.visibleProperty().bind(taskesc.getSimCompletedProperty());
                    tpESCMI.visibleProperty().bind(taskesc.getSimCompletedProperty());
                    dcESCMI.visibleProperty().bind(taskesc.getSimCompletedProperty());
                    radioContactRatioSeries.getData().add(taskesc.getRadioContactRatio());
                    throughputCompareChart.getData().add(taskesc.getThroughput());
                    throughputCompareChartLC.getData().add(taskesc.getThroughputLC());
                    startESCMI.visibleProperty().bind(taskesc.getSimNotStartedProperty());
                    stopESCMI.visibleProperty().bind(taskesc.getSimStartedProperty());
                    pauseESCMI.visibleProperty().bind(taskesc.getSimInProgressProperty());
                    resumeESCMI.visibleProperty().bind(taskesc.getSimPausedProperty());

                    taskemt = new ExpectedMeetingtime();
                    progressTextEMT.textProperty().bind(taskemt.getSimprogressProperty().asString());
                    //emtViewMI.visibleProperty().bind(taskemt.getSimStartedProperty());
                    gpsLogEMTMI.visibleProperty().bind(taskemt.getSimCompletedProperty());
                    emtExportMI.visibleProperty().bind(taskemt.getSimCompletedProperty());
                    emtDBMI.visibleProperty().bind(taskemt.getSimCompletedProperty());
                    tpEMTMI.visibleProperty().bind(taskemt.getSimCompletedProperty());
                    dcEMTMI.visibleProperty().bind(taskemt.getSimCompletedProperty());
                    radioContactRatioSeries.getData().add(taskemt.getRadioContactRatio());
                    throughputCompareChart.getData().add(taskemt.getThroughput());
                    throughputCompareChartLC.getData().add(taskemt.getThroughputLC());
                    startEMTMI.visibleProperty().bind(taskemt.getSimNotStartedProperty());
                    stopEMTMI.visibleProperty().bind(taskemt.getSimStartedProperty());
                    pauseEMTMI.visibleProperty().bind(taskemt.getSimInProgressProperty());
                    resumeEMTMI.visibleProperty().bind(taskemt.getSimPausedProperty());

                    taskgtr = new GameTheoryRouting();
                    progressTextGTR.textProperty().bind(taskgtr.getSimprogressProperty().asString());
                    //gtrViewMI.visibleProperty().bind(taskgtr.getSimStartedProperty());
                    gpsLogGTRMI.visibleProperty().bind(taskgtr.getSimCompletedProperty());
                    gtrExportMI.visibleProperty().bind(taskgtr.getSimCompletedProperty());
                    gtrDBMI.visibleProperty().bind(taskgtr.getSimCompletedProperty());
                    tpGTRMI.visibleProperty().bind(taskgtr.getSimCompletedProperty());
                    dcGTRMI.visibleProperty().bind(taskgtr.getSimCompletedProperty());
                    radioContactRatioSeries.getData().add(taskgtr.getRadioContactRatio());
                    throughputCompareChart.getData().add(taskgtr.getThroughput());
                    throughputCompareChartLC.getData().add(taskgtr.getThroughputLC());
                    startGTRMI.visibleProperty().bind(taskgtr.getSimNotStartedProperty());
                    stopGTRMI.visibleProperty().bind(taskgtr.getSimStartedProperty());
                    pauseGTRMI.visibleProperty().bind(taskgtr.getSimInProgressProperty());
                    resumeGTRMI.visibleProperty().bind(taskgtr.getSimPausedProperty());

                    tasknpr = new NewProphetRouting();
                    progressTextNPR.textProperty().bind(tasknpr.getSimprogressProperty().asString());
                    //nprViewMI.visibleProperty().bind(tasknpr.getSimStartedProperty());
                    gpsLogNPRMI.visibleProperty().bind(tasknpr.getSimCompletedProperty());
                    nprExportMI.visibleProperty().bind(tasknpr.getSimCompletedProperty());
                    nprDBMI.visibleProperty().bind(tasknpr.getSimCompletedProperty());
                    tpNPRMI.visibleProperty().bind(tasknpr.getSimCompletedProperty());
                    dcNPRMI.visibleProperty().bind(tasknpr.getSimCompletedProperty());
                    radioContactRatioSeries.getData().add(tasknpr.getRadioContactRatio());
                    throughputCompareChart.getData().add(tasknpr.getThroughput());
                    throughputCompareChartLC.getData().add(tasknpr.getThroughputLC());
                    startNPRMI.visibleProperty().bind(tasknpr.getSimNotStartedProperty());
                    stopNPRMI.visibleProperty().bind(tasknpr.getSimStartedProperty());
                    pauseNPRMI.visibleProperty().bind(tasknpr.getSimInProgressProperty());
                    resumeNPRMI.visibleProperty().bind(tasknpr.getSimPausedProperty());

                    taskpr = new ProphetRouting();
                    progressTextPR.textProperty().bind(taskpr.getSimprogressProperty().asString());
                    //nprViewMI.visibleProperty().bind(taskpr.getSimStartedProperty());
                    gpsLogPRMI.visibleProperty().bind(taskpr.getSimCompletedProperty());
                    prExportMI.visibleProperty().bind(taskpr.getSimCompletedProperty());
                    prDBMI.visibleProperty().bind(taskpr.getSimCompletedProperty());
                    tpPRMI.visibleProperty().bind(taskpr.getSimCompletedProperty());
                    dcPRMI.visibleProperty().bind(taskpr.getSimCompletedProperty());
                    radioContactRatioSeries.getData().add(taskpr.getRadioContactRatio());
                    throughputCompareChart.getData().add(taskpr.getThroughput());
                    throughputCompareChartLC.getData().add(taskpr.getThroughputLC());
                    startPRMI.visibleProperty().bind(taskpr.getSimNotStartedProperty());
                    stopPRMI.visibleProperty().bind(taskpr.getSimStartedProperty());
                    pausePRMI.visibleProperty().bind(taskpr.getSimInProgressProperty());
                    resumePRMI.visibleProperty().bind(taskpr.getSimPausedProperty());

                    simulations.add(taskcth);
                    simulations.add(taskesc);
                    simulations.add(taskemc);
                    simulations.add(taskemt);
                    simulations.add(taskgtr);
                    simulations.add(taskpr);
                    simulations.add(tasknpr);
                    //Table
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

                    //SimDataTable
                    dbAlgorithmName.setCellValueFactory(new PropertyValueFactory<>("simName"));
                    dbRunDate.setCellValueFactory(new PropertyValueFactory<>("simRunDate"));
                    dbStartTime.setCellValueFactory(new PropertyValueFactory<>("st"));
                    dbEndTime.setCellValueFactory(new PropertyValueFactory<>("et"));
                    dbRadioContactRatio.setCellValueFactory(new PropertyValueFactory<>("rcr"));
                    dbCumulativeThroughput.setCellValueFactory(new PropertyValueFactory<>("ct"));
                    dbT1Throughput.setCellValueFactory(new PropertyValueFactory<>("t1tp"));
                    dbT2Throughput.setCellValueFactory(new PropertyValueFactory<>("t2tp"));
                    dbT3Throughput.setCellValueFactory(new PropertyValueFactory<>("t3tp"));
                    dbBHThroughput.setCellValueFactory(new PropertyValueFactory<>("bhtp"));
                    dbFHThroughput.setCellValueFactory(new PropertyValueFactory<>("fhtp"));
                    dbMHThroughput.setCellValueFactory(new PropertyValueFactory<>("mhtp"));
                    dbNFThroughput.setCellValueFactory(new PropertyValueFactory<>("nftp"));
                    databaseTable.setItems(DBConnect.sdms);

                    //PacketTally Table
                    packetTallyTableNodeColumn.setCellValueFactory(new PropertyValueFactory<>("nodeName"));
                    cthtallytablecolumn.setCellValueFactory(new PropertyValueFactory<>("value"));
                    esctallytablecolumn.setCellValueFactory(new PropertyValueFactory<>("value"));
                    emctallytablecolumn.setCellValueFactory(new PropertyValueFactory<>("value"));
                    emttallytablecolumn.setCellValueFactory(new PropertyValueFactory<>("value"));
                    gtrtallytablecolumn.setCellValueFactory(new PropertyValueFactory<>("value"));
                    prtallytablecolumn.setCellValueFactory(new PropertyValueFactory<>("value"));
                    nprtallytablecolumn.setCellValueFactory(new PropertyValueFactory<>("value"));
                    ObservableList<PacketTallyNodeModel> nodes = FXCollections.observableArrayList();
                    nodes.add(new PacketTallyNodeModel("Tx-t1"));
                    nodes.add(new PacketTallyNodeModel("Tx-t2"));
                    nodes.add(new PacketTallyNodeModel("Tx-t3"));
                    nodes.add(new PacketTallyNodeModel("Tx-bh"));
                    nodes.add(new PacketTallyNodeModel("Tx-mh"));
                    nodes.add(new PacketTallyNodeModel("Tx-fh"));
                    nodes.add(new PacketTallyNodeModel("Tx-nf"));
                    nodes.add(new PacketTallyNodeModel("Total-Tx"));
                    nodes.add(new PacketTallyNodeModel("Rx-t1"));
                    nodes.add(new PacketTallyNodeModel("Rx-t2"));
                    nodes.add(new PacketTallyNodeModel("Rx-t3"));
                    nodes.add(new PacketTallyNodeModel("Rx-bh"));
                    nodes.add(new PacketTallyNodeModel("Rx-mh"));
                    nodes.add(new PacketTallyNodeModel("Rx-fh"));
                    nodes.add(new PacketTallyNodeModel("Rx-nf"));
                    nodes.add(new PacketTallyNodeModel("Total-Rx"));
                    nodes.add(new PacketTallyNodeModel("Tc"));
                    nodes.add(new PacketTallyNodeModel("RC"));
                    nodes.add(new PacketTallyNodeModel("RCR"));
                    packetTallyTableNode.setItems(nodes);
                    cthtallytable.setItems(taskcth.getPacktTallyData());
                    esctallytable.setItems(taskesc.getPacktTallyData());
                    emctallytable.setItems(taskemc.getPacktTallyData());
                    emttallytable.setItems(taskemt.getPacktTallyData());
                    gtrtallytable.setItems(taskgtr.getPacktTallyData());
                    prtallytable.setItems(taskpr.getPacktTallyData());
                    nprtallytable.setItems(tasknpr.getPacktTallyData());

                    saveIndividualSimDataToDBM.disableProperty().bind(autoSaveDBRB.selectedProperty());

                    autoSaveDBRB.setSelected(prefs.getBoolean(autoSave, true));
                    if(autoSaveDBRB.isSelected())
                    {
                        taskcth.getSimCompletedProperty().addListener(e -> saveSimDatatoDB(taskcth));
                        taskesc.getSimCompletedProperty().addListener(e -> saveSimDatatoDB(taskesc));
                        taskemc.getSimCompletedProperty().addListener(e -> saveSimDatatoDB(taskemc));
                        taskemt.getSimCompletedProperty().addListener(e -> saveSimDatatoDB(taskemt));
                        taskgtr.getSimCompletedProperty().addListener(e -> saveSimDatatoDB(taskgtr));
                        taskpr.getSimCompletedProperty().addListener(e -> saveSimDatatoDB(taskpr));
                        tasknpr.getSimCompletedProperty().addListener(e -> saveSimDatatoDB(tasknpr));
                    }
                });
               return "";
            }
        };
        new Thread(task).start();
    }
    
    /**
     * Sets the initial theme for the simulation tool based on previous session.
     */
    public void setInitialTheme()
    {
        if (!prefs.getBoolean(normalTheme, true)) {
            (menuBar.getScene()).getStylesheets().add(SimController.class.getResource("/bucksimulator/css/DarkTheme.css").toExternalForm());
            darkTheme.setSelected(true);
        } else {
            (menuBar.getScene()).getStylesheets().add(SimController.class.getResource("/bucksimulator/css/NormalTheme.css").toExternalForm());
        }
    }
    
    /**
     * Adds the tool tip for the radio contact charts. Based on the data being
     * shown tool tip will be added.
     * @param chart 
     */
    void addTootips(ScatterChart<Number,Number> chart)
    {
        if (currentSim.getSimStartedProperty().get() || currentSim.getSimCompletedProperty().get())
        {
            if (rclSeriesType == 0)
            {
                for (XYChart.Series<Number, Number> s : chart.getData())
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
            else if (rclSeriesType == 1)
            {
                for (XYChart.Series<Number, Number> s : chart.getData())
                {
                    for (XYChart.Data<Number, Number> d : s.getData())
                    {
                        if (d.getXValue().intValue() != 8)
                        {
                            int n = currentSim.getDataTranferMapXT().get(d.getXValue()).get(d.getYValue());
                            Tooltip tt = new Tooltip("Node "+d.getXValue().toString() + " sent to node " + n + " at " + d.getYValue().toString());
                            tt.setShowDelay(Duration.millis(20));
                            Tooltip.install(d.getNode(), tt);
                        }
                    }
                }
            }
            else if (rclSeriesType == 2)
            {
                for (XYChart.Series<Number, Number> s : chart.getData())
                {
                    for (XYChart.Data<Number, Number> d : s.getData())
                    {
                        if (d.getXValue().intValue() != 9)
                        {
                            int n = currentSim.getDataTranferMapXT().get(d.getXValue()).get(d.getYValue());
                            Tooltip tt = new Tooltip("Node "+d.getXValue().toString() + " received from node " + n + " at " + d.getYValue().toString());
                            tt.setShowDelay(Duration.millis(20));
                            Tooltip.install(d.getNode(), tt);
                        }
                    }
                }
            }
        }
    }

    /**
     * Creates popup to be shown on the simulation tool.
     * @param message
     * @return 
     */
    public Popup createPopup(final String message)
    {
        Popup popup = new Popup();
        popup.setAutoFix(true);
        popup.setAutoHide(true);
        popup.setHideOnEscape(true);
        Label label = new Label(message);
        label.setOnMouseMoved(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent e)
            {
                popup.hide();
            }
        });
        label.getStylesheets().add(SimController.class.getResource("/bucksimulator/css/popup.css").toExternalForm());
        label.getStyleClass().add("popup");
        popup.getContent().add(label);
        return popup;
    }

    /**
     * Popup message will be showed on the center of the simulation tool.
     * @param message
     * @param stage 
     */
    public void showPopupMessage(final String message, final Stage stage)
    {
        final Popup popup = createPopup(message);
        popup.setOnShown(new EventHandler<WindowEvent>()
        {
            @Override
            public void handle(WindowEvent e)
            {
                popup.setX(stage.getX() + stage.getWidth() / 2 - popup.getWidth() / 2);
                popup.setY(stage.getY() + stage.getHeight() / 2 - popup.getHeight() / 2);
            }
        });
        popup.show(stage);
    }

    String browseFolder(boolean save)
    {
        String folderName = "";
        DirectoryChooser folderChooser = new DirectoryChooser();
        if (!save)
        {
        folderChooser.setTitle("Browse folder with simulation results");
        }
        else
        {
            folderChooser.setTitle("Browse folder to export simulation results");
        }
        File file = folderChooser.showDialog((menuBar.getScene()).getWindow());
        if (file != null) {
            folderName = file.getAbsolutePath();
        }
        return folderName;
    }
    
    String browseFile()
    {
        String fileName = "";
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select GPS Chart Background Image");
        File file = fileChooser.showOpenDialog((menuBar.getScene()).getWindow());
        if (file != null) {
            fileName = file.getAbsolutePath().strip();
        }
        return fileName;
    }

    /**
     * Saves the simulation data to database and shows the database report page.
     * Highlights the newly added row in the database report page.
     * @param sim 
     */
    void saveSimDatatoDB(Simulation sim)
    {
        if (sim.getSimCompletedProperty().get())
        {
            int tableIndex = sim.saveSimData();
            masterTabPane.getSelectionModel().select(databaseReport);
            databaseTable.requestFocus();
            databaseTable.getSelectionModel().select(tableIndex);
            databaseTable.getFocusModel().focus(tableIndex);
        }
    }
    
    /**
     * Saves the chart to the user selected directory when user performs ctrl+s
     * action on the chart.
     * @param image 
     */
    public void saveChartToImage(WritableImage image)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Chart");
        fileChooser.setInitialFileName("");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG File(*.png)", "*.png"));
        File file = fileChooser.showSaveDialog((menuBar.getScene()).getWindow());
        if (!file.getName().contains(".")) {
            file = new File(file.getAbsolutePath() + ".png");
        }
        if (file != null) {
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            } catch (IOException e) {
                // TODO: handle exception here
            }
        }
    }

    /**
     * 
     * @param sim 
     */
    public void addDataCenterSeries(Simulation sim)
    {
        currentSim = sim;
        masterTabPane.getSelectionModel().select(dataCenterTab);
        dataCenterChart.setTitle(sim.simName());
        ArrayList<Boolean> al = sim.getDCCheckBoxTrack();
        if (!dataCenterChart.getData().isEmpty())
        {
            dataCenterChart.getData().clear();
        }
        for (int i = 0; i < 7; i++)
        {
            dcCBDisPropList.get(i).set(sim.getDTNLog().get(i).getData().isEmpty());
            if (al.get(i))
            {
                dataCenterChart.getData().add(sim.getDTNLog().get(i));
            }
        }
        dataCenterChart.layout();
        restoreDCCheckBoxStatus(sim);
    }

    public void addGPSLogSeries(Simulation sim)
    {
        currentSim = sim;
        masterTabPane.getSelectionModel().select(gpsLogTab);
        gpsLogChart.setTitle(sim.simName());
        ArrayList<Boolean> al = sim.getGLCheckBoxTrack();
        if (!gpsLogChart.getData().isEmpty())
        {
            gpsLogChart.getData().clear();
        }
        for (int i = 0; i < 8; i++) {
            glCBDisPropList.get(i).set(sim.getGPSLog().get(i).getData().isEmpty());
            if (al.get(i)) {
                gpsLogChart.getData().add(sim.getGPSLog().get(i));
            }
        }
        restoreGLCheckBoxStatus(sim);
    }

    public void restoreDCCheckBoxStatus(Simulation sim)
    {
        //Clear all check boxes before restoring
        t1CBDC.setSelected(false);
        t2CBDC.setSelected(false);
        t3CBDC.setSelected(false);
        bhCBDC.setSelected(false);
        mhCBDC.setSelected(false);
        fhCBDC.setSelected(false);
        nfCBDC.setSelected(false);
        //Get the previously selected checkboxes list for new algorithm
        ArrayList<Boolean> al = sim.getDCCheckBoxTrack();
        for(int i = 0; i < al.size(); i++)
        {
            if (!dcCBDisPropList.get(i).get())
            {
                switch (i)
                {
                    case 0 -> t1CBDC.setSelected(al.get(i));
                    case 1 -> t2CBDC.setSelected(al.get(i));
                    case 2 -> t3CBDC.setSelected(al.get(i));
                    case 3 -> bhCBDC.setSelected(al.get(i));
                    case 4 -> mhCBDC.setSelected(al.get(i));
                    case 5 -> fhCBDC.setSelected(al.get(i));
                    case 6 -> nfCBDC.setSelected(al.get(i));
                }
            }
        }
    }

    public void restoreGLCheckBoxStatus(Simulation sim)
    {
        //Clear all check boxes before restoring
        t1CBGL.setSelected(false);
        t2CBGL.setSelected(false);
        t3CBGL.setSelected(false);
        bhCBGL.setSelected(false);
        mhCBGL.setSelected(false);
        fhCBGL.setSelected(false);
        nfCBGL.setSelected(false);
        w1CBGL.setSelected(false);
        //Get the previously selected checkboxes list for new algorithm
        ArrayList<Boolean> al = sim.getGLCheckBoxTrack();
        //Set the checkbox based on the retrived list
        for(int i = 0; i < al.size(); i++)
        {
            if (!glCBDisPropList.get(i).get())
            {
                switch(i)
                {
                    case 0 -> t1CBGL.setSelected(al.get(i));
                    case 1 -> t2CBGL.setSelected(al.get(i));
                    case 2 -> t3CBGL.setSelected(al.get(i));
                    case 3 -> bhCBGL.setSelected(al.get(i));
                    case 4 -> mhCBGL.setSelected(al.get(i));
                    case 5 -> fhCBGL.setSelected(al.get(i));
                    case 6 -> nfCBGL.setSelected(al.get(i));
                    case 7 -> w1CBGL.setSelected(al.get(i));
                }
            }
        }
    }

    void addLabelForThroughputBar(BarChart<String,Number> chart)
    {
        for (XYChart.Series<String, Number> s : chart.getData()) {
            for (XYChart.Data<String, Number> d : s.getData()) {
                Tooltip tt = new Tooltip(d.getXValue().toString()+" : "+d.getYValue().toString());
                tt.setShowDelay(Duration.millis(20));
                Tooltip.install(d.getNode(), tt);
            }
        }
    }

    /**
     * Bar chart will be updated with the throughput data from the selected
     * simulation. Same chart is used to show the through put for all 
     * simulations. So the current data will be swapped with the new data from
     * the newly selected simulation.
     * @param sim 
     */
    public void addThroughputSeries(Simulation sim)
    {
        masterTabPane.getSelectionModel().select(throughputTab);
        throughputChart.setTitle(sim.simName());
        if (!throughputChart.getData().isEmpty())
        {
            throughputChart.getData().clear();
        }
        throughputChart.getData().add(sim.getThroughput());
        addLabelForThroughputBar(throughputChart);
    }

    /**
     * Add the Radio Contact Data to the chart for the corresponding simulation.
     * Same chart is used for all the simulations, so the current data will be
     * swapped with the data from the new simulation selected.
     * This function adds the receiving agents id and global tick data to the
     * chart.
     * @param sim 
     */
    public void addRCLSeriesYT(Simulation sim)
    {
        rclSeriesType = 2;
        currentSim = sim;
        masterTabPane.getSelectionModel().select(rclTab);
        radioContactLogChart.setTitle(sim.simName());
        if (!radioContactLogChart.getData().isEmpty())
        {
            radioContactLogChart.getData().remove(0);
        }
        rclXAxis.setLabel("Receiving Agent ID");
        rclYAxis.setLabel("Global Tick");
        rclYAxis.setLowerBound(0);
        radioContactLogChart.setMouseTransparent(false);
        rclYAxis.setAutoRanging(true);
        radioContactLogChart.layout();//to force redraw the data after adding.
        radioContactLogChart.getData().add(sim.getRCLYGTSeries());
    }
    
    /**
     * Add the Radio Contact Data to the chart for the corresponding simulation.
     * Same chart is used for all the simulations, so the current data will be
     * swapped with the data from the new simulation selected.
     * This function adds the sending agent and global tick data to the
     * chart.
     * @param sim 
     */
    public void addRCLSeriesXT(Simulation sim)
    {
        rclSeriesType = 1;
        currentSim = sim;
        masterTabPane.getSelectionModel().select(rclTab);
        radioContactLogChart.setTitle(sim.simName());
        if (!radioContactLogChart.getData().isEmpty())
        {
            radioContactLogChart.getData().remove(0);
        }
        rclXAxis.setLabel("Sending Agent ID");
        rclYAxis.setLabel("Global Tick");
        rclYAxis.setLowerBound(0);
        radioContactLogChart.setMouseTransparent(false);
        rclYAxis.setAutoRanging(true);
        radioContactLogChart.layout();//to force redraw the data after adding.
        radioContactLogChart.getData().add(sim.getRCLXGTSeries());
    }
    
    /**
     * Add the Radio Contact Data to the chart for the corresponding simulation.
     * Same chart is used for all the simulations, so the current data will be
     * swapped with the data from the new simulation selected.
     * This function adds the sending agent and receiving agents ids data to the
     * chart.
     * @param sim 
     */
    public void addRCLSeriesXY(Simulation sim)
    {
        rclSeriesType = 0;
        currentSim = sim;
        /**
         * When showing XY chart no need for zoom as this only maximum 10x10 chart
         * When showing XGT/YGT chart and zoom is enabled then disable auto ranging
         * and consume the mouse events.
        */
        //Set the axis names and upper bounds for the data being shown
        // Show the chart
        masterTabPane.getSelectionModel().select(rclTab);
        radioContactLogChart.setTitle(sim.simName());
        if (!radioContactLogChart.getData().isEmpty())
        {
            radioContactLogChart.getData().remove(0);
        }
        rclXAxis.setLabel("Sending Agent ID");
        rclYAxis.setLabel("Receiving Agent ID");
        rclYAxis.setLowerBound(0);
        rclYAxis.setUpperBound(10);
        radioContactLogChart.setMouseTransparent(false);
        rclYAxis.setAutoRanging(true);
        radioContactLogChart.layout();//to force redraw the data after adding.
        radioContactLogChart.getData().add(sim.getRClXYSeries());

    }
    
    /**
     * Add the GPS Live Data to the chart for the corresponding simulation.
     * Same chart is used for all the simulations, so the current data will be
     * swapped with the data from the new simulation selected.
     * @param sim 
     */
    public void addGPSLiveSeries(Simulation sim)
    {
        currentSim = sim;
        if (popoutStages.get(sim.simName()) == null || popoutStages.get(sim.simName()).isEmpty())
        {
            masterTabPane.getSelectionModel().select(gpsLiveTab);
            gpsLiveChart.setTitle(sim.simName());
            if (!gpsLiveChart.getData().isEmpty())
            {
                gpsLiveChart.getData().clear();
            }
            gpsLiveChart.getData().addAll(sim.getGpsLiveDataProperty());
            gpsLiveChart.layout();//Keep this to force redraw the new series, else new series wont show up
        }
    }
    
    void addGPSLiveDataTable(Simulation sim)
    {
        currentSim = sim;
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
            
            scroolToEnd();
            });
        }).start();
        masterTabPane.getSelectionModel().select(nodeDataTables);
    }
    
    /**
     * Kills all the simulations if they are started but not completed when the
     * simulation tool is being closed.
     */
    public void killTasks()
    {
        if(t1 != null)
        {
            taskcth.signalTerminate();
            t1.interrupt();
            t1 = null;
        }
        if(t2 != null)
        {
            taskesc.signalTerminate();
            t2.interrupt();
            t2 = null;
        }
        if(t3 != null)
        {
            taskemc.signalTerminate();
            t3.interrupt();
            t3 = null;
        }
        if(t4 != null)
        {
            taskemt.signalTerminate();
            t4.interrupt();
            t4 = null;
        }
        if(t5 != null)
        {
            taskgtr.signalTerminate();
            t5.interrupt();
            t5 = null;
        }
        if(t6 != null)
        {
            taskpr.signalTerminate();
            t6.interrupt();
            t6 = null;
        }
        if(t7 != null)
        {
            tasknpr.signalTerminate();
            t7.interrupt();
            t7 = null;
        }
    }

    @FXML
    private void startSimulation(ActionEvent event) {
        if(t1 == null)
        {
            startCaptureTimeHideSim(event);
        }
        if(t2 == null)
        {
            startEpidemicSingleCopySim(event);
        }
        if(t3 == null)
        {
            startEpidemicMultiCopySim(event);
        }
        if(t4 == null)
        {
            startExpectedMeetingtimeSim(event);
        }
        if(t5 == null)
        {
            startGameTheoryRoutingSim(event);
        }
        if(t6 == null)
        {
            startProphetRoutingSim(event);
        }
        if(t7 == null)
        {
            startNewProphetRoutingSim(event);
        }
    }

    @FXML
    private void closeApplication(ActionEvent event) {
        ((Stage) menuBar.getScene().getWindow()).close();
    }

    @FXML
    private void stopSimulation(ActionEvent event) {
        killTasks();
    }

    @FXML
    private void pauseSimulation(ActionEvent event)
    {
        if(t1 != null && taskcth.getSimInProgressProperty().get())
        {
            pauseResumeCTHSim(event);
        }
        if(t2 != null && taskesc.getSimInProgressProperty().get())
        {
            pauseResumeESCSim(event);
        }
        if(t3 != null && taskemc.getSimInProgressProperty().get())
        {
            pauseResumeEMCSim(event);
        }
        if(t4 != null && taskemt.getSimInProgressProperty().get())
        {
            pauseResumeEMTSim(event);
        }
        if(t5 != null && taskgtr.getSimInProgressProperty().get())
        {
            pauseResumeGTRSim(event);
        }
        if(t6 != null && taskpr.getSimInProgressProperty().get())
        {
            pauseResumePRSim(event);
        }
        if(t7 != null && tasknpr.getSimInProgressProperty().get())
        {
            pauseResumeNPRSim(event);
        }
    }

    @FXML
    private void resumeSimulation(ActionEvent event)
    {
        if(t1 != null && taskcth.getSimNotInProgressProperty().get())
        {
            pauseResumeCTHSim(event);
        }
        if(t2 != null && taskesc.getSimNotInProgressProperty().get())
        {
            pauseResumeESCSim(event);
        }
        if(t3 != null && taskemc.getSimNotInProgressProperty().get())
        {
            pauseResumeEMCSim(event);
        }
        if(t4 != null && taskemt.getSimNotInProgressProperty().get())
        {
            pauseResumeEMTSim(event);
        }
        if(t5 != null && taskgtr.getSimNotInProgressProperty().get())
        {
            pauseResumeGTRSim(event);
        }
        if(t6 != null && taskpr.getSimNotInProgressProperty().get())
        {
            pauseResumePRSim(event);
        }
        if(t7 != null && tasknpr.getSimNotInProgressProperty().get())
        {
            pauseResumeNPRSim(event);
        }
    }

    @FXML
    private void startCaptureTimeHideSim(ActionEvent event) {
        addGPSLiveSeries(taskcth);
        t1 = new Thread(taskcth);
        t1.start();
        showPopupMessage("Capture Time Hide Simulation Started",(Stage) menuBar.getScene().getWindow());
    }

    @FXML
    private void startEpidemicMultiCopySim(ActionEvent event) {
        addGPSLiveSeries(taskemc);
        t3 = new Thread(taskemc);
        t3.start();
        showPopupMessage("Epedemic Multicopy Simulation Started",(Stage) menuBar.getScene().getWindow());
    }

    @FXML
    private void startEpidemicSingleCopySim(ActionEvent event) {
        addGPSLiveSeries(taskesc);
        t2 = new Thread(taskesc);
        t2.start();
        showPopupMessage("Epedemic Singlecopy Simulation Started",(Stage) menuBar.getScene().getWindow());
    }

    @FXML
    private void startExpectedMeetingtimeSim(ActionEvent event) {
        addGPSLiveSeries(taskemt);
        t4 = new Thread(taskemt);
        t4.start();
        showPopupMessage("Expected Meetingtime Simulation Started",(Stage) menuBar.getScene().getWindow());
    }

    @FXML
    private void startGameTheoryRoutingSim(ActionEvent event)  {
        addGPSLiveSeries(taskgtr);
        t5 = new Thread(taskgtr);
        t5.start();
        showPopupMessage("Game Theory Routing Simulation Started",(Stage) menuBar.getScene().getWindow());
    }

    @FXML
    private void startProphetRoutingSim(ActionEvent event)  {
        addGPSLiveSeries(taskpr);
        t6 = new Thread(taskpr);
        t6.start();
        showPopupMessage("Prophet Routing Simulation Started",(Stage) menuBar.getScene().getWindow());
    }

    @FXML
    private void startNewProphetRoutingSim(ActionEvent event)  {
        addGPSLiveSeries(tasknpr);
        t7 = new Thread(tasknpr);
        t7.start();
        showPopupMessage("New Prophet Routing Simulation Started",(Stage) menuBar.getScene().getWindow());
    }

    @FXML
    private void stopCaptureTimeHideSim(ActionEvent event) {
        radioContactRatioSeries.getData().remove(taskcth.getRadioContactRatio());
        throughputCompareChart.getData().remove(taskcth.getThroughput());
        taskcth.signalTerminate();
        t1.interrupt();
        t1 = null;
    }

    @FXML
    private void stopEpidemicSingleCopySim(ActionEvent event) {
        radioContactRatioSeries.getData().remove(taskesc.getRadioContactRatio());
        throughputCompareChart.getData().remove(taskesc.getThroughput());
        taskesc.signalTerminate();
        t2.interrupt();
        t2 = null;
    }

    @FXML
    private void stopEpidemicMultiCopySim(ActionEvent event) {
        radioContactRatioSeries.getData().remove(taskemc.getRadioContactRatio());
        throughputCompareChart.getData().remove(taskemc.getThroughput());
        taskemc.signalTerminate();
        t3.interrupt();
        t3 = null;
    }

    @FXML
    private void stopExpectedMeetingtimeSim(ActionEvent event) {
        radioContactRatioSeries.getData().remove(taskemt.getRadioContactRatio());
        throughputCompareChart.getData().remove(taskemt.getThroughput());
        taskemt.signalTerminate();
        t4.interrupt();
        t4 = null;
    }

    @FXML
    private void stopGameTheoryRoutingSim(ActionEvent event) {
        radioContactRatioSeries.getData().remove(taskgtr.getRadioContactRatio());
        throughputCompareChart.getData().remove(taskgtr.getThroughput());
        taskgtr.signalTerminate();
        t5.interrupt();
        t5 = null;
    }

    @FXML
    private void stopProphetRoutingSim(ActionEvent event) {
        radioContactRatioSeries.getData().remove(taskpr.getRadioContactRatio());
        throughputCompareChart.getData().remove(taskpr.getThroughput());
        taskpr.signalTerminate();
        t6.interrupt();
        t6 = null;
    }

    @FXML
    private void stopNewProphetRoutingSim(ActionEvent event) {
        radioContactRatioSeries.getData().remove(tasknpr.getRadioContactRatio());
        throughputCompareChart.getData().remove(tasknpr.getThroughput());
        tasknpr.signalTerminate();
        t7.interrupt();
        t7 = null;
    }

    @FXML
    private void showXYRCLViewCTH(ActionEvent event) {
        addRCLSeriesXY(taskcth);
    }

    @FXML
    private void showXTRCLViewCTH(ActionEvent event) {
        addRCLSeriesXT(taskcth);
    }

    @FXML
    private void showYTRCLViewCTH(ActionEvent event) {
        addRCLSeriesYT(taskcth);
    }

    @FXML
    private void showXYRCLViewESC(ActionEvent event) {
        addRCLSeriesXY(taskesc);
    }

    @FXML
    private void showXTRCLViewESC(ActionEvent event) {
        addRCLSeriesXT(taskesc);
    }

    @FXML
    private void showYTRCLViewESC(ActionEvent event) {
        addRCLSeriesYT(taskesc);
    }

    @FXML
    private void showXYRCLViewEMC(ActionEvent event) {
        addRCLSeriesXY(taskemc);
    }

    @FXML
    private void showXTRCLViewEMC(ActionEvent event) {
        addRCLSeriesXT(taskemc);
    }

    @FXML
    private void showYTRCLViewEMC(ActionEvent event) {
        addRCLSeriesYT(taskemc);
    }

    @FXML
    private void showXYRCLViewEMT(ActionEvent event) {
        addRCLSeriesXY(taskemt);
    }

    @FXML
    private void showXTRCLViewEMT(ActionEvent event) {
        addRCLSeriesXT(taskemt);
    }

    @FXML
    private void showYTRCLViewEMT(ActionEvent event) {
        addRCLSeriesYT(taskemt);
    }

    @FXML
    private void showXYRCLViewGTR(ActionEvent event) {
        addRCLSeriesXY(taskgtr);
    }

    @FXML
    private void showXTRCLViewGTR(ActionEvent event) {
        addRCLSeriesXT(taskgtr);
    }

    @FXML
    private void showYTRCLViewGTR(ActionEvent event) {
        addRCLSeriesYT(taskgtr);
    }

    @FXML
    private void showXYRCLViewPR(ActionEvent event) {
        addRCLSeriesXY(taskpr);
    }

    @FXML
    private void showXTRCLViewPR(ActionEvent event) {
        addRCLSeriesXT(taskpr);
    }

    @FXML
    private void showYTRCLViewPR(ActionEvent event) {
        addRCLSeriesYT(taskpr);
    }

    @FXML
    private void showXYRCLViewNPR(ActionEvent event) {
        addRCLSeriesXY(tasknpr);
    }

    @FXML
    private void showXTRCLViewNPR(ActionEvent event) {
        addRCLSeriesXT(tasknpr);
    }

    @FXML
    private void showYTRCLViewNPR(ActionEvent event) {
        addRCLSeriesYT(tasknpr);
    }

    @FXML
    private void showGPSLiveViewCTH(ActionEvent event) {
        addGPSLiveSeries(taskcth);
    }

    @FXML
    private void showGPSLiveViewESC(ActionEvent event) {
        addGPSLiveSeries(taskesc);
    }

    @FXML
    private void showGPSLiveViewEMC(ActionEvent event) {
        addGPSLiveSeries(taskemc);
    }

    @FXML
    private void showGPSLiveViewEMT(ActionEvent event) {
        addGPSLiveSeries(taskemt);
    }

    @FXML
    private void showGPSLiveViewGTR(ActionEvent event) {
        addGPSLiveSeries(taskgtr);
    }

    @FXML
    private void showGPSLiveViewPR(ActionEvent event) {
        addGPSLiveSeries(taskpr);
    }

    @FXML
    private void showGPSLiveViewNPR(ActionEvent event) {
        addGPSLiveSeries(tasknpr);
    }

    @FXML
    private void showThroughputCTH(ActionEvent event) {
        addThroughputSeries(taskcth);
    }

    @FXML
    private void showThroughputESC(ActionEvent event) {
        addThroughputSeries(taskesc);
    }

    @FXML
    private void showThroughputEMC(ActionEvent event) {
        addThroughputSeries(taskemc);
    }

    @FXML
    private void showThroughputEMT(ActionEvent event) {
        addThroughputSeries(taskemt);
    }

    @FXML
    private void showThroughputGTR(ActionEvent event) {
        addThroughputSeries(taskgtr);
    }

    @FXML
    private void showThroughputPR(ActionEvent event) {
        addThroughputSeries(taskpr);
    }

    @FXML
    private void showThroughputNPR(ActionEvent event) {
        addThroughputSeries(tasknpr);
    }

    @FXML
    private void toggleTheme(ActionEvent event) {
        if (darkTheme.isSelected())
        {
            (menuBar.getScene()).getStylesheets().clear();
            (menuBar.getScene()).getStylesheets().add(SimController.class.getResource("/bucksimulator/css/DarkTheme.css").toExternalForm());
            prefs.putBoolean(normalTheme, false);
            
            Iterator it = popoutStages.entrySet().iterator();
            while (it.hasNext())
            {
                Map.Entry pair = (Map.Entry) it.next();
                for (Stage stage : (ArrayList<Stage>) pair.getValue())
                {
                    stage.getScene().getStylesheets().clear();
                    stage.getScene().getStylesheets().add(SimController.class.getResource("/bucksimulator/css/DarkTheme.css").toExternalForm());
                }
            }
        }
        else
        {
            (menuBar.getScene()).getStylesheets().clear();
            (menuBar.getScene()).getStylesheets().add(SimController.class.getResource("/bucksimulator/css/NormalTheme.css").toExternalForm());
            prefs.putBoolean(normalTheme, true);
            Iterator it = popoutStages.entrySet().iterator();
            while (it.hasNext())
            {
                Map.Entry pair = (Map.Entry) it.next();
                for (Stage stage : (ArrayList<Stage>) pair.getValue())
                {
                    stage.getScene().getStylesheets().clear();
                    stage.getScene().getStylesheets().add(SimController.class.getResource("/bucksimulator/css/NormalTheme.css").toExternalForm());
                }
            }
        }
        
    }

    @FXML
    private void showSimStatus(ActionEvent event) {
        masterTabPane.getSelectionModel().select(simStatusTab);
    }

    @FXML
    private void showDataCenterCTH(ActionEvent event) {
        addDataCenterSeries(taskcth);
    }

    @FXML
    private void showDataCenterESC(ActionEvent event) {
        addDataCenterSeries(taskesc);
    }

    @FXML
    private void showDataCenterEMC(ActionEvent event) {
        addDataCenterSeries(taskemc);
    }

    @FXML
    private void showDataCenterEMT(ActionEvent event) {
        addDataCenterSeries(taskemt);
    }

    @FXML
    private void showDataCenterGTR(ActionEvent event) {
        addDataCenterSeries(taskgtr);
    }

    @FXML
    private void showDataCenterPR(ActionEvent event) {
        addDataCenterSeries(taskpr);
    }

    @FXML
    private void showDataCenterNPR(ActionEvent event) {
        addDataCenterSeries(tasknpr);
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

    @FXML
    private void popoutCTHCharts(ActionEvent event) throws IOException 
    {
        if (popoutCTHChartsMI.isSelected())
        {
            masterTabPane.getSelectionModel().select(busyTab);
            cthViewMI.setDisable(true);
            new Thread(() ->
            {
                //Below delay is to ensure the fx thread gets time to show busy tab.
                try
                {
                    Thread.currentThread().sleep(100);
                }
                catch (InterruptedException ex)
                {
                    Logger.getLogger(SimController.class.getName()).log(Level.SEVERE, null, ex);
                }
                ArrayList<Stage> stages = new ArrayList<>();
                popoutStages.put(taskcth.simName(), stages);
                generateGPSLivePopoutCharts(taskcth);
                generateRCLXYPopoutCharts(taskcth);
                generateRCLXTPopoutCharts(taskcth);
                generateRCLYTPopoutCharts(taskcth);
                generateGPSLogPopoutCharts(taskcth);
                generateGPSLiveDataTablePopout(taskcth);
                generateDCLogPopoutCharts(taskcth);
                masterTabPane.getSelectionModel().select(simStatusTab);
            }).start();
        }
        else
        {
            ArrayList<Stage> stages = popoutStages.get(taskcth.simName());
            for (Stage stage:stages)
            {
                stage.close();
            }
            stages.clear();
            cthViewMI.setDisable(false);
        }
    }

    @FXML
    private void popoutESCCharts(ActionEvent event)
    {
        if (popoutESCChartsMI.isSelected())
        {
            masterTabPane.getSelectionModel().select(busyTab);
            escViewMI.setDisable(true);
            new Thread(() ->
            {
                //Below delay is to ensure the fx thread gets time to show busy tab.
                try
                {
                    Thread.currentThread().sleep(100);
                }
                catch (InterruptedException ex)
                {
                    Logger.getLogger(SimController.class.getName()).log(Level.SEVERE, null, ex);
                }
                ArrayList<Stage> stages = new ArrayList<>();
                popoutStages.put(taskesc.simName(), stages);
                generateGPSLivePopoutCharts(taskesc);
                generateRCLXYPopoutCharts(taskesc);
                generateRCLXTPopoutCharts(taskesc);
                generateRCLYTPopoutCharts(taskesc);
                generateGPSLogPopoutCharts(taskesc);
                generateGPSLiveDataTablePopout(taskesc);
                generateDCLogPopoutCharts(taskesc);
                masterTabPane.getSelectionModel().select(simStatusTab);
            }).start();
        }
        else
        {
            ArrayList<Stage> stages = popoutStages.get(taskesc.simName());
            for (Stage stage:stages)
            {
                stage.close();
            }
            stages.clear();
            escViewMI.setDisable(false);
        }
    }
    
    @FXML
    private void popoutEMCCharts(ActionEvent event)
    {
        if (popoutEMCChartsMI.isSelected())
        {
            masterTabPane.getSelectionModel().select(busyTab);
            emcViewMI.setDisable(true);
            new Thread(() ->
            {
                //Below delay is to ensure the fx thread gets time to show busy tab.
                try
                {
                    Thread.currentThread().sleep(100);
                }
                catch (InterruptedException ex)
                {
                    Logger.getLogger(SimController.class.getName()).log(Level.SEVERE, null, ex);
                }
                ArrayList<Stage> stages = new ArrayList<>();
                popoutStages.put(taskemc.simName(), stages);
                generateGPSLivePopoutCharts(taskemc);
                generateRCLXYPopoutCharts(taskemc);
                generateRCLXTPopoutCharts(taskemc);
                generateRCLYTPopoutCharts(taskemc);
                generateGPSLiveDataTablePopout(taskemc);
                generateGPSLogPopoutCharts(taskemc);
                generateDCLogPopoutCharts(taskemc);
                masterTabPane.getSelectionModel().select(simStatusTab);
            }).start();
        }
        else
        {
            ArrayList<Stage> stages = popoutStages.get(taskemc.simName());
            for (Stage stage:stages)
            {
                stage.close();
            }
            stages.clear();
            emcViewMI.setDisable(false);
        }
    }
    
    @FXML
    private void popoutEMTCharts(ActionEvent event)
    {
        if (popoutEMTChartsMI.isSelected())
        {
            masterTabPane.getSelectionModel().select(busyTab);
            emtViewMI.setDisable(true);
            new Thread(() ->
            {
                //Below delay is to ensure the fx thread gets time to show busy tab.
                try
                {
                    Thread.currentThread().sleep(100);
                }
                catch (InterruptedException ex)
                {
                    Logger.getLogger(SimController.class.getName()).log(Level.SEVERE, null, ex);
                }
                ArrayList<Stage> stages = new ArrayList<>();
                popoutStages.put(taskemt.simName(), stages);
                generateGPSLivePopoutCharts(taskemt);
                generateRCLXYPopoutCharts(taskemt);
                generateRCLXTPopoutCharts(taskemt);
                generateRCLYTPopoutCharts(taskemt);
                generateGPSLiveDataTablePopout(taskemt);
                generateGPSLogPopoutCharts(taskemt);
                generateDCLogPopoutCharts(taskemt);
                masterTabPane.getSelectionModel().select(simStatusTab);
            }).start();
        }
        else
        {
            ArrayList<Stage> stages = popoutStages.get(taskemt.simName());
            for (Stage stage:stages)
            {
                stage.close();
            }
            stages.clear();
            emtViewMI.setDisable(false);
        }
    }
    
    @FXML
    private void popoutGTRCharts(ActionEvent event)
    {
        if (popoutGTRChartsMI.isSelected())
        {
            masterTabPane.getSelectionModel().select(busyTab);
            gtrViewMI.setDisable(true);
            new Thread(() ->
            {
                //Below delay is to ensure the fx thread gets time to show busy tab.
                try
                {
                    Thread.currentThread().sleep(100);
                }
                catch (InterruptedException ex)
                {
                    Logger.getLogger(SimController.class.getName()).log(Level.SEVERE, null, ex);
                }
                ArrayList<Stage> stages = new ArrayList<>();
                popoutStages.put(taskgtr.simName(), stages);
                generateGPSLivePopoutCharts(taskgtr);
                generateRCLXYPopoutCharts(taskgtr);
                generateRCLXTPopoutCharts(taskgtr);
                generateRCLYTPopoutCharts(taskgtr);
                generateGPSLiveDataTablePopout(taskgtr);
                generateGPSLogPopoutCharts(taskgtr);
                generateDCLogPopoutCharts(taskgtr);
                masterTabPane.getSelectionModel().select(simStatusTab);
            }).start();
        }
        else
        {
            ArrayList<Stage> stages = popoutStages.get(taskgtr.simName());
            for (Stage stage:stages)
            {
                stage.close();
            }
            stages.clear();
            gtrViewMI.setDisable(false);
        }
    }
    
    @FXML
    private void popoutPRCharts(ActionEvent event)
    {
        if (popoutPRChartsMI.isSelected())
        {
            masterTabPane.getSelectionModel().select(busyTab);
            prViewMI.setDisable(true);
            new Thread(() ->
            {
                //Below delay is to ensure the fx thread gets time to show busy tab.
                try
                {
                    Thread.currentThread().sleep(100);
                }
                catch (InterruptedException ex)
                {
                    Logger.getLogger(SimController.class.getName()).log(Level.SEVERE, null, ex);
                }
                ArrayList<Stage> stages = new ArrayList<>();
                popoutStages.put(taskpr.simName(), stages);
                generateGPSLivePopoutCharts(taskpr);
                generateRCLXYPopoutCharts(taskpr);
                generateRCLXTPopoutCharts(taskpr);
                generateRCLYTPopoutCharts(taskpr);
                generateGPSLiveDataTablePopout(taskpr);
                generateGPSLogPopoutCharts(taskpr);
                generateDCLogPopoutCharts(taskpr);
                masterTabPane.getSelectionModel().select(simStatusTab);
            }).start();
        }
        else
        {
            ArrayList<Stage> stages = popoutStages.get(taskpr.simName());
            for (Stage stage:stages)
            {
                stage.close();
            }
            stages.clear();
            prViewMI.setDisable(false);
        }
    }
    
    @FXML
    private void popoutNPRCharts(ActionEvent event)
    {
        if (popoutNPRChartsMI.isSelected())
        {
            masterTabPane.getSelectionModel().select(busyTab);
            nprViewMI.setDisable(true);
            new Thread(() ->
            {
                //Below delay is to ensure the fx thread gets time to show busy tab.
                try
                {
                    Thread.currentThread().sleep(100);
                }
                catch (InterruptedException ex)
                {
                    Logger.getLogger(SimController.class.getName()).log(Level.SEVERE, null, ex);
                }
                ArrayList<Stage> stages = new ArrayList<>();
                popoutStages.put(tasknpr.simName(), stages);
                generateGPSLivePopoutCharts(tasknpr);
                generateRCLXYPopoutCharts(tasknpr);
                generateRCLXTPopoutCharts(tasknpr);
                generateRCLYTPopoutCharts(tasknpr);
                generateGPSLiveDataTablePopout(tasknpr);
                generateGPSLogPopoutCharts(tasknpr);
                generateDCLogPopoutCharts(tasknpr);
                masterTabPane.getSelectionModel().select(simStatusTab);
            }).start();
        }
        else
        {
            ArrayList<Stage> stages = popoutStages.get(tasknpr.simName());
            for (Stage stage:stages)
            {
                stage.close();
            }
            stages.clear();
            nprViewMI.setDisable(false);
        }
    }
    
    @FXML
    private void showRadioContactRatioChart(ActionEvent event) {
        masterTabPane.getSelectionModel().select(rcrTab);
    }

    @FXML
    private void showThroughputCompareChart(ActionEvent event) {
        masterTabPane.getSelectionModel().select(throughputCompareTab);
    }

    @FXML
    private void saveChart(KeyEvent event) {
        if (event.isControlDown() && event.getCode() == KeyCode.S)
        {
            Tab t = masterTabPane.getSelectionModel().getSelectedItem();
            Node n = t.getContent();
            Node chart = n.lookup("ScatterChart");
            if(chart == null)
            {
                chart = n.lookup("BarChart");
                if(chart == null)
                {
                    chart = n.lookup("LineChart");
                }
            }
            if(chart != null)
            {   
                SnapshotParameters parameters = new SnapshotParameters();
                if(darkTheme.isSelected())
                {
                    Color backgroundColor = Color.valueOf("#4E555A"); //https://convertingcolors.com/
                    parameters.setFill(backgroundColor);
                }
                WritableImage image = chart.snapshot(parameters, null);
                saveChartToImage(image);
            }
        }
        else if(event.getCode() == KeyCode.P)
        {
            if (gpsLiveTab.isSelected() || rclTab.isSelected() || nodeDataTables.isSelected())
            {
                pausePlaySpecificSim(currentSim);
            }
        }
    }

    @FXML
    private void zoomChart(MouseEvent event) {
        if(zoomStarted)
        {
            rclYAxis.setAutoRanging(false);
            double lowerbound = zoomStartY <= zoomEndY ? zoomStartY : zoomEndY;
            double upperbound = zoomEndY >= zoomStartY ? zoomEndY : zoomStartY;
            rclYAxis.setLowerBound(lowerbound);
            rclYAxis.setUpperBound(upperbound);
            rclYAxis.setTickUnit(5);
            zoomArea.setWidth(0);
            zoomArea.setHeight(0);
            zoomStarted = false;
        }
    }

    @FXML
    private void setZoomStartLocation(MouseEvent event) {
        mouseAnchor.set(new Point2D(event.getX(), event.getY()));
        Point2D pointInScene = new Point2D(event.getSceneX(), event.getSceneY());
        double yPosInAxis = rclYAxis.sceneToLocal(new Point2D(0, pointInScene.getY())).getY();
        double y1 = rclYAxis.getValueForDisplay(yPosInAxis).doubleValue();
        zoomEndY = y1;
    }

    @FXML
    private void setZoomSize(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();
        zoomArea.setX(Math.min(x, mouseAnchor.get().getX()));
        zoomArea.setY(Math.min(y, mouseAnchor.get().getY()));
        zoomArea.setWidth(Math.abs(x - mouseAnchor.get().getX()));
        zoomArea.setHeight(Math.abs(y - mouseAnchor.get().getY()));
        zoomStarted = true;
        Point2D pointInScene = new Point2D(event.getSceneX(), event.getSceneY());
        double yPosInAxis = rclYAxis.sceneToLocal(new Point2D(0, pointInScene.getY())).getY();
        double y1 = rclYAxis.getValueForDisplay(yPosInAxis).doubleValue();
        zoomStartY = y1;
    }

    @FXML
    private void resetChartZoom(KeyEvent event) {
        if (event.getCode() == KeyCode.R) {
            rclYAxis.setLowerBound(0);
            rclYAxis.setAutoRanging(true);
            radioContactLogChart.layout();
            zoomArea.setWidth(0);
            zoomArea.setHeight(0);
            zoomStarted = false;
        }
    }

    @FXML
    private void toggleCharts(ScrollEvent event) {
        //Disabled this feature not to interfere with the table scrolling in GPS Live Data Tables.
        /*if (event.getDeltaY() > 0 )
        {
            masterTabPane.getSelectionModel().selectNext();
        }
        else if (event.getDeltaY() < 0)
        {
            masterTabPane.getSelectionModel().selectPrevious();
        }*/
    }

    @FXML
    private void showGPSLogViewCTH(ActionEvent event) {
        addGPSLogSeries(taskcth);
    }

    @FXML
    private void showGPSLogViewESC(ActionEvent event) {
        addGPSLogSeries(taskesc);
    }

    @FXML
    private void showGPSLogViewEMC(ActionEvent event) {
        addGPSLogSeries(taskemc);
    }

    @FXML
    private void showGPSLogViewEMT(ActionEvent event) {
        addGPSLogSeries(taskemt);
    }

    @FXML
    private void showGPSLogViewGTR(ActionEvent event) {
        addGPSLogSeries(taskgtr);
    }

    @FXML
    private void showGPSLogViewPR(ActionEvent event) {
        addGPSLogSeries(taskpr);
    }

    @FXML
    private void showGPSLogViewNPR(ActionEvent event) {
        addGPSLogSeries(tasknpr);
    }

    @FXML
    private void exportCTHSimData(ActionEvent event)
    {
        String folderPath = browseFolder(true);
        if (!folderPath.isBlank())
        {
            new Thread(() ->
            {
                taskcth.exportSimulationData(folderPath);
            }).start();
        }
    }

    @FXML
    private void exportESCSimData(ActionEvent event)
    {
        String folderPath = browseFolder(true);
        if (!folderPath.isBlank())
        {
            new Thread(() ->
            {
                taskesc.exportSimulationData(folderPath);
            }).start();
        }
    }

    @FXML
    private void exportEMCSimData(ActionEvent event)
    {
        String folderPath = browseFolder(true);
        if (!folderPath.isBlank())
        {
            new Thread(() ->
            {
                taskemc.exportSimulationData(folderPath);
            }).start();
        }
    }

    @FXML
    private void exportEMTSimData(ActionEvent event)
    {
        String folderPath = browseFolder(true);
        if (!folderPath.isBlank())
        {
            new Thread(() ->
            {
                taskemt.exportSimulationData(folderPath);
            }).start();
        }
    }

    @FXML
    private void exportGTRSimData(ActionEvent event)
    {
        String folderPath = browseFolder(true);
        if (!folderPath.isBlank())
        {
            new Thread(() ->
            {
                taskgtr.exportSimulationData(folderPath);
            }).start();
        }
    }

    @FXML
    private void exportPRSimData(ActionEvent event)
    {
        String folderPath = browseFolder(true);
        if (!folderPath.isBlank())
        {
            new Thread(() ->
            {
                taskpr.exportSimulationData(folderPath);
            }).start();
        }
    }

    @FXML
    private void exportNPRSimData(ActionEvent event)
    {
        String folderPath = browseFolder(true);
        if (!folderPath.isBlank())
        {
            new Thread(() ->
            {
                tasknpr.exportSimulationData(folderPath);
            }).start();
        }
    }

    @FXML
    private void removeStartScreenTab(Event event)
    {
        //Remove the startscreen once gps live tab is shown.
        masterTabPane.getTabs().remove(startScreenTab);
    }

    @FXML
    private void showCTHGpsDataTable(ActionEvent event)
    {
        addGPSLiveDataTable(taskcth);
    }

    @FXML
    private void showESCGpsDataTable(ActionEvent event)
    {
        addGPSLiveDataTable(taskesc);
    }

    @FXML
    private void showEMCGpsDataTable(ActionEvent event)
    {
        addGPSLiveDataTable(taskemc);
    }

    @FXML
    private void showEMTGpsDataTable(ActionEvent event)
    {
        addGPSLiveDataTable(taskemt);
    }

    @FXML
    private void showGTRGpsDataTable(ActionEvent event)
    {
        addGPSLiveDataTable(taskgtr);
    }

    @FXML
    private void showPRGpsDataTable(ActionEvent event)
    {
        addGPSLiveDataTable(taskpr);
    }

    @FXML
    private void showNPRGpsDataTable(ActionEvent event)
    {
        addGPSLiveDataTable(tasknpr);
    }

    @FXML
    private void pauseResumeCTHSim(ActionEvent event)
    {
        try
        {
            t1.interrupt();
        } catch (Exception ex)
        {
            Logger.getLogger(SimController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void pauseResumeESCSim(ActionEvent event)
    {
        try
        {
            t2.interrupt();
        } catch (Exception ex)
        {
            Logger.getLogger(SimController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void pauseResumeEMCSim(ActionEvent event)
    {
        try
        {
            t3.interrupt();
        } catch (Exception ex)
        {
            Logger.getLogger(SimController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void pauseResumeEMTSim(ActionEvent event)
    {
        try
        {
            t4.interrupt();
        } catch (Exception ex)
        {
            Logger.getLogger(SimController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void pauseResumeGTRSim(ActionEvent event)
    {
        try
        {
            t5.interrupt();
        } catch (Exception ex)
        {
            Logger.getLogger(SimController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void pauseResumePRSim(ActionEvent event)
    {
        try
        {
            t6.interrupt();
        } catch (Exception ex)
        {
            Logger.getLogger(SimController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void pauseResumeNPRSim(ActionEvent event)
    {
        try
        {
            t7.interrupt();
        } catch (Exception ex)
        {
            Logger.getLogger(SimController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void addTooltipsForChart(MouseEvent event)
    {
        addTootips(radioContactLogChart);
    }

    @FXML
    private void addLabelForRCRBars(Event event)
    {
        addLabelForThroughputBar(radioContactRatioChart);
    }

    @FXML
    private void addLabelForTpCompareChart(Event event)
    {
        addLabelForThroughputBar(throughputCompareChart);
    }

    @FXML
    private void showThroughputCompareChartLC(ActionEvent event)
    {
        masterTabPane.getSelectionModel().select(throughputCompareTabLC);
    }

    @FXML
    private void validatePreferences(ActionEvent event)
    {
//        if(popoutStages.isEmpty())
//        {
//            popoutChartsMI.setSelected(false);
//        }
    }
    
    void generateGPSLivePopoutCharts(Simulation sim)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(SimController.class.getResource("/bucksimulator/ui/GPSLive.fxml"));
            Scene scene = new Scene(loader.load());
            if (darkTheme.isSelected())
            {
                scene.getStylesheets().add(SimController.class.getResource("/bucksimulator/css/DarkTheme.css").toExternalForm());
            }
            else
            {
                scene.getStylesheets().add(SimController.class.getResource("/bucksimulator/css/NormalTheme.css").toExternalForm());
            }
            GPSLiveController controller = loader.<GPSLiveController>getController();
 
            Platform.runLater(() ->
            {
                controller.setData(sim);
                Stage stage = new Stage();
                stage.setTitle(sim.simName() + " GPS Live");
                stage.setScene(scene);
                stage.setResizable(true);
                stage.show();
                stage.setOnHidden(e ->
                {
                    addGPSLiveSeries(sim);
                });
                stage.setOnCloseRequest(e -> e.consume());
                stage.getScene().setOnKeyPressed(event -> 
                {
                    if (event.isControlDown() && event.getCode() == KeyCode.S)
                    {
                        SnapshotParameters parameters = new SnapshotParameters();
                        if (darkTheme.isSelected())
                        {
                            Color backgroundColor = Color.valueOf("#373e43");
                            parameters.setFill(backgroundColor);
                        }
                        WritableImage image = controller.getChart().snapshot(parameters, null);
                        saveChartToImage(image);
                    }
                    else if(event.getCode() == KeyCode.P)
                    {
                        pausePlaySpecificSim(sim);
                    }
                });
                popoutStages.get(sim.simName()).add(stage);
            });
        }
        catch (IOException ex)
        {
            Logger.getLogger(SimController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void generateRCLXYPopoutCharts(Simulation sim)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(SimController.class.getResource("/bucksimulator/ui/RadioContactLogXY.fxml"));
            Scene scene = new Scene(loader.load());
            if (darkTheme.isSelected())
            {
                scene.getStylesheets().add(SimController.class.getResource("/bucksimulator/css/DarkTheme.css").toExternalForm());
            }
            else
            {
                scene.getStylesheets().add(SimController.class.getResource("/bucksimulator/css/NormalTheme.css").toExternalForm());
            }
            RadioContactLogXYController controller = loader.<RadioContactLogXYController>getController();
            
            Platform.runLater(() ->
            {
                controller.setData(sim);
                Stage stage = new Stage();
                stage.setTitle(sim.simName() + " Radio Contact XY");
                stage.setScene(scene);
                stage.setResizable(true);
                stage.show();
                stage.setOnCloseRequest(e -> e.consume());
                stage.getScene().setOnKeyPressed(event -> 
                {
                    if (event.isControlDown() && event.getCode() == KeyCode.S)
                    {
                        SnapshotParameters parameters = new SnapshotParameters();
                        if (darkTheme.isSelected())
                        {
                            Color backgroundColor = Color.valueOf("#373e43");
                            parameters.setFill(backgroundColor);
                        }
                        WritableImage image = controller.getChart().snapshot(parameters, null);
                        saveChartToImage(image);
                    }
                    else if(event.getCode() == KeyCode.P)
                    {
                        pausePlaySpecificSim(sim);
                    }
                });
                popoutStages.get(sim.simName()).add(stage);
            });
        }
        catch (IOException ex)
        {
            Logger.getLogger(SimController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    void generateRCLXTPopoutCharts(Simulation sim)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(SimController.class.getResource("/bucksimulator/ui/RadioContactLogXT.fxml"));
            Scene scene = new Scene(loader.load());
            if (darkTheme.isSelected())
            {
                scene.getStylesheets().add(SimController.class.getResource("/bucksimulator/css/DarkTheme.css").toExternalForm());
            }
            else
            {
                scene.getStylesheets().add(SimController.class.getResource("/bucksimulator/css/NormalTheme.css").toExternalForm());
            }
            RadioContactLogXTController controller = loader.<RadioContactLogXTController>getController();
            
            Platform.runLater(() ->
            {
                controller.setData(sim);
                Stage stage = new Stage();
                stage.setTitle(sim.simName() + " Radio Contact XT");
                stage.setScene(scene);
                stage.setResizable(true);
                stage.show();
                stage.setOnCloseRequest(e -> e.consume());
                stage.getScene().setOnKeyPressed(e -> controller.resetZoom(e));
                stage.getScene().setOnKeyPressed(event -> 
                {
                    if (event.isControlDown() && event.getCode() == KeyCode.S)
                    {
                        SnapshotParameters parameters = new SnapshotParameters();
                        if (darkTheme.isSelected())
                        {
                            Color backgroundColor = Color.valueOf("#373e43");
                            parameters.setFill(backgroundColor);
                        }
                        WritableImage image = controller.getChart().snapshot(parameters, null);
                        saveChartToImage(image);
                    }
                    else if(event.getCode() == KeyCode.P)
                    {
                        pausePlaySpecificSim(sim);
                    }
                });
                popoutStages.get(sim.simName()).add(stage);
            });
        }
        catch (IOException ex)
        {
            Logger.getLogger(SimController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    void generateRCLYTPopoutCharts(Simulation sim)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(SimController.class.getResource("/bucksimulator/ui/RadioContactLogYT.fxml"));
            Scene scene = new Scene(loader.load());
            if (darkTheme.isSelected())
            {
                scene.getStylesheets().add(SimController.class.getResource("/bucksimulator/css/DarkTheme.css").toExternalForm());
            }
            else
            {
                scene.getStylesheets().add(SimController.class.getResource("/bucksimulator/css/NormalTheme.css").toExternalForm());
            }
            RadioContactLogYTController controller = loader.<RadioContactLogYTController>getController();
            
            Platform.runLater(() ->
            {
                controller.setData(sim);
                Stage stage = new Stage();
                stage.setTitle(sim.simName() + " Radio Contact YT");
                stage.setScene(scene);
                stage.setResizable(true);
                stage.show();
                stage.setOnCloseRequest(e -> e.consume());
                stage.getScene().setOnKeyPressed(e -> controller.resetZoom(e));
                stage.getScene().setOnKeyPressed(event -> 
                {
                    if (event.isControlDown() && event.getCode() == KeyCode.S)
                    {
                        SnapshotParameters parameters = new SnapshotParameters();
                        if (darkTheme.isSelected())
                        {
                            Color backgroundColor = Color.valueOf("#373e43");
                            parameters.setFill(backgroundColor);
                        }
                        WritableImage image = controller.getChart().snapshot(parameters, null);
                        saveChartToImage(image);
                    }
                    else if(event.getCode() == KeyCode.P)
                    {
                        pausePlaySpecificSim(sim);
                    }
                });
                popoutStages.get(sim.simName()).add(stage);
            });
        }
        catch (IOException ex)
        {
            Logger.getLogger(SimController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    void generateGPSLogPopoutCharts(Simulation sim)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(SimController.class.getResource("/bucksimulator/ui/GPSLogData.fxml"));
            Scene scene = new Scene(loader.load());
            if (darkTheme.isSelected())
            {
                scene.getStylesheets().add(SimController.class.getResource("/bucksimulator/css/DarkTheme.css").toExternalForm());
            }
            else
            {
                scene.getStylesheets().add(SimController.class.getResource("/bucksimulator/css/NormalTheme.css").toExternalForm());
            }
            GPSLogDataController controller = loader.<GPSLogDataController>getController();
            
            Platform.runLater(() ->
            {
                controller.setData(sim);
                Stage stage = new Stage();
                stage.setTitle(sim.simName() + " GPS Log Chart");
                stage.setScene(scene);
                stage.setResizable(true);
                stage.show();
                stage.setOnCloseRequest(e -> e.consume());
                stage.getScene().setOnKeyPressed(event -> 
                {
                    if (event.isControlDown() && event.getCode() == KeyCode.S)
                    {
                        SnapshotParameters parameters = new SnapshotParameters();
                        if (darkTheme.isSelected())
                        {
                            Color backgroundColor = Color.valueOf("#373e43");
                            parameters.setFill(backgroundColor);
                        }
                        WritableImage image = controller.getChart().snapshot(parameters, null);
                        saveChartToImage(image);
                    }
                });
                popoutStages.get(sim.simName()).add(stage);
            });
        }
        catch (IOException ex)
        {
            Logger.getLogger(SimController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    void generateDCLogPopoutCharts(Simulation sim)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(SimController.class.getResource("/bucksimulator/ui/DCLogData.fxml"));
            Scene scene = new Scene(loader.load());
            if (darkTheme.isSelected())
            {
                scene.getStylesheets().add(SimController.class.getResource("/bucksimulator/css/DarkTheme.css").toExternalForm());
            }
            else
            {
                scene.getStylesheets().add(SimController.class.getResource("/bucksimulator/css/NormalTheme.css").toExternalForm());
            }
            DataCenterLogController controller = loader.<DataCenterLogController>getController();
            
            Platform.runLater(() ->
            {
                controller.setData(sim);
                Stage stage = new Stage();
                stage.setTitle(sim.simName() + " Datacenter Chart");
                stage.setScene(scene);
                stage.setResizable(true);
                stage.show();
                stage.setOnCloseRequest(e -> e.consume());
                stage.getScene().setOnKeyPressed(event -> 
                {
                    if (event.isControlDown() && event.getCode() == KeyCode.S)
                    {
                        SnapshotParameters parameters = new SnapshotParameters();
                        if (darkTheme.isSelected())
                        {
                            Color backgroundColor = Color.valueOf("#373e43");
                            parameters.setFill(backgroundColor);
                        }
                        WritableImage image = controller.getChart().snapshot(parameters, null);
                        saveChartToImage(image);
                    }
                });
                popoutStages.get(sim.simName()).add(stage);
            });
        }
        catch (IOException ex)
        {
            Logger.getLogger(SimController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    void generateGPSLiveDataTablePopout(Simulation sim)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(SimController.class.getResource("/bucksimulator/ui/GPSLiveDataTables.fxml"));
            Scene scene = new Scene(loader.load());
            if (darkTheme.isSelected())
            {
                scene.getStylesheets().add(SimController.class.getResource("/bucksimulator/css/DarkTheme.css").toExternalForm());
            }
            else
            {
                scene.getStylesheets().add(SimController.class.getResource("/bucksimulator/css/NormalTheme.css").toExternalForm());
            }
            GPSLiveDataTablesController controller = loader.<GPSLiveDataTablesController>getController();
            
            Platform.runLater(() ->
            {
                controller.setData(sim);
                Stage stage = new Stage();
                stage.setTitle(sim.simName() + " GPS Data");
                stage.setScene(scene);
                stage.setResizable(true);
                stage.show();
                stage.setOnCloseRequest(e -> e.consume());
                stage.getScene().setOnKeyPressed(event -> 
                {
                    if(event.getCode() == KeyCode.P)
                    {
                        pausePlaySpecificSim(sim);
                    }
                });
                popoutStages.get(sim.simName()).add(stage);
            });
        }
        catch (IOException ex)
        {
            Logger.getLogger(SimController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void popoutAllCharts(ActionEvent event)
    {
    }

    @FXML
    private void setFullScreen(ActionEvent event)
    {
        ((Stage) menuBar.getScene().getWindow()).setFullScreen(fullScreenMI.isSelected());
    }

    @FXML
    private void showAboutInfo(ActionEvent event)
    {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.getDialogPane().getStylesheets().add((menuBar.getScene()).getStylesheets().get(0));
        alert.setTitle("About");
        alert.setHeaderText(null);
        alert.setContentText("General purpose buck simulator\nto test different alorithms.");
        alert.showAndWait();
    }

    @FXML
    private void saveCTHSimData(ActionEvent event)
    {
        saveSimDatatoDB(taskcth);
    }

    @FXML
    private void saveESCSimData(ActionEvent event)
    {
        saveSimDatatoDB(taskesc);
    }

    @FXML
    private void saveEMCSimData(ActionEvent event)
    {
        saveSimDatatoDB(taskemc);
    }

    @FXML
    private void saveEMTSimData(ActionEvent event)
    {
        saveSimDatatoDB(taskemt);
    }

    @FXML
    private void saveGTRSimData(ActionEvent event)
    {
        saveSimDatatoDB(taskgtr);
    }

    @FXML
    private void savePRSimData(ActionEvent event)
    {
        saveSimDatatoDB(taskpr);
    }

    @FXML
    private void saveNPRSimData(ActionEvent event)
    {
        saveSimDatatoDB(tasknpr);
    }

    @FXML
    private void showDatabaseLog(ActionEvent event)
    {
        masterTabPane.getSelectionModel().select(databaseReport);
    }

    @FXML
    private void showPacketTally(ActionEvent event)
    {
        masterTabPane.getSelectionModel().select(packetTallyTab);
    }

    @FXML
    private void validateReportsMenu(Event event)
    {
        for(Simulation sim: simulations)
        {
            if(sim.getSimCompletedProperty().get())
            {
                packetTallyMI.setDisable(false);
                rcrMI.setDisable(false);
                tpCompareMI.setDisable(false);
                break;
            }
        }
    }

    @FXML
    private void autoSaveSimDataToDB(ActionEvent event)
    {
        if (autoSaveDBRB.isSelected())
        {
            taskcth.getSimCompletedProperty().addListener(e -> saveSimDatatoDB(taskcth));
            taskesc.getSimCompletedProperty().addListener(e -> saveSimDatatoDB(taskesc));
            taskemc.getSimCompletedProperty().addListener(e -> saveSimDatatoDB(taskemc));
            taskemt.getSimCompletedProperty().addListener(e -> saveSimDatatoDB(taskemt));
            taskgtr.getSimCompletedProperty().addListener(e -> saveSimDatatoDB(taskgtr));
            taskpr.getSimCompletedProperty().addListener(e -> saveSimDatatoDB(taskpr));
            tasknpr.getSimCompletedProperty().addListener(e -> saveSimDatatoDB(tasknpr));
            prefs.putBoolean(autoSave, true);
        }
        else
        {
            taskcth.getSimCompletedProperty().removeListener(e -> saveSimDatatoDB(taskcth));
            taskesc.getSimCompletedProperty().removeListener(e -> saveSimDatatoDB(taskesc));
            taskemc.getSimCompletedProperty().removeListener(e -> saveSimDatatoDB(taskemc));
            taskemt.getSimCompletedProperty().removeListener(e -> saveSimDatatoDB(taskemt));
            taskgtr.getSimCompletedProperty().removeListener(e -> saveSimDatatoDB(taskgtr));
            taskpr.getSimCompletedProperty().removeListener(e -> saveSimDatatoDB(taskpr));
            tasknpr.getSimCompletedProperty().removeListener(e -> saveSimDatatoDB(tasknpr));
            prefs.putBoolean(autoSave, false);
        }
    }
    
    void pausePlaySpecificSim(Simulation sim)
    {
        if (sim.simName().contains("Capture"))
        {
            pauseResumeCTHSim(new ActionEvent());
        }
        else if (sim.simName().contains("Single"))
        {
            pauseResumeESCSim(new ActionEvent());
        }
        else if (sim.simName().contains("Multi"))
        {
            pauseResumeEMCSim(new ActionEvent());
        }
        else if (sim.simName().contains("Expected"))
        {
            pauseResumeEMTSim(new ActionEvent());
        }
        else if (sim.simName().contains("Game"))
        {
            pauseResumeGTRSim(new ActionEvent());
        }
        else if (sim.simName().contains("New"))
        {
            pauseResumeNPRSim(new ActionEvent());
        }
        else
        {
            pauseResumePRSim(new ActionEvent());
        }
    }

    @FXML
    private void browseGPSChartBackground(ActionEvent event)
    {
        String fileName = browseFile();
        if(!fileName.isBlank())
        {
            String style = "-fx-background-image: url(\""+"file:"+ fileName +"\");";
            gpsLiveChart.lookup(".chart-plot-background").setStyle(style);
            gpsLogChart.lookup(".chart-plot-background").setStyle(style);
            dataCenterChart.lookup(".chart-plot-background").setStyle(style);
            chartBackgroundTF.setText(fileName);
        }
    }

    @FXML
    private void showPreferecesTab(ActionEvent event)
    {
        masterTabPane.getSelectionModel().select(preferencesTab);
    }

    private void scroolToEnd()
    {
        w1DataTable.requestFocus();
        w1DataTable.scrollTo(currentSim.getW1GrazeLog().size());
        
        t1DataTable.requestFocus();
        t1DataTable.scrollTo(currentSim.getT1GrazeLog().size());
        
        t2DataTable.requestFocus();
        t2DataTable.scrollTo(currentSim.getT2GrazeLog().size());
        
        t3DataTable.requestFocus();
        t3DataTable.scrollTo(currentSim.getT3GrazeLog().size());
        
        bhDataTable.requestFocus();
        bhDataTable.scrollTo(currentSim.getBHGrazeLog().size());
        
        mhDataTable.requestFocus();
        mhDataTable.scrollTo(currentSim.getMHGrazeLog().size());
        
        fhDataTable.requestFocus();
        fhDataTable.scrollTo(currentSim.getFHGrazeLog().size());
        
        nfDataTable.requestFocus();
        nfDataTable.scrollTo(currentSim.getNFGrazeLog().size());
    }
}
