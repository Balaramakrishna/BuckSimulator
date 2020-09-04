/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bucksimulator.model;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.chart.XYChart;

/**
 *
 * @author balaram
 */
public class EpidemicSinglecopy extends BaseSimulation{
    int t1_xmax_coordinate = 12;
    int t1_ymax_coordinate = 25;
    int t1_xmin_coordinate = 8;
    int t1_ymin_coordinate = 21;
    float t1_discoveryrate = 1;
    float t1_emt = 0;

    int t2_xmax_coordinate = 14;
    int t2_ymax_coordinate = 19;
    int t2_xmin_coordinate = 10;
    int t2_ymin_coordinate = 15;
    float t2_discoveryrate = 1;
    float t2_emt = 0;

    int t3_xmax_coordinate = 14;
    int t3_ymax_coordinate = 6;
    int t3_xmin_coordinate = 10;
    int t3_ymin_coordinate = 2;
    float t3_discoveryrate = 1;
    float t3_emt = 0;

    int bh_xmax_coordinate = 24;
    int bh_ymax_coordinate = 31;
    int bh_xmin_coordinate = 0;
    int bh_ymin_coordinate = 0;
    float bh_discoveryrate = 1;
    float bh_emt = 0;

    int mh_xmax_coordinate = 24;
    int mh_ymax_coordinate = 31;
    int mh_xmin_coordinate = 0;
    int mh_ymin_coordinate = 0;
    float mh_discoveryrate = 1;
    float mh_emt = 0;

    int fh_xmax_coordinate = 24;
    int fh_ymax_coordinate = 31;
    int fh_xmin_coordinate = 0;
    int fh_ymin_coordinate = 0;
    float fh_discoveryrate = 1;
    float fh_emt = 0;

    int nf_xmax_coordinate = 24;
    int nf_ymax_coordinate = 31;
    int nf_xmin_coordinate = 0;
    int nf_ymin_coordinate = 0;
    float nf_discoveryrate = 1;
    float nf_emt = 0;

    float lebesguemeasure = 1;
    /* Set for a given simulation */
    float epsilon = 0.1f;
    /* Set for a given simulation */
    float[] sendingagentemt={700,700,700,700,700,700,700,700,700};

    long c4random_seed = 10;
    public EpidemicSinglecopy()
    {
        rcrSeries = new XYChart.Data("ESC",0);
        tps.setName("ESC");
        tpslc.setName("ESC");
    }
    
    void init()
    {
        t1_x_coordinate = 10;
        t1_y_coordinate = 23;
        t2_x_coordinate = 12;
        t2_y_coordinate = 17;
        t3_x_coordinate = 12;
        t3_y_coordinate = 4;
        bh_x_coordinate = 0;
        bh_y_coordinate = 0;
        mh_x_coordinate = 24;
        mh_y_coordinate = 0;
        fh_x_coordinate = 24;
        fh_y_coordinate = 31;
        nf_x_coordinate = 0;
        nf_y_coordinate = 31;
        w1_x_coordinate = 6;
        w1_y_coordinate = 31;
        
        prev_w1_x_coordinate = 0;
        prev_w1_y_coordinate = 0;
        for(int i = 0; i < 9; i++)
        {
            sendingagentemt[i] = 700;
        }
        t1_discoveryrate = 1;
        t1_emt = 0;
        t2_discoveryrate = 1;
        t2_emt = 0;
        t3_discoveryrate = 1;
        t3_emt = 0;
        bh_discoveryrate = 1;
        bh_emt = 0;
        fh_discoveryrate = 1;
        fh_emt = 0;
        mh_discoveryrate = 1;
        mh_emt = 0;
        nf_discoveryrate = 1;
        nf_emt = 0;
        baseInit();
    }
    /*
 ** FHdr-beg ******************************************************************
 **
 ** Function name:  main
 **
 ** Purpose: This is the main function of the simulator that calls the other
 **          functions. At its heart is the globaltick which is the basis
 **          for the timing of the simulation.
 **
 ** FHdr-end ******************************************************************
 */

    @Override
    public void run()
    {
        init();
        Platform.runLater(()->
        {
            simStarted.set(true);
            simInProgress.set(true);
            simCompleted.set(false);
            simNotInProgress.set(false);
        });
	buckRouter();
        if(!terminate)
        {
            results(1);
        }
    }

    @Override
    public void t1() 
    {
        int sendingagentid = 1;
        epidemicsinglecopyrouting(sendingagentid);
        int t1buckdirector = ((rand.nextInt() >> rightshift) & 7);
        if ((t1_x_coordinate == t1_xmax_coordinate) && (t1_y_coordinate == t1_ymax_coordinate))
        /* NE corner */ {
            switch (t1buckdirector)
            {
                case 3 -> // Step west
                    stepwest(sendingagentid);
                case 5 -> // Step south west
                    stepsouthwest(sendingagentid);
                case 6 -> // Step south
                    stepsouth(sendingagentid);
                default -> // Everything else - step southwest
                    stepsouthwest(sendingagentid);
            }
        } else if ((t1_x_coordinate == t1_xmin_coordinate) && (t1_y_coordinate == t1_ymin_coordinate))
        /* SW corner */ {
            switch (t1buckdirector)
            {
                case 1 -> // Step north
                    stepnorth(sendingagentid);
                case 2 -> // Step north east
                    stepnortheast(sendingagentid);
                case 4 -> // Step east
                    stepeast(sendingagentid);
                default -> // Everything else - step northest
                    stepnortheast(sendingagentid);
            }
        } else if ((t1_x_coordinate == t1_xmin_coordinate) && (t1_y_coordinate == t1_ymax_coordinate))
        /* NW corner */ {
            switch (t1buckdirector)
            {
                case 4 -> // Step east
                    stepeast(sendingagentid);
                case 6 -> // Step south
                    stepsouth(sendingagentid);
                case 7 -> // Step south east
                    stepsoutheast(sendingagentid);
                default -> // Everything else - step southeast
                    stepsoutheast(sendingagentid);
            }
        } else if ((t1_x_coordinate == t1_xmax_coordinate) && (t1_y_coordinate == t1_ymin_coordinate))
        /* SE corner */ {
            switch (t1buckdirector)
            {
                case 0 -> // Step north west
                    stepnorthwest(sendingagentid);
                case 1 -> // Step north
                    stepnorth(sendingagentid);
                case 3 -> // Step west
                    stepwest(sendingagentid);
                default -> // Everything else - step northwest
                    stepnorthwest(sendingagentid);
            }
        } else if ((t1_x_coordinate == t1_xmin_coordinate) && (t1_y_coordinate < t1_ymax_coordinate) && (t1_y_coordinate > t1_ymin_coordinate))
        /* Right of West*/ {
            switch (t1buckdirector)
            {
                case 1 -> // Step north
                    stepnorth(sendingagentid);
                case 2 -> // Step north east
                    stepnortheast(sendingagentid);
                case 4 -> // Step east
                    stepeast(sendingagentid);
                case 6 -> // Step south
                    stepsouth(sendingagentid);
                case 7 -> // Step south east
                    stepsoutheast(sendingagentid);
                default -> // Everything else - step east
                    stepeast(sendingagentid);
            }
        } else if ((t1_x_coordinate == t1_xmax_coordinate) && (t1_y_coordinate < t1_ymax_coordinate) && (t1_y_coordinate > t1_ymin_coordinate))
        /* Left of East*/ {
            switch (t1buckdirector)
            {
                case 0 -> // Step north west
                    stepnorthwest(sendingagentid);
                case 1 -> // Step north
                    stepnorth(sendingagentid);
                case 3 -> // Step west
                    stepwest(sendingagentid);
                case 5 -> // Step south west
                    stepsouthwest(sendingagentid);
                case 6 -> // Step south
                    stepsouth(sendingagentid);
                default -> // Everything else - step west
                    stepwest(sendingagentid);
            }
        } else if ((t1_y_coordinate == t1_ymax_coordinate) && (t1_x_coordinate < t1_xmax_coordinate) && (t1_x_coordinate > t1_xmin_coordinate))
        /* Below North*/ {
            switch (t1buckdirector)
            {
                case 3 -> // Step west
                    stepwest(sendingagentid);
                case 4 -> // Step east
                    stepeast(sendingagentid);
                case 5 -> // Step south west
                    stepsouthwest(sendingagentid);
                case 6 -> // Step south
                    stepsouth(sendingagentid);
                case 7 -> // Step south east
                    stepsoutheast(sendingagentid);
                default -> // Everything else - step south
                    stepsouth(sendingagentid);
            }
        } else if ((t1_y_coordinate == t1_ymin_coordinate) && (t1_x_coordinate < t1_xmax_coordinate) && (t1_x_coordinate > t1_xmin_coordinate))
        /* Above South*/ {
            switch (t1buckdirector)
            {
                case 0 -> // Step north west
                    stepnorthwest(sendingagentid);
                case 1 -> // Step north
                    stepnorth(sendingagentid);
                case 2 -> // Step north east
                    stepnortheast(sendingagentid);
                case 3 -> // Step west
                    stepwest(sendingagentid);
                case 4 -> // Step east
                    stepeast(sendingagentid);
                default -> // Everything else - step north
                    stepnorth(sendingagentid);
            }
        } else
        {
            switch (t1buckdirector)
            {
                case 0 -> // Step north west
                    stepnorthwest(sendingagentid);
                case 1 -> // Step north
                    stepnorth(sendingagentid);
                case 2 -> // Step north east
                    stepnortheast(sendingagentid);
                case 3 -> // Step west
                    stepwest(sendingagentid);
                case 4 -> // Step east
                    stepeast(sendingagentid);
                case 5 -> // Step south west
                    stepsouthwest(sendingagentid);
                case 6 -> // Step south
                    stepsouth(sendingagentid);
                default -> // Everything else - step south east
                    stepsoutheast(sendingagentid);
            }
        }

        t1_grazecoordinate[t1_x_coordinate][t1_y_coordinate] = 1;
        t1GrazeGPSLog.add(new GPSLog(t1_x_coordinate, t1_y_coordinate));
        Platform.runLater(() -> {
            t1Series.getData().add(new XYChart.Data(t1_x_coordinate - 0.5, t1_y_coordinate - 0.5));
            t1Series.getData().remove(0);
        });
        int t1n = 0;
        for (int rbucklocationx = 0; rbucklocationx < MAX_X_GRID; rbucklocationx++) {
            for (int rbucklocationy = 0; rbucklocationy < MAX_Y_GRID; rbucklocationy++) {
                if (t1_grazecoordinate[rbucklocationx][rbucklocationy] == 1) {
                    t1n++;
                }
            }
        }
        sendingagentemt[sendingagentid] = t1_emt = calculateemt(t1n);
        mapgridintolinearmem(sendingagentid);
    }

/*
 ** FHdr-beg ******************************************************************
 **
 ** Function name:  t2
 **
 ** Purpose: This function simulates the movement of a territorial blackbuck.
 **			 The relaxed pace of grazing is constant.However the direction is random.
 **
 ** FHdr-end ******************************************************************
 */

    @Override
    public void t2() {

        int sendingagentid = 2;
//
        epidemicsinglecopyrouting(sendingagentid);
        int t2buckdirector = ((rand.nextInt() >> rightshift) & 7);
        if ((t2_x_coordinate == t2_xmax_coordinate) && (t2_y_coordinate == t2_ymax_coordinate)) /* NE corner */ {
            switch (t2buckdirector) {
                case 3:            // Step west
                    stepwest(sendingagentid);
                    break;
                case 5:            // Step south west
                    stepsouthwest(sendingagentid);
                    break;
                case 6:            // Step south
                    stepsouth(sendingagentid);
                    break;
                default:           // Everything else - step southwest
                    stepsouthwest(sendingagentid);
                    break;
            }
        } else if ((t2_x_coordinate == t2_xmin_coordinate) && (t2_y_coordinate == t2_ymin_coordinate)) /* SW corner */ {
            switch (t2buckdirector) {
                case 1:            // Step north
                    stepnorth(sendingagentid);
                    break;
                case 2:            // Step north east
                    stepnortheast(sendingagentid);
                    break;
                case 4:            // Step east
                    stepeast(sendingagentid);
                    break;
                default:           // Everything else - step northest
                    stepnortheast(sendingagentid);
                    break;
            }
        } else if ((t2_x_coordinate == t2_xmin_coordinate) && (t2_y_coordinate == t2_ymax_coordinate)) /* NW corner */ {
            switch (t2buckdirector) {
                case 4:            // Step east
                    stepeast(sendingagentid);
                    break;
                case 6:            // Step south
                    stepsouth(sendingagentid);
                    break;
                case 7:            // Step south east
                    stepsoutheast(sendingagentid);
                    break;
                default:           // Everything else - step southeast
                    stepsoutheast(sendingagentid);
                    break;
            }
        } else if ((t2_x_coordinate == t2_xmax_coordinate) && (t2_y_coordinate == t2_ymin_coordinate)) /* SE corner */ {
            switch (t2buckdirector) {
                case 0:            // Step north west
                    stepnorthwest(sendingagentid);
                    break;
                case 1:            // Step north
                    stepnorth(sendingagentid);
                    break;
                case 3:            // Step west
                    stepwest(sendingagentid);
                    break;
                default:           // Everything else - step northwest
                    stepnorthwest(sendingagentid);
                    break;
            }
        } else if ((t2_x_coordinate == t2_xmin_coordinate) && (t2_y_coordinate < t2_ymax_coordinate) && (t2_y_coordinate > t2_ymin_coordinate)) /* Right of West*/ {
            switch (t2buckdirector) {
                case 1:            // Step north
                    stepnorth(sendingagentid);
                    break;
                case 2:            // Step north east
                    stepnortheast(sendingagentid);
                    break;
                case 4:            // Step east
                    stepeast(sendingagentid);
                    break;
                case 6:            // Step south
                    stepsouth(sendingagentid);
                    break;
                case 7:            // Step south east
                    stepsoutheast(sendingagentid);
                    break;
                default:           // Everything else - step east
                    stepeast(sendingagentid);
                    break;
            }
        } else if ((t2_x_coordinate == t2_xmax_coordinate) && (t2_y_coordinate < t2_ymax_coordinate) && (t2_y_coordinate > t2_ymin_coordinate)) /* Left of East*/ {
            switch (t2buckdirector) {
                case 0:            // Step north west
                    stepnorthwest(sendingagentid);
                    break;
                case 1:            // Step north
                    stepnorth(sendingagentid);
                    break;
                case 3:            // Step west
                    stepwest(sendingagentid);
                    break;
                case 5:            // Step south west
                    stepsouthwest(sendingagentid);
                    break;
                case 6:            // Step south
                    stepsouth(sendingagentid);
                    break;
                default:           // Everything else - step west
                    stepwest(sendingagentid);
                    break;
            }
        } else if ((t2_y_coordinate == t2_ymax_coordinate) && (t2_x_coordinate < t2_xmax_coordinate) && (t2_x_coordinate > t2_xmin_coordinate)) /* Below North*/ {
            switch (t2buckdirector) {
                case 3:            // Step west
                    stepwest(sendingagentid);
                    break;
                case 4:            // Step east
                    stepeast(sendingagentid);
                    break;
                case 5:            // Step south west
                    stepsouthwest(sendingagentid);
                    break;
                case 6:            // Step south
                    stepsouth(sendingagentid);
                    break;
                case 7:            // Step south east
                    stepsoutheast(sendingagentid);
                    break;
                default:           // Everything else - step south
                    stepsouth(sendingagentid);
                    break;
            }
        } else if ((t2_y_coordinate == t2_ymin_coordinate) && (t2_x_coordinate < t2_xmax_coordinate) && (t2_x_coordinate > t2_xmin_coordinate)) /* Above South*/ {
            switch (t2buckdirector) {
                case 0:            // Step north west
                    stepnorthwest(sendingagentid);
                    break;
                case 1:            // Step north
                    stepnorth(sendingagentid);
                    break;
                case 2:            // Step north east
                    stepnortheast(sendingagentid);
                    break;
                case 3:            // Step west
                    stepwest(sendingagentid);
                    break;
                case 4:            // Step east
                    stepeast(sendingagentid);
                    break;
                default:           // Everything else - step north
                    stepnorth(sendingagentid);
                    break;
            }
        } else {

            switch (t2buckdirector) {
                case 0:            // Step north west
                    stepnorthwest(sendingagentid);
                    break;
                case 1:            // Step north
                    /*t2stepsouth();*/
                    stepnorth(sendingagentid);
                    break;
                case 2:            // Step north east
                    stepnortheast(sendingagentid);
                    break;
                case 3:            // Step west
                    stepwest(sendingagentid);
                    break;
                case 4:            // Step east
                    /*t2stepwest();*/
                    stepeast(sendingagentid);
                    break;
                case 5:            // Step south west
                    stepsouthwest(sendingagentid);
                    break;
                case 6:            // Step south
                    stepsouth(sendingagentid);
                    break;
                default:           // Everything else - step south east
                    /*t2stepsouthwest();*/
                    stepsoutheast(sendingagentid);
                    break;
            }
        }

        t2_grazecoordinate[t2_x_coordinate][t2_y_coordinate] = 1;
        t2GrazeGPSLog.add(new GPSLog(t2_x_coordinate, t2_y_coordinate));
        int t2n = 0;
        for (int rbucklocationx = 0; rbucklocationx < MAX_X_GRID; rbucklocationx++) {
            for (int rbucklocationy = 0; rbucklocationy < MAX_Y_GRID; rbucklocationy++) {
                if (t2_grazecoordinate[rbucklocationx][rbucklocationy] == 1) {
                    t2n++;
                }
            }
        }

        sendingagentemt[sendingagentid] = t2_emt = calculateemt(t2n);
        mapgridintolinearmem(sendingagentid);
        Platform.runLater(() -> {
            t2Series.getData().add(new XYChart.Data(t2_x_coordinate - 0.5, t2_y_coordinate - 0.5));
            t2Series.getData().remove(0);
        });
    }
/*
 ** FHdr-beg ******************************************************************
 **
 ** Function name:  t3
 **
 ** Purpose: This function simulates the movement of a territorial blackbuck.
 **			 The relaxed pace of grazing is constant.However the direction is random.
 **
 ** FHdr-end ******************************************************************
 */

    @Override
    public void t3() {
        int sendingagentid = 3;
        epidemicsinglecopyrouting(sendingagentid);
        int t3buckdirector = ((rand.nextInt() >> rightshift) & 7);
        if ((t3_x_coordinate == t3_xmax_coordinate) && (t3_y_coordinate == t3_ymax_coordinate)) /* NE corner */ {
            switch (t3buckdirector) {
                case 3:            // Step west
                    stepwest(sendingagentid);
                    break;
                case 5:            // Step south west
                    stepsouthwest(sendingagentid);
                    break;
                case 6:            // Step south
                    stepsouth(sendingagentid);
                    break;
                default:           // Everything else - step southwest
                    stepsouthwest(sendingagentid);
                    break;
            }
        } else if ((t3_x_coordinate == t3_xmin_coordinate) && (t3_y_coordinate == t3_ymin_coordinate)) /* SW corner */ {
            switch (t3buckdirector) {
                case 1:            // Step north
                    stepnorth(sendingagentid);
                    break;
                case 2:            // Step north east
                    stepnortheast(sendingagentid);
                    break;
                case 4:            // Step east
                    stepeast(sendingagentid);
                    break;
                default:           // Everything else - step northest
                    stepnortheast(sendingagentid);
                    break;
            }
        } else if ((t3_x_coordinate == t3_xmin_coordinate) && (t3_y_coordinate == t3_ymax_coordinate)) /* NW corner */ {
            switch (t3buckdirector) {
                case 4:            // Step east
                    stepeast(sendingagentid);
                    break;
                case 6:            // Step south
                    stepsouth(sendingagentid);
                    break;
                case 7:            // Step south east
                    stepsoutheast(sendingagentid);
                    break;
                default:           // Everything else - step southeast
                    stepsoutheast(sendingagentid);
                    break;
            }
        } else if ((t3_x_coordinate == t3_xmax_coordinate) && (t3_y_coordinate == t3_ymin_coordinate)) /* SE corner */ {
            switch (t3buckdirector) {
                case 0:            // Step north west
                    stepnorthwest(sendingagentid);
                    break;
                case 1:            // Step north
                    stepnorth(sendingagentid);
                    break;
                case 3:            // Step west
                    stepwest(sendingagentid);
                    break;
                default:           // Everything else - step northwest
                    stepnorthwest(sendingagentid);
                    break;
            }
        } else if ((t3_x_coordinate == t3_xmin_coordinate) && (t3_y_coordinate < t3_ymax_coordinate) && (t3_y_coordinate > t3_ymin_coordinate)) /* Right of West*/ {
            switch (t3buckdirector) {
                case 1:            // Step north
                    stepnorth(sendingagentid);
                    break;
                case 2:            // Step north east
                    stepnortheast(sendingagentid);
                    break;
                case 4:            // Step east
                    stepeast(sendingagentid);
                    break;
                case 6:            // Step south
                    stepsouth(sendingagentid);
                    break;
                case 7:            // Step south east
                    stepsoutheast(sendingagentid);
                    break;
                default:           // Everything else - step east
                    stepeast(sendingagentid);
                    break;
            }
        } else if ((t3_x_coordinate == t3_xmax_coordinate) && (t3_y_coordinate < t3_ymax_coordinate) && (t3_y_coordinate > t3_ymin_coordinate)) /* Left of East*/ {
            switch (t3buckdirector) {
                case 0:            // Step north west
                    stepnorthwest(sendingagentid);
                    break;
                case 1:            // Step north
                    stepnorth(sendingagentid);
                    break;
                case 3:            // Step west
                    stepwest(sendingagentid);
                    break;
                case 5:            // Step south west
                    stepsouthwest(sendingagentid);
                    break;
                case 6:            // Step south
                    stepsouth(sendingagentid);
                    break;
                default:           // Everything else - step west
                    stepwest(sendingagentid);
                    break;
            }
        } else if ((t3_y_coordinate == t3_ymax_coordinate) && (t3_x_coordinate < t3_xmax_coordinate) && (t3_x_coordinate > t3_xmin_coordinate)) /* Below North*/ {
            switch (t3buckdirector) {
                case 3:            // Step west
                    stepwest(sendingagentid);
                    break;
                case 4:            // Step east
                    stepeast(sendingagentid);
                    break;
                case 5:            // Step south west
                    stepsouthwest(sendingagentid);
                    break;
                case 6:            // Step south
                    stepsouth(sendingagentid);
                    break;
                case 7:            // Step south east
                    stepsoutheast(sendingagentid);
                    break;
                default:           // Everything else - step south
                    stepsouth(sendingagentid);
                    break;
            }
        } else if ((t3_y_coordinate == t3_ymin_coordinate) && (t3_x_coordinate < t3_xmax_coordinate) && (t3_x_coordinate > t3_xmin_coordinate)) /* Above South*/ {
            switch (t3buckdirector) {
                case 0:            // Step north west
                    stepnorthwest(sendingagentid);
                    break;
                case 1:            // Step north
                    stepnorth(sendingagentid);
                    break;
                case 2:            // Step north east
                    stepnortheast(sendingagentid);
                    break;
                case 3:            // Step west
                    stepwest(sendingagentid);
                    break;
                case 4:            // Step east
                    stepeast(sendingagentid);
                    break;
                default:           // Everything else - step north
                    stepnorth(sendingagentid);
                    break;
            }
        } else {

            switch (t3buckdirector) {
                case 0:            // Step north west
                    stepnorthwest(sendingagentid);
                    break;
                case 1:            // Step north
                    /*t3stepsouth();*/
                    stepnorth(sendingagentid);
                    break;
                case 2:            // Step north east
                    stepnortheast(sendingagentid);
                    break;
                case 3:            // Step west
                    stepwest(sendingagentid);
                    break;
                case 4:            // Step east
                    /*t3stepwest();*/
                    stepeast(sendingagentid);
                    break;
                case 5:            // Step south west
                    stepsouthwest(sendingagentid);
                    break;
                case 6:            // Step south
                    stepsouth(sendingagentid);
                    break;
                default:           // Everything else - step south east
                    /*t3stepsouthwest();*/
                    stepsoutheast(sendingagentid);
                    break;
            }
        }

        t3_grazecoordinate[t3_x_coordinate][t3_y_coordinate] = 1;
        t3GrazeGPSLog.add(new GPSLog(t3_x_coordinate, t3_y_coordinate));
        int t3n = 0;
        for (int rbucklocationx = 0; rbucklocationx < MAX_X_GRID; rbucklocationx++) {
            for (int rbucklocationy = 0; rbucklocationy < MAX_Y_GRID; rbucklocationy++) {
                if (t3_grazecoordinate[rbucklocationx][rbucklocationy] == 1) {
                    t3n++;
                }
            }
        }

        sendingagentemt[sendingagentid] = t3_emt = calculateemt(t3n);
        mapgridintolinearmem(sendingagentid);
        Platform.runLater(() -> {
            t3Series.getData().add(new XYChart.Data(t3_x_coordinate - 0.5, t3_y_coordinate - 0.5));
            t3Series.getData().remove(0);
        });
    }
/*
 ** FHdr-beg ******************************************************************
 **
 ** Function name:  bh
 **
 ** Purpose: This function simulates the movement of a territorial blackbuck.
 **			 The relaxed pace of grazing is constant.However the direction is random.
 **
 ** FHdr-end ******************************************************************
 */

    @Override
    public void bh() {

        int sendingagentid = 4;
//
        if (bhroutinglock == routing.open) {
            epidemicsinglecopyrouting(sendingagentid);
        }
        bhroutinglock = routing.shut;

        int bhbuckdirector = ((rand.nextInt() >> rightshift) & 7);

        if ((bh_x_coordinate == bh_xmax_coordinate) && (bh_y_coordinate == bh_ymax_coordinate)) /* NE corner */ {
            switch (bhbuckdirector) {
                case 3:            // Step west
                    stepwest(sendingagentid);
                    break;
                case 5:            // Step south west
                    stepsouthwest(sendingagentid);
                    break;
                case 6:            // Step south
                    stepsouth(sendingagentid);
                    break;
                default:           // Everything else - step southwest
                    stepsouthwest(sendingagentid);
                    break;
            }
        } else if ((bh_x_coordinate == bh_xmin_coordinate) && (bh_y_coordinate == bh_ymin_coordinate)) /* SW corner */ {
            switch (bhbuckdirector) {
                case 1:            // Step north
                    stepnorth(sendingagentid);
                    break;
                case 2:            // Step north east
                    stepnortheast(sendingagentid);
                    break;
                case 4:            // Step east
                    stepeast(sendingagentid);
                    break;
                default:           // Everything else - step northest
                    stepnortheast(sendingagentid);
                    break;
            }
        } else if ((bh_x_coordinate == bh_xmin_coordinate) && (bh_y_coordinate == bh_ymax_coordinate)) /* NW corner */ {
            switch (bhbuckdirector) {
                case 4:            // Step east
                    stepeast(sendingagentid);
                    break;
                case 6:            // Step south
                    stepsouth(sendingagentid);
                    break;
                case 7:            // Step south east
                    stepsoutheast(sendingagentid);
                    break;
                default:           // Everything else - step southeast
                    stepsoutheast(sendingagentid);
                    break;
            }
        } else if ((bh_x_coordinate == bh_xmax_coordinate) && (bh_y_coordinate == bh_ymin_coordinate)) /* SE corner */ {
            switch (bhbuckdirector) {
                case 0:            // Step north west
                    stepnorthwest(sendingagentid);
                    break;
                case 1:            // Step north
                    stepnorth(sendingagentid);
                    break;
                case 3:            // Step west
                    stepwest(sendingagentid);
                    break;
                default:           // Everything else - step northwest
                    stepnorthwest(sendingagentid);
                    break;
            }
        } else if ((bh_x_coordinate == bh_xmin_coordinate) && (bh_y_coordinate < bh_ymax_coordinate) && (bh_y_coordinate > bh_ymin_coordinate)) /* Right of West*/ {
            switch (bhbuckdirector) {
                case 1:            // Step north
                    stepnorth(sendingagentid);
                    break;
                case 2:            // Step north east
                    stepnortheast(sendingagentid);
                    break;
                case 4:            // Step east
                    stepeast(sendingagentid);
                    break;
                case 6:            // Step south
                    stepsouth(sendingagentid);
                    break;
                case 7:            // Step south east
                    stepsoutheast(sendingagentid);
                    break;
                default:           // Everything else - step east
                    stepeast(sendingagentid);
                    break;
            }
        } else if ((bh_x_coordinate == bh_xmax_coordinate) && (bh_y_coordinate < bh_ymax_coordinate) && (bh_y_coordinate > bh_ymin_coordinate)) /* Left of East*/ {
            switch (bhbuckdirector) {
                case 0:            // Step north west
                    stepnorthwest(sendingagentid);
                    break;
                case 1:            // Step north
                    stepnorth(sendingagentid);
                    break;
                case 3:            // Step west
                    stepwest(sendingagentid);
                    break;
                case 5:            // Step south west
                    stepsouthwest(sendingagentid);
                    break;
                case 6:            // Step south
                    stepsouth(sendingagentid);
                    break;
                default:           // Everything else - step west
                    stepwest(sendingagentid);
                    break;
            }
        } else if ((bh_y_coordinate == bh_ymax_coordinate) && (bh_x_coordinate < bh_xmax_coordinate) && (bh_x_coordinate > bh_xmin_coordinate)) /* Below North*/ {
            switch (bhbuckdirector) {
                case 3:            // Step west
                    stepwest(sendingagentid);
                    break;
                case 4:            // Step east
                    stepeast(sendingagentid);
                    break;
                case 5:            // Step south west
                    stepsouthwest(sendingagentid);
                    break;
                case 6:            // Step south
                    stepsouth(sendingagentid);
                    break;
                case 7:            // Step south east
                    stepsoutheast(sendingagentid);
                    break;
                default:           // Everything else - step south
                    stepsouth(sendingagentid);
                    break;
            }
        } else if ((bh_y_coordinate == bh_ymin_coordinate) && (bh_x_coordinate < bh_xmax_coordinate) && (bh_x_coordinate > bh_xmin_coordinate)) /* Above South*/ {
            switch (bhbuckdirector) {
                case 0:            // Step north west
                    stepnorthwest(sendingagentid);
                    break;
                case 1:            // Step north
                    stepnorth(sendingagentid);
                    break;
                case 2:            // Step north east
                    stepnortheast(sendingagentid);
                    break;
                case 3:            // Step west
                    stepwest(sendingagentid);
                    break;
                case 4:            // Step east
                    stepeast(sendingagentid);
                    break;
                default:           // Everything else - step north
                    stepnorth(sendingagentid);
                    break;
            }
        } else {

            switch (bhbuckdirector) {
                case 0:            // Step north west
                    stepnorthwest(sendingagentid);
                    break;
                case 1:            // Step north
                    /*bhstepsouth();*/
                    stepnorth(sendingagentid);
                    break;
                case 2:            // Step north east
                    stepnortheast(sendingagentid);
                    break;
                case 3:            // Step west
                    stepwest(sendingagentid);
                    break;
                case 4:            // Step east
                    /*bhstepwest();*/
                    stepeast(sendingagentid);
                    break;
                case 5:            // Step south west
                    stepsouthwest(sendingagentid);
                    break;
                case 6:            // Step south
                    stepsouth(sendingagentid);
                    break;
                default:           // Everything else - step south east
                    /*bhstepsouthwest();*/
                    stepsoutheast(sendingagentid);
                    break;
            }
        }

        if ((bh_x_coordinate == w1_x_coordinate) && (bh_y_coordinate == w1_y_coordinate)) {
            bh();
        }

        bh_grazecoordinate[bh_x_coordinate][bh_y_coordinate] = 1;
        bhGrazeGPSLog.add(new GPSLog(bh_x_coordinate, bh_y_coordinate));
        int bhn = 0;
        for (int rbucklocationx = 0; rbucklocationx < MAX_X_GRID; rbucklocationx++) {
            for (int rbucklocationy = 0; rbucklocationy < MAX_Y_GRID; rbucklocationy++) {
                if (bh_grazecoordinate[rbucklocationx][rbucklocationy] == 1) {
                    bhn++;
                }
            }
        }

        sendingagentemt[sendingagentid] = bh_emt = calculateemt(bhn);
        mapgridintolinearmem(sendingagentid);
        Platform.runLater(() -> {
            bhSeries.getData().add(new XYChart.Data(bh_x_coordinate - 0.5, bh_y_coordinate - 0.5));
            bhSeries.getData().remove(0);
        });
    }

/*
 ** FHdr-beg ******************************************************************
 **
 ** Function name:  mh
 **
 ** Purpose: This function simulates the movement of a territorial blackbuck.
 **			 The relaxed pace of grazing is constant.However the direction is random.
 **
 ** FHdr-end ******************************************************************
 */

    @Override
    public void mh() {

        int sendingagentid = 5;
//
        if (mhroutinglock == routing.open) {
            epidemicsinglecopyrouting(sendingagentid);
        }
        mhroutinglock = routing.shut;
        int mhbuckdirector = ((rand.nextInt() >> rightshift) & 7);
        if ((mh_x_coordinate == mh_xmax_coordinate) && (mh_y_coordinate == mh_ymax_coordinate)) /* NE corner */ {
            switch (mhbuckdirector) {
                case 3:            // Step west
                    stepwest(sendingagentid);
                    break;
                case 5:            // Step south west
                    stepsouthwest(sendingagentid);
                    break;
                case 6:            // Step south
                    stepsouth(sendingagentid);
                    break;
                default:           // Everything else - step southwest
                    stepsouthwest(sendingagentid);
                    break;
            }
        } else if ((mh_x_coordinate == mh_xmin_coordinate) && (mh_y_coordinate == mh_ymin_coordinate)) /* SW corner */ {
            switch (mhbuckdirector) {
                case 1:            // Step north
                    stepnorth(sendingagentid);
                    break;
                case 2:            // Step north east
                    stepnortheast(sendingagentid);
                    break;
                case 4:            // Step east
                    stepeast(sendingagentid);
                    break;
                default:           // Everything else - step northest
                    stepnortheast(sendingagentid);
                    break;
            }
        } else if ((mh_x_coordinate == mh_xmin_coordinate) && (mh_y_coordinate == mh_ymax_coordinate)) /* NW corner */ {
            switch (mhbuckdirector) {
                case 4:            // Step east
                    stepeast(sendingagentid);
                    break;
                case 6:            // Step south
                    stepsouth(sendingagentid);
                    break;
                case 7:            // Step south east
                    stepsoutheast(sendingagentid);
                    break;
                default:           // Everything else - step southeast
                    stepsoutheast(sendingagentid);
                    break;
            }
        } else if ((mh_x_coordinate == mh_xmax_coordinate) && (mh_y_coordinate == mh_ymin_coordinate)) /* SE corner */ {
            switch (mhbuckdirector) {
                case 0:            // Step north west
                    stepnorthwest(sendingagentid);
                    break;
                case 1:            // Step north
                    stepnorth(sendingagentid);
                    break;
                case 3:            // Step west
                    stepwest(sendingagentid);
                    break;
                default:           // Everything else - step northwest
                    stepnorthwest(sendingagentid);
                    break;
            }
        } else if ((mh_x_coordinate == mh_xmin_coordinate) && (mh_y_coordinate < mh_ymax_coordinate) && (mh_y_coordinate > mh_ymin_coordinate)) /* Right of West*/ {
            switch (mhbuckdirector) {
                case 1:            // Step north
                    stepnorth(sendingagentid);
                    break;
                case 2:            // Step north east
                    stepnortheast(sendingagentid);
                    break;
                case 4:            // Step east
                    stepeast(sendingagentid);
                    break;
                case 6:            // Step south
                    stepsouth(sendingagentid);
                    break;
                case 7:            // Step south east
                    stepsoutheast(sendingagentid);
                    break;
                default:           // Everything else - step east
                    stepeast(sendingagentid);
                    break;
            }
        } else if ((mh_x_coordinate == mh_xmax_coordinate) && (mh_y_coordinate < mh_ymax_coordinate) && (mh_y_coordinate > mh_ymin_coordinate)) /* Left of East*/ {
            switch (mhbuckdirector) {
                case 0:            // Step north west
                    stepnorthwest(sendingagentid);
                    break;
                case 1:            // Step north
                    stepnorth(sendingagentid);
                    break;
                case 3:            // Step west
                    stepwest(sendingagentid);
                    break;
                case 5:            // Step south west
                    stepsouthwest(sendingagentid);
                    break;
                case 6:            // Step south
                    stepsouth(sendingagentid);
                    break;
                default:           // Everything else - step west
                    stepwest(sendingagentid);
                    break;
            }
        } else if ((mh_y_coordinate == mh_ymax_coordinate) && (mh_x_coordinate < mh_xmax_coordinate) && (mh_x_coordinate > mh_xmin_coordinate)) /* Below North*/ {
            switch (mhbuckdirector) {
                case 3:            // Step west
                    stepwest(sendingagentid);
                    break;
                case 4:            // Step east
                    stepeast(sendingagentid);
                    break;
                case 5:            // Step south west
                    stepsouthwest(sendingagentid);
                    break;
                case 6:            // Step south
                    stepsouth(sendingagentid);
                    break;
                case 7:            // Step south east
                    stepsoutheast(sendingagentid);
                    break;
                default:           // Everything else - step south
                    stepsouth(sendingagentid);
                    break;
            }
        } else if ((mh_y_coordinate == mh_ymin_coordinate) && (mh_x_coordinate < mh_xmax_coordinate) && (mh_x_coordinate > mh_xmin_coordinate)) /* Above South*/ {
            switch (mhbuckdirector) {
                case 0:            // Step north west
                    stepnorthwest(sendingagentid);
                    break;
                case 1:            // Step north
                    stepnorth(sendingagentid);
                    break;
                case 2:            // Step north east
                    stepnortheast(sendingagentid);
                    break;
                case 3:            // Step west
                    stepwest(sendingagentid);
                    break;
                case 4:            // Step east
                    stepeast(sendingagentid);
                    break;
                default:           // Everything else - step north
                    stepnorth(sendingagentid);
                    break;
            }
        } else {

            switch (mhbuckdirector) {
                case 0:            // Step north west
                    stepnorthwest(sendingagentid);
                    break;
                case 1:            // Step north
                    /*mhstepsouth();*/
                    stepnorth(sendingagentid);
                    break;
                case 2:            // Step north east
                    stepnortheast(sendingagentid);
                    break;
                case 3:            // Step west
                    stepwest(sendingagentid);
                    break;
                case 4:            // Step east
                    /*mhstepwest();*/
                    stepeast(sendingagentid);
                    break;
                case 5:            // Step south west
                    stepsouthwest(sendingagentid);
                    break;
                case 6:            // Step south
                    stepsouth(sendingagentid);
                    break;
                default:           // Everything else - step south east
                    /*mhstepsouthwest();*/
                    stepsoutheast(sendingagentid);
                    break;
            }
        }

        if ((mh_x_coordinate == w1_x_coordinate) && (mh_y_coordinate == w1_y_coordinate)) {
            mh();
        }

        mh_grazecoordinate[mh_x_coordinate][mh_y_coordinate] = 1;
        mhGrazeGPSLog.add(new GPSLog(mh_x_coordinate, mh_y_coordinate));
        int mhn = 0;
        for (int rbucklocationx = 0; rbucklocationx < MAX_X_GRID; rbucklocationx++) {
            for (int rbucklocationy = 0; rbucklocationy < MAX_Y_GRID; rbucklocationy++) {
                if (mh_grazecoordinate[rbucklocationx][rbucklocationy] == 1) {
                    mhn++;
                }
            }
        }

        sendingagentemt[sendingagentid] = mh_emt = calculateemt(mhn);

        mapgridintolinearmem(sendingagentid);
        Platform.runLater(() -> {
            mhSeries.getData().add(new XYChart.Data(mh_x_coordinate - 0.5, mh_y_coordinate - 0.5));
            mhSeries.getData().remove(0);
        });
    }

/*
 ** FHdr-beg ******************************************************************
 **
 ** Function name:  fh
 **
 ** Purpose: This function simulates the movement of a territorial blackbuck.
 **			 The relaxed pace of grazing is constant.However the direction is random.
 **
 ** FHdr-end ******************************************************************
 */

    @Override
    public void fh() {

        int sendingagentid = 6;
//
        if (fhroutinglock == routing.open) {
            epidemicsinglecopyrouting(sendingagentid);
        }
        fhroutinglock = routing.shut;
        int fhbuckdirector = ((rand.nextInt() >> rightshift) & 7);
        if ((fh_x_coordinate == fh_xmax_coordinate) && (fh_y_coordinate == fh_ymax_coordinate)) /* NE corner */ {
            switch (fhbuckdirector) {
                case 3:            // Step west
                    stepwest(sendingagentid);
                    break;
                case 5:            // Step south west
                    stepsouthwest(sendingagentid);
                    break;
                case 6:            // Step south
                    stepsouth(sendingagentid);
                    break;
                default:           // Everything else - step southwest
                    stepsouthwest(sendingagentid);
                    break;
            }
        } else if ((fh_x_coordinate == fh_xmin_coordinate) && (fh_y_coordinate == fh_ymin_coordinate)) /* SW corner */ {
            switch (fhbuckdirector) {
                case 1:            // Step north
                    stepnorth(sendingagentid);
                    break;
                case 2:            // Step north east
                    stepnortheast(sendingagentid);
                    break;
                case 4:            // Step east
                    stepeast(sendingagentid);
                    break;
                default:           // Everything else - step northest
                    stepnortheast(sendingagentid);
                    break;
            }
        } else if ((fh_x_coordinate == fh_xmin_coordinate) && (fh_y_coordinate == fh_ymax_coordinate)) /* NW corner */ {
            switch (fhbuckdirector) {
                case 4:            // Step east
                    stepeast(sendingagentid);
                    break;
                case 6:            // Step south
                    stepsouth(sendingagentid);
                    break;
                case 7:            // Step south east
                    stepsoutheast(sendingagentid);
                    break;
                default:           // Everything else - step southeast
                    stepsoutheast(sendingagentid);
                    break;
            }
        } else if ((fh_x_coordinate == fh_xmax_coordinate) && (fh_y_coordinate == fh_ymin_coordinate)) /* SE corner */ {
            switch (fhbuckdirector) {
                case 0:            // Step north west
                    stepnorthwest(sendingagentid);
                    break;
                case 1:            // Step north
                    stepnorth(sendingagentid);
                    break;
                case 3:            // Step west
                    stepwest(sendingagentid);
                    break;
                default:           // Everything else - step northwest
                    stepnorthwest(sendingagentid);
                    break;
            }
        } else if ((fh_x_coordinate == fh_xmin_coordinate) && (fh_y_coordinate < fh_ymax_coordinate) && (fh_y_coordinate > fh_ymin_coordinate)) /* Right of West*/ {
            switch (fhbuckdirector) {
                case 1:            // Step north
                    stepnorth(sendingagentid);
                    break;
                case 2:            // Step north east
                    stepnortheast(sendingagentid);
                    break;
                case 4:            // Step east
                    stepeast(sendingagentid);
                    break;
                case 6:            // Step south
                    stepsouth(sendingagentid);
                    break;
                case 7:            // Step south east
                    stepsoutheast(sendingagentid);
                    break;
                default:           // Everything else - step east
                    stepeast(sendingagentid);
                    break;
            }
        } else if ((fh_x_coordinate == fh_xmax_coordinate) && (fh_y_coordinate < fh_ymax_coordinate) && (fh_y_coordinate > fh_ymin_coordinate)) /* Left of East*/ {
            switch (fhbuckdirector) {
                case 0:            // Step north west
                    stepnorthwest(sendingagentid);
                    break;
                case 1:            // Step north
                    stepnorth(sendingagentid);
                    break;
                case 3:            // Step west
                    stepwest(sendingagentid);
                    break;
                case 5:            // Step south west
                    stepsouthwest(sendingagentid);
                    break;
                case 6:            // Step south
                    stepsouth(sendingagentid);
                    break;
                default:           // Everything else - step west
                    stepwest(sendingagentid);
                    break;
            }
        } else if ((fh_y_coordinate == fh_ymax_coordinate) && (fh_x_coordinate < fh_xmax_coordinate) && (fh_x_coordinate > fh_xmin_coordinate)) /* Below North*/ {
            switch (fhbuckdirector) {
                case 3:            // Step west
                    stepwest(sendingagentid);
                    break;
                case 4:            // Step east
                    stepeast(sendingagentid);
                    break;
                case 5:            // Step south west
                    stepsouthwest(sendingagentid);
                    break;
                case 6:            // Step south
                    stepsouth(sendingagentid);
                    break;
                case 7:            // Step south east
                    stepsoutheast(sendingagentid);
                    break;
                default:           // Everything else - step south
                    stepsouth(sendingagentid);
                    break;
            }
        } else if ((fh_y_coordinate == fh_ymin_coordinate) && (fh_x_coordinate < fh_xmax_coordinate) && (fh_x_coordinate > fh_xmin_coordinate)) /* Above South*/ {
            switch (fhbuckdirector) {
                case 0:            // Step north west
                    stepnorthwest(sendingagentid);
                    break;
                case 1:            // Step north
                    stepnorth(sendingagentid);
                    break;
                case 2:            // Step north east
                    stepnortheast(sendingagentid);
                    break;
                case 3:            // Step west
                    stepwest(sendingagentid);
                    break;
                case 4:            // Step east
                    stepeast(sendingagentid);
                    break;
                default:           // Everything else - step north
                    stepnorth(sendingagentid);
                    break;
            }
        } else {

            switch (fhbuckdirector) {
                case 0:            // Step north west
                    stepnorthwest(sendingagentid);
                    break;
                case 1:            // Step north
                    /*fhstepsouth();*/
                    stepnorth(sendingagentid);
                    break;
                case 2:            // Step north east
                    stepnortheast(sendingagentid);
                    break;
                case 3:            // Step west
                    stepwest(sendingagentid);
                    break;
                case 4:            // Step east
                    /*fhstepwest();*/
                    stepeast(sendingagentid);
                    break;
                case 5:            // Step south west
                    stepsouthwest(sendingagentid);
                    break;
                case 6:            // Step south
                    stepsouth(sendingagentid);
                    break;
                default:           // Everything else - step south east
                    /*fhstepsouthwest();*/
                    stepsoutheast(sendingagentid);
                    break;
            }
        }

        if (((fh_x_coordinate == w1_x_coordinate) && (fh_y_coordinate == w1_y_coordinate)) || (fh_x_coordinate == bh_x_coordinate) && (fh_y_coordinate == bh_y_coordinate)) {
            fh();
        }

        fh_grazecoordinate[fh_x_coordinate][fh_y_coordinate] = 1;
        fhGrazeGPSLog.add(new GPSLog(fh_x_coordinate, fh_y_coordinate));
        int fhn = 0;
        for (int rbucklocationx = 0; rbucklocationx < MAX_X_GRID; rbucklocationx++) {
            for (int rbucklocationy = 0; rbucklocationy < MAX_Y_GRID; rbucklocationy++) {
                if (fh_grazecoordinate[rbucklocationx][rbucklocationy] == 1) {
                    fhn++;
                }
            }
        }

        sendingagentemt[sendingagentid] = fh_emt = calculateemt(fhn);

        mapgridintolinearmem(sendingagentid);
        Platform.runLater(() -> {
            fhSeries.getData().add(new XYChart.Data(fh_x_coordinate - 0.5, fh_y_coordinate - 0.5));
            fhSeries.getData().remove(0);
        });
    }

/*
 ** FHdr-beg ******************************************************************
 **
 ** Function name:  nf
 **
 ** Purpose: This function simulates the movement of a territorial blackbuck.
 **			 The relaxed pace of grazing is constant.However the direction is random.
 **
 ** FHdr-end ******************************************************************
 */

    @Override
    public void nf() {

        int sendingagentid = 7;
//
        if (nfroutinglock == routing.open) {
            epidemicsinglecopyrouting(sendingagentid);
        }
        nfroutinglock = routing.shut;

        int nfbuckdirector = ((rand.nextInt() >> rightshift) & 7);
        if ((nf_x_coordinate == nf_xmax_coordinate) && (nf_y_coordinate == nf_ymax_coordinate)) /* NE corner */ {
            switch (nfbuckdirector) {
                case 3:            // Step west
                    stepwest(sendingagentid);
                    break;
                case 5:            // Step south west
                    stepsouthwest(sendingagentid);
                    break;
                case 6:            // Step south
                    stepsouth(sendingagentid);
                    break;
                default:           // Everything else - step southwest
                    stepsouthwest(sendingagentid);
                    break;
            }
        } else if ((nf_x_coordinate == nf_xmin_coordinate) && (nf_y_coordinate == nf_ymin_coordinate)) /* SW corner */ {
            switch (nfbuckdirector) {
                case 1:            // Step north
                    stepnorth(sendingagentid);
                    break;
                case 2:            // Step north east
                    stepnortheast(sendingagentid);
                    break;
                case 4:            // Step east
                    stepeast(sendingagentid);
                    break;
                default:           // Everything else - step northest
                    stepnortheast(sendingagentid);
                    break;
            }
        } else if ((nf_x_coordinate == nf_xmin_coordinate) && (nf_y_coordinate == nf_ymax_coordinate)) /* NW corner */ {
            switch (nfbuckdirector) {
                case 4:            // Step east
                    stepeast(sendingagentid);
                    break;
                case 6:            // Step south
                    stepsouth(sendingagentid);
                    break;
                case 7:            // Step south east
                    stepsoutheast(sendingagentid);
                    break;
                default:           // Everything else - step southeast
                    stepsoutheast(sendingagentid);
                    break;
            }
        } else if ((nf_x_coordinate == nf_xmax_coordinate) && (nf_y_coordinate == nf_ymin_coordinate)) /* SE corner */ {
            switch (nfbuckdirector) {
                case 0:            // Step north west
                    stepnorthwest(sendingagentid);
                    break;
                case 1:            // Step north
                    stepnorth(sendingagentid);
                    break;
                case 3:            // Step west
                    stepwest(sendingagentid);
                    break;
                default:           // Everything else - step northwest
                    stepnorthwest(sendingagentid);
                    break;
            }
        } else if ((nf_x_coordinate == nf_xmin_coordinate) && (nf_y_coordinate < nf_ymax_coordinate) && (nf_y_coordinate > nf_ymin_coordinate)) /* Right of West*/ {
            switch (nfbuckdirector) {
                case 1:            // Step north
                    stepnorth(sendingagentid);
                    break;
                case 2:            // Step north east
                    stepnortheast(sendingagentid);
                    break;
                case 4:            // Step east
                    stepeast(sendingagentid);
                    break;
                case 6:            // Step south
                    stepsouth(sendingagentid);
                    break;
                case 7:            // Step south east
                    stepsoutheast(sendingagentid);
                    break;
                default:           // Everything else - step east
                    stepeast(sendingagentid);
                    break;
            }
        } else if ((nf_x_coordinate == nf_xmax_coordinate) && (nf_y_coordinate < nf_ymax_coordinate) && (nf_y_coordinate > nf_ymin_coordinate)) /* Left of East*/ {
            switch (nfbuckdirector) {
                case 0:            // Step north west
                    stepnorthwest(sendingagentid);
                    break;
                case 1:            // Step north
                    stepnorth(sendingagentid);
                    break;
                case 3:            // Step west
                    stepwest(sendingagentid);
                    break;
                case 5:            // Step south west
                    stepsouthwest(sendingagentid);
                    break;
                case 6:            // Step south
                    stepsouth(sendingagentid);
                    break;
                default:           // Everything else - step west
                    stepwest(sendingagentid);
                    break;
            }
        } else if ((nf_y_coordinate == nf_ymax_coordinate) && (nf_x_coordinate < nf_xmax_coordinate) && (nf_x_coordinate > nf_xmin_coordinate)) /* Below North*/ {
            switch (nfbuckdirector) {
                case 3:            // Step west
                    stepwest(sendingagentid);
                    break;
                case 4:            // Step east
                    stepeast(sendingagentid);
                    break;
                case 5:            // Step south west
                    stepsouthwest(sendingagentid);
                    break;
                case 6:            // Step south
                    stepsouth(sendingagentid);
                    break;
                case 7:            // Step south east
                    stepsoutheast(sendingagentid);
                    break;
                default:           // Everything else - step south
                    stepsouth(sendingagentid);
                    break;
            }
        } else if ((nf_y_coordinate == nf_ymin_coordinate) && (nf_x_coordinate < nf_xmax_coordinate) && (nf_x_coordinate > nf_xmin_coordinate)) /* Above South*/ {
            switch (nfbuckdirector) {
                case 0:            // Step north west
                    stepnorthwest(sendingagentid);
                    break;
                case 1:            // Step north
                    stepnorth(sendingagentid);
                    break;
                case 2:            // Step north east
                    stepnortheast(sendingagentid);
                    break;
                case 3:            // Step west
                    stepwest(sendingagentid);
                    break;
                case 4:            // Step east
                    stepeast(sendingagentid);
                    break;
                default:           // Everything else - step north
                    stepnorth(sendingagentid);
                    break;
            }
        } else {

            switch (nfbuckdirector) {
                case 0:            // Step north west
                    stepnorthwest(sendingagentid);
                    break;
                case 1:            // Step north
                    /*nfstepsouth();*/
                    stepnorth(sendingagentid);
                    break;
                case 2:            // Step north east
                    stepnortheast(sendingagentid);
                    break;
                case 3:            // Step west
                    stepwest(sendingagentid);
                    break;
                case 4:            // Step east
                    /*nfstepwest();*/
                    stepeast(sendingagentid);
                    break;
                case 5:            // Step south west
                    stepsouthwest(sendingagentid);
                    break;
                case 6:            // Step south
                    stepsouth(sendingagentid);
                    break;
                default:           // Everything else - step south east
                    /*nfstepsouthwest();*/
                    stepsoutheast(sendingagentid);
                    break;
            }
        }

        if (((nf_x_coordinate == w1_x_coordinate) && (nf_y_coordinate == w1_y_coordinate)) || ((nf_x_coordinate == bh_x_coordinate) && (nf_y_coordinate == bh_y_coordinate))
                || ((nf_x_coordinate == t1_x_coordinate) && (nf_y_coordinate == t1_y_coordinate)) || ((nf_x_coordinate == t2_x_coordinate) && (nf_y_coordinate == t2_y_coordinate)) || ((nf_x_coordinate == t3_x_coordinate) && (nf_y_coordinate == t3_y_coordinate))) {
            nf();
        }

        nf_grazecoordinate[nf_x_coordinate][nf_y_coordinate] = 1;
        nfGrazeGPSLog.add(new GPSLog(nf_x_coordinate, nf_y_coordinate));
        int nfn = 0;
        for (int rbucklocationx = 0; rbucklocationx < MAX_X_GRID; rbucklocationx++) {
            for (int rbucklocationy = 0; rbucklocationy < MAX_Y_GRID; rbucklocationy++) {
                if (nf_grazecoordinate[rbucklocationx][rbucklocationy] == 1) {
                    nfn++;
                }
            }
        }

        sendingagentemt[sendingagentid] = nf_emt = calculateemt(nfn);

        mapgridintolinearmem(sendingagentid);
        Platform.runLater(() -> {
            nfSeries.getData().add(new XYChart.Data(nf_x_coordinate - 0.5, nf_y_coordinate - 0.5));
            nfSeries.getData().remove(0);
        });
    }

/*
 ** FHdr-beg ******************************************************************
 **
 ** Function name:  epidemicsinglecopyrouting
 **
 ** Purpose: This function simulates the buck moving north west based on the
 **			 random decision by t1buckdirector, after ensuring that the new step
 **			 indeed leads to a fresh grazing spot.
 ** FHdr-end ******************************************************************
 */

void epidemicsinglecopyrouting(int sendingagentid)
	{

	float recvingagentemt;
/* Identify the agents in radio contact or same grid and update in array*/
	int[] agentsincontact={0,0,0,0,0,0,0};
	////System.out.printf("sendingagentid %d	\n",sendingagentid);
//		Sleep(1000);
	switch(sendingagentid)
	{
	case 1:
		{

			if((t1_x_coordinate == t2_x_coordinate)&&(t1_y_coordinate == t2_y_coordinate))
			{
				agentsincontact[0]=2;
			}
			if((t1_x_coordinate == t3_x_coordinate)&&(t1_y_coordinate == t3_y_coordinate))
			{
				agentsincontact[1]=3;
			}
			if((t1_x_coordinate == bh_x_coordinate)&&(t1_y_coordinate == bh_y_coordinate))
			{
				agentsincontact[2]=4;
			}
			if((t1_x_coordinate == mh_x_coordinate)&&(t1_y_coordinate == mh_y_coordinate))
			{
				agentsincontact[3]=5;
			}
			if((t1_x_coordinate == fh_x_coordinate)&&(t1_y_coordinate == fh_y_coordinate))
			{
				agentsincontact[4]=6;
			}
			if((t1_x_coordinate == nf_x_coordinate)&&(t1_y_coordinate == nf_y_coordinate))
			{
				agentsincontact[5]=7;
			}
			if((t1_x_coordinate == w1_x_coordinate)&&(t1_y_coordinate == w1_y_coordinate))
			{
				agentsincontact[6]=8;
//				t1_discoveryrate++;	/* Discovery rate is incremented	*/
//				////System.out.printf("t1 Discovery rate is incremented\n");
//				(1 - epsilon)*(lebesguemeasure/t1_discoveryrate);	/* hide effort is recomputed	*/
			}
		}
		break;
	case 2:
		{
			if((t2_x_coordinate == t1_x_coordinate)&&(t2_y_coordinate == t1_y_coordinate))
			{
				agentsincontact[0]=1;
			}
			if((t2_x_coordinate == t3_x_coordinate)&&(t2_y_coordinate == t3_y_coordinate))
			{
				agentsincontact[1]=3;
			}
			if((t2_x_coordinate == bh_x_coordinate)&&(t2_y_coordinate == bh_y_coordinate))
			{
				agentsincontact[2]=4;
			}
			if((t2_x_coordinate == mh_x_coordinate)&&(t2_y_coordinate == mh_y_coordinate))
			{
				agentsincontact[3]=5;
			}
			if((t2_x_coordinate == fh_x_coordinate)&&(t2_y_coordinate == fh_y_coordinate))
			{
				agentsincontact[4]=6;
			}
			if((t2_x_coordinate == nf_x_coordinate)&&(t2_y_coordinate == nf_y_coordinate))
			{
				agentsincontact[5]=7;
			}
			if((t2_x_coordinate == w1_x_coordinate)&&(t2_y_coordinate == w1_y_coordinate))
			{
				agentsincontact[6]=8;
//				t2_discoveryrate++;	/* Discovery rate is incremented	*/
//				////System.out.printf("t2 Discovery rate is incremented\n");
//				t2_emt = (1 - epsilon)*(lebesguemeasure/t2_discoveryrate);/* hide effort is recomputed	*/
			}
		}
		break;
	case 3:
		{
			if((t3_x_coordinate == t1_x_coordinate)&&(t3_y_coordinate == t1_y_coordinate))
			{
				agentsincontact[0]=1;
			}
			if((t3_x_coordinate == t2_x_coordinate)&&(t3_y_coordinate == t2_y_coordinate))
			{
				agentsincontact[1]=2;
			}
			if((t3_x_coordinate == bh_x_coordinate)&&(t3_y_coordinate == bh_y_coordinate))
			{
				agentsincontact[2]=4;
			}
			if((t3_x_coordinate == mh_x_coordinate)&&(t3_y_coordinate == mh_y_coordinate))
			{
				agentsincontact[3]=5;
			}
			if((t3_x_coordinate == fh_x_coordinate)&&(t3_y_coordinate == fh_y_coordinate))
			{
				agentsincontact[4]=6;
			}
			if((t3_x_coordinate == nf_x_coordinate)&&(t3_y_coordinate == nf_y_coordinate))
			{
				agentsincontact[5]=7;
			}
			if((t3_x_coordinate == w1_x_coordinate)&&(t3_y_coordinate == w1_y_coordinate))
			{
				agentsincontact[6]=8;
//				t3_discoveryrate++;	/* Discovery rate is incremented	*/
//				////System.out.printf("t3 Discovery rate is incremented\n");
//				t3_emt = (1 - epsilon)*(lebesguemeasure/t3_discoveryrate);	/* hide effort is recomputed	*/
			}
		}
		break;
	case 4:
		{
			if((bh_x_coordinate == t1_x_coordinate)&&(bh_y_coordinate == t1_y_coordinate))
			{
				agentsincontact[0]=1;
			}
			if((bh_x_coordinate == t2_x_coordinate)&&(bh_y_coordinate == t2_y_coordinate))
			{
				agentsincontact[1]=2;
			}
			if((bh_x_coordinate == t3_x_coordinate)&&(bh_y_coordinate == t3_y_coordinate))
			{
				agentsincontact[2]=3;
			}
			if((bh_x_coordinate == mh_x_coordinate)&&(bh_y_coordinate == mh_y_coordinate))
			{
				agentsincontact[3]=5;
			}
			if((bh_x_coordinate == fh_x_coordinate)&&(bh_y_coordinate == fh_y_coordinate))
			{
				agentsincontact[4]=6;
			}
			if((bh_x_coordinate == nf_x_coordinate)&&(bh_y_coordinate == nf_y_coordinate))
			{
				agentsincontact[5]=7;
			}
			if((bh_x_coordinate == w1_x_coordinate)&&(bh_y_coordinate == w1_y_coordinate))
			{
				agentsincontact[6]=8;
//				bh_discoveryrate++;	/* Discovery rate is incremented	*/
//				////System.out.printf("bh Discovery rate is incremented\n");
//				bh_emt = (1 - epsilon)*(lebesguemeasure/bh_discoveryrate);	/* hide effort is recomputed	*/
			}
		}
		break;
	case 5:
		{
			if((mh_x_coordinate == t1_x_coordinate)&&(mh_y_coordinate == t1_y_coordinate))
			{
				agentsincontact[0]=1;
			}
			if((mh_x_coordinate == t2_x_coordinate)&&(mh_y_coordinate == t2_y_coordinate))
			{
				agentsincontact[1]=2;
			}
			if((mh_x_coordinate == t3_x_coordinate)&&(mh_y_coordinate == t3_y_coordinate))
			{
				agentsincontact[2]=3;
			}
			if((mh_x_coordinate == bh_x_coordinate)&&(mh_y_coordinate == bh_y_coordinate))
			{
				agentsincontact[3]=4;
			}
			if((mh_x_coordinate == fh_x_coordinate)&&(mh_y_coordinate == fh_y_coordinate))
			{
				agentsincontact[4]=6;
			}
			if((mh_x_coordinate == nf_x_coordinate)&&(mh_y_coordinate == nf_y_coordinate))
			{
				agentsincontact[5]=7;
			}
			if((mh_x_coordinate == w1_x_coordinate)&&(mh_y_coordinate == w1_y_coordinate))
			{
				agentsincontact[6]=8;
//				mh_discoveryrate++;	/* Discovery rate is incremented	*/
//				////System.out.printf("mh Discovery rate is incremented\n");
//				mh_emt = (1 - epsilon)*(lebesguemeasure/mh_discoveryrate);	/* hide effort is recomputed	*/
			}
		}
		break;
	case 6:
		{
			if((fh_x_coordinate == t1_x_coordinate)&&(fh_y_coordinate == t1_y_coordinate))
			{
				agentsincontact[0]=1;
				////System.out.printf("ONE \n");
			}
			if((fh_x_coordinate == t2_x_coordinate)&&(fh_y_coordinate == t2_y_coordinate))
			{
				agentsincontact[1]=2;
				////System.out.printf("TWO \n");
			}
			if((fh_x_coordinate == t3_x_coordinate)&&(fh_y_coordinate == t3_y_coordinate))
			{
				agentsincontact[2]=3;
				////System.out.printf("THREE \n");
			}
			if((fh_x_coordinate == bh_x_coordinate)&&(fh_y_coordinate == bh_y_coordinate))
			{
				agentsincontact[3]=4;
				////System.out.printf("FOUR \n");
			}
			if((fh_x_coordinate == mh_x_coordinate)&&(fh_y_coordinate == mh_y_coordinate))
			{
				agentsincontact[4]=5;
				////System.out.printf("FIVE \n");
			}
			if((fh_x_coordinate == nf_x_coordinate)&&(fh_y_coordinate == nf_y_coordinate))
			{
				agentsincontact[5]=7;
				////System.out.printf("SIX \n");
			}
			if((fh_x_coordinate == w1_x_coordinate)&&(fh_y_coordinate == w1_y_coordinate))
			{
				agentsincontact[6]=8;
//				fh_discoveryrate++;	/* Discovery rate is incremented	*/
//				////System.out.printf("fh Discovery rate is incremented\n");
//				fh_emt = (1 - epsilon)*(lebesguemeasure/fh_discoveryrate);	/* hide effort is recomputed	*/
			}
		}
		break;
	case 7:
		{
			if((nf_x_coordinate == t1_x_coordinate)&&(nf_y_coordinate == t1_y_coordinate))
			{
				agentsincontact[0]=1;
			}
			if((nf_x_coordinate == t2_x_coordinate)&&(nf_y_coordinate == t2_y_coordinate))
			{
				agentsincontact[1]=2;
			}
			if((nf_x_coordinate == t3_x_coordinate)&&(nf_y_coordinate == t3_y_coordinate))
			{
				agentsincontact[2]=3;
			}
			if((nf_x_coordinate == bh_x_coordinate)&&(nf_y_coordinate == bh_y_coordinate))
			{
				agentsincontact[3]=4;
			}
			if((nf_x_coordinate == mh_x_coordinate)&&(nf_y_coordinate == mh_y_coordinate))
			{
				agentsincontact[4]=5;
			}
			if((nf_x_coordinate == fh_x_coordinate)&&(nf_y_coordinate == fh_y_coordinate))
			{
				agentsincontact[5]=6;
			}
			if((nf_x_coordinate == w1_x_coordinate)&&(nf_y_coordinate == w1_y_coordinate))
			{
				agentsincontact[6]=8;
//				nf_discoveryrate++;	/* Discovery rate is incremented	*/
//				////System.out.printf("nf Discovery rate is incremented\n");
//				nf_emt = (1 - epsilon)*(lebesguemeasure/nf_discoveryrate);	/* hide effort is recomputed	*/
			}
		}
		break;
	default:
		{
		}
		break;
	}
		/*		if(sendingagentid==8)
		{
			if((w_x_coordinate == d1_x_coordinate)&&(w_y_coordinate == d1_y_coordinate))
			{
				agentsincontact[0]=9;
			}
		}*/

		float[] agentsemt={700,700,700,700,700,700,700,700};

		for(int loop1=0;loop1<7;loop1++)
		{
			if (agentsincontact[loop1]!=0)
			{
				switch (agentsincontact[loop1])
				{
			  case 1:            // Step west
				agentsemt[0]=t1_emt;
				break;
			  case 2:            // Step south west
				agentsemt[1]=t2_emt;
				break;
			  case 3:            // Step south
				agentsemt[2]=t3_emt;
				break;
			  case 4:            // Step west
				agentsemt[3]=bh_emt;
				break;
			  case 5:            // Step south west
				agentsemt[4]=mh_emt;
				break;
			  case 6:            // Step south
				agentsemt[5]=fh_emt;
				break;
			  case 7:            // Step south
				agentsemt[6]=nf_emt;
				break;
			  case 8:            // Step south
				agentsemt[7]=w1_emt;
				break;
/*			  case 9:            // Step south
				agentsemt[8]=w_emt;
				break;*/

				}
			}
		}
////System.out.printf("Agents in contact %d	%d	%d	%d	%d	%d	%d	\n",agentsincontact[0],agentsincontact[1],agentsincontact[2],agentsincontact[3],agentsincontact[4],agentsincontact[5],agentsincontact[6]);

		int x,y,n; n=8;
		for(x=0;x<n;x++)
			{
			 for(y=0;y<n-1;y++)
				{
					if(agentsemt[y] > agentsemt[y+1])
					{
						float temp = agentsemt[y+1];
						agentsemt[y+1] = agentsemt[y];
						agentsemt[y] = temp;
					}
				 }
			}
		////System.out.printf("Expected meeting time %f	%f	%f	%f	%f	%f	%f	%f	\n",agentsemt[0],agentsemt[1],agentsemt[2],agentsemt[3],agentsemt[4],agentsemt[5],agentsemt[6],agentsemt[7]);
//		return (agentsemt[0]);

		recvingagentemt=agentsemt[0];

		if(recvingagentemt==700)
		{
			////System.out.printf("no contact\n");
		}
		else
		{
			////System.out.printf("CONTACT recvingagentemt %f \n",recvingagentemt);//Sleep(1000);
		}
		if((recvingagentemt==t1_emt)&&((t1_agentid==agentsincontact[0])||(t1_agentid==agentsincontact[1])||(t1_agentid==agentsincontact[2])||(t1_agentid==agentsincontact[3])||(t1_agentid==agentsincontact[4])||(t1_agentid==agentsincontact[5])||(t1_agentid==agentsincontact[6]))&&disruption())
		{
			push(sendingagentid,t1_agentid,recvingagentemt);
		}
		if((recvingagentemt==t2_emt)&&((t2_agentid==agentsincontact[0])||(t2_agentid==agentsincontact[1])||(t2_agentid==agentsincontact[2])||(t2_agentid==agentsincontact[3])||(t2_agentid==agentsincontact[4])||(t2_agentid==agentsincontact[5])||(t2_agentid==agentsincontact[6]))&&disruption())
		{
			push(sendingagentid,t2_agentid,recvingagentemt);
		}
		if((recvingagentemt==t3_emt)&&((t3_agentid==agentsincontact[0])||(t3_agentid==agentsincontact[1])||(t3_agentid==agentsincontact[2])||(t3_agentid==agentsincontact[3])||(t3_agentid==agentsincontact[4])||(t3_agentid==agentsincontact[5])||(t3_agentid==agentsincontact[6]))&&disruption())
		{
			push(sendingagentid,t3_agentid,recvingagentemt);
		}
		if((recvingagentemt==bh_emt)&&((bh_agentid==agentsincontact[0])||(bh_agentid==agentsincontact[1])||(bh_agentid==agentsincontact[2])||(bh_agentid==agentsincontact[3])||(bh_agentid==agentsincontact[4])||(bh_agentid==agentsincontact[5])||(bh_agentid==agentsincontact[6]))&&disruption())
		{
			push(sendingagentid,bh_agentid,recvingagentemt);
		}
		if((recvingagentemt==mh_emt)&&((mh_agentid==agentsincontact[0])||(mh_agentid==agentsincontact[1])||(mh_agentid==agentsincontact[2])||(mh_agentid==agentsincontact[3])||(mh_agentid==agentsincontact[4])||(mh_agentid==agentsincontact[5])||(mh_agentid==agentsincontact[6]))&&disruption())
		{
			push(sendingagentid,mh_agentid,recvingagentemt);
		}
		if((recvingagentemt==fh_emt)&&((fh_agentid==agentsincontact[0])||(fh_agentid==agentsincontact[1])||(fh_agentid==agentsincontact[2])||(fh_agentid==agentsincontact[3])||(fh_agentid==agentsincontact[4])||(fh_agentid==agentsincontact[5])||(fh_agentid==agentsincontact[6]))&&disruption())
		{
			push(sendingagentid,fh_agentid,recvingagentemt);
		}
		if((recvingagentemt==nf_emt)&&((nf_agentid==agentsincontact[0])||(nf_agentid==agentsincontact[1])||(nf_agentid==agentsincontact[2])||(nf_agentid==agentsincontact[3])||(nf_agentid==agentsincontact[4])||(nf_agentid==agentsincontact[5])||(nf_agentid==agentsincontact[6]))&&disruption())
		{
			push(sendingagentid,nf_agentid,recvingagentemt);
		}
		if((recvingagentemt==w1_emt)&&((w1_agentid==agentsincontact[0])||(w1_agentid==agentsincontact[1])||(w1_agentid==agentsincontact[2])||(w1_agentid==agentsincontact[3])||(w1_agentid==agentsincontact[4])||(w1_agentid==agentsincontact[5])||(w1_agentid==agentsincontact[6]))&&disruption())
		{
			push(sendingagentid,w1_agentid,recvingagentemt);
		}
	}

/*
 ** FHdr-beg ******************************************************************
 **
 ** Function name:  push
 **
 ** Purpose: This function simulates the buck moving north west based on the
 **			 random decision by t1buckdirector, after ensuring that the new step
 **			 indeed leads to a fresh grazing spot_
 ** FHdr-end ******************************************************************
 */

    public void push(int sagentid, int ragentid, float rhideeffort) {

        if (ragentid != 9) {

            int[] sendmsg = new int[2000];
            int[] recvmsg = new int[2000];
            int key = 0;

            if (sendingagentemt[sagentid] >= rhideeffort)
            {
                sendingagentemt[sagentid] = rhideeffort;
                key = 1;
            }
            if (key == 1) {
                switch (sagentid) {
                    case 1:
                        sendmsg = Arrays.copyOf(t1_message, sendmsg.length);
                        break;
                    case 2:
                        sendmsg = Arrays.copyOf(t2_message, sendmsg.length);
                        break;
                    case 3:
                        sendmsg = Arrays.copyOf(t3_message, sendmsg.length);
                        break;
                    case 4:
                        sendmsg = Arrays.copyOf(bh_message, sendmsg.length);
                        break;
                    case 5:
                        sendmsg = Arrays.copyOf(mh_message, sendmsg.length);
                        break;
                    case 6:
                        sendmsg = Arrays.copyOf(fh_message, sendmsg.length);
                        break;
                    case 7:
                        sendmsg = Arrays.copyOf(nf_message, sendmsg.length);
                        break;
                    case 8:
                        sendmsg = Arrays.copyOf(w1_message, sendmsg.length);
                        break;
                }

                switch (ragentid) {
                    case 1:
                        recvmsg = Arrays.copyOf(t1_message, recvmsg.length);
                        break;
                    case 2:
                        recvmsg = Arrays.copyOf(t2_message, recvmsg.length);
                        break;
                    case 3:
                        recvmsg = Arrays.copyOf(t3_message, recvmsg.length);
                        break;
                    case 4:
                        recvmsg = Arrays.copyOf(bh_message, recvmsg.length);
                        break;
                    case 5:
                        recvmsg = Arrays.copyOf(mh_message, recvmsg.length);
                        break;
                    case 6:
                        recvmsg = Arrays.copyOf(fh_message, recvmsg.length);
                        break;
                    case 7:
                        recvmsg = Arrays.copyOf(nf_message, recvmsg.length);
                        break;
                    case 8:
                        recvmsg = Arrays.copyOf(w1_message, recvmsg.length);
                        break;
                }

                int flag;
                for (int loop1 = 0; loop1 < BUCK_MAX_PACKETS; loop1++) /* send(b2) to recv(b1)*/ {
                    //		printf("BUCK_MAX_PACKETS loop1 %d	\n",loop1);
                    if (sendmsg[loop1] != 0) {
                        for (int loop2 = 0; loop2 < BUCK_MAX_PACKETS; loop2++) {
                            if ((recvmsg[loop2] == 0) && (sendmsg[loop1] != 0))//Erasing duplicate at sender's side
                            {
                                flag = 0;
                                for (int loop3 = 0; loop3 < BUCK_MAX_PACKETS; loop3++) {
                                    if (sendmsg[loop1] == recvmsg[loop3]) {
                                        flag++;
                                    }
                                }
                                if (flag == 0) {
                                    recvmsg[loop2] = sendmsg[loop1];
                                    /*	Copy data	*/

                                    sendmsg[loop1] = 0;

                                    /*	Erase it	*/
                                    ////System.out.printf("sendmsg to recvmsg \n");
                                } else {

                                    sendmsg[loop1] = 0;

                                }

                            }

                        }
                    }
                }
                ////System.out.printf("%d	\t	%d	\t", sagentid, ragentid);
                ////System.out.printf("\n");

                switch (sagentid) {
                    case 1:
                        t1_message = Arrays.copyOf(sendmsg, t1_message.length);
                        break;
                    case 2:
                        t2_message = Arrays.copyOf(sendmsg, t2_message.length);
                        break;
                    case 3:
                        t3_message = Arrays.copyOf(sendmsg, t3_message.length);
                        break;
                    case 4:
                        bh_message = Arrays.copyOf(sendmsg, bh_message.length);
                        break;
                    case 5:
                        mh_message = Arrays.copyOf(sendmsg, mh_message.length);
                        break;
                    case 6:
                        fh_message = Arrays.copyOf(sendmsg, fh_message.length);
                        break;
                    case 7:
                        nf_message = Arrays.copyOf(sendmsg, nf_message.length);
                        break;
                    case 8:
                        w1_message = Arrays.copyOf(sendmsg, w1_message.length);
                        break;
                }

                switch (ragentid) {
                    case 1:
                        t1_message = Arrays.copyOf(recvmsg, t1_message.length);
                        break;
                    case 2:
                        t2_message = Arrays.copyOf(recvmsg, t2_message.length);
                        break;
                    case 3:
                        t3_message = Arrays.copyOf(recvmsg, t3_message.length);
                        break;
                    case 4:
                        bh_message = Arrays.copyOf(recvmsg, bh_message.length);
                        break;
                    case 5:
                        mh_message = Arrays.copyOf(recvmsg, mh_message.length);
                        break;
                    case 6:
                        fh_message = Arrays.copyOf(recvmsg, fh_message.length);
                        break;
                    case 7:
                        nf_message = Arrays.copyOf(recvmsg, nf_message.length);
                        break;
                    case 8:
                        w1_message = Arrays.copyOf(recvmsg, w1_message.length);
                        break;
                }

                radioContactLog.add(new RadioContactLog(sagentid, ragentid, globaltick));
                Platform.runLater(() -> {
                rclXYSeries.getData().add(new XYChart.Data(sagentid,ragentid));
                rclXTSeries.getData().add(new XYChart.Data(sagentid,globaltick));
                rclYTSeries.getData().add(new XYChart.Data(ragentid,globaltick));
                if (clearFirstRCL)
                {
                    rclXYSeries.getData().remove(0);
                    rclXTSeries.getData().remove(0);
                    rclYTSeries.getData().remove(0);
                    clearFirstRCL = false;
                }
                });
                showDataTransfer(sagentid, ragentid);
            }

        } else {

            int[] sendmsg = new int[2000];
            int[] recvmsg = new int[3500];
            int key = 0;

            if (sendingagentemt[sagentid] >= rhideeffort) {
                sendingagentemt[sagentid] = rhideeffort;
                key = 1;
                ////System.out.printf("key %d	\n", key);
//				Sleep(1000);
            }
            if (key == 1) {
                switch (sagentid) {
                    case 8:
                        sendmsg = Arrays.copyOf(w1_message, sendmsg.length);
                        break;
                    default:
                        ////System.out.printf("Some things is wrong with Push! \n");
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(CaptureTimeHide.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                }

                switch (ragentid) {
                    case 9:
                        recvmsg = Arrays.copyOf(d1_message, recvmsg.length);
                        break;
                    default:
                        ////System.out.printf("Some things is wrong with Push! \n");
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(CaptureTimeHide.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;

                }

                int flag;
                for (int loop1 = 0; loop1 < BUCK_MAX_PACKETS; loop1++) /* send(b2) to recv(b1)*/ {
                    //		printf("BUCK_MAX_PACKETS loop1 %d	\n",loop1);
                    if (sendmsg[loop1] != 0) {
                        for (int loop2 = 0; loop2 < DATA_MAX_PACKETS; loop2++) {
                            if ((recvmsg[loop2] == 0) && (sendmsg[loop1] != 0))//Erasing duplicate at sender's side
                            {
                                flag = 0;
                                for (int loop3 = 0; loop3 < DATA_MAX_PACKETS; loop3++) {
                                    if (sendmsg[loop1] == recvmsg[loop3]) {
                                        flag++;
                                    }
                                }
                                if (flag == 0) {
                                    recvmsg[loop2] = sendmsg[loop1];
                                    /*	Copy data	*/
                                    sendmsg[loop1] = 0;
                                    /*	Erase it	*/
                                    ////System.out.printf("sendmsg to recvmsg \n");
                                } else {
        				sendmsg[loop1]=0;//This is important in erasing duplicate at sender's side
                                    //as TTL is absent in AUDTHMN.
                                }

                            }

                        }
                    }
                }
                ////System.out.printf("%d	\t	%d	\t", sagentid, ragentid);
                ////System.out.printf("\n");

                switch (sagentid) {
                    case 8:
                        w1_message = Arrays.copyOf(sendmsg, w1_message.length);
                        break;
                    default:
                        ////System.out.printf("Some things is wrong with Push! \n");
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(CaptureTimeHide.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                }

                switch (ragentid)
                {
                    case 9:
                        d1_message = Arrays.copyOf(recvmsg, d1_message.length);
                        break;
                    default:
                        ////System.out.printf("Some things is wrong with Push! \n");

                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(CaptureTimeHide.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        break;

                }
                radioContactLog.add(new RadioContactLog(sagentid, ragentid, globaltick));
                Platform.runLater(() ->
                {
                    rclXYSeries.getData().add(new XYChart.Data(sagentid,ragentid));
                    rclXTSeries.getData().add(new XYChart.Data(sagentid,globaltick));
                    rclYTSeries.getData().add(new XYChart.Data(ragentid,globaltick));
                    if (clearFirstRCL)
                    {
                        rclXYSeries.getData().remove(0);
                        rclXTSeries.getData().remove(0);
                        rclYTSeries.getData().remove(0);
                        clearFirstRCL = false;
                    }
                });
                
                if(sagentid == 8 && ragentid == 9)
                {
                    showDataTransfer(8, 9);
                }
            }

        }

    }

/*
 ** FHdr-beg ******************************************************************
 **
 ** Function name: mapgridintolinearmem
 **
 ** Purpose: This function simulates the data pull operation from sensor node s5 to cow c1_
 **			 Duplicate copying of message packets is avoided_ The copied message
 **			 packet is erased from source so as to release memory_
 ** FHdr-end ******************************************************************
 */

    void mapgridintolinearmem(int mapagentid) {
        switch (mapagentid) {
            case 1: //
            {
                int flag;
                for (int bucklocationx = 0; bucklocationx < MAX_X_GRID; bucklocationx++) {
                    for (int bucklocationy = 0; bucklocationy < MAX_Y_GRID; bucklocationy++) {
                        if (t1_grazecoordinate[bucklocationx][bucklocationy] == 1) {
                            //		////System.out.printf("bucklocationx %d	\t	bucklocationy %d	\n",bucklocationx,bucklocationy);
                            int tempmessage = bucklocationx << 6;//Bits 10,9,8,7,6 are for location x
                            tempmessage = tempmessage | bucklocationy;//Bits 5,4,3,2,1,0 are for location y
                            tempmessage = tempmessage | (t1_agentid << 11);//Bits 15,14,13,12,11 are for Buck ID (for t1 is 00001)
                            for (int loop2 = 0; loop2 < BUCK_MAX_PACKETS; loop2++) {
                                if (t1_message[loop2] == 0) {
                                    flag = 0;
                                    for (int loop3 = 0; loop3 < BUCK_MAX_PACKETS; loop3++) {
                                        if (tempmessage == t1_message[loop3]) {
                                            flag++;
                                        }
                                    }
                                    if (flag == 0) {
                                        t1_message[loop2] = tempmessage;
                                        /* Copy data */
                                        tempmessage = 0;
                                        //	s5message[sringcount(loop1)]=0;	/* Erase it for First Contact Routing */
//												////System.out.printf("t1 \n");
//												////System.out.printf("bucklocationx %d	\t	bucklocationy %d	\n",bucklocationx,bucklocationy);
                                    }

                                }

                            }

                        }

                    }
                }
            }
//				  ////System.out.printf("T1 \n");
            break;//	End of case 1_
            case 2: //
            {
                int flag;
                for (int bucklocationx = 0; bucklocationx < MAX_X_GRID; bucklocationx++) {
                    for (int bucklocationy = 0; bucklocationy < MAX_Y_GRID; bucklocationy++) {
                        if (t2_grazecoordinate[bucklocationx][bucklocationy] == 1) {
                            int tempmessage = bucklocationx << 6;//Bits 10,9,8,7,6 are for location x
                            tempmessage = tempmessage | bucklocationy;//Bits 5,4,3,2,1,0 are for location y
                            tempmessage = tempmessage | (t2_agentid << 11);//Bits 15,14,13,12,11 are for Buck ID (for t1 is 00010)
                            for (int loop2 = 0; loop2 < BUCK_MAX_PACKETS; loop2++) {
                                if (t2_message[loop2] == 0) {
                                    flag = 0;
                                    for (int loop3 = 0; loop3 < BUCK_MAX_PACKETS; loop3++) {
                                        if (tempmessage == t2_message[loop3]) {
                                            flag++;
                                        }
                                    }
                                    if (flag == 0) {
                                        t2_message[loop2] = tempmessage;
                                        /* Copy data */
                                        tempmessage = 0;
                                        //	s5message[sringcount(loop1)]=0;	/* Erase it for First Contact Routing */
//												////System.out.printf("t2 \n");
                                    }

                                }

                            }

                        }

                    }
                }
            }
//				  ////System.out.printf("T2 \n");
            break;//	End of case 2_
            case 3: //
            {
                int flag;
                for (int bucklocationx = 0; bucklocationx < MAX_X_GRID; bucklocationx++) {
                    for (int bucklocationy = 0; bucklocationy < MAX_Y_GRID; bucklocationy++) {
                        if (t3_grazecoordinate[bucklocationx][bucklocationy] == 1) {
                            int tempmessage = bucklocationx << 6;//Bits 10,9,8,7,6 are for location x
                            tempmessage = tempmessage | bucklocationy;//Bits 5,4,3,2,1,0 are for location y
                            tempmessage = tempmessage | (t3_agentid << 11);//Bits 15,14,13,12,11 are for Buck ID (for t1 is 00010)
                            for (int loop2 = 0; loop2 < BUCK_MAX_PACKETS; loop2++) {
                                if (t3_message[loop2] == 0) {
                                    flag = 0;
                                    for (int loop3 = 0; loop3 < BUCK_MAX_PACKETS; loop3++) {
                                        if (tempmessage == t3_message[loop3]) {
                                            flag++;
                                        }
                                    }
                                    if (flag == 0) {
                                        t3_message[loop2] = tempmessage;
                                        /* Copy data */
                                        tempmessage = 0;
                                        //	s5message[sringcount(loop1)]=0;	/* Erase it for First Contact Routing */
//												////System.out.printf("t3 \n");
                                    }

                                }

                            }

                        }

                    }
                }
            }
//				  ////System.out.printf("T3 \n");
            break;//	End of case 3_
            case 4: //
            {
                int flag;
                for (int bucklocationx = 0; bucklocationx < MAX_X_GRID; bucklocationx++) {
                    for (int bucklocationy = 0; bucklocationy < MAX_Y_GRID; bucklocationy++) {
                        if (bh_grazecoordinate[bucklocationx][bucklocationy] == 1) {
                            int tempmessage = bucklocationx << 6;//Bits 10,9,8,7,6 are for location x
                            tempmessage = tempmessage | bucklocationy;//Bits 5,4,3,2,1,0 are for location y
                            tempmessage = tempmessage | (bh_agentid << 11);//Bits 15,14,13,12,11 are for Buck ID (for t1 is 00010)
                            for (int loop2 = 0; loop2 < BUCK_MAX_PACKETS; loop2++) {
                                if (bh_message[loop2] == 0) {
                                    flag = 0;
                                    for (int loop3 = 0; loop3 < BUCK_MAX_PACKETS; loop3++) {
                                        if (tempmessage == bh_message[loop3]) {
                                            flag++;
                                        }
                                    }
                                    if (flag == 0) {
                                        bh_message[loop2] = tempmessage;
                                        /* Copy data */
                                        tempmessage = 0;
                                        //	s5message[sringcount(loop1)]=0;	/* Erase it for First Contact Routing */
//												////System.out.printf("bh \n");
                                    }

                                }

                            }

                        }

                    }
                }
            }
//				  ////System.out.printf("BH \n");
            break;//	End of case 4_
            case 5: //
            {
                int flag;
                for (int bucklocationx = 0; bucklocationx < MAX_X_GRID; bucklocationx++) {
                    for (int bucklocationy = 0; bucklocationy < MAX_Y_GRID; bucklocationy++) {
                        if (mh_grazecoordinate[bucklocationx][bucklocationy] == 1) {
                            int tempmessage = bucklocationx << 6;//Bits 10,9,8,7,6 are for location x
                            tempmessage = tempmessage | bucklocationy;//Bits 5,4,3,2,1,0 are for location y
                            tempmessage = tempmessage | (mh_agentid << 11);//Bits 15,14,13,12,11 are for Buck ID (for t1 is 00010)
                            for (int loop2 = 0; loop2 < BUCK_MAX_PACKETS; loop2++) {
                                if (mh_message[loop2] == 0) {
                                    flag = 0;
                                    for (int loop3 = 0; loop3 < BUCK_MAX_PACKETS; loop3++) {
                                        if (tempmessage == mh_message[loop3]) {
                                            flag++;
                                        }
                                    }
                                    if (flag == 0) {
                                        mh_message[loop2] = tempmessage;
                                        /* Copy data */
                                        tempmessage = 0;
                                        //	s5message[sringcount(loop1)]=0;	/* Erase it for First Contact Routing */
//												////System.out.printf("mh \n");
                                    }

                                }

                            }

                        }

                    }
                }
            }
            break;//	End of case 5_
            case 6: //
            {
                int flag;
                for (int bucklocationx = 0; bucklocationx < MAX_X_GRID; bucklocationx++) {
                    for (int bucklocationy = 0; bucklocationy < MAX_Y_GRID; bucklocationy++) {
                        if (fh_grazecoordinate[bucklocationx][bucklocationy] == 1) {
                            int tempmessage = bucklocationx << 6;//Bits 10,9,8,7,6 are for location x
                            tempmessage = tempmessage | bucklocationy;//Bits 5,4,3,2,1,0 are for location y
                            tempmessage = tempmessage | (fh_agentid << 11);//Bits 15,14,13,12,11 are for Buck ID (for t1 is 00010)
                            for (int loop2 = 0; loop2 < BUCK_MAX_PACKETS; loop2++) {
                                if (fh_message[loop2] == 0) {
                                    flag = 0;
                                    for (int loop3 = 0; loop3 < BUCK_MAX_PACKETS; loop3++) {
                                        if (tempmessage == fh_message[loop3]) {
                                            flag++;
                                        }
                                    }
                                    if (flag == 0) {
                                        fh_message[loop2] = tempmessage;
                                        /* Copy data */
                                        tempmessage = 0;
                                        //	s5message[sringcount(loop1)]=0;	/* Erase it for First Contact Routing */
//												////System.out.printf("fh \n");
                                    }

                                }

                            }

                        }

                    }
                }
            }
            break;//	End of case 6_
            case 7: //
            {
                int flag;
                for (int bucklocationx = 0; bucklocationx < MAX_X_GRID; bucklocationx++) {
                    for (int bucklocationy = 0; bucklocationy < MAX_Y_GRID; bucklocationy++) {
                        if (nf_grazecoordinate[bucklocationx][bucklocationy] == 1) {
                            int tempmessage = bucklocationx << 6;//Bits 10,9,8,7,6 are for location x
                            tempmessage = tempmessage | bucklocationy;//Bits 5,4,3,2,1,0 are for location y
                            tempmessage = tempmessage | (nf_agentid << 11);//Bits 15,14,13,12,11 are for Buck ID (for t1 is 00010)
                            for (int loop2 = 0; loop2 < BUCK_MAX_PACKETS; loop2++) {
                                if (nf_message[loop2] == 0) {
                                    flag = 0;
                                    for (int loop3 = 0; loop3 < BUCK_MAX_PACKETS; loop3++) {
                                        if (tempmessage == nf_message[loop3]) {
                                            flag++;
                                        }
                                    }
                                    if (flag == 0) {
                                        nf_message[loop2] = tempmessage;
                                        /* Copy data */
                                        tempmessage = 0;
                                        //	s5message[sringcount(loop1)]=0;	/* Erase it for First Contact Routing */
//												////System.out.printf("nf \n");
                                    }

                                }

                            }

                        }

                    }
                }
            }
            break;//	End of case 7_
            default:            //
                ////System.out.printf("Invalid Call \n");
                break;
        }

    }

/*
 ** FHdr-beg ******************************************************************
 **
 ** Function name: results
 **
 ** Purpose: TBD
 **
 ** FHdr-end ******************************************************************
 */

 float calculateemt(int n)
 {
	float calc_emt = 1.0f;
	return  calc_emt;
 }
    
    
    @Override
    public String simName()
    {
        return "Epedemic Single Copy";
    }
    
    @Override
    String simShortName()
    {
        return "ESC";
    }
}