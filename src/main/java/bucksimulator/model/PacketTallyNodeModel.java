/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bucksimulator.model;

/**
 * Model to show the name of the filed on the packet tally screen.
 * @author balaram
 */
public class PacketTallyNodeModel
{
    String nodeName;

    public PacketTallyNodeModel(String nodeName)
    {
        this.nodeName = nodeName;
    }

    public String getNodeName()
    {
        return nodeName;
    }

    public void setNodeName(String nodeName)
    {
        this.nodeName = nodeName;
    }
    
}
