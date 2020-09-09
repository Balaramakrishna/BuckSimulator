/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bucksimulator.model;

import static java.lang.Math.max;
import static java.lang.Math.pow;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.chart.XYChart;

/**
 * Simulates the Prophet Routing simulation strategy.
 * @author balaram
 */
public class ProphetRouting extends BaseSimulation
{
    int k = 1;
    int beta = 1;
    int gamma = 1, pinit = 1;
    int t1_xmax_coordinate = 12;
    int t1_ymax_coordinate = 25;
    int t1_xmin_coordinate = 8;
    int t1_ymin_coordinate = 21;
    float t1_discoveryrate = 1;
    float t1_hideeffort;
    int t1_ageing = 0;

    int t2_xmax_coordinate = 14;
    int t2_ymax_coordinate = 19;
    int t2_xmin_coordinate = 10;
    int t2_ymin_coordinate = 15;
    float t2_discoveryrate = 1;
    float t2_hideeffort;
    int t2_ageing = 0;

    int t3_xmax_coordinate = 14;
    int t3_ymax_coordinate = 6;
    int t3_xmin_coordinate = 10;
    int t3_ymin_coordinate = 2;
    float t3_discoveryrate = 1;
    float t3_hideeffort;
    int t3_ageing = 0;

    int bh_xmax_coordinate = 24;
    int bh_ymax_coordinate = 31;
    int bh_xmin_coordinate = 0;
    int bh_ymin_coordinate = 0;
    float bh_discoveryrate = 1;
    float bh_hideeffort;
    int bh_ageing = 0;

    int mh_xmax_coordinate = 24;
    int mh_ymax_coordinate = 31;
    int mh_xmin_coordinate = 0;
    int mh_ymin_coordinate = 0;
    float mh_discoveryrate = 1;
    float mh_hideeffort;
    int mh_ageing = 0;

    int fh_xmax_coordinate = 24;
    int fh_ymax_coordinate = 31;
    int fh_xmin_coordinate = 0;
    int fh_ymin_coordinate = 0;
    float fh_discoveryrate = 1;
    float fh_hideeffort;
    int fh_ageing = 0;

    int nf_xmax_coordinate = 24;
    int nf_ymax_coordinate = 31;
    int nf_xmin_coordinate = 0;
    int nf_ymin_coordinate = 0;
    float nf_discoveryrate = 1;
    float nf_hideeffort;
    int nf_ageing = 0;

    float lebesguemeasure = 1;
    float epsilon = 0.1f;
    float[][] ptransitive = new float[8][8];


    public ProphetRouting()
    {
        rcrSeries = new XYChart.Data("PR", 0);
        tps.setName("PR");
        tpslc.setName("PR");
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
        t1_discoveryrate = 1;
        t1_hideeffort = 0;
        t1_ageing = 0;
        t2_discoveryrate = 1;
        t2_hideeffort = 0;
        t2_ageing = 0;
        t3_discoveryrate = 1;
        t3_hideeffort = 0;
        t3_ageing = 0;
        bh_discoveryrate = 1;
        bh_hideeffort = 0;
        bh_ageing = 0;
        fh_discoveryrate = 1;
        fh_hideeffort = 0;
        fh_ageing = 0;
        mh_discoveryrate = 1;
        mh_hideeffort = 0;
        mh_ageing = 0;
        nf_discoveryrate = 1;
        nf_hideeffort = 0;
        nf_ageing = 0;
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                ptransitive[i][j] = 0;
            }
        }
        baseInit();
    }

    @Override
    public void run()
    {
        init();
        Platform.runLater(() ->
        {
            simStarted.set(true);
            simInProgress.set(true);
            simCompleted.set(false);
            simNotInProgress.set(false);
        });
        t1_hideeffort = (1 - epsilon) * (lebesguemeasure / t1_discoveryrate);
        t2_hideeffort = (1 - epsilon) * (lebesguemeasure / t2_discoveryrate);
        t3_hideeffort = (1 - epsilon) * (lebesguemeasure / t3_discoveryrate);
        bh_hideeffort = (1 - epsilon) * (lebesguemeasure / bh_discoveryrate);
        mh_hideeffort = (1 - epsilon) * (lebesguemeasure / mh_discoveryrate);
        fh_hideeffort = (1 - epsilon) * (lebesguemeasure / fh_discoveryrate);
        nf_hideeffort = (1 - epsilon) * (lebesguemeasure / nf_discoveryrate);

        buckRouter();
        if (!terminate)
        {
            results();
        }
    }

    public void t1()
    {
        int sendingagentid = 1;
        prophetrouting(sendingagentid);

        int t1buckdirector = ((rand.nextInt() >> rightshift) & 7);

        if ((t1_x_coordinate == t1_xmax_coordinate) && (t1_y_coordinate == t1_ymax_coordinate))
        /*
         * NE corner
         */
        {
            switch (t1buckdirector)
            {
                case 3 ->
                    stepwest(sendingagentid);
                case 5 ->
                    stepsouthwest(sendingagentid);
                case 6 ->
                    stepsouth(sendingagentid);
                default ->
                    stepsouthwest(sendingagentid);
            }
        }
        else if ((t1_x_coordinate == t1_xmin_coordinate) && (t1_y_coordinate == t1_ymin_coordinate))
        /*
         * SW corner
         */
        {
            switch (t1buckdirector)
            {
                case 1 ->
                    stepnorth(sendingagentid);
                case 2 ->
                    stepnortheast(sendingagentid);
                case 4 ->
                    stepeast(sendingagentid);
                default ->
                    stepnortheast(sendingagentid);
            }
        }
        else if ((t1_x_coordinate == t1_xmin_coordinate) && (t1_y_coordinate == t1_ymax_coordinate))
        /*
         * NW corner
         */
        {
            switch (t1buckdirector)
            {
                case 4 ->
                    stepeast(sendingagentid);
                case 6 ->
                    stepsouth(sendingagentid);
                case 7 ->
                    stepsoutheast(sendingagentid);
                default ->
                    stepsoutheast(sendingagentid);
            }
        }
        else if ((t1_x_coordinate == t1_xmax_coordinate) && (t1_y_coordinate == t1_ymin_coordinate))
        /*
         * SE corner
         */
        {
            switch (t1buckdirector)
            {
                case 0 ->
                    stepnorthwest(sendingagentid);
                case 1 ->
                    stepnorth(sendingagentid);
                case 3 ->
                    stepwest(sendingagentid);
                default ->
                    stepnorthwest(sendingagentid);
            }
        }
        else if ((t1_x_coordinate == t1_xmin_coordinate) && (t1_y_coordinate < t1_ymax_coordinate) && (t1_y_coordinate > t1_ymin_coordinate))
        /*
         * Right of West
         */
        {
            switch (t1buckdirector)
            {
                case 1 ->
                    stepnorth(sendingagentid);
                case 2 ->
                    stepnortheast(sendingagentid);
                case 4 ->
                    stepeast(sendingagentid);
                case 6 ->
                    stepsouth(sendingagentid);
                case 7 ->
                    stepsoutheast(sendingagentid);
                default ->
                    stepeast(sendingagentid);
            }
        }
        else if ((t1_x_coordinate == t1_xmax_coordinate) && (t1_y_coordinate < t1_ymax_coordinate) && (t1_y_coordinate > t1_ymin_coordinate))
        /*
         * Left of East
         */
        {
            switch (t1buckdirector)
            {
                case 0 ->
                    stepnorthwest(sendingagentid);
                case 1 ->
                    stepnorth(sendingagentid);
                case 3 ->
                    stepwest(sendingagentid);
                case 5 ->
                    stepsouthwest(sendingagentid);
                case 6 ->
                    stepsouth(sendingagentid);
                default ->
                    stepwest(sendingagentid);
            }
        }
        else if ((t1_y_coordinate == t1_ymax_coordinate) && (t1_x_coordinate < t1_xmax_coordinate) && (t1_x_coordinate > t1_xmin_coordinate))
        /*
         * Below North
         */
        {
            switch (t1buckdirector)
            {
                case 3:
                    stepwest(sendingagentid);
                    break;
                case 4:
                    stepeast(sendingagentid);
                    break;
                case 5:
                    stepsouthwest(sendingagentid);
                    break;
                case 6:
                    stepsouth(sendingagentid);
                    break;
                case 7:
                    stepsoutheast(sendingagentid);
                    break;
                default:
                    stepsouth(sendingagentid);
                    break;
            }
        }
        else if ((t1_y_coordinate == t1_ymin_coordinate) && (t1_x_coordinate < t1_xmax_coordinate) && (t1_x_coordinate > t1_xmin_coordinate))
        /*
         * Above South
         */
        {
            switch (t1buckdirector)
            {
                case 0:
                    stepnorthwest(sendingagentid);
                    break;
                case 1:
                    stepnorth(sendingagentid);
                    break;
                case 2:
                    stepnortheast(sendingagentid);
                    break;
                case 3:
                    stepwest(sendingagentid);
                    break;
                case 4:
                    stepeast(sendingagentid);
                    break;
                default:           // Everything else - step north
                    stepnorth(sendingagentid);
                    break;
            }
        }
        else
        {

            switch (t1buckdirector)
            {
                case 0:
                    stepnorthwest(sendingagentid);
                    break;
                case 1:
                    /*
                     * t1stepsouth();
                     */
                    stepnorth(sendingagentid);
                    break;
                case 2:
                    stepnortheast(sendingagentid);
                    break;
                case 3:
                    stepwest(sendingagentid);
                    break;
                case 4:
                    /*
                     * t1stepwest();
                     */
                    stepeast(sendingagentid);
                    break;
                case 5:
                    stepsouthwest(sendingagentid);
                    break;
                case 6:
                    stepsouth(sendingagentid);
                    break;
                default:
                    stepsoutheast(sendingagentid);
                    break;
            }
        }
        t1_grazecoordinate[t1_x_coordinate][t1_y_coordinate] = 1;
        t1GrazeGPSLog.add(new GPSLog(t1_x_coordinate, t1_y_coordinate));
        Platform.runLater(() ->
        {
            t1Series.getData().add(new XYChart.Data(t1_x_coordinate - 0.5, t1_y_coordinate - 0.5));
            t1Series.getData().remove(0);
        });
        mapgridintolinearmem(sendingagentid);
    }

    /*
     ** FHdr-beg
     * ****************************************************************** * *
     * Function name: t2 * * Purpose: This function simulates the movement of a
     * territorial blackbuck. *	The relaxed pace of grazing is constant.However
     * the direction is random. * * FHdr-end
     * ******************************************************************
     */
    public void t2()
    {
        int sendingagentid = 2;
        prophetrouting(sendingagentid);
        int t2buckdirector = ((rand.nextInt() >> rightshift) & 7);

        if ((t2_x_coordinate == t2_xmax_coordinate) && (t2_y_coordinate == t2_ymax_coordinate))
        /*
         * NE corner
         */
        {
            switch (t2buckdirector)
            {
                case 3 -> stepwest(sendingagentid);
                case 5 -> stepsouthwest(sendingagentid);
                case 6 -> stepsouth(sendingagentid);
                default -> stepsouthwest(sendingagentid);
            }
        }
        else if ((t2_x_coordinate == t2_xmin_coordinate) && (t2_y_coordinate == t2_ymin_coordinate))
        {
            switch (t2buckdirector)
            {
                case 1 -> stepnorth(sendingagentid);
                case 2 -> stepnortheast(sendingagentid);
                case 4 -> stepeast(sendingagentid);
                default -> stepnortheast(sendingagentid);
            }
        }
        else if ((t2_x_coordinate == t2_xmin_coordinate) && (t2_y_coordinate == t2_ymax_coordinate))
        {
            switch (t2buckdirector)
            {
                case 4 -> stepeast(sendingagentid);
                case 6 -> stepsouth(sendingagentid);
                case 7 -> stepsoutheast(sendingagentid);
                default -> stepsoutheast(sendingagentid);
            }
        }
        else if ((t2_x_coordinate == t2_xmax_coordinate) && (t2_y_coordinate == t2_ymin_coordinate))
        {
            switch (t2buckdirector)
            {
                case 0 -> stepnorthwest(sendingagentid);
                case 1 -> stepnorth(sendingagentid);
                case 3 -> stepwest(sendingagentid);
                default -> stepnorthwest(sendingagentid);
            }
        }
        else if ((t2_x_coordinate == t2_xmin_coordinate) && (t2_y_coordinate < t2_ymax_coordinate) && (t2_y_coordinate > t2_ymin_coordinate))
        {
            switch (t2buckdirector)
            {
                case 1 -> stepnorth(sendingagentid);
                case 2 -> stepnortheast(sendingagentid);
                case 4 -> stepeast(sendingagentid);
                case 6 -> stepsouth(sendingagentid);
                case 7 -> stepsoutheast(sendingagentid);
                default -> stepeast(sendingagentid);
            }
        }
        else if ((t2_x_coordinate == t2_xmax_coordinate) && (t2_y_coordinate < t2_ymax_coordinate) && (t2_y_coordinate > t2_ymin_coordinate))
        /*
         * Left of East
         */
        {
            switch (t2buckdirector)
            {
                case 0 -> stepnorthwest(sendingagentid);
                case 1 -> stepnorth(sendingagentid);
                case 3 -> stepwest(sendingagentid);
                case 5 -> stepsouthwest(sendingagentid);
                case 6 -> stepsouth(sendingagentid);
                default -> stepwest(sendingagentid);
            }
        }
        else if ((t2_y_coordinate == t2_ymax_coordinate) && (t2_x_coordinate < t2_xmax_coordinate) && (t2_x_coordinate > t2_xmin_coordinate))
        {
            switch (t2buckdirector)
            {
                case 3 -> stepwest(sendingagentid);
                case 4 -> stepeast(sendingagentid);
                case 5 -> stepsouthwest(sendingagentid);
                case 6 -> stepsouth(sendingagentid);
                case 7 -> stepsoutheast(sendingagentid);
                default -> stepsouth(sendingagentid);
            }
        }
        else if ((t2_y_coordinate == t2_ymin_coordinate) && (t2_x_coordinate < t2_xmax_coordinate) && (t2_x_coordinate > t2_xmin_coordinate))
        /*
         * Above South
         */
        {
            switch (t2buckdirector)
            {
                case 0 -> stepnorthwest(sendingagentid);
                case 1 -> stepnorth(sendingagentid);
                case 2 -> stepnortheast(sendingagentid);
                case 3 -> stepwest(sendingagentid);
                case 4 -> stepeast(sendingagentid);
                default -> stepnorth(sendingagentid);
            }
        }
        else
        {
            switch (t2buckdirector)
            {
                case 0 -> stepnorthwest(sendingagentid);
                case 1 -> stepnorth(sendingagentid);
                case 2 -> stepnortheast(sendingagentid);
                case 3 -> stepwest(sendingagentid);
                case 4 -> stepeast(sendingagentid);
                case 5 -> stepsouthwest(sendingagentid);
                case 6 -> stepsouth(sendingagentid);
                default -> stepsoutheast(sendingagentid);
            }
        }

        t2_grazecoordinate[t2_x_coordinate][t2_y_coordinate] = 1;
        t2GrazeGPSLog.add(new GPSLog(t2_x_coordinate, t2_y_coordinate));
        mapgridintolinearmem(sendingagentid);
        Platform.runLater(() ->
        {
            t2Series.getData().add(new XYChart.Data(t2_x_coordinate - 0.5, t2_y_coordinate - 0.5));
            t2Series.getData().remove(0);
        });
    }

    public void t3()
    {
        int sendingagentid = 3;
        prophetrouting(sendingagentid);
        int t3buckdirector = ((rand.nextInt() >> rightshift) & 7);

        if ((t3_x_coordinate == t3_xmax_coordinate) && (t3_y_coordinate == t3_ymax_coordinate))
        {
            switch (t3buckdirector)
            {
                case 3 -> stepwest(sendingagentid);
                case 5 -> stepsouthwest(sendingagentid);
                case 6 -> stepsouth(sendingagentid);
                default -> stepsouthwest(sendingagentid);
            }
        }
        else if ((t3_x_coordinate == t3_xmin_coordinate) && (t3_y_coordinate == t3_ymin_coordinate))
        /*
         * SW corner
         */
        {
            switch (t3buckdirector)
            {
                case 1:
                    stepnorth(sendingagentid);
                    break;
                case 2:
                    stepnortheast(sendingagentid);
                    break;
                case 4:
                    stepeast(sendingagentid);
                    break;
                default:
                    stepnortheast(sendingagentid);
                    break;
            }
        }
        else if ((t3_x_coordinate == t3_xmin_coordinate) && (t3_y_coordinate == t3_ymax_coordinate))
        /*
         * NW corner
         */
        {
            switch (t3buckdirector)
            {
                case 4:
                    stepeast(sendingagentid);
                    break;
                case 6:
                    stepsouth(sendingagentid);
                    break;
                case 7:
                    stepsoutheast(sendingagentid);
                    break;
                default:
                    stepsoutheast(sendingagentid);
                    break;
            }
        }
        else if ((t3_x_coordinate == t3_xmax_coordinate) && (t3_y_coordinate == t3_ymin_coordinate))
        /*
         * SE corner
         */
        {
            switch (t3buckdirector)
            {
                case 0:
                    stepnorthwest(sendingagentid);
                    break;
                case 1:
                    stepnorth(sendingagentid);
                    break;
                case 3:
                    stepwest(sendingagentid);
                    break;
                default:           // Everything else - step northwest
                    stepnorthwest(sendingagentid);
                    break;
            }
        }
        else if ((t3_x_coordinate == t3_xmin_coordinate) && (t3_y_coordinate < t3_ymax_coordinate) && (t3_y_coordinate > t3_ymin_coordinate))
        /*
         * Right of West
         */
        {
            switch (t3buckdirector)
            {
                case 1:
                    stepnorth(sendingagentid);
                    break;
                case 2:
                    stepnortheast(sendingagentid);
                    break;
                case 4:
                    stepeast(sendingagentid);
                    break;
                case 6:
                    stepsouth(sendingagentid);
                    break;
                case 7:
                    stepsoutheast(sendingagentid);
                    break;
                default:           // Everything else - step
                    stepeast(sendingagentid);
                    break;
            }
        }
        else if ((t3_x_coordinate == t3_xmax_coordinate) && (t3_y_coordinate < t3_ymax_coordinate) && (t3_y_coordinate > t3_ymin_coordinate))
        {
            switch (t3buckdirector)
            {
                case 0 ->
                    stepnorthwest(sendingagentid);
                case 1 ->
                    stepnorth(sendingagentid);
                case 3 ->
                    stepwest(sendingagentid);
                case 5 ->
                    stepsouthwest(sendingagentid);
                case 6 ->
                    stepsouth(sendingagentid);
                default ->
                    stepwest(sendingagentid);
            }
        }
        else if ((t3_y_coordinate == t3_ymax_coordinate) && (t3_x_coordinate < t3_xmax_coordinate) && (t3_x_coordinate > t3_xmin_coordinate))
        /*
         * Below North
         */
        {
            switch (t3buckdirector)
            {
                case 3:
                    stepwest(sendingagentid);
                    break;
                case 4:
                    stepeast(sendingagentid);
                    break;
                case 5:
                    stepsouthwest(sendingagentid);
                    break;
                case 6:
                    stepsouth(sendingagentid);
                    break;
                case 7:
                    stepsoutheast(sendingagentid);
                    break;
                default:
                    stepsouth(sendingagentid);
                    break;
            }
        }
        else if ((t3_y_coordinate == t3_ymin_coordinate) && (t3_x_coordinate < t3_xmax_coordinate) && (t3_x_coordinate > t3_xmin_coordinate))
        /*
         * Above South
         */
        {
            switch (t3buckdirector)
            {
                case 0:
                    stepnorthwest(sendingagentid);
                    break;
                case 1:
                    stepnorth(sendingagentid);
                    break;
                case 2:
                    stepnortheast(sendingagentid);
                    break;
                case 3:
                    stepwest(sendingagentid);
                    break;
                case 4:
                    stepeast(sendingagentid);
                    break;
                default:           // Everything else - step north
                    stepnorth(sendingagentid);
                    break;
            }
        }
        else
        {

            switch (t3buckdirector)
            {
                case 0:
                    stepnorthwest(sendingagentid);
                    break;
                case 1:
                    /*
                     * t3stepsouth();
                     */
                    stepnorth(sendingagentid);
                    break;
                case 2:
                    stepnortheast(sendingagentid);
                    break;
                case 3:
                    stepwest(sendingagentid);
                    break;
                case 4:
                    /*
                     * t3stepwest();
                     */
                    stepeast(sendingagentid);
                    break;
                case 5:
                    stepsouthwest(sendingagentid);
                    break;
                case 6:
                    stepsouth(sendingagentid);
                    break;
                default:
                    /*
                     * t3stepsouthwest();
                     */
                    stepsoutheast(sendingagentid);
                    break;
            }
        }

        t3_grazecoordinate[t3_x_coordinate][t3_y_coordinate] = 1;
        t3GrazeGPSLog.add(new GPSLog(t3_x_coordinate, t3_y_coordinate));
        mapgridintolinearmem(sendingagentid);
        Platform.runLater(() ->
        {
            t3Series.getData().add(new XYChart.Data(t3_x_coordinate - 0.5, t3_y_coordinate - 0.5));
            t3Series.getData().remove(0);
        });
    }

    /*
     ** FHdr-beg
     * ****************************************************************** * *
     * Function name: bh * * Purpose: This function simulates the movement of a
     * territorial blackbuck. *	The relaxed pace of grazing is constant.However
     * the direction is random. * * FHdr-end
     * ******************************************************************
     */
    public void bh()
    {

        int sendingagentid = 4;
//
        if (bhroutinglock == routing.open)
        {
            prophetrouting(sendingagentid);
        }
        bhroutinglock = routing.shut;


        /*
         * Generate random number and use the last 3 bits to decide the
         * direction of buck
         */
//previous		int c1buckdirector = (rand()&7);
        int bhbuckdirector = ((rand.nextInt() >> rightshift) & 7);
//		//System.out.printf("%d \t %d% \n",c1buckdirector1,c1buckdirector);

        if ((bh_x_coordinate == bh_xmax_coordinate) && (bh_y_coordinate == bh_ymax_coordinate))
        /*
         * NE corner
         */
        {
            switch (bhbuckdirector)
            {
                case 3:
                    stepwest(sendingagentid);
                    break;
                case 5:
                    stepsouthwest(sendingagentid);
                    break;
                case 6:
                    stepsouth(sendingagentid);
                    break;
                default:
                    stepsouthwest(sendingagentid);
                    break;
            }
        }
        else if ((bh_x_coordinate == bh_xmin_coordinate) && (bh_y_coordinate == bh_ymin_coordinate))
        /*
         * SW corner
         */
        {
            switch (bhbuckdirector)
            {
                case 1:
                    stepnorth(sendingagentid);
                    break;
                case 2:
                    stepnortheast(sendingagentid);
                    break;
                case 4:
                    stepeast(sendingagentid);
                    break;
                default:
                    stepnortheast(sendingagentid);
                    break;
            }
        }
        else if ((bh_x_coordinate == bh_xmin_coordinate) && (bh_y_coordinate == bh_ymax_coordinate))
        /*
         * NW corner
         */
        {
            switch (bhbuckdirector)
            {
                case 4:
                    stepeast(sendingagentid);
                    break;
                case 6:
                    stepsouth(sendingagentid);
                    break;
                case 7:
                    stepsoutheast(sendingagentid);
                    break;
                default:
                    stepsoutheast(sendingagentid);
                    break;
            }
        }
        else if ((bh_x_coordinate == bh_xmax_coordinate) && (bh_y_coordinate == bh_ymin_coordinate))
        /*
         * SE corner
         */
        {
            switch (bhbuckdirector)
            {
                case 0:
                    stepnorthwest(sendingagentid);
                    break;
                case 1:
                    stepnorth(sendingagentid);
                    break;
                case 3:
                    stepwest(sendingagentid);
                    break;
                default:           // Everything else - step northwest
                    stepnorthwest(sendingagentid);
                    break;
            }
        }
        else if ((bh_x_coordinate == bh_xmin_coordinate) && (bh_y_coordinate < bh_ymax_coordinate) && (bh_y_coordinate > bh_ymin_coordinate))
        /*
         * Right of West
         */
        {
            switch (bhbuckdirector)
            {
                case 1:
                    stepnorth(sendingagentid);
                    break;
                case 2:
                    stepnortheast(sendingagentid);
                    break;
                case 4:
                    stepeast(sendingagentid);
                    break;
                case 6:
                    stepsouth(sendingagentid);
                    break;
                case 7:
                    stepsoutheast(sendingagentid);
                    break;
                default:           // Everything else - step
                    stepeast(sendingagentid);
                    break;
            }
        }
        else if ((bh_x_coordinate == bh_xmax_coordinate) && (bh_y_coordinate < bh_ymax_coordinate) && (bh_y_coordinate > bh_ymin_coordinate))
        /*
         * Left of East
         */
        {
            switch (bhbuckdirector)
            {
                case 0:
                    stepnorthwest(sendingagentid);
                    break;
                case 1:
                    stepnorth(sendingagentid);
                    break;
                case 3:
                    stepwest(sendingagentid);
                    break;
                case 5:
                    stepsouthwest(sendingagentid);
                    break;
                case 6:
                    stepsouth(sendingagentid);
                    break;
                default:           // Everything else - step west
                    stepwest(sendingagentid);
                    break;
            }
        }
        else if ((bh_y_coordinate == bh_ymax_coordinate) && (bh_x_coordinate < bh_xmax_coordinate) && (bh_x_coordinate > bh_xmin_coordinate))
        /*
         * Below North
         */
        {
            switch (bhbuckdirector)
            {
                case 3:
                    stepwest(sendingagentid);
                    break;
                case 4:
                    stepeast(sendingagentid);
                    break;
                case 5:
                    stepsouthwest(sendingagentid);
                    break;
                case 6:
                    stepsouth(sendingagentid);
                    break;
                case 7:
                    stepsoutheast(sendingagentid);
                    break;
                default:
                    stepsouth(sendingagentid);
                    break;
            }
        }
        else if ((bh_y_coordinate == bh_ymin_coordinate) && (bh_x_coordinate < bh_xmax_coordinate) && (bh_x_coordinate > bh_xmin_coordinate))
        /*
         * Above South
         */
        {
            switch (bhbuckdirector)
            {
                case 0:
                    stepnorthwest(sendingagentid);
                    break;
                case 1:
                    stepnorth(sendingagentid);
                    break;
                case 2:
                    stepnortheast(sendingagentid);
                    break;
                case 3:
                    stepwest(sendingagentid);
                    break;
                case 4:
                    stepeast(sendingagentid);
                    break;
                default:           // Everything else - step north
                    stepnorth(sendingagentid);
                    break;
            }
        }
        else
        {

            switch (bhbuckdirector)
            {
                case 0:
                    stepnorthwest(sendingagentid);
                    break;
                case 1:
                    /*
                     * bhstepsouth();
                     */
                    stepnorth(sendingagentid);
                    break;
                case 2:
                    stepnortheast(sendingagentid);
                    break;
                case 3:
                    stepwest(sendingagentid);
                    break;
                case 4:
                    /*
                     * bhstepwest();
                     */
                    stepeast(sendingagentid);
                    break;
                case 5:
                    stepsouthwest(sendingagentid);
                    break;
                case 6:
                    stepsouth(sendingagentid);
                    break;
                default:
                    /*
                     * bhstepsouthwest();
                     */
                    stepsoutheast(sendingagentid);
                    break;
            }
        }

        if ((bh_x_coordinate == w1_x_coordinate) && (bh_y_coordinate == w1_y_coordinate))
        {
            bh();
        }

        bh_grazecoordinate[bh_x_coordinate][bh_y_coordinate] = 1;
        bhGrazeGPSLog.add(new GPSLog(bh_x_coordinate, bh_y_coordinate));
        mapgridintolinearmem(sendingagentid);
        Platform.runLater(() ->
        {
            bhSeries.getData().add(new XYChart.Data(bh_x_coordinate - 0.5, bh_y_coordinate - 0.5));
            bhSeries.getData().remove(0);
        });
    }

    /*
     ** FHdr-beg
     * ****************************************************************** * *
     * Function name: mh * * Purpose: This function simulates the movement of a
     * territorial blackbuck. *	The relaxed pace of grazing is constant.However
     * the direction is random. * * FHdr-end
     * ******************************************************************
     */
    public void mh()
    {

        int sendingagentid = 5;
//
        if (mhroutinglock == routing.open)
        {
            prophetrouting(sendingagentid);
        }
        mhroutinglock = routing.shut;

        /*
         * Generate random number and use the last 3 bits to decide the
         * direction of buck
         */
//previous		int c1buckdirector = (rand()&7);
        int mhbuckdirector = ((rand.nextInt() >> rightshift) & 7);
//		//System.out.printf("%d \t %d% \n",c1buckdirector1,c1buckdirector);

        if ((mh_x_coordinate == mh_xmax_coordinate) && (mh_y_coordinate == mh_ymax_coordinate))
        /*
         * NE corner
         */
        {
            switch (mhbuckdirector)
            {
                case 3:
                    stepwest(sendingagentid);
                    break;
                case 5:
                    stepsouthwest(sendingagentid);
                    break;
                case 6:
                    stepsouth(sendingagentid);
                    break;
                default:
                    stepsouthwest(sendingagentid);
                    break;
            }
        }
        else if ((mh_x_coordinate == mh_xmin_coordinate) && (mh_y_coordinate == mh_ymin_coordinate))
        /*
         * SW corner
         */
        {
            switch (mhbuckdirector)
            {
                case 1:
                    stepnorth(sendingagentid);
                    break;
                case 2:
                    stepnortheast(sendingagentid);
                    break;
                case 4:
                    stepeast(sendingagentid);
                    break;
                default:
                    stepnortheast(sendingagentid);
                    break;
            }
        }
        else if ((mh_x_coordinate == mh_xmin_coordinate) && (mh_y_coordinate == mh_ymax_coordinate))
        /*
         * NW corner
         */
        {
            switch (mhbuckdirector)
            {
                case 4:
                    stepeast(sendingagentid);
                    break;
                case 6:
                    stepsouth(sendingagentid);
                    break;
                case 7:
                    stepsoutheast(sendingagentid);
                    break;
                default:
                    stepsoutheast(sendingagentid);
                    break;
            }
        }
        else if ((mh_x_coordinate == mh_xmax_coordinate) && (mh_y_coordinate == mh_ymin_coordinate))
        /*
         * SE corner
         */
        {
            switch (mhbuckdirector)
            {
                case 0:
                    stepnorthwest(sendingagentid);
                    break;
                case 1:
                    stepnorth(sendingagentid);
                    break;
                case 3:
                    stepwest(sendingagentid);
                    break;
                default:           // Everything else - step northwest
                    stepnorthwest(sendingagentid);
                    break;
            }
        }
        else if ((mh_x_coordinate == mh_xmin_coordinate) && (mh_y_coordinate < mh_ymax_coordinate) && (mh_y_coordinate > mh_ymin_coordinate))
        /*
         * Right of West
         */
        {
            switch (mhbuckdirector)
            {
                case 1:
                    stepnorth(sendingagentid);
                    break;
                case 2:
                    stepnortheast(sendingagentid);
                    break;
                case 4:
                    stepeast(sendingagentid);
                    break;
                case 6:
                    stepsouth(sendingagentid);
                    break;
                case 7:
                    stepsoutheast(sendingagentid);
                    break;
                default:           // Everything else - step
                    stepeast(sendingagentid);
                    break;
            }
        }
        else if ((mh_x_coordinate == mh_xmax_coordinate) && (mh_y_coordinate < mh_ymax_coordinate) && (mh_y_coordinate > mh_ymin_coordinate))
        /*
         * Left of East
         */
        {
            switch (mhbuckdirector)
            {
                case 0:
                    stepnorthwest(sendingagentid);
                    break;
                case 1:
                    stepnorth(sendingagentid);
                    break;
                case 3:
                    stepwest(sendingagentid);
                    break;
                case 5:
                    stepsouthwest(sendingagentid);
                    break;
                case 6:
                    stepsouth(sendingagentid);
                    break;
                default:           // Everything else - step west
                    stepwest(sendingagentid);
                    break;
            }
        }
        else if ((mh_y_coordinate == mh_ymax_coordinate) && (mh_x_coordinate < mh_xmax_coordinate) && (mh_x_coordinate > mh_xmin_coordinate))
        /*
         * Below North
         */
        {
            switch (mhbuckdirector)
            {
                case 3:
                    stepwest(sendingagentid);
                    break;
                case 4:
                    stepeast(sendingagentid);
                    break;
                case 5:
                    stepsouthwest(sendingagentid);
                    break;
                case 6:
                    stepsouth(sendingagentid);
                    break;
                case 7:
                    stepsoutheast(sendingagentid);
                    break;
                default:
                    stepsouth(sendingagentid);
                    break;
            }
        }
        else if ((mh_y_coordinate == mh_ymin_coordinate) && (mh_x_coordinate < mh_xmax_coordinate) && (mh_x_coordinate > mh_xmin_coordinate))
        /*
         * Above South
         */
        {
            switch (mhbuckdirector)
            {
                case 0:
                    stepnorthwest(sendingagentid);
                    break;
                case 1:
                    stepnorth(sendingagentid);
                    break;
                case 2:
                    stepnortheast(sendingagentid);
                    break;
                case 3:
                    stepwest(sendingagentid);
                    break;
                case 4:
                    stepeast(sendingagentid);
                    break;
                default:           // Everything else - step north
                    stepnorth(sendingagentid);
                    break;
            }
        }
        else
        {

            switch (mhbuckdirector)
            {
                case 0:
                    stepnorthwest(sendingagentid);
                    break;
                case 1:
                    /*
                     * mhstepsouth();
                     */
                    stepnorth(sendingagentid);
                    break;
                case 2:
                    stepnortheast(sendingagentid);
                    break;
                case 3:
                    stepwest(sendingagentid);
                    break;
                case 4:
                    /*
                     * mhstepwest();
                     */
                    stepeast(sendingagentid);
                    break;
                case 5:
                    stepsouthwest(sendingagentid);
                    break;
                case 6:
                    stepsouth(sendingagentid);
                    break;
                default:
                    stepsoutheast(sendingagentid);
                    break;
            }
        }

        if ((mh_x_coordinate == w1_x_coordinate) && (mh_y_coordinate == w1_y_coordinate))
        {
            mh();
        }
        mh_grazecoordinate[mh_x_coordinate][mh_y_coordinate] = 1;
        mhGrazeGPSLog.add(new GPSLog(mh_x_coordinate, mh_y_coordinate));
        mapgridintolinearmem(sendingagentid);
        Platform.runLater(() ->
        {
            mhSeries.getData().add(new XYChart.Data(mh_x_coordinate - 0.5, mh_y_coordinate - 0.5));
            mhSeries.getData().remove(0);
        });
    }

    /*
     ** FHdr-beg
     * ****************************************************************** * *
     * Function name: fh * * Purpose: This function simulates the movement of a
     * territorial blackbuck. *	The relaxed pace of grazing is constant.However
     * the direction is random. * * FHdr-end
     * ******************************************************************
     */
    public void fh()
    {

        int sendingagentid = 6;
//
        if (fhroutinglock == routing.open)
        {
            prophetrouting(sendingagentid);
        }
        fhroutinglock = routing.shut;


        /*
         * Generate random number and use the last 3 bits to decide the
         * direction of buck
         */
//previous		int c1buckdirector = (rand()&7);
        int fhbuckdirector = ((rand.nextInt() >> rightshift) & 7);
//		//System.out.printf("%d \t %d% \n",c1buckdirector1,c1buckdirector);

        if ((fh_x_coordinate == fh_xmax_coordinate) && (fh_y_coordinate == fh_ymax_coordinate))
        /*
         * NE corner
         */
        {
            switch (fhbuckdirector)
            {
                case 3:
                    stepwest(sendingagentid);
                    break;
                case 5:
                    stepsouthwest(sendingagentid);
                    break;
                case 6:
                    stepsouth(sendingagentid);
                    break;
                default:
                    stepsouthwest(sendingagentid);
                    break;
            }
        }
        else if ((fh_x_coordinate == fh_xmin_coordinate) && (fh_y_coordinate == fh_ymin_coordinate))
        /*
         * SW corner
         */
        {
            switch (fhbuckdirector)
            {
                case 1:
                    stepnorth(sendingagentid);
                    break;
                case 2:
                    stepnortheast(sendingagentid);
                    break;
                case 4:
                    stepeast(sendingagentid);
                    break;
                default:
                    stepnortheast(sendingagentid);
                    break;
            }
        }
        else if ((fh_x_coordinate == fh_xmin_coordinate) && (fh_y_coordinate == fh_ymax_coordinate))
        /*
         * NW corner
         */
        {
            switch (fhbuckdirector)
            {
                case 4:
                    stepeast(sendingagentid);
                    break;
                case 6:
                    stepsouth(sendingagentid);
                    break;
                case 7:
                    stepsoutheast(sendingagentid);
                    break;
                default:
                    stepsoutheast(sendingagentid);
                    break;
            }
        }
        else if ((fh_x_coordinate == fh_xmax_coordinate) && (fh_y_coordinate == fh_ymin_coordinate))
        /*
         * SE corner
         */
        {
            switch (fhbuckdirector)
            {
                case 0:
                    stepnorthwest(sendingagentid);
                    break;
                case 1:
                    stepnorth(sendingagentid);
                    break;
                case 3:
                    stepwest(sendingagentid);
                    break;
                default:           // Everything else - step northwest
                    stepnorthwest(sendingagentid);
                    break;
            }
        }
        else if ((fh_x_coordinate == fh_xmin_coordinate) && (fh_y_coordinate < fh_ymax_coordinate) && (fh_y_coordinate > fh_ymin_coordinate))
        /*
         * Right of West
         */
        {
            switch (fhbuckdirector)
            {
                case 1:
                    stepnorth(sendingagentid);
                    break;
                case 2:
                    stepnortheast(sendingagentid);
                    break;
                case 4:
                    stepeast(sendingagentid);
                    break;
                case 6:
                    stepsouth(sendingagentid);
                    break;
                case 7:
                    stepsoutheast(sendingagentid);
                    break;
                default:           // Everything else - step
                    stepeast(sendingagentid);
                    break;
            }
        }
        else if ((fh_x_coordinate == fh_xmax_coordinate) && (fh_y_coordinate < fh_ymax_coordinate) && (fh_y_coordinate > fh_ymin_coordinate))
        /*
         * Left of East
         */
        {
            switch (fhbuckdirector)
            {
                case 0:
                    stepnorthwest(sendingagentid);
                    break;
                case 1:
                    stepnorth(sendingagentid);
                    break;
                case 3:
                    stepwest(sendingagentid);
                    break;
                case 5:
                    stepsouthwest(sendingagentid);
                    break;
                case 6:
                    stepsouth(sendingagentid);
                    break;
                default:           // Everything else - step west
                    stepwest(sendingagentid);
                    break;
            }
        }
        else if ((fh_y_coordinate == fh_ymax_coordinate) && (fh_x_coordinate < fh_xmax_coordinate) && (fh_x_coordinate > fh_xmin_coordinate))
        /*
         * Below North
         */
        {
            switch (fhbuckdirector)
            {
                case 3:
                    stepwest(sendingagentid);
                    break;
                case 4:
                    stepeast(sendingagentid);
                    break;
                case 5:
                    stepsouthwest(sendingagentid);
                    break;
                case 6:
                    stepsouth(sendingagentid);
                    break;
                case 7:
                    stepsoutheast(sendingagentid);
                    break;
                default:
                    stepsouth(sendingagentid);
                    break;
            }
        }
        else if ((fh_y_coordinate == fh_ymin_coordinate) && (fh_x_coordinate < fh_xmax_coordinate) && (fh_x_coordinate > fh_xmin_coordinate))
        /*
         * Above South
         */
        {
            switch (fhbuckdirector)
            {
                case 0:
                    stepnorthwest(sendingagentid);
                    break;
                case 1:
                    stepnorth(sendingagentid);
                    break;
                case 2:
                    stepnortheast(sendingagentid);
                    break;
                case 3:
                    stepwest(sendingagentid);
                    break;
                case 4:
                    stepeast(sendingagentid);
                    break;
                default:           // Everything else - step north
                    stepnorth(sendingagentid);
                    break;
            }
        }
        else
        {

            switch (fhbuckdirector)
            {
                case 0:
                    stepnorthwest(sendingagentid);
                    break;
                case 1:
                    /*
                     * fhstepsouth();
                     */
                    stepnorth(sendingagentid);
                    break;
                case 2:
                    stepnortheast(sendingagentid);
                    break;
                case 3:
                    stepwest(sendingagentid);
                    break;
                case 4:
                    /*
                     * fhstepwest();
                     */
                    stepeast(sendingagentid);
                    break;
                case 5:
                    stepsouthwest(sendingagentid);
                    break;
                case 6:
                    stepsouth(sendingagentid);
                    break;
                default:
                    /*
                     * fhstepsouthwest();
                     */
                    stepsoutheast(sendingagentid);
                    break;
            }
        }

        if (((fh_x_coordinate == w1_x_coordinate) && (fh_y_coordinate == w1_y_coordinate)) || (fh_x_coordinate == bh_x_coordinate) && (fh_y_coordinate == bh_y_coordinate))
        {
            fh();
        }

        fh_grazecoordinate[fh_x_coordinate][fh_y_coordinate] = 1;
        fhGrazeGPSLog.add(new GPSLog(fh_x_coordinate, fh_y_coordinate));
        mapgridintolinearmem(sendingagentid);
        Platform.runLater(() ->
        {
            fhSeries.getData().add(new XYChart.Data(fh_x_coordinate - 0.5, fh_y_coordinate - 0.5));
            fhSeries.getData().remove(0);
        });

    }

    /*
     ** FHdr-beg
     * ****************************************************************** * *
     * Function name: nf * * Purpose: This function simulates the movement of a
     * territorial blackbuck. *	The relaxed pace of grazing is constant.However
     * the direction is random. * * FHdr-end
     * ******************************************************************
     */
    public void nf()
    {
        int sendingagentid = 7;
        if (nfroutinglock == routing.open)
        {
            prophetrouting(sendingagentid);
        }
        nfroutinglock = routing.shut;
        int nfbuckdirector = ((rand.nextInt() >> rightshift) & 7);

        if ((nf_x_coordinate == nf_xmax_coordinate) && (nf_y_coordinate == nf_ymax_coordinate))
        /*
         * NE corner
         */
        {
            switch (nfbuckdirector)
            {
                case 3:
                    stepwest(sendingagentid);
                    break;
                case 5:
                    stepsouthwest(sendingagentid);
                    break;
                case 6:
                    stepsouth(sendingagentid);
                    break;
                default:
                    stepsouthwest(sendingagentid);
                    break;
            }
        }
        else if ((nf_x_coordinate == nf_xmin_coordinate) && (nf_y_coordinate == nf_ymin_coordinate))
        /*
         * SW corner
         */
        {
            switch (nfbuckdirector)
            {
                case 1:
                    stepnorth(sendingagentid);
                    break;
                case 2:
                    stepnortheast(sendingagentid);
                    break;
                case 4:
                    stepeast(sendingagentid);
                    break;
                default:
                    stepnortheast(sendingagentid);
                    break;
            }
        }
        else if ((nf_x_coordinate == nf_xmin_coordinate) && (nf_y_coordinate == nf_ymax_coordinate))
        /*
         * NW corner
         */
        {
            switch (nfbuckdirector)
            {
                case 4:
                    stepeast(sendingagentid);
                    break;
                case 6:
                    stepsouth(sendingagentid);
                    break;
                case 7:
                    stepsoutheast(sendingagentid);
                    break;
                default:
                    stepsoutheast(sendingagentid);
                    break;
            }
        }
        else if ((nf_x_coordinate == nf_xmax_coordinate) && (nf_y_coordinate == nf_ymin_coordinate))
        /*
         * SE corner
         */
        {
            switch (nfbuckdirector)
            {
                case 0:
                    stepnorthwest(sendingagentid);
                    break;
                case 1:
                    stepnorth(sendingagentid);
                    break;
                case 3:
                    stepwest(sendingagentid);
                    break;
                default:           // Everything else - step northwest
                    stepnorthwest(sendingagentid);
                    break;
            }
        }
        else if ((nf_x_coordinate == nf_xmin_coordinate) && (nf_y_coordinate < nf_ymax_coordinate) && (nf_y_coordinate > nf_ymin_coordinate))
        /*
         * Right of West
         */
        {
            switch (nfbuckdirector)
            {
                case 1:
                    stepnorth(sendingagentid);
                    break;
                case 2:
                    stepnortheast(sendingagentid);
                    break;
                case 4:
                    stepeast(sendingagentid);
                    break;
                case 6:
                    stepsouth(sendingagentid);
                    break;
                case 7:
                    stepsoutheast(sendingagentid);
                    break;
                default:           // Everything else - step
                    stepeast(sendingagentid);
                    break;
            }
        }
        else if ((nf_x_coordinate == nf_xmax_coordinate) && (nf_y_coordinate < nf_ymax_coordinate) && (nf_y_coordinate > nf_ymin_coordinate))
        /*
         * Left of East
         */
        {
            switch (nfbuckdirector)
            {
                case 0:
                    stepnorthwest(sendingagentid);
                    break;
                case 1:
                    stepnorth(sendingagentid);
                    break;
                case 3:
                    stepwest(sendingagentid);
                    break;
                case 5:
                    stepsouthwest(sendingagentid);
                    break;
                case 6:
                    stepsouth(sendingagentid);
                    break;
                default:           // Everything else - step west
                    stepwest(sendingagentid);
                    break;
            }
        }
        else if ((nf_y_coordinate == nf_ymax_coordinate) && (nf_x_coordinate < nf_xmax_coordinate) && (nf_x_coordinate > nf_xmin_coordinate))
        /*
         * Below North
         */
        {
            switch (nfbuckdirector)
            {
                case 3:
                    stepwest(sendingagentid);
                    break;
                case 4:
                    stepeast(sendingagentid);
                    break;
                case 5:
                    stepsouthwest(sendingagentid);
                    break;
                case 6:
                    stepsouth(sendingagentid);
                    break;
                case 7:
                    stepsoutheast(sendingagentid);
                    break;
                default:
                    stepsouth(sendingagentid);
                    break;
            }
        }
        else if ((nf_y_coordinate == nf_ymin_coordinate) && (nf_x_coordinate < nf_xmax_coordinate) && (nf_x_coordinate > nf_xmin_coordinate))
        /*
         * Above South
         */
        {
            switch (nfbuckdirector)
            {
                case 0:
                    stepnorthwest(sendingagentid);
                    break;
                case 1:
                    stepnorth(sendingagentid);
                    break;
                case 2:
                    stepnortheast(sendingagentid);
                    break;
                case 3:
                    stepwest(sendingagentid);
                    break;
                case 4:
                    stepeast(sendingagentid);
                    break;
                default:           // Everything else - step north
                    stepnorth(sendingagentid);
                    break;
            }
        }
        else
        {

            switch (nfbuckdirector)
            {
                case 0:
                    stepnorthwest(sendingagentid);
                    break;
                case 1:
                    /*
                     * nfstepsouth();
                     */
                    stepnorth(sendingagentid);
                    break;
                case 2:
                    stepnortheast(sendingagentid);
                    break;
                case 3:
                    stepwest(sendingagentid);
                    break;
                case 4:
                    /*
                     * nfstepwest();
                     */
                    stepeast(sendingagentid);
                    break;
                case 5:
                    stepsouthwest(sendingagentid);
                    break;
                case 6:
                    stepsouth(sendingagentid);
                    break;
                default:
                    /*
                     * nfstepsouthwest();
                     */
                    stepsoutheast(sendingagentid);
                    break;
            }
        }

        if (((nf_x_coordinate == w1_x_coordinate) && (nf_y_coordinate == w1_y_coordinate)) || ((nf_x_coordinate == bh_x_coordinate) && (nf_y_coordinate == bh_y_coordinate))
                || ((nf_x_coordinate == t1_x_coordinate) && (nf_y_coordinate == t1_y_coordinate)) || ((nf_x_coordinate == t2_x_coordinate) && (nf_y_coordinate == t2_y_coordinate)) || ((nf_x_coordinate == t3_x_coordinate) && (nf_y_coordinate == t3_y_coordinate)))
        {
            nf();
        }

        nf_grazecoordinate[nf_x_coordinate][nf_y_coordinate] = 1;
        nfGrazeGPSLog.add(new GPSLog(nf_x_coordinate, nf_y_coordinate));
        mapgridintolinearmem(sendingagentid);
        Platform.runLater(() ->
        {
            nfSeries.getData().add(new XYChart.Data(nf_x_coordinate - 0.5, nf_y_coordinate - 0.5));
            nfSeries.getData().remove(0);
        });
    }

    /*
     ** FHdr-beg
     * ****************************************************************** * *
     * Function name: mapgridintolinearmem * * Purpose: This function simulates
     * the data pull operation from sensor node s5 to cow c1_ *	Duplicate
     * copying of message packets is avoided_ The copied message *	packet is
     * erased from source so as to release memory_ * FHdr-end
     * ******************************************************************
     */
    void mapgridintolinearmem(int mapagentid)
    {
        switch (mapagentid)
        {
            case 1: //
            {
                int flag;
                for (int bucklocationx = 0; bucklocationx < MAX_X_GRID; bucklocationx++)
                {
                    for (int bucklocationy = 0; bucklocationy < MAX_Y_GRID; bucklocationy++)
                    {
                        if (t1_grazecoordinate[bucklocationx][bucklocationy] == 1)
                        {
                            //		//System.out.printf("bucklocationx %d	\t	bucklocationy %d	\n",bucklocationx,bucklocationy);
                            int tempmessage = bucklocationx << 6;//Bits 10,9,8,7,6 are for location x
                            tempmessage = tempmessage | bucklocationy;//Bits 5,4,3,2,1,0 are for location y
                            tempmessage = tempmessage | (t1_agentid << 11);//Bits 15,14,13,12,11 are for Buck ID (for t1 is 00001)
                            for (int loop2 = 0; loop2 < BUCK_MAX_PACKETS; loop2++)
                            {
                                if (t1_message[loop2] == 0)
                                {
                                    flag = 0;
                                    for (int loop3 = 0; loop3 < BUCK_MAX_PACKETS; loop3++)
                                    {
                                        if (tempmessage == t1_message[loop3])
                                        {
                                            flag++;
                                        }
                                    }
                                    if (flag == 0)
                                    {
                                        t1_message[loop2] = tempmessage;
                                        /*
                                         * Copy data
                                         */
                                        tempmessage = 0;
                                        //	s5message[sringcount(loop1)]=0;	/* Erase it for First Contact Routing */
//												//System.out.printf("t1 \n");
//												//System.out.printf("bucklocationx %d	\t	bucklocationy %d	\n",bucklocationx,bucklocationy);
                                    }

                                }

                            }

                        }

                    }
                }
            }
//				  //System.out.printf("T1 \n");
            break;//	End of case 1_
            case 2: //
            {
                int flag;
                for (int bucklocationx = 0; bucklocationx < MAX_X_GRID; bucklocationx++)
                {
                    for (int bucklocationy = 0; bucklocationy < MAX_Y_GRID; bucklocationy++)
                    {
                        if (t2_grazecoordinate[bucklocationx][bucklocationy] == 1)
                        {
                            int tempmessage = bucklocationx << 6;//Bits 10,9,8,7,6 are for location x
                            tempmessage = tempmessage | bucklocationy;//Bits 5,4,3,2,1,0 are for location y
                            tempmessage = tempmessage | (t2_agentid << 11);//Bits 15,14,13,12,11 are for Buck ID (for t1 is 00010)
                            for (int loop2 = 0; loop2 < BUCK_MAX_PACKETS; loop2++)
                            {
                                if (t2_message[loop2] == 0)
                                {
                                    flag = 0;
                                    for (int loop3 = 0; loop3 < BUCK_MAX_PACKETS; loop3++)
                                    {
                                        if (tempmessage == t2_message[loop3])
                                        {
                                            flag++;
                                        }
                                    }
                                    if (flag == 0)
                                    {
                                        t2_message[loop2] = tempmessage;
                                        /*
                                         * Copy data
                                         */
                                        tempmessage = 0;
                                        //	s5message[sringcount(loop1)]=0;	/* Erase it for First Contact Routing */
//												//System.out.printf("t2 \n");
                                    }

                                }

                            }

                        }

                    }
                }
            }
//				  //System.out.printf("T2 \n");
            break;//	End of case 2_
            case 3: //
            {
                int flag;
                for (int bucklocationx = 0; bucklocationx < MAX_X_GRID; bucklocationx++)
                {
                    for (int bucklocationy = 0; bucklocationy < MAX_Y_GRID; bucklocationy++)
                    {
                        if (t3_grazecoordinate[bucklocationx][bucklocationy] == 1)
                        {
                            int tempmessage = bucklocationx << 6;//Bits 10,9,8,7,6 are for location x
                            tempmessage = tempmessage | bucklocationy;//Bits 5,4,3,2,1,0 are for location y
                            tempmessage = tempmessage | (t3_agentid << 11);//Bits 15,14,13,12,11 are for Buck ID (for t1 is 00010)
                            for (int loop2 = 0; loop2 < BUCK_MAX_PACKETS; loop2++)
                            {
                                if (t3_message[loop2] == 0)
                                {
                                    flag = 0;
                                    for (int loop3 = 0; loop3 < BUCK_MAX_PACKETS; loop3++)
                                    {
                                        if (tempmessage == t3_message[loop3])
                                        {
                                            flag++;
                                        }
                                    }
                                    if (flag == 0)
                                    {
                                        t3_message[loop2] = tempmessage;
                                        /*
                                         * Copy data
                                         */
                                        tempmessage = 0;
                                        //	s5message[sringcount(loop1)]=0;	/* Erase it for First Contact Routing */
//												//System.out.printf("t3 \n");
                                    }

                                }

                            }

                        }

                    }
                }
            }
//				  //System.out.printf("T3 \n");
            break;//	End of case 3_
            case 4: //
            {
                int flag;
                for (int bucklocationx = 0; bucklocationx < MAX_X_GRID; bucklocationx++)
                {
                    for (int bucklocationy = 0; bucklocationy < MAX_Y_GRID; bucklocationy++)
                    {
                        if (bh_grazecoordinate[bucklocationx][bucklocationy] == 1)
                        {
                            int tempmessage = bucklocationx << 6;//Bits 10,9,8,7,6 are for location x
                            tempmessage = tempmessage | bucklocationy;//Bits 5,4,3,2,1,0 are for location y
                            tempmessage = tempmessage | (bh_agentid << 11);//Bits 15,14,13,12,11 are for Buck ID (for t1 is 00010)
                            for (int loop2 = 0; loop2 < BUCK_MAX_PACKETS; loop2++)
                            {
                                if (bh_message[loop2] == 0)
                                {
                                    flag = 0;
                                    for (int loop3 = 0; loop3 < BUCK_MAX_PACKETS; loop3++)
                                    {
                                        if (tempmessage == bh_message[loop3])
                                        {
                                            flag++;
                                        }
                                    }
                                    if (flag == 0)
                                    {
                                        bh_message[loop2] = tempmessage;
                                        /*
                                         * Copy data
                                         */
                                        tempmessage = 0;
                                        //	s5message[sringcount(loop1)]=0;	/* Erase it for First Contact Routing */
//												//System.out.printf("bh \n");
                                    }

                                }

                            }

                        }

                    }
                }
            }
//				  //System.out.printf("BH \n");
            break;//	End of case 4_
            case 5: //
            {
                int flag;
                for (int bucklocationx = 0; bucklocationx < MAX_X_GRID; bucklocationx++)
                {
                    for (int bucklocationy = 0; bucklocationy < MAX_Y_GRID; bucklocationy++)
                    {
                        if (mh_grazecoordinate[bucklocationx][bucklocationy] == 1)
                        {
                            int tempmessage = bucklocationx << 6;//Bits 10,9,8,7,6 are for location x
                            tempmessage = tempmessage | bucklocationy;//Bits 5,4,3,2,1,0 are for location y
                            tempmessage = tempmessage | (mh_agentid << 11);//Bits 15,14,13,12,11 are for Buck ID (for t1 is 00010)
                            for (int loop2 = 0; loop2 < BUCK_MAX_PACKETS; loop2++)
                            {
                                if (mh_message[loop2] == 0)
                                {
                                    flag = 0;
                                    for (int loop3 = 0; loop3 < BUCK_MAX_PACKETS; loop3++)
                                    {
                                        if (tempmessage == mh_message[loop3])
                                        {
                                            flag++;
                                        }
                                    }
                                    if (flag == 0)
                                    {
                                        mh_message[loop2] = tempmessage;
                                        /*
                                         * Copy data
                                         */
                                        tempmessage = 0;
                                        //	s5message[sringcount(loop1)]=0;	/* Erase it for First Contact Routing */
//												//System.out.printf("mh \n");
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
                for (int bucklocationx = 0; bucklocationx < MAX_X_GRID; bucklocationx++)
                {
                    for (int bucklocationy = 0; bucklocationy < MAX_Y_GRID; bucklocationy++)
                    {
                        if (fh_grazecoordinate[bucklocationx][bucklocationy] == 1)
                        {
                            int tempmessage = bucklocationx << 6;//Bits 10,9,8,7,6 are for location x
                            tempmessage = tempmessage | bucklocationy;//Bits 5,4,3,2,1,0 are for location y
                            tempmessage = tempmessage | (fh_agentid << 11);//Bits 15,14,13,12,11 are for Buck ID (for t1 is 00010)
                            for (int loop2 = 0; loop2 < BUCK_MAX_PACKETS; loop2++)
                            {
                                if (fh_message[loop2] == 0)
                                {
                                    flag = 0;
                                    for (int loop3 = 0; loop3 < BUCK_MAX_PACKETS; loop3++)
                                    {
                                        if (tempmessage == fh_message[loop3])
                                        {
                                            flag++;
                                        }
                                    }
                                    if (flag == 0)
                                    {
                                        fh_message[loop2] = tempmessage;
                                        /*
                                         * Copy data
                                         */
                                        tempmessage = 0;
                                        //	s5message[sringcount(loop1)]=0;	/* Erase it for First Contact Routing */
//												//System.out.printf("fh \n");
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
                for (int bucklocationx = 0; bucklocationx < MAX_X_GRID; bucklocationx++)
                {
                    for (int bucklocationy = 0; bucklocationy < MAX_Y_GRID; bucklocationy++)
                    {
                        if (nf_grazecoordinate[bucklocationx][bucklocationy] == 1)
                        {
                            int tempmessage = bucklocationx << 6;//Bits 10,9,8,7,6 are for location x
                            tempmessage = tempmessage | bucklocationy;//Bits 5,4,3,2,1,0 are for location y
                            tempmessage = tempmessage | (nf_agentid << 11);//Bits 15,14,13,12,11 are for Buck ID (for t1 is 00010)
                            for (int loop2 = 0; loop2 < BUCK_MAX_PACKETS; loop2++)
                            {
                                if (nf_message[loop2] == 0)
                                {
                                    flag = 0;
                                    for (int loop3 = 0; loop3 < BUCK_MAX_PACKETS; loop3++)
                                    {
                                        if (tempmessage == nf_message[loop3])
                                        {
                                            flag++;
                                        }
                                    }
                                    if (flag == 0)
                                    {
                                        nf_message[loop2] = tempmessage;
                                        /*
                                         * Copy data
                                         */
                                        tempmessage = 0;
                                        //	s5message[sringcount(loop1)]=0;	/* Erase it for First Contact Routing */
//												//System.out.printf("nf \n");
                                    }

                                }

                            }

                        }

                    }
                }
            }
            break;//	End of case 7_
            default:            //
                //System.out.printf("Invalid Call \n");
                break;
        }

    }

    /*
     ** FHdr-beg
     * ****************************************************************** * *
     * Function name: prophetrouting * * Purpose: This function simulates the
     * buck moving north west based on the *	random decision by t1buckdirector,
     * after ensuring that the new step *	indeed leads to a fresh grazing spot.
     * * FHdr-end
     * ******************************************************************
     */

    void prophetrouting(int sendingagentid)
	{
	float receivingagentprobability;
	float[] probability = {0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f};
	float[] storeprobability = {0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f};
	switch(sendingagentid)
	{
	case 1:
		{
			if((t1_x_coordinate == t2_x_coordinate)&&(t1_y_coordinate == t2_y_coordinate))
			{
//															P_(A,C) = MAX( P_(A,C)_old,		     P_(A,B)        * P_(B,C)_recv	  * beta)
				storeprobability[1]=probability[1]=ptransitive[0][1]=max((ptransitive[0][1]),(ptransitive[0][7]*ptransitive[1][7]*beta));
			}
			if((t1_x_coordinate == t3_x_coordinate)&&(t1_y_coordinate == t3_y_coordinate))
			{
				storeprobability[2]=probability[2]=ptransitive[0][2]=max((ptransitive[0][2]),(ptransitive[0][7]*ptransitive[2][7]*beta));
			}
			if((t1_x_coordinate == bh_x_coordinate)&&(t1_y_coordinate == bh_y_coordinate))
			{
				storeprobability[3]=probability[3]=ptransitive[0][3]=max((ptransitive[0][3]),(ptransitive[0][7]*ptransitive[3][7]*beta));
			}
			if((t1_x_coordinate == mh_x_coordinate)&&(t1_y_coordinate == mh_y_coordinate))
			{
				storeprobability[4]=probability[4]=ptransitive[0][4]=max((ptransitive[0][4]),(ptransitive[0][7]*ptransitive[4][7]*beta));
			}
			if((t1_x_coordinate == fh_x_coordinate)&&(t1_y_coordinate == fh_y_coordinate))
			{
				storeprobability[5]=probability[5]=ptransitive[0][5]=max((ptransitive[0][5]),(ptransitive[0][7]*ptransitive[5][7]*beta));
			}
			if((t1_x_coordinate == nf_x_coordinate)&&(t1_y_coordinate == nf_y_coordinate))
			{
				storeprobability[6]=probability[6]=ptransitive[0][6]=max((ptransitive[0][6]),(ptransitive[0][7]*ptransitive[6][7]*beta));
			}
			if((t1_x_coordinate == w1_x_coordinate)&&(t1_y_coordinate == w1_y_coordinate))
			{
				storeprobability[7]=probability[7]=ptransitive[0][7]=ptransitive[0][7]+((1-ptransitive[0][7])*pinit);
				t1_ageing=0;
			}
			else
			{
				t1_ageing++;
			}
			if(t1_ageing==k)
			{
				ptransitive[0][7]=(float) (ptransitive[0][7]*pow(gamma,k));
				t1_ageing=0;
			}


		}
		break;
	case 2:
		{
			if((t2_x_coordinate == t1_x_coordinate)&&(t2_y_coordinate == t1_y_coordinate))
			{
//															P_(A,C) = MAX( P_(A,C)_old,		     P_(A,B)        * P_(B,C)_recv	  * beta)
				storeprobability[0]=probability[0]=ptransitive[1][0]=max((ptransitive[1][0]),(ptransitive[1][7]*ptransitive[0][7]*beta));
			}
			if((t2_x_coordinate == t3_x_coordinate)&&(t2_y_coordinate == t3_y_coordinate))
			{
				storeprobability[2]=probability[2]=ptransitive[1][2]=max((ptransitive[1][2]),(ptransitive[1][7]*ptransitive[2][7]*beta));
			}
			if((t2_x_coordinate == bh_x_coordinate)&&(t2_y_coordinate == bh_y_coordinate))
			{
				storeprobability[3]=probability[3]=ptransitive[1][3]=max((ptransitive[1][3]),(ptransitive[1][7]*ptransitive[3][7]*beta));
			}
			if((t2_x_coordinate == mh_x_coordinate)&&(t2_y_coordinate == mh_y_coordinate))
			{
				storeprobability[4]=probability[4]=ptransitive[1][4]=max((ptransitive[1][4]),(ptransitive[1][7]*ptransitive[4][7]*beta));
			}
			if((t2_x_coordinate == fh_x_coordinate)&&(t2_y_coordinate == fh_y_coordinate))
			{
				storeprobability[5]=probability[5]=ptransitive[1][5]=max((ptransitive[1][5]),(ptransitive[1][7]*ptransitive[5][7]*beta));
			}
			if((t2_x_coordinate == nf_x_coordinate)&&(t2_y_coordinate == nf_y_coordinate))
			{
				storeprobability[6]=probability[6]=ptransitive[1][6]=max((ptransitive[1][7]),(ptransitive[1][7]*ptransitive[6][7]*beta));
			}
			if((t2_x_coordinate == w1_x_coordinate)&&(t2_y_coordinate == w1_y_coordinate))
			{
				storeprobability[7]=probability[7]=ptransitive[1][7]=ptransitive[1][7]+((1-ptransitive[1][7])*pinit);
				t2_ageing=0;
			}
			else
			{
				t2_ageing++;
			}
			if(t2_ageing==k)
			{
				ptransitive[1][7]=(float) (ptransitive[1][7]*pow(gamma,k));
				t2_ageing=0;
			}
		}
		break;
	case 3:
		{
			if((t3_x_coordinate == t1_x_coordinate)&&(t3_y_coordinate == t1_y_coordinate))
			{
//															P_(A,C) = MAX( P_(A,C)_old,		     P_(A,B)        * P_(B,C)_recv	  * beta)
				storeprobability[0]=probability[0]=ptransitive[2][0]=max((ptransitive[2][0]),(ptransitive[2][7]*ptransitive[0][7]*beta));
			}
			if((t3_x_coordinate == t2_x_coordinate)&&(t3_y_coordinate == t2_y_coordinate))
			{
				storeprobability[1]=probability[1]=ptransitive[2][1]=max((ptransitive[2][1]),(ptransitive[2][7]*ptransitive[1][7]*beta));
			}
			if((t3_x_coordinate == bh_x_coordinate)&&(t3_y_coordinate == bh_y_coordinate))
			{
				storeprobability[3]=probability[3]=ptransitive[2][3]=max((ptransitive[2][3]),(ptransitive[2][7]*ptransitive[3][7]*beta));
			}
			if((t3_x_coordinate == mh_x_coordinate)&&(t3_y_coordinate == mh_y_coordinate))
			{
				storeprobability[4]=probability[4]=ptransitive[2][4]=max((ptransitive[2][4]),(ptransitive[2][7]*ptransitive[4][7]*beta));
			}
			if((t3_x_coordinate == fh_x_coordinate)&&(t3_y_coordinate == fh_y_coordinate))
			{
				storeprobability[5]=probability[5]=ptransitive[2][5]=max((ptransitive[2][5]),(ptransitive[2][7]*ptransitive[5][7]*beta));
			}
			if((t3_x_coordinate == nf_x_coordinate)&&(t3_y_coordinate == nf_y_coordinate))
			{
				storeprobability[6]=probability[6]=ptransitive[2][6]=max((ptransitive[2][6]),(ptransitive[2][7]*ptransitive[6][7]*beta));
			}
			if((t3_x_coordinate == w1_x_coordinate)&&(t3_y_coordinate == w1_y_coordinate))
			{
				storeprobability[7]=probability[7]=ptransitive[2][7]=ptransitive[2][7]+((1-ptransitive[2][7])*pinit);
				t3_ageing=0;
			}
			else
			{
				t3_ageing++;
			}
			if(t3_ageing==k)
			{
				ptransitive[2][7]=(float) (ptransitive[2][7]*pow(gamma,k));
				t3_ageing=0;
			}
		}
		break;
	case 4:
		{
			if((bh_x_coordinate == t1_x_coordinate)&&(bh_y_coordinate == t1_y_coordinate))
			{
//															P_(A,C) = MAX( P_(A,C)_old,		     P_(A,B)        * P_(B,C)_recv	  * beta)
				storeprobability[0]=probability[0]=ptransitive[3][0]=max((ptransitive[3][0]),(ptransitive[3][7]*ptransitive[0][7]*beta));
			}
			if((bh_x_coordinate == t2_x_coordinate)&&(bh_y_coordinate == t2_y_coordinate))
			{
				storeprobability[1]=probability[1]=ptransitive[3][1]=max((ptransitive[3][1]),(ptransitive[3][7]*ptransitive[1][7]*beta));
			}
			if((bh_x_coordinate == t3_x_coordinate)&&(bh_y_coordinate == t3_y_coordinate))
			{
				storeprobability[2]=probability[2]=ptransitive[3][2]=max((ptransitive[3][2]),(ptransitive[3][7]*ptransitive[2][7]*beta));
			}
			if((bh_x_coordinate == mh_x_coordinate)&&(bh_y_coordinate == mh_y_coordinate))
			{
				storeprobability[4]=probability[4]=ptransitive[3][4]=max((ptransitive[3][4]),(ptransitive[3][7]*ptransitive[4][7]*beta));
			}
			if((bh_x_coordinate == fh_x_coordinate)&&(bh_y_coordinate == fh_y_coordinate))
			{
				storeprobability[5]=probability[5]=ptransitive[3][5]=max((ptransitive[3][7]),(ptransitive[3][7]*ptransitive[5][7]*beta));
			}
			if((bh_x_coordinate == nf_x_coordinate)&&(bh_y_coordinate == nf_y_coordinate))
			{
				storeprobability[6]=probability[6]=ptransitive[3][6]=max((ptransitive[3][7]),(ptransitive[3][7]*ptransitive[6][7]*beta));
			}
			if((bh_x_coordinate == w1_x_coordinate)&&(bh_y_coordinate == w1_y_coordinate))
			{
				storeprobability[7]=probability[7]=ptransitive[3][7]=ptransitive[3][7]+((1-ptransitive[3][7])*pinit);
				bh_ageing=0;
			}
			else
			{
				bh_ageing++;
			}
			if(bh_ageing==k)
			{
				ptransitive[3][7]=(float) (ptransitive[3][7]*pow(gamma,k));
				bh_ageing=0;
			}
		}
		break;
	case 5:
		{
			if((mh_x_coordinate == t1_x_coordinate)&&(mh_y_coordinate == t1_y_coordinate))
			{
//															P_(A,C) = MAX( P_(A,C)_old,		     P_(A,B)        * P_(B,C)_recv	  * beta)
				storeprobability[0]=probability[0]=ptransitive[4][0]=max((ptransitive[4][0]),(ptransitive[4][7]*ptransitive[0][7]*beta));
			}
			if((mh_x_coordinate == t2_x_coordinate)&&(mh_y_coordinate == t2_y_coordinate))
			{
				storeprobability[1]=probability[1]=ptransitive[4][1]=max((ptransitive[4][1]),(ptransitive[4][7]*ptransitive[1][7]*beta));
			}
			if((mh_x_coordinate == t3_x_coordinate)&&(mh_y_coordinate == t3_y_coordinate))
			{
				storeprobability[2]=probability[2]=ptransitive[4][2]=max((ptransitive[4][2]),(ptransitive[4][7]*ptransitive[2][7]*beta));
			}
			if((mh_x_coordinate == bh_x_coordinate)&&(mh_y_coordinate == bh_y_coordinate))
			{
				storeprobability[3]=probability[3]=ptransitive[4][3]=max((ptransitive[4][3]),(ptransitive[4][7]*ptransitive[3][7]*beta));
			}
			if((mh_x_coordinate == fh_x_coordinate)&&(mh_y_coordinate == fh_y_coordinate))
			{
				storeprobability[5]=probability[5]=ptransitive[4][5]=max((ptransitive[4][5]),(ptransitive[4][7]*ptransitive[5][7]*beta));
			}
			if((mh_x_coordinate == nf_x_coordinate)&&(mh_y_coordinate == nf_y_coordinate))
			{
				storeprobability[6]=probability[6]=ptransitive[4][6]=max((ptransitive[4][7]),(ptransitive[4][7]*ptransitive[6][7]*beta));
			}
			if((mh_x_coordinate == w1_x_coordinate)&&(mh_y_coordinate == w1_y_coordinate))
			{
				storeprobability[7]=probability[7]=ptransitive[4][7]=ptransitive[4][7]+((1-ptransitive[4][7])*pinit);
				mh_ageing=0;
			}
			else
			{
				mh_ageing++;
			}
			if(mh_ageing==k)
			{
				ptransitive[4][7]=(float) (ptransitive[4][7]*pow(gamma,k));
				mh_ageing=0;
			}
		}
		break;
	case 6:
		{
			if((fh_x_coordinate == t1_x_coordinate)&&(fh_y_coordinate == t1_y_coordinate))
			{
//															P_(A,C) = MAX( P_(A,C)_old,		     P_(A,B)        * P_(B,C)_recv	  * beta)
				storeprobability[0]=probability[0]=ptransitive[5][0]=max((ptransitive[5][0]),(ptransitive[5][7]*ptransitive[0][7]*beta));
			}
			if((fh_x_coordinate == t2_x_coordinate)&&(fh_y_coordinate == t2_y_coordinate))
			{
				storeprobability[1]=probability[1]=ptransitive[5][1]=max((ptransitive[5][1]),(ptransitive[5][7]*ptransitive[1][7]*beta));
			}
			if((fh_x_coordinate == t3_x_coordinate)&&(fh_y_coordinate == t3_y_coordinate))
			{
				storeprobability[2]=probability[2]=ptransitive[5][2]=max((ptransitive[5][2]),(ptransitive[5][7]*ptransitive[2][7]*beta));
			}
			if((fh_x_coordinate == bh_x_coordinate)&&(fh_y_coordinate == bh_y_coordinate))
			{
				storeprobability[3]=probability[3]=ptransitive[5][3]=max((ptransitive[5][3]),(ptransitive[5][7]*ptransitive[3][7]*beta));
			}
			if((fh_x_coordinate == mh_x_coordinate)&&(fh_y_coordinate == mh_y_coordinate))
			{
				storeprobability[4]=probability[4]=ptransitive[5][4]=max((ptransitive[5][4]),(ptransitive[5][7]*ptransitive[4][7]*beta));
			}
			if((fh_x_coordinate == nf_x_coordinate)&&(fh_y_coordinate == nf_y_coordinate))
			{
				storeprobability[6]=probability[6]=ptransitive[5][6]=max((ptransitive[5][6]),(ptransitive[5][7]*ptransitive[6][7]*beta));
			}
			if((fh_x_coordinate == w1_x_coordinate)&&(fh_y_coordinate == w1_y_coordinate))
			{
				storeprobability[7]=probability[7]=ptransitive[5][7]=ptransitive[5][7]+((1-ptransitive[5][7])*pinit);
				fh_ageing=0;
			}
			else
			{
				fh_ageing++;
			}
			if(fh_ageing==k)
			{
				ptransitive[5][7]=(float) (ptransitive[5][7]*pow(gamma,k));
				fh_ageing=0;
			}
		}
		break;
	case 7:
		{
			if((nf_x_coordinate == t1_x_coordinate)&&(nf_y_coordinate == t1_y_coordinate))
			{
//															P_(A,C) = MAX( P_(A,C)_old,		     P_(A,B)        * P_(B,C)_recv	  * beta )
				storeprobability[0]=probability[0]=ptransitive[6][0]=max((ptransitive[6][0]),(ptransitive[6][7]*ptransitive[0][7]*beta));
			}
			if((nf_x_coordinate == t2_x_coordinate)&&(nf_y_coordinate == t2_y_coordinate))
			{
				storeprobability[1]=probability[1]=ptransitive[6][1]=max((ptransitive[6][1]),(ptransitive[6][7]*ptransitive[1][7]*beta));
			}
			if((nf_x_coordinate == t3_x_coordinate)&&(nf_y_coordinate == t3_y_coordinate))
			{
				storeprobability[2]=probability[2]=ptransitive[6][2]=max((ptransitive[6][2]),(ptransitive[6][7]*ptransitive[2][7]*beta));
			}
			if((nf_x_coordinate == bh_x_coordinate)&&(nf_y_coordinate == bh_y_coordinate))
			{
				storeprobability[3]=probability[3]=ptransitive[6][3]=max((ptransitive[6][3]),(ptransitive[6][7]*ptransitive[3][7]*beta));
			}
			if((nf_x_coordinate == mh_x_coordinate)&&(nf_y_coordinate == mh_y_coordinate))
			{
				storeprobability[4]=probability[4]=ptransitive[6][4]=max((ptransitive[6][4]),(ptransitive[6][7]*ptransitive[4][7]*beta));
			}
			if((nf_x_coordinate == fh_x_coordinate)&&(nf_y_coordinate == fh_y_coordinate))
			{
				storeprobability[5]=probability[5]=ptransitive[6][5]=max((ptransitive[6][7]),(ptransitive[6][7]*ptransitive[5][7]*beta));
			}
			if((nf_x_coordinate == w1_x_coordinate)&&(nf_y_coordinate == w1_y_coordinate))
			{
				storeprobability[7]=probability[7]=ptransitive[6][7]=ptransitive[6][7]+((1-ptransitive[6][7])*pinit);
				nf_ageing=0;
			}
			else
			{
				nf_ageing++;
			}
			if(nf_ageing==k)
			{
				ptransitive[6][7]=(float) (ptransitive[6][7]*pow(gamma,k));
				nf_ageing=0;
			}
		}
		break;
	default:
		{
		}
		break;
	}

		int x,y,n; n=8;  
		for(x=0;x<n;x++)
			{
			 for(y=0;y<n-1;y++)
				{
					if(probability[y] < probability[y+1])
					{
						float temp = probability[y+1];
						probability[y+1] = probability[y];
						probability[y] = temp;
					}
				 }
			}
		receivingagentprobability=probability[0];

		if(receivingagentprobability==0)
		{
			//System.out.printf("NO CONTACT\n");
		}
		else
		{
			for(int index=0;index<8;index++)
			{
				if(receivingagentprobability==storeprobability[index])
				{
					//System.out.printf("****************************************************************\n");
					//System.out.printf("CONTACT receivingagentprobability %lf \n",receivingagentprobability);
					push(sendingagentid,index+1, 0.0f);
				}
			}
		}

	}

    public void push(int sagentid, int ragentid, float a)
    {
        if (ragentid != 9)
        {
            int[] sendmsg = new int[2000];
            int[] recvmsg = new int[2000];

            switch (sagentid)
            {
                case 1 ->
                    sendmsg = Arrays.copyOf(t1_message, sendmsg.length);
                case 2 ->
                    sendmsg = Arrays.copyOf(t2_message, sendmsg.length);
                case 3 ->
                    sendmsg = Arrays.copyOf(t3_message, sendmsg.length);
                case 4 ->
                    sendmsg = Arrays.copyOf(bh_message, sendmsg.length);
                case 5 ->
                    sendmsg = Arrays.copyOf(mh_message, sendmsg.length);
                case 6 ->
                    sendmsg = Arrays.copyOf(fh_message, sendmsg.length);
                case 7 ->
                    sendmsg = Arrays.copyOf(nf_message, sendmsg.length);
                case 8 ->
                    sendmsg = Arrays.copyOf(w1_message, sendmsg.length);
            }

            switch (ragentid)
            {
                case 1 ->
                    recvmsg = Arrays.copyOf(t1_message, recvmsg.length);
                case 2 ->
                    recvmsg = Arrays.copyOf(t2_message, recvmsg.length);
                case 3 ->
                    recvmsg = Arrays.copyOf(t3_message, recvmsg.length);
                case 4 ->
                    recvmsg = Arrays.copyOf(bh_message, recvmsg.length);
                case 5 ->
                    recvmsg = Arrays.copyOf(mh_message, recvmsg.length);
                case 6 ->
                    recvmsg = Arrays.copyOf(fh_message, recvmsg.length);
                case 7 ->
                    recvmsg = Arrays.copyOf(nf_message, recvmsg.length);
                case 8 ->
                    recvmsg = Arrays.copyOf(w1_message, recvmsg.length);
            }

            int flag;
            for (int loop1 = 0; loop1 < BUCK_MAX_PACKETS; loop1++)
            {
                if (sendmsg[loop1] != 0)
                {
                    for (int loop2 = 0; loop2 < BUCK_MAX_PACKETS; loop2++)
                    {
                        if ((recvmsg[loop2] == 0) && (sendmsg[loop1] != 0))//Erasing duplicate at sender's side
                        {
                            flag = 0;
                            for (int loop3 = 0; loop3 < BUCK_MAX_PACKETS; loop3++)
                            {
                                if (sendmsg[loop1] == recvmsg[loop3])
                                {
                                    flag++;
                                }
                            }
                            if (flag == 0)
                            {
                                recvmsg[loop2] = sendmsg[loop1];
                                /*
                                 * Copy data
                                 */
                                sendmsg[loop1] = 0;
                                /*
                                 * Erase it
                                 */
                                //System.out.printf("sendmsg to recvmsg \n");
                            }
                        }

                    }
                }
            }
            //System.out.printf("%d	\t	%d	\t", sagentid, ragentid);
            //System.out.printf("\n");

            switch (sagentid)
            {
                case 1 ->
                    t1_message = Arrays.copyOf(sendmsg, t1_message.length);
                case 2 ->
                    t2_message = Arrays.copyOf(sendmsg, t2_message.length);
                case 3 ->
                    t3_message = Arrays.copyOf(sendmsg, t3_message.length);
                case 4 ->
                    bh_message = Arrays.copyOf(sendmsg, bh_message.length);
                case 5 ->
                    mh_message = Arrays.copyOf(sendmsg, mh_message.length);
                case 6 ->
                    fh_message = Arrays.copyOf(sendmsg, fh_message.length);
                case 7 ->
                    nf_message = Arrays.copyOf(sendmsg, nf_message.length);
                case 8 ->
                    w1_message = Arrays.copyOf(sendmsg, w1_message.length);
            }

            switch (ragentid)
            {
                case 1 -> t1_message = Arrays.copyOf(recvmsg, t1_message.length);
                case 2 -> t2_message = Arrays.copyOf(recvmsg, t2_message.length);
                case 3 -> t3_message = Arrays.copyOf(recvmsg, t3_message.length);
                case 4 -> bh_message = Arrays.copyOf(recvmsg, bh_message.length);
                case 5 -> mh_message = Arrays.copyOf(recvmsg, mh_message.length);
                case 6 -> fh_message = Arrays.copyOf(recvmsg, fh_message.length);
                case 7 -> nf_message = Arrays.copyOf(recvmsg, nf_message.length);
                case 8 -> w1_message = Arrays.copyOf(recvmsg, w1_message.length);
            }
            radioContactLog.add(new RadioContactLog(sagentid, ragentid, globaltick));
            Platform.runLater(() ->
            {
                rclXYSeries.getData().add(new XYChart.Data(sagentid, ragentid));
                rclXTSeries.getData().add(new XYChart.Data(sagentid, globaltick));
                rclYTSeries.getData().add(new XYChart.Data(ragentid, globaltick));
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
        else
        {
            int[] sendmsg = new int[2000];
            int[] recvmsg = new int[3500];
            switch (sagentid)
            {
                case 8 ->
                    sendmsg = Arrays.copyOf(w1_message, sendmsg.length);
                default ->
                {
                    //System.out.printf("Some things is wrong with Push! \n");
                    try
                    {
                        Thread.sleep(1);
                    }
                    catch (InterruptedException ex)
                    {
                        Logger.getLogger(CaptureTimeHide.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            switch (ragentid)
            {
                case 9 ->
                    recvmsg = Arrays.copyOf(d1_message, recvmsg.length);
                default ->
                {
                    //System.out.printf("Some things is wrong with Push! \n");
                    try
                    {
                        Thread.sleep(1);
                    }
                    catch (InterruptedException ex)
                    {
                        Logger.getLogger(CaptureTimeHide.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            int flag;
            for (int loop1 = 0; loop1 < BUCK_MAX_PACKETS; loop1++)
            {
                if (sendmsg[loop1] != 0)
                {
                    for (int loop2 = 0; loop2 < DATA_MAX_PACKETS; loop2++)
                    {
                        if ((recvmsg[loop2] == 0) && (sendmsg[loop1] != 0))//Erasing duplicate at sender's side
                        {
                            flag = 0;
                            for (int loop3 = 0; loop3 < DATA_MAX_PACKETS; loop3++)
                            {
                                if (sendmsg[loop1] == recvmsg[loop3])
                                {
                                    flag++;
                                }
                            }
                            if (flag == 0)
                            {
                                recvmsg[loop2] = sendmsg[loop1];
                                sendmsg[loop1] = 0;
                            }

                        }

                    }
                }
            }
            //Systen.out.printf("%d	\t	%d	\t", sagentid, ragentid);
            //Systen.out.printf("\n");
            switch (sagentid)
            {
                case 8 ->
                    w1_message = Arrays.copyOf(sendmsg, w1_message.length);
                default ->
                {
                    //System.out.printf("Some things is wrong with Push! \n");
                    try
                    {
                        Thread.sleep(1);
                    }
                    catch (InterruptedException ex)
                    {
                        Logger.getLogger(CaptureTimeHide.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            switch (ragentid)
            {
                case 9 ->
                    d1_message = Arrays.copyOf(recvmsg, d1_message.length);
                default ->
                {
                    //System.out.printf("Some things is wrong with Push! \n");
                    try
                    {
                        Thread.sleep(1);
                    }
                    catch (InterruptedException ex)
                    {
                        Logger.getLogger(CaptureTimeHide.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            radioContactLog.add(new RadioContactLog(sagentid, ragentid, globaltick));
            Platform.runLater(() ->
            {
                rclXYSeries.getData().add(new XYChart.Data(sagentid, ragentid));
                rclXTSeries.getData().add(new XYChart.Data(sagentid, globaltick));
                rclYTSeries.getData().add(new XYChart.Data(ragentid, globaltick));
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

    @Override
    public String simName()
    {
        return "Prophet Routing";
    }
    
    @Override
    String simShortName()
    {
        return "PR";
    }
}
