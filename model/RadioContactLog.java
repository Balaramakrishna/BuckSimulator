/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bucksimulator.model;

/**
 *
 * @author balaram
 */
public class RadioContactLog {
    int txAgentX;
    int rxAgentY;
    double globalTick;

    public RadioContactLog(int txAgentX, int rxAgentY, double globalTick) {
        this.txAgentX = txAgentX;
        this.rxAgentY = rxAgentY;
        this.globalTick = globalTick;
    }

    public int getTxAgentX() {
        return txAgentX;
    }

    public int getRxAgentY() {
        return rxAgentY;
    }

    public double getGlobalTick() {
        return globalTick;
    }
    
}
