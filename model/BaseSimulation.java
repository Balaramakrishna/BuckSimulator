/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bucksimulator.model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import java.sql.Time;

/**
 *
 * @author balaram
 */
public abstract class BaseSimulation implements Simulation{

    int BUCK_MAX_PACKETS = 2000;
    int DATA_MAX_PACKETS = 3500;
    int MAX_X_GRID = 25;
    int MAX_Y_GRID = 32;
    int TICK_MAX = 120960;
    int globaltick = 0;
    double last_w1d1_contact_globaltick = 0;

    int t1_x_coordinate;
    int t1_y_coordinate;
    int t1_agentid = 1;
    int[][] t1_grazecoordinate = new int[MAX_X_GRID][MAX_Y_GRID];
    int[] t1_message = new int[BUCK_MAX_PACKETS];

    int t2_x_coordinate;
    int t2_y_coordinate;
    int t2_agentid = 2;
    int[][] t2_grazecoordinate = new int[MAX_X_GRID][MAX_Y_GRID];
    int[] t2_message = new int[BUCK_MAX_PACKETS];

    int t3_x_coordinate;
    int t3_y_coordinate;
    int t3_agentid = 3;
    int[][] t3_grazecoordinate = new int[MAX_X_GRID][MAX_Y_GRID];
    int[] t3_message = new int[BUCK_MAX_PACKETS];

    int bh_x_coordinate;
    int bh_y_coordinate;
    int bh_agentid = 4;
    int[][] bh_grazecoordinate = new int[MAX_X_GRID][MAX_Y_GRID];
    int[] bh_message = new int[BUCK_MAX_PACKETS];

    int mh_x_coordinate;
    int mh_y_coordinate;
    int mh_agentid = 5;
    int[][] mh_grazecoordinate = new int[MAX_X_GRID][MAX_Y_GRID];
    int[] mh_message = new int[BUCK_MAX_PACKETS];

    int fh_x_coordinate;
    int fh_y_coordinate;
    int fh_agentid = 6;
    int[][] fh_grazecoordinate = new int[MAX_X_GRID][MAX_Y_GRID];
    int[] fh_message = new int[BUCK_MAX_PACKETS];

    int nf_x_coordinate;
    int nf_y_coordinate;
    int nf_agentid = 7;
    int[][] nf_grazecoordinate = new int[MAX_X_GRID][MAX_Y_GRID];
    int[] nf_message = new int[BUCK_MAX_PACKETS];

    int w1_x_coordinate;
    int w1_y_coordinate;
    int prev_w1_x_coordinate;
    int prev_w1_y_coordinate;
    int[] w1_message = new int[BUCK_MAX_PACKETS];
    int w1_agentid = 8;
    float w1_discoveryrate = 1;
    float w1_hideeffort = 0;
    float w1_emt = 0;
    int[] d1_message = new int[DATA_MAX_PACKETS];
    int d1_x_coordinate = 6;
    int d1_y_coordinate = 31;
    enum almanac {day, night};
    almanac dayornight = almanac.day;
    enum routing {open, shut};
    routing t1routinglock, t2routinglock, t3routinglock, bhroutinglock,
            mhroutinglock, fhroutinglock, nfroutinglock, w1logginglock;
    /* The simulation starts at 6:00 AM and it is day */

    int rightshift = 4;
    long disruption_seed = 10;
    int zero, one, two, three, four, five, six, seven, other = 0;

    int daycount = 1;
    int prevdaycount=0;
    int w1step=0;
    //Java
    Random rand = new Random();
    ArrayList<GPSLog> W1GPSLogList = new ArrayList<>();
    ArrayList<RadioContactLog> radioContactLog = new ArrayList<>();
    ObservableList<GPSLog> t1GrazeGPSLog = FXCollections.observableArrayList();
    ObservableList<GPSLog> t2GrazeGPSLog = FXCollections.observableArrayList();
    ObservableList<GPSLog> t3GrazeGPSLog = FXCollections.observableArrayList();
    ObservableList<GPSLog> bhGrazeGPSLog = FXCollections.observableArrayList();
    ObservableList<GPSLog> mhGrazeGPSLog = FXCollections.observableArrayList();
    ObservableList<GPSLog> fhGrazeGPSLog = FXCollections.observableArrayList();
    ObservableList<GPSLog> nfGrazeGPSLog = FXCollections.observableArrayList();
    ObservableList<GPSLog> w1GrazeGPSLog = FXCollections.observableArrayList();
    XYChart.Series<Number, Number> w1Series, t1Series, t2Series, t3Series,
            bhSeries, mhSeries,fhSeries, nfSeries;
    XYChart.Series rclXYSeries = new XYChart.Series<>(), rclXTSeries = new XYChart.Series<>()
            , rclYTSeries = new XYChart.Series<>();
    XYChart.Series tps = new XYChart.Series();
    XYChart.Series tpslc = new XYChart.Series();
    //String[] temp = {"T1","T2","T3","BH","MH","FH","NF","W1","D1"};
    boolean clearFirstRCL = true;
    IntegerProperty simProgress = new SimpleIntegerProperty();
    ArrayList<GPSLog> t1DTNLog = new ArrayList<>();
    ArrayList<GPSLog> t2DTNLog = new ArrayList<>();
    ArrayList<GPSLog> t3DTNLog = new ArrayList<>();
    ArrayList<GPSLog> bhDTNLog = new ArrayList<>();
    ArrayList<GPSLog> mhDTNLog = new ArrayList<>();
    ArrayList<GPSLog> fhDTNLog = new ArrayList<>();
    ArrayList<GPSLog> nfgDTNLog = new ArrayList<>();

    ArrayList<GPSLog> t1GPSLog = new ArrayList<>();
    ArrayList<GPSLog> t2GPSLog = new ArrayList<>();
    ArrayList<GPSLog> t3GPSLog = new ArrayList<>();
    ArrayList<GPSLog> bhGPSLog = new ArrayList<>();
    ArrayList<GPSLog> mhGPSLog = new ArrayList<>();
    ArrayList<GPSLog> fhGPSLog = new ArrayList<>();
    ArrayList<GPSLog> nfgGPSLog = new ArrayList<>();
    ArrayList<String> percentageLog = new ArrayList<>();
    ArrayList<XYChart.Series> dtnl = new ArrayList<>(), gpslog = new ArrayList<>();
    XYChart.Data rcrSeries;

    BooleanProperty simStarted = new SimpleBooleanProperty();
    BooleanProperty simNotStarted = new SimpleBooleanProperty();
    BooleanProperty simCompleted = new SimpleBooleanProperty();
    BooleanProperty simInProgress = new SimpleBooleanProperty();
    BooleanProperty simNotInProgress = new SimpleBooleanProperty();
    BooleanProperty simPaused = new SimpleBooleanProperty();
    BooleanProperty simSaved = new SimpleBooleanProperty();
    ObservableList<XYChart.Series<Number,Number>> gpslive = FXCollections.observableArrayList();
    boolean terminate = false;
    ArrayList<Boolean> glCheckBoxTrack = new ArrayList<>();
    float ct, t1tp, t2tp, t3tp, bhtp, mhtp, fhtp, nftp;
    ObservableList<PacktTallyModel> packetModels = FXCollections.observableArrayList();
    Time startTime, endTime;
    SimDataModel sdm;
    

    public BaseSimulation(ArrayList<XYChart.Series> dtnl) {
        this.dtnl = dtnl;
        for(int i = 0; i < 7; i++)
        {
            gpslog.add(new XYChart.Series());
            glCheckBoxTrack.add(Boolean.TRUE);
        }
        terminate = false;
        simNotStarted.set(true);
        simNotInProgress.set(true);
        simCompleted.set(false);
        w1Series = new XYChart.Series();
        t1Series = new XYChart.Series();
        t2Series = new XYChart.Series();
        t3Series = new XYChart.Series();
        bhSeries = new XYChart.Series();
        mhSeries = new XYChart.Series();
        fhSeries = new XYChart.Series();
        nfSeries = new XYChart.Series();
        gpslive.add(w1Series);
        gpslive.add(t1Series);
        gpslive.add(t2Series);
        gpslive.add(t3Series);
        gpslive.add(bhSeries);
        gpslive.add(mhSeries);
        gpslive.add(fhSeries);
        gpslive.add(nfSeries);
        packetModels.add(new PacktTallyModel(0));
        packetModels.add(new PacktTallyModel(0));
        packetModels.add(new PacktTallyModel(0));
        packetModels.add(new PacktTallyModel(0));
        packetModels.add(new PacktTallyModel(0));
        packetModels.add(new PacktTallyModel(0));
        packetModels.add(new PacktTallyModel(0));
        packetModels.add(new PacktTallyModel(0));
        packetModels.add(new PacktTallyModel(0));
        packetModels.add(new PacktTallyModel(0));
        packetModels.add(new PacktTallyModel(0));
        packetModels.add(new PacktTallyModel(0));
        packetModels.add(new PacktTallyModel(0));
        packetModels.add(new PacktTallyModel(0));
        packetModels.add(new PacktTallyModel(0));
        packetModels.add(new PacktTallyModel(0));
        packetModels.add(new PacktTallyModel(0));
        packetModels.add(new PacktTallyModel(0));
        packetModels.add(new PacktTallyModel(0));
        Platform.runLater(()->
        {
            w1Series.getData().add(new XYChart.Data<>(-1,-1));
            w1Series.setName("W1");
            t1Series.getData().add(new XYChart.Data<>(-1,-1));
            t1Series.setName("T1");
            t2Series.getData().add(new XYChart.Data<>(-1,-1));
            t2Series.setName("T2");
            t3Series.getData().add(new XYChart.Data<>(-1,-1));
            t3Series.setName("T3");
            bhSeries.getData().add(new XYChart.Data<>(-1,-1));
            bhSeries.setName("BH");
            mhSeries.getData().add(new XYChart.Data<>(-1,-1));
            mhSeries.setName("MH");
            fhSeries.getData().add(new XYChart.Data<>(-1,-1));
            fhSeries.setName("FH");
            nfSeries.getData().add(new XYChart.Data<>(-1,-1));
            nfSeries.setName("NF");
            rclXYSeries.getData().add(new XYChart.Data<>(-1,-1));
            rclXYSeries.setName("Sending Agent VS Receiving Agent IDs");
            rclXTSeries.getData().add(new XYChart.Data<>(-1,-1));
            rclXTSeries.setName("Sending Agent ID VS Global Tick");
            rclYTSeries.getData().add(new XYChart.Data<>(-1,-1));
            rclYTSeries.setName("Receiving Agent ID VS Global Tick");
        });
    }

    void baseInit()
    {
        Time t = new Time(System.currentTimeMillis());
        startTime = t;
        terminate = false;
        for(int i = 0; i < MAX_X_GRID; i++)
        {
            for(int j = 0; j < MAX_Y_GRID; j++)
            {
                t1_grazecoordinate[i][j] = 0;
                t2_grazecoordinate[i][j] = 0;
                t3_grazecoordinate[i][j] = 0;
                bh_grazecoordinate[i][j] = 0;
                mh_grazecoordinate[i][j] = 0;
                fh_grazecoordinate[i][j] = 0;
                nf_grazecoordinate[i][j] = 0;
            }
        }
        for(int i = 0; i < BUCK_MAX_PACKETS; i++)
        {
            t1_message[i] = 0;
            t2_message[i] = 0;
            t3_message[i] = 0;
            bh_message[i] = 0;
            mh_message[i] = 0;
            fh_message[i] = 0;
            nf_message[i] = 0;
            w1_message[i] = 0;
        }
        for(int i = 0; i < DATA_MAX_PACKETS; i++)
        {
            d1_message[i] = 0;
        }
        disruption_seed = 10;
        zero = one = two = three = four = five = six = seven = other = 0;
        daycount = 1;
        prevdaycount=0;
        w1step=0;
        rcrSeries.setYValue(0);
        W1GPSLogList.clear();
        radioContactLog.clear();
        t1GrazeGPSLog.clear();
        t2GrazeGPSLog.clear();
        t3GrazeGPSLog.clear();
        bhGrazeGPSLog.clear();
        mhGrazeGPSLog.clear();
        fhGrazeGPSLog.clear();
        nfGrazeGPSLog.clear();
        w1GrazeGPSLog.clear();
        Platform.runLater(()->
        {
            rclXYSeries.getData().clear();
            rclXTSeries.getData().clear();
            rclYTSeries.getData().clear();

            rclXYSeries.getData().add(new XYChart.Data<>(-1, -1));
            rclXYSeries.setName("Sending Agent VS Receiving Agent IDs");
            rclXTSeries.getData().add(new XYChart.Data<>(-1, -1));
            rclXTSeries.setName("Sending Agent ID VS Global Tick");
            rclYTSeries.getData().add(new XYChart.Data<>(-1, -1));
            rclYTSeries.setName("Receiving Agent ID VS Global Tick");

            simNotStarted.set(false);
            simPaused.set(false);
            simSaved.set(false);
            simCompleted.set(false);
        });
    }
    void buckRouter()
    {
        w1logginglock=routing.open;
        for(globaltick=1;globaltick<TICK_MAX;globaltick++)
	{
            if (Thread.interrupted())
            {
                try
                {
                    if(terminate)
                    {
                        Platform.runLater(() ->
                        {
                            simNotInProgress.set(true);
                            simInProgress.set(false);
                            simStarted.set(false);
                            simNotStarted.set(true);
                            simPaused.set(false);
                        });
                        break;
                    }
                    else
                    {
                        Platform.runLater(() ->
                        {
                            simNotInProgress.set(true);
                            simInProgress.set(false);
                            simPaused.set(true);
                        });
                        Thread.currentThread().sleep(Long.MAX_VALUE);
                    }
                } catch (InterruptedException ex) {
                    if(terminate)
                    {
                        Platform.runLater(() ->
                        {
                            simNotInProgress.set(true);
                            simInProgress.set(false);
                            simStarted.set(false);
                            simNotStarted.set(true);
                            simPaused.set(false);
                        });
                        break;
                    }
                    else
                    {
                        Platform.runLater(() ->
                        {
                            simNotInProgress.set(false);
                            simInProgress.set(true);
                            simPaused.set(false);
                        });
                    }
                }
            }
            Platform.runLater(()-> {
                simProgress.set(globaltick);
            });

            if(globaltick%8640.0==0)
            {
                if(dayornight == almanac.day)
                {
                    dayornight = almanac.night;
                    prevdaycount=daycount;
                    w1step=0;
                }
                else
                {
                    dayornight = almanac.day;
                    daycount++;
                }
            }
            if(dayornight == almanac.day)
            {
                t1routinglock=routing.open;
                if((globaltick%pacemodulator())==0)
                {
                    t1();
                }
                t2routinglock=routing.open;
                if((globaltick%pacemodulator())==0)
                {
                    t2();
                }
                t3routinglock=routing.open;
                if((globaltick%pacemodulator())==0)
                {
                    t3();
                }
                bhroutinglock=routing.open;
                if((globaltick%pacemodulator())==0)
                {
                    bh();
                }
                mhroutinglock=routing.open;
                if((globaltick%pacemodulator())==0)
                {
                    mh();
                }
                fhroutinglock=routing.open;
                if((globaltick%pacemodulator())==0)
                {
                    fh();
                }
                nfroutinglock=routing.open;
                if((globaltick%pacemodulator())==0)
                {
                    nf();
                }
                if((globaltick%10)==0)
                {
                    if ((prevdaycount!=daycount)&&(w1step<57))
                    {
                        w1();
                        w1step++;
                    }
                }
                d1();
            }
	}
    }

    /**
     * @param localagentid
     * This function simulates the buck moving north west
     * based on the random decision by buck director, after ensuring that the
     * new step indeed leads to a fresh grazing spot.
     */
    void stepnorthwest(int localagentid) {
        switch (localagentid) {
            case 1 ->
            {
                t1_x_coordinate -= 1;
                t1_y_coordinate += 1;
            }
            case 2 ->
            {
                t2_x_coordinate -= 1;
                t2_y_coordinate += 1;
            }
            case 3 ->
            {
                t3_x_coordinate -= 1;
                t3_y_coordinate += 1;
            }
            case 4 ->
            {
                bh_x_coordinate -= 1;
                bh_y_coordinate += 1;
            }
            case 5 ->
            {
                mh_x_coordinate -= 1;
                mh_y_coordinate += 1;
            }
            case 6 ->
            {
                fh_x_coordinate -= 1;
                fh_y_coordinate += 1;
            }
            case 7 ->
            {
                nf_x_coordinate -= 1;
                nf_y_coordinate += 1;
            }
            case 8 ->
            {
                w1_x_coordinate -= 1;
                w1_y_coordinate += 1;
            }
        }

    }

    /**
     * @param localagentid
     * This function simulates the buck moving north based
     * on the random decision by buck director, after ensuring that the new step
     * indeed leads to a fresh grazing spot.
     */
    void stepnorth(int localagentid) {

        switch (localagentid) {
            case 1 -> t1_y_coordinate += 1;
            case 2 -> t2_y_coordinate += 1;
            case 3 -> t3_y_coordinate += 1;
            case 4 -> bh_y_coordinate += 1;
            case 5 -> mh_y_coordinate += 1;
            case 6 -> fh_y_coordinate += 1;
            case 7 -> nf_y_coordinate += 1;
            case 8 -> w1_y_coordinate += 1;
        }
    }

    /**
     * @param localagentid
     * This function simulates the buck moving northeast
     * based on the random decision by buck director, after ensuring that the
     * new step indeed leads to a fresh grazing spot.
     */
    void stepnortheast(int localagentid) {

        switch (localagentid) {
            case 1 ->
            {
                t1_x_coordinate += 1;
                t1_y_coordinate += 1;
            }
            case 2 ->
            {
                t2_x_coordinate += 1;
                t2_y_coordinate += 1;
            }
            case 3 ->
            {
                t3_x_coordinate += 1;
                t3_y_coordinate += 1;
            }
            case 4 ->
            {
                bh_x_coordinate += 1;
                bh_y_coordinate += 1;
            }
            case 5 ->
            {
                mh_x_coordinate += 1;
                mh_y_coordinate += 1;
            }
            case 6 ->
            {
                fh_x_coordinate += 1;
                fh_y_coordinate += 1;
            }
            case 7 ->
            {
                nf_x_coordinate += 1;
                nf_y_coordinate += 1;
            }
            case 8 ->
            {
                w1_x_coordinate += 1;
                w1_y_coordinate += 1;
            }
        }

    }

    /**
     * @param localagentid
     * This function simulates the buck moving west based on
     * the random decision by buck director, after ensuring that the new step
     * indeed leads to a fresh grazing spot.
     */
    void stepwest(int localagentid) {
        switch (localagentid) {
            case 1 -> t1_x_coordinate -= 1;
            case 2 -> t2_x_coordinate -= 1;
            case 3 -> t3_x_coordinate -= 1;
            case 4 -> bh_x_coordinate -= 1;
            case 5 -> mh_x_coordinate -= 1;
            case 6 -> fh_x_coordinate -= 1;
            case 7 -> nf_x_coordinate -= 1;
            case 8 -> w1_x_coordinate -= 1;
        }

    }

    /**
     * @param localagentid
     * This function simulates the buck moving east based on
     * the random decision by buck director, after ensuring that the new step
     * indeed leads to a fresh grazing spot.
     */
    void stepeast(int localagentid) {
        switch (localagentid) {
            case 1 -> t1_x_coordinate += 1;
            case 2 -> t2_x_coordinate += 1;
            case 3 -> t3_x_coordinate += 1;
            case 4 -> bh_x_coordinate += 1;
            case 5 -> mh_x_coordinate += 1;
            case 6 -> fh_x_coordinate += 1;
            case 7 -> nf_x_coordinate += 1;
            case 8 -> w1_x_coordinate += 1;
        }
    }

    /**
     * @param localagentid
     * This function simulates the buck moving southwest
     * based on the random decision by buck director, after ensuring that the
     * new step indeed leads to a fresh grazing spot.
     */
    void stepsouthwest(int localagentid) {

        switch (localagentid) {
            case 1 ->
            {
                t1_x_coordinate -= 1;
                t1_y_coordinate -= 1;
            }
            case 2 ->
            {
                t2_x_coordinate -= 1;
                t2_y_coordinate -= 1;
            }
            case 3 ->
            {
                t3_x_coordinate -= 1;
                t3_y_coordinate -= 1;
            }
            case 4 ->
            {
                bh_x_coordinate -= 1;
                bh_y_coordinate -= 1;
            }
            case 5 ->
            {
                mh_x_coordinate -= 1;
                mh_y_coordinate -= 1;
            }
            case 6 ->
            {
                fh_x_coordinate -= 1;
                fh_y_coordinate -= 1;
            }
            case 7 ->
            {
                nf_x_coordinate -= 1;
                nf_y_coordinate -= 1;
            }
            case 8 ->
            {
                w1_x_coordinate -= 1;
                w1_y_coordinate -= 1;
            }
        }

    }

    /**
     * @param localagentid
     * This function simulates the buck moving south based
     * on the random decision by buck director, after ensuring that the new step
     * indeed leads to a fresh grazing spot.
     */
    void stepsouth(int localagentid) {
        switch (localagentid) {
            case 1 -> t1_y_coordinate -= 1;
            case 2 -> t2_y_coordinate -= 1;
            case 3 -> t3_y_coordinate -= 1;
            case 4 -> bh_y_coordinate -= 1;
            case 5 -> mh_y_coordinate -= 1;
            case 6 -> fh_y_coordinate -= 1;
            case 7 -> nf_y_coordinate -= 1;
            case 8 -> w1_y_coordinate -= 1;
        }
    }

    /**
     * @param localagentid
     * This function simulates the buck moving southeast
     * based on the random decision by buck director, after ensuring that the
     * new step indeed leads to a fresh grazing spot.
     */
    void stepsoutheast(int localagentid) {
        switch (localagentid) {
            case 1 ->
            {
                t1_x_coordinate += 1;
                t1_y_coordinate -= 1;
            }
            case 2 ->
            {
                t2_x_coordinate += 1;
                t2_y_coordinate -= 1;
            }
            case 3 ->
            {
                t3_x_coordinate += 1;
                t3_y_coordinate -= 1;
            }
            case 4 ->
            {
                bh_x_coordinate += 1;
                bh_y_coordinate -= 1;
            }
            case 5 ->
            {
                mh_x_coordinate += 1;
                mh_y_coordinate -= 1;
            }
            case 6 ->
            {
                fh_x_coordinate += 1;
                fh_y_coordinate -= 1;
            }
            case 7 ->
            {
                nf_x_coordinate += 1;
                nf_y_coordinate -= 1;
            }
            case 8 ->
            {
                w1_x_coordinate += 1;
                w1_y_coordinate -= 1;
            }
        }

    }

    /**
     * A pace modulator function decides if any of these functions have to be
     * invoked for a particular global-tick. The pace modulator function uses a
     * rand library function to bring randomness in decision making.
     *
     * @return pace modulation
     */
    int pacemodulator() {
        int buckpacedirector = ((rand.nextInt() >> rightshift) & 7);
        int rbuckpacedirector = 0;

        rbuckpacedirector = switch (buckpacedirector)
        {
            case 0 -> 2;
            case 1 -> 3;
            case 2 -> 4;
            case 3 -> 5;
            case 4 -> 6;
            case 5 -> 7;
            case 6 -> 8;
            default -> 1;
        };
        return rbuckpacedirector;
    }

    /**
     * The disruption function models disruption events happening during radio
     * contact and when message packet transfer is in progress. It uses the rand
     * library function that randomly decides whether a particular message
     * packet transfer should be disrupted or not.
     *
     * @return disrupted
     */
    boolean disruption() {
        disruption_seed = disruption_seed * 1103515245 + 12345;
        long disruption_director = (disruption_seed / 65536) % 32768;//The bits considered is changed so that c4 is unique
        int disruption_director1 = (int) ((disruption_director >> 4) & 7);

        switch (disruption_director1) /* Check the randomness of the pseudo random generator */ {
            case 0 -> zero++;
            case 1 -> one++;
            case 2 -> two++;
            case 3 -> three++;
            case 4 -> four++;
            case 5 -> five++;
            case 6 -> six++;
            case 7 -> seven++;
            default -> other++;
        }

        if (/*(disruption_director == 0)||(disruption_director == 1)||(disruption_director == 2)||(disruption_director == 3)||*/(disruption_director == 4) || (disruption_director == 5) || (disruption_director == 6) || (disruption_director == 7)) /* Disrupt 50% of the time */ {
            System.out.printf("Disruption %d \n", disruption_director);
            //Sleep(100);
            return false;
        } else {
            return true;
        }
    }

    /**
     * Saves the GPS and DTN logs into text files and generates the throughput
     * bar graph series to be drawn on bar chart.
     */
    void results(int algorithmNum) {
        Time t = new Time(System.currentTimeMillis());
        endTime = t;
        Platform.runLater(()-> {
            simStarted.set(false);
            simInProgress.set(false);
            simCompleted.set(true);
            simNotInProgress.set(true);
            simPaused.set(false);
            simNotStarted.set(true);
        });

        float t1gpslog, t2gpslog, t3gpslog, bhgpslog, mhgpslog, fhgpslog, nfgpslog,
                t1dtnlog, t2dtnlog, t3dtnlog, bhdtnlog, mhdtnlog, fhdtnlog, nfdtnlog;
        float t1logpercentage, t2logpercentage, t3logpercentage, bhlogpercentage,
                mhlogpercentage, fhlogpercentage, nflogpercentage;

        t1gpslog = t2gpslog = t3gpslog = bhgpslog = mhgpslog = fhgpslog = nfgpslog
                = t1dtnlog = t2dtnlog = t3dtnlog = bhdtnlog = mhdtnlog = fhdtnlog = nfdtnlog = 0;
        int xval, yval;

        for (int rbucklocationx = 0; rbucklocationx < MAX_X_GRID; rbucklocationx++) {
            for (int rbucklocationy = 0; rbucklocationy < MAX_Y_GRID; rbucklocationy++) {
                if (t1_grazecoordinate[rbucklocationx][rbucklocationy] == 1) {
                    t1GPSLog.add(new GPSLog(rbucklocationx, rbucklocationy));
                    t1gpslog++;
                }

                if (t2_grazecoordinate[rbucklocationx][rbucklocationy] == 1) {
                    t2GPSLog.add(new GPSLog(rbucklocationx, rbucklocationy));
                    t2gpslog++;
                }

                if (t3_grazecoordinate[rbucklocationx][rbucklocationy] == 1) {
                    t3GPSLog.add(new GPSLog(rbucklocationx, rbucklocationy));
                    t3gpslog++;
                }
                if (bh_grazecoordinate[rbucklocationx][rbucklocationy] == 1) {
                    bhGPSLog.add(new GPSLog(rbucklocationx, rbucklocationy));
                    bhgpslog++;
                }
                if (mh_grazecoordinate[rbucklocationx][rbucklocationy] == 1) {
                    mhGPSLog.add(new GPSLog(rbucklocationx, rbucklocationy));
                    mhgpslog++;
                }
                if (fh_grazecoordinate[rbucklocationx][rbucklocationy] == 1) {
                    fhGPSLog.add(new GPSLog(rbucklocationx, rbucklocationy));
                    fhgpslog++;
                }
                if (nf_grazecoordinate[rbucklocationx][rbucklocationy] == 1) {
                    nfgGPSLog.add(new GPSLog(rbucklocationx, rbucklocationy));
                    nfgpslog++;
                }
            }
        }

        for (int resultloop = 0; resultloop < DATA_MAX_PACKETS; resultloop++)
        {
            if (d1_message[resultloop] != 0) {
                if ((d1_message[resultloop] >> 11) == t1_agentid) {
                    xval = ((d1_message[resultloop] >> 6) & 31);
                    yval = (d1_message[resultloop] & 63);
                    t1DTNLog.add(new GPSLog(xval, yval));
                    t1dtnlog++;
                }
                if ((d1_message[resultloop] >> 11) == t2_agentid) {
                    xval = ((d1_message[resultloop] >> 6) & 31);
                    yval = (d1_message[resultloop] & 63);
                    t2DTNLog.add(new GPSLog(xval, yval));
                    t2dtnlog++;
                }
                if ((d1_message[resultloop] >> 11) == t3_agentid) {
                    xval = ((d1_message[resultloop] >> 6) & 31);
                    yval = (d1_message[resultloop] & 63);
                    t3DTNLog.add(new GPSLog(xval, yval));
                    t3dtnlog++;
                }
                if ((d1_message[resultloop] >> 11) == bh_agentid) {
                    xval = ((d1_message[resultloop] >> 6) & 31);
                    yval = (d1_message[resultloop] & 63);
                    bhDTNLog.add(new GPSLog(xval, yval));
                    bhdtnlog++;
                }
                if ((d1_message[resultloop] >> 11) == mh_agentid) {
                    xval = ((d1_message[resultloop] >> 6) & 31);
                    yval = (d1_message[resultloop] & 63);
                    mhDTNLog.add(new GPSLog(xval, yval));
                    mhdtnlog++;
                }
                if ((d1_message[resultloop] >> 11) == fh_agentid) {
                    xval = ((d1_message[resultloop] >> 6) & 31);
                    yval = (d1_message[resultloop] & 63);
                    fhDTNLog.add(new GPSLog(xval, yval));
                    fhdtnlog++;
                }
                if ((d1_message[resultloop] >> 11) == nf_agentid) {
                    xval = ((d1_message[resultloop] >> 6) & 31);
                    yval = (d1_message[resultloop] & 63);
                    nfgDTNLog.add(new GPSLog(xval, yval));
                    nfdtnlog++;
                }
            }
        }

        if (t1gpslog != 0) {
            t1logpercentage = (t1dtnlog / t1gpslog) * 100;
            percentageLog.add("t1percentage is " + t1logpercentage);
            Platform.runLater(()-> {
                tps.getData().add(new XYChart.Data("T1", t1logpercentage));
                tpslc.getData().add(new XYChart.Data("T1", t1logpercentage));
            });
            t1tp = t1logpercentage;
        }

        if (t2gpslog != 0) {
            t2logpercentage = (t2dtnlog / t2gpslog) * 100;
            percentageLog.add("t2percentage is " + t2logpercentage);
            Platform.runLater(()-> {
                tps.getData().add(new XYChart.Data("T2", t2logpercentage));
                tpslc.getData().add(new XYChart.Data("T2", t2logpercentage));
            });
            t2tp = t2logpercentage;
        }

        if (t3gpslog != 0) {
            t3logpercentage = (t3dtnlog / t3gpslog) * 100;
            percentageLog.add("t3percentage is " + t3logpercentage);
            Platform.runLater(()-> {
                tps.getData().add(new XYChart.Data("T3", t3logpercentage));
                tpslc.getData().add(new XYChart.Data("T3", t3logpercentage));
            });
            t3tp = t3logpercentage;
        }

        if (bhgpslog != 0) {
            bhlogpercentage = (bhdtnlog / bhgpslog) * 100;
            percentageLog.add("bhpercentage is " + bhlogpercentage);
            Platform.runLater(()-> {
                tps.getData().add(new XYChart.Data("BH", bhlogpercentage));
                tpslc.getData().add(new XYChart.Data("BH", bhlogpercentage));
            });
            bhtp = bhlogpercentage;
        }

        if (mhgpslog != 0) {
            mhlogpercentage = (mhdtnlog / mhgpslog) * 100;
            percentageLog.add("mhpercentage is " + mhlogpercentage);
            Platform.runLater(()-> {
                tps.getData().add(new XYChart.Data("MH", mhlogpercentage));
                tpslc.getData().add(new XYChart.Data("MH", mhlogpercentage));
            });
            mhtp = mhlogpercentage;
        }

        if (fhgpslog != 0) {
            fhlogpercentage = (fhdtnlog / fhgpslog) * 100;
            percentageLog.add("fhpercentage is " + fhlogpercentage);
            Platform.runLater(()-> {
                tps.getData().add(new XYChart.Data("FH", fhlogpercentage));
                tpslc.getData().add(new XYChart.Data("FH", fhlogpercentage));
            });
            fhtp = fhlogpercentage;
        }

        if (nfgpslog != 0) {
            nflogpercentage = (nfdtnlog / nfgpslog) * 100;
            percentageLog.add("nfpercentage is " + nflogpercentage);
            Platform.runLater(()-> {
                tps.getData().add(new XYChart.Data("NF", nflogpercentage));
                tpslc.getData().add(new XYChart.Data("NF", nflogpercentage));
            });
            nftp = nflogpercentage;
        }

        float gpslogSum = t1gpslog + t2gpslog + t3gpslog + bhgpslog + mhgpslog + fhgpslog + nfgpslog;
        float dtnlogSum = t1dtnlog + t2dtnlog + t3dtnlog + bhdtnlog + mhdtnlog + fhdtnlog + nfdtnlog;

             
        if (gpslogSum > 0)
        {
            float collectiveThroughput = ((dtnlogSum) / (gpslogSum)) * 100;
            Platform.runLater(()-> {
            tps.getData().add(new XYChart.Data("CT", collectiveThroughput));
            tpslc.getData().add(new XYChart.Data("CT", collectiveThroughput));
            });
            ct = collectiveThroughput;
        }

        //Add the Radio Contact Ratio value to the series
        float rcratio;
        if (dtnlogSum > 0)
        {
            rcratio = radioContactLog.size()/dtnlogSum;
        }
        else
        {
            rcratio = 0;
        }
        Platform.runLater(()-> {
        rcrSeries.setYValue(rcratio);
        });
        updateGPSLogSeries(algorithmNum);
        updateDataCenterSeries(algorithmNum);

        //Update the binding properties
        Platform.runLater(()->
        {
            simInProgress.set(false);
            simCompleted.set(true);
        });
        packetModels.clear();
        packetModels.add(new PacktTallyModel(t1gpslog));
        packetModels.add(new PacktTallyModel(t2gpslog));
        packetModels.add(new PacktTallyModel(t3gpslog));
        packetModels.add(new PacktTallyModel(bhgpslog));
        packetModels.add(new PacktTallyModel(mhgpslog));
        packetModels.add(new PacktTallyModel(fhgpslog));
        packetModels.add(new PacktTallyModel(nfgpslog));
        packetModels.add(new PacktTallyModel(gpslogSum));
        packetModels.add(new PacktTallyModel(t1dtnlog));
        packetModels.add(new PacktTallyModel(t2dtnlog));
        packetModels.add(new PacktTallyModel(t3dtnlog));
        packetModels.add(new PacktTallyModel(bhdtnlog));
        packetModels.add(new PacktTallyModel(mhdtnlog));
        packetModels.add(new PacktTallyModel(fhdtnlog));
        packetModels.add(new PacktTallyModel(nfdtnlog));
        packetModels.add(new PacktTallyModel(dtnlogSum));
        packetModels.add(new PacktTallyModel(ct));
        packetModels.add(new PacktTallyModel(radioContactLog.size()));
        packetModels.add(new PacktTallyModel(rcratio));
        
    }


    @Override
    public void exportSimulationData(String folderPath)
    {
        String algorithmName = simShortName();
        try
        {
            BufferedWriter bw = new BufferedWriter(new FileWriter(folderPath + "\\t1dtnlog_" + algorithmName + ".txt"));
            PrintWriter pw = new PrintWriter(bw);
            for (GPSLog l : t1DTNLog)
            {
                pw.println(l);
            }
            pw.close();
            bw.close();
            bw = new BufferedWriter(new FileWriter(folderPath + "\\t2dtnlog_" + algorithmName + ".txt"));
            pw = new PrintWriter(bw);
            for (GPSLog l : t2DTNLog)
            {
                pw.println(l);
            }
            pw.close();
            bw.close();
            bw = new BufferedWriter(new FileWriter(folderPath + "\\t3dtnlog_" + algorithmName + ".txt"));
            pw = new PrintWriter(bw);
            for (GPSLog l : t3DTNLog)
            {
                pw.println(l);
            }
            pw.close();
            bw.close();
            bw = new BufferedWriter(new FileWriter(folderPath + "\\bhdtnlog_" + algorithmName + ".txt"));
            pw = new PrintWriter(bw);
            for (GPSLog l : bhDTNLog)
            {
                pw.println(l);
            }
            pw.close();
            bw.close();
            bw = new BufferedWriter(new FileWriter(folderPath + "\\mhdtnlog_" + algorithmName + ".txt"));
            pw = new PrintWriter(bw);
            for (GPSLog l : mhDTNLog)
            {
                pw.println(l);
            }
            pw.close();
            bw.close();
            bw = new BufferedWriter(new FileWriter(folderPath + "\\fhdtnlog_" + algorithmName + ".txt"));
            pw = new PrintWriter(bw);
            for (GPSLog l : fhDTNLog)
            {
                pw.println(l);
            }
            pw.close();
            bw.close();
            bw = new BufferedWriter(new FileWriter(folderPath + "\\nfdtnlog_" + algorithmName + ".txt"));
            pw = new PrintWriter(bw);
            for (GPSLog l : nfgDTNLog)
            {
                pw.println(l);
            }
            pw.close();
            bw.close();

            //
            bw = new BufferedWriter(new FileWriter(folderPath + "\\t1gpslog_" + algorithmName + ".txt"));
            pw = new PrintWriter(bw);
            for (GPSLog l : t1GPSLog)
            {
                pw.println(l);
            }
            pw.close();
            bw.close();
            bw = new BufferedWriter(new FileWriter(folderPath + "\\t2gpslog_" + algorithmName + ".txt"));
            pw = new PrintWriter(bw);
            for (GPSLog l : t2GPSLog)
            {
                pw.println(l);
            }
            pw.close();
            bw.close();
            bw = new BufferedWriter(new FileWriter(folderPath + "\\t3gpslog_" + algorithmName + ".txt"));
            pw = new PrintWriter(bw);
            for (GPSLog l : t3GPSLog)
            {
                pw.println(l);
            }
            pw.close();
            bw.close();
            bw = new BufferedWriter(new FileWriter(folderPath + "\\bhgpslog_" + algorithmName + ".txt"));
            pw = new PrintWriter(bw);
            for (GPSLog l : bhGPSLog)
            {
                pw.println(l);
            }
            pw.close();
            bw.close();
            bw = new BufferedWriter(new FileWriter(folderPath + "\\mhgpslog_" + algorithmName + ".txt"));
            pw = new PrintWriter(bw);
            for (GPSLog l : mhGPSLog)
            {
                pw.println(l);
            }
            pw.close();
            bw.close();
            bw = new BufferedWriter(new FileWriter(folderPath + "\\fhgpslog_" + algorithmName + ".txt"));
            pw = new PrintWriter(bw);
            for (GPSLog l : fhGPSLog)
            {
                pw.println(l);
            }
            pw.close();
            bw.close();
            bw = new BufferedWriter(new FileWriter(folderPath + "\\nfgpslog_" + algorithmName + ".txt"));
            pw = new PrintWriter(bw);
            for (GPSLog l : nfgGPSLog)
            {
                pw.println(l);
            }
            pw.close();
            bw.close();
            bw = new BufferedWriter(new FileWriter(folderPath + "\\percentagelog_" + algorithmName + ".txt"));
            pw = new PrintWriter(bw);
            for (String s : percentageLog)
            {
                pw.println(s);
            }
            pw.close();
            bw.close();
        } catch (IOException ex)
        {

        }
    }

    void updateGPSLogSeries(final int algorithmNum)
    {
        for (GPSLog gpl : t1GPSLog) {
            Platform.runLater(()-> {
                gpslog.get(0).getData().add(new XYChart.Data<>(gpl.getxCord() - 0.5, gpl.getyCord() - 0.5));
            });
        }
        for (GPSLog gpl : t2GPSLog) {
            Platform.runLater(()-> {
                gpslog.get(1).getData().add(new XYChart.Data<>(gpl.getxCord() - 0.5, gpl.getyCord() - 0.5));
            });
        }
        for (GPSLog gpl : t3GPSLog) {
            Platform.runLater(()-> {
                gpslog.get(2).getData().add(new XYChart.Data<>(gpl.getxCord() - 0.5, gpl.getyCord() - 0.5));
            });
        }
        for (GPSLog gpl : bhGPSLog) {
            Platform.runLater(()-> {
                gpslog.get(3).getData().add(new XYChart.Data<>(gpl.getxCord() - 0.5, gpl.getyCord() - 0.5));
            });
        }
        for (GPSLog gpl : mhGPSLog) {
            Platform.runLater(()-> {
                gpslog.get(4).getData().add(new XYChart.Data<>(gpl.getxCord() - 0.5, gpl.getyCord() - 0.5));
            });
        }
        for (GPSLog gpl : fhGPSLog) {
            Platform.runLater(()-> {
                gpslog.get(5).getData().add(new XYChart.Data<>(gpl.getxCord() - 0.5, gpl.getyCord() - 0.5));
            });
        }
        for (GPSLog gpl : nfgGPSLog) {
            Platform.runLater(()-> {
                gpslog.get(6).getData().add(new XYChart.Data<>(gpl.getxCord() - 0.5, gpl.getyCord() - 0.5));
            });
        }
        Platform.runLater(()-> {
        gpslog.get(0).setName("T1");
        gpslog.get(1).setName("T2");
        gpslog.get(2).setName("T3");
        gpslog.get(3).setName("BH");
        gpslog.get(4).setName("MH");
        gpslog.get(5).setName("FH");
        gpslog.get(6).setName("NF");
        });
    }

    void updateDataCenterSeries(final int algorithmNum)
    {
        for (GPSLog gpl : t1DTNLog) {
            Platform.runLater(()-> {
                dtnl.get(algorithmNum * 7).getData().add(new XYChart.Data<>(gpl.getxCord() - 0.5, gpl.getyCord() - 0.5));
            });
        }
        for (GPSLog gpl : t2DTNLog) {
            Platform.runLater(()-> {
                dtnl.get((algorithmNum * 7)+1).getData().add(new XYChart.Data<>(gpl.getxCord() - 0.5, gpl.getyCord() - 0.5));
            });
        }
        for (GPSLog gpl : t3DTNLog) {
            Platform.runLater(()-> {
                dtnl.get((algorithmNum * 7)+2).getData().add(new XYChart.Data<>(gpl.getxCord() - 0.5, gpl.getyCord() - 0.5));
            });
        }
        for (GPSLog gpl : bhDTNLog) {
            Platform.runLater(()-> {
                dtnl.get((algorithmNum * 7)+3).getData().add(new XYChart.Data<>(gpl.getxCord() - 0.5, gpl.getyCord() - 0.5));
            });
        }
        for (GPSLog gpl : mhDTNLog) {
            Platform.runLater(()-> {
                dtnl.get((algorithmNum * 7)+4).getData().add(new XYChart.Data<>(gpl.getxCord() - 0.5, gpl.getyCord() - 0.5));
            });
        }
        for (GPSLog gpl : fhDTNLog) {
            Platform.runLater(()-> {
                dtnl.get((algorithmNum * 7)+5).getData().add(new XYChart.Data<>(gpl.getxCord() - 0.5, gpl.getyCord() - 0.5));
            });
        }
        for (GPSLog gpl : nfgDTNLog) {
            Platform.runLater(()-> {
                dtnl.get((algorithmNum * 7)+6).getData().add(new XYChart.Data<>(gpl.getxCord() - 0.5, gpl.getyCord() - 0.5));
            });
        }
        Platform.runLater(()-> {
        dtnl.get((algorithmNum * 7)).setName("T1");
        dtnl.get((algorithmNum * 7)+1).setName("T2");
        dtnl.get((algorithmNum * 7)+2).setName("T3");
        dtnl.get((algorithmNum * 7)+3).setName("BH");
        dtnl.get((algorithmNum * 7)+4).setName("MH");
        dtnl.get((algorithmNum * 7)+5).setName("FH");
        dtnl.get((algorithmNum * 7)+6).setName("NF");
        });
    }

    @Override
    public abstract void run();

    public abstract void push(int a, int b, float c);
    /**
     *This function simulates the data center.It does data pull from b1
     * when it comes in radio contact with them.It has linear memory of 560 words.
     */

    public void d1()
    {
        if((w1_x_coordinate == d1_x_coordinate)&&(w1_y_coordinate == d1_y_coordinate)&&(prev_w1_x_coordinate!=w1_x_coordinate)&&(prev_w1_y_coordinate!=w1_y_coordinate))
        {
            prev_w1_x_coordinate = w1_x_coordinate;
            prev_w1_y_coordinate = w1_y_coordinate; //last_w1d1_contact_globaltick = globaltick;
            push(8,9,0);
        }
        else
        {
            if(((w1_x_coordinate != d1_x_coordinate)||(w1_y_coordinate != d1_y_coordinate))&&((prev_w1_x_coordinate!=w1_x_coordinate)||(prev_w1_y_coordinate!=w1_y_coordinate)))
            {
                //System.out.print("w1 and d1 are Out of Range! \n");
            }
            else if((w1_x_coordinate == d1_x_coordinate)&&(w1_y_coordinate == d1_y_coordinate)&&(prev_w1_x_coordinate==w1_x_coordinate)&&(prev_w1_y_coordinate==w1_y_coordinate))
            {
                //System.out.print("w1 and d1 are In Range, However w1 is Stationary and stale contact is prevented! \n");
            }
            else
            {
                //System.out.print("w1 not pushing data to d1! \n");
            }
        }
    }

    /**
     * Wildlife data collector.
     */

    public void w1()
    {
        if((w1_x_coordinate == 6)&&(31 >= w1_y_coordinate)&&(w1_y_coordinate >= 13))
        {
                w1_y_coordinate--;
        }
        else if((6 <= w1_x_coordinate)&&(w1_x_coordinate <= 23)&&(w1_y_coordinate == 12))
        {
                w1_x_coordinate++;
        }
        else if((w1_x_coordinate == 24)&&(12 <= w1_y_coordinate)&&(w1_y_coordinate <= 13))
        {
                w1_y_coordinate++;
        }
        else if((24 >= w1_x_coordinate)&&(w1_x_coordinate >= 8)&&(14 <= w1_y_coordinate)&&(w1_y_coordinate <= 30))
        {
                w1_x_coordinate--;w1_y_coordinate++;
        }
        else if((w1_x_coordinate == 7 )&&(w1_y_coordinate == 31))
        {
                w1_x_coordinate--;
        }
        if(w1logginglock==routing.open)
        {
            W1GPSLogList.add(new GPSLog(w1_x_coordinate, w1_y_coordinate));
            w1logginglock=routing.shut;
            Platform.runLater(() -> {
            w1Series.getData().add(new XYChart.Data(w1_x_coordinate-0.5,w1_y_coordinate-0.5));
            w1Series.getData().remove(0);
            });
        }
        int duplicate = 0;
        for(GPSLog log :W1GPSLogList)
        {
            if((w1_x_coordinate==log.getxCord())&&(w1_y_coordinate==log.getyCord()))
            {
                duplicate++;
                break;
            }
        }
        if(duplicate==0)
        {
            W1GPSLogList.add(new GPSLog(w1_x_coordinate, w1_y_coordinate));
            w1GrazeGPSLog.add(new GPSLog(w1_x_coordinate, w1_y_coordinate));
            Platform.runLater(() -> {
            w1Series.getData().add(new XYChart.Data(w1_x_coordinate-0.5,w1_y_coordinate-0.5));
            w1Series.getData().remove(0);
            });
        }
        if((prev_w1_x_coordinate != w1_x_coordinate)||(prev_w1_y_coordinate != w1_y_coordinate))
        {
            prev_w1_x_coordinate = 0;// Once w1 starts moving, reset the prev coordinate value to allow contact
            prev_w1_y_coordinate = 0;// Once w1 starts moving, reset the prev coordinate value to allow contact
        }
    }

    /**
     * This function simulates the movement of a territorial blackbuck.
     * The relaxed pace of grazing is constant.However the direction is random.
     */
    public abstract void t1();

    /**
     * This function simulates the movement of a territorial blackbuck.
     * The relaxed pace of grazing is constant.However the direction is random.
     */
    public abstract void t2();

    /**
     * This function simulates the movement of a territorial blackbuck.
     * The relaxed pace of grazing is constant.However the direction is random.
     */
    public abstract void t3();

    public abstract void bh();

    /**
     * This function simulates the movement of a male blackbuck.
     * The relaxed pace of grazing is constant.However the direction is random.
     */
    public abstract void mh();

    public abstract void fh();

    public abstract void nf();

    abstract String simShortName();

    @Override
    public IntegerProperty getSimprogressProperty()
    {
        return simProgress;
    }

    @Override
    public BooleanProperty getSimStartedProperty()
    {
        return simStarted;
    }

    @Override
    public BooleanProperty getSimNotStartedProperty()
    {
        return simNotStarted;
    }

    @Override
    public BooleanProperty getSimCompletedProperty()
    {
        return simCompleted;
    }

    @Override
    public BooleanProperty getSimInProgressProperty()
    {
        return simInProgress;
    }

    @Override
    public BooleanProperty getSimNotInProgressProperty()
    {
        return simNotInProgress;
    }

    @Override
    public BooleanProperty getSimPausedProperty()
    {
        return simPaused;
    }

    @Override
    public ObservableList getGpsLiveDataProperty()
    {
        return gpslive;
    }

    @Override
    public XYChart.Series getRClXYSeries()
    {
        return rclXYSeries;
    }
    @Override
    public XYChart.Series getRCLXGTSeries()
    {
        return rclXTSeries;
    }

    @Override
    public XYChart.Series getRCLYGTSeries()
    {
        return rclYTSeries;
    }

    @Override
    public XYChart.Series getThroughput()
    {
        return tps;
    }

    @Override
    public XYChart.Series getThroughputLC()
    {
        return tpslc;
    }

    @Override
    public ObservableList<GPSLog> getT1GrazeLog()
    {
        return t1GrazeGPSLog;
    }

    @Override
    public ObservableList<GPSLog> getT2GrazeLog()
    {
        return t2GrazeGPSLog;
    }

    @Override
    public ObservableList<GPSLog> getT3GrazeLog()
    {
        return t3GrazeGPSLog;
    }

    @Override
    public ObservableList<GPSLog> getBHGrazeLog()
    {
        return bhGrazeGPSLog;
    }

    @Override
    public ObservableList<GPSLog> getMHGrazeLog()
    {
        return mhGrazeGPSLog;
    }

    @Override
    public ObservableList<GPSLog> getFHGrazeLog()
    {
        return fhGrazeGPSLog;
    }

    @Override
    public ObservableList<GPSLog> getNFGrazeLog()
    {
        return nfGrazeGPSLog;
    }

    @Override
    public ObservableList<GPSLog> getW1GrazeLog()
    {
        return w1GrazeGPSLog;
    }

    @Override
    public synchronized void pauseSim()
    {
        try
        {
            wait();
        } catch (InterruptedException ex)
        {
            Logger.getLogger(BaseSimulation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public synchronized void resumeSim()
    {
        notify();
    }

    @Override
    public void signalTerminate()
    {
        terminate = true;
    }

    @Override
    public XYChart.Data getRadioContactRatio()
    {
        return rcrSeries;
    }
    
    @Override
    public ArrayList<XYChart.Series> getGPSLog()
    {
        return gpslog;
    }
    
    @Override
    public ArrayList<Boolean> getGLCheckBoxTrack()
    {
        return glCheckBoxTrack;
    }
    
    @Override
    public int saveSimData()
    {
        if (!simSaved.get())
        {
            sdm = new SimDataModel();
            sdm.setSimName(simName());
            sdm.setSimRunDate(LocalDate.now());
            sdm.setRcr(Float.parseFloat(rcrSeries.getYValue().toString()));
            sdm.setCt(ct);
            sdm.setT1tp(t1tp);
            sdm.setT2tp(t2tp);
            sdm.setT3tp(t3tp);
            sdm.setBhtp(bhtp);
            sdm.setMhtp(mhtp);
            sdm.setFhtp(fhtp);
            sdm.setNftp(nftp);
            sdm.setSt(startTime);
            sdm.setEt(endTime);
            DBConnect.sdms.add(sdm);
            DBConnect.updateSimulationDataTable(sdm);
            Platform.runLater(() ->
            {
                simSaved.set(true);
            });
            return (DBConnect.sdms.size()-1);
        }
        else
        {
            return DBConnect.sdms.indexOf(sdm);
        }
    }
    
    @Override
    public ObservableList<PacktTallyModel> getPacktTallyData()
    {
        return packetModels;
    }
    
    @Override
    public BooleanProperty getSimSavedProperty()
    {
        return simSaved;
    }
}
