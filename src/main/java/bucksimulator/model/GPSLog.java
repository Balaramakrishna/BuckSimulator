/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bucksimulator.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * GPS Log Model with x and y coordinates.
 * @author balaram
 */
public class GPSLog {
    int xCord;
    int yCord;

    public GPSLog(int xCord, int yCord) {
        this.xCord = xCord;
        this.yCord = yCord;
    }

    public int getxCord() {
        return xCord;
    }

    public int getyCord() {
        return yCord;
    }
    
    public IntegerProperty xCordProperty() {
        return new SimpleIntegerProperty(xCord);
    }

    public IntegerProperty yCordProperty() {
        return new SimpleIntegerProperty(yCord);
    }
    
    @Override
    public String toString()
    {
        return xCord+"  "+yCord;
    }
    
}
