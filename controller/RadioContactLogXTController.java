/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bucksimulator.controller;

import bucksimulator.model.Simulation;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author balaram
 */
public class RadioContactLogXTController implements Initializable
{
    boolean zoomStarted;
    Rectangle zoomArea = new Rectangle();
    Simulation currentSim;
    final ObjectProperty<Point2D> mouseAnchor = new SimpleObjectProperty<>();
    @FXML
    private ScatterChart<Number, Number> radioContactLogChart;
    @FXML
    private NumberAxis rclYAxis;
    @FXML
    private NumberAxis rclXAxis;
    @FXML
    private AnchorPane rclAnchorPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        zoomArea.setManaged(false);
        zoomArea.setFill(Color.MEDIUMTURQUOISE.deriveColor(0, 1, 1, 0.5));
        rclAnchorPane.getChildren().add(zoomArea);
    }    

    
    void setData(Simulation sim)
    {
        currentSim = sim;
        radioContactLogChart.getData().add(sim.getRCLXGTSeries());
    }
    
    @FXML
    private void zoomChart(MouseEvent event)
    {
        if(zoomStarted)
        {
            rclYAxis.setAutoRanging(false);
            Point2D zoomBottomRight = new Point2D(zoomArea.getX() + zoomArea.getWidth(), zoomArea.getY() + zoomArea.getHeight());
            Point2D xAxisInScene = rclXAxis.localToScene(0, 0);
            double yOffset = zoomBottomRight.getY() - xAxisInScene.getY();
            double yAxisScale = rclYAxis.getScale();
            rclYAxis.setLowerBound(rclYAxis.getLowerBound() + yOffset / yAxisScale);
            rclYAxis.setUpperBound(rclYAxis.getLowerBound() - zoomArea.getHeight() / yAxisScale);
            rclYAxis.setTickUnit(5);
            zoomArea.setWidth(0);
            zoomArea.setHeight(0);
            zoomStarted = false;
        }
    }

    @FXML
    private void setZoomSize(MouseEvent event)
    {
        double x = event.getX();
        double y = event.getY();
        zoomArea.setX(Math.min(x, mouseAnchor.get().getX()));
        zoomArea.setY(Math.min(y, mouseAnchor.get().getY()));
        zoomArea.setWidth(Math.abs(x - mouseAnchor.get().getX()));
        zoomArea.setHeight(Math.abs(y - mouseAnchor.get().getY()));
        zoomStarted = true;
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
    }

    @FXML
    private void setZoomStartLocation(MouseEvent event)
    {
        mouseAnchor.set(new Point2D(event.getX(), event.getY()));
        zoomArea.setWidth(0);
        zoomArea.setHeight(0);
    }
    
    void resetZoom(KeyEvent event)
    {
        if (event.getCode() == KeyCode.R) {
            rclYAxis.setLowerBound(0);
            rclYAxis.setAutoRanging(true);
            radioContactLogChart.layout();
            zoomArea.setWidth(0);
            zoomArea.setHeight(0);
            zoomStarted = false;
        }
    }
    
    public ScatterChart<?, ?> getChart()
    {
        return radioContactLogChart;
    }
}
