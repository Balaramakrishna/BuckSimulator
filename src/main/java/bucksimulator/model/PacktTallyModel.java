/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bucksimulator.model;

/**
 * Model to hold the value to be shown on packet tally table.
 * @author balaram
 */
public class PacktTallyModel
{
    float value;

    public PacktTallyModel(float count)
    {
        this.value = count;
    }

    public float getValue()
    {
        return value;
    }
    
}
