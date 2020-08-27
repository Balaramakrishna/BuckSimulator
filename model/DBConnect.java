/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bucksimulator.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**
 * Class to connect to the Derby database
 * @author balaram
 */
public class DBConnect {

    static private String framework = "embedded";
    static private String protocol = "jdbc:derby:";
    static String dbName = "BuckSimulationData";
    static Statement s;
    static Connection conn = null;
    static PreparedStatement psInsert;
    static ResultSet rs = null;
    public static ObservableList<SimDataModel> sdms = FXCollections.observableArrayList();
    
    public static String DBConnect() {
        try {
            conn = DriverManager.getConnection(protocol + dbName+ ";create=true");
            conn.setAutoCommit(false);
            s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            return "Success";
        } catch (SQLException ex) {
            return ex.getMessage();
        }
    }
    
    public static String createSimulationDataTable()
    {
        try {
            s.execute("create table SimulationData(simName varchar(40), rundate date, rcr float, ct float, t1tp float, t2tp float,"
                    + "t3tp float, bhtp float, fhtp float, mhtp float, nftp float, st time, et time)");
            return "Success";
        } catch (SQLException ex) {
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, null, ex);
            return ex.getMessage();
        }
    }
    
    public static ResultSet getListOfTables()
    {
        try {
            rs = s.executeQuery("SELECT TABLENAME FROM SYS.SYSTABLES WHERE TABLETYPE='T'");
        } catch (SQLException ex) {
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }
    
    public static ResultSet getSimulationDataTable()
    {
        rs = null;
        try {
            rs = s.executeQuery("SELECT * FROM SimulationData");
        } catch (Exception ex) {
        }
        return rs;
    }
    
    public static boolean updateSimulationDataTable(SimDataModel sdm)
    {
        try
        {
            psInsert = conn.prepareStatement("insert into SimulationData values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            psInsert.setString(1, sdm.getSimName());
            psInsert.setDate(2, Date.valueOf(sdm.getSimRunDate()));
            psInsert.setFloat(3, sdm.getRcr());
            psInsert.setFloat(4, sdm.getCt());
            psInsert.setFloat(5, sdm.getT1tp());
            psInsert.setFloat(6, sdm.getT2tp());
            psInsert.setFloat(7, sdm.getT3tp());
            psInsert.setFloat(8, sdm.getBhtp());
            psInsert.setFloat(9, sdm.getFhtp());
            psInsert.setFloat(10, sdm.getMhtp());
            psInsert.setFloat(11, sdm.getNftp());
            psInsert.setTime(12, sdm.getSt());
            psInsert.setTime(13, sdm.getEt());
            psInsert.executeUpdate();
            return true;  
        }
        catch(SQLException ex)
        {
            return false;
        }
    }
    
    public static void getSimulationDataTableData()
    {
        ResultSet rs = getListOfTables();
        boolean result = false;
        try {
            while(rs.next())
            {
                if (rs.getString("TABLENAME").equalsIgnoreCase("SimulationData"))
                {
                    result = true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(!result)
        {
            createSimulationDataTable();
        }
        
        ResultSet rs1 = getSimulationDataTable();
        try
        {
            while(rs1.next())            
            {
                SimDataModel sdm = new SimDataModel();
                sdm.setSimName(rs1.getString(1));
                sdm.setSimRunDate(rs1.getDate(2).toLocalDate());
                sdm.setRcr(rs1.getFloat(3));
                sdm.setCt(rs1.getFloat(4));
                sdm.setT1tp(rs1.getFloat(5));
                sdm.setT2tp(rs1.getFloat(6));
                sdm.setT3tp(rs1.getFloat(7));
                sdm.setBhtp(rs1.getFloat(8));
                sdm.setFhtp(rs1.getFloat(9));
                sdm.setMhtp(rs1.getFloat(10));
                sdm.setNftp(rs1.getFloat(11));
                sdm.setSt(rs1.getTime(12));
                sdm.setEt(rs1.getTime(13));
                sdms.add(sdm);
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static String DBShutdown()
    {
        try 
        {
            if(conn != null)
            {
                conn.commit();
                DriverManager.getConnection("jdbc:derby:;shutdown=true");
                return "Success";
            }
            else
            {
                return "Connection Not established";
            }
        } 
        catch (SQLException ex) 
        {
            if (( (ex.getErrorCode() == 50000)&& ("XJ015".equals(ex.getSQLState()) ))) 
            {
                return "Derby shut down normally";
            } 
            else
            {
                return "Derby did not shut down normally";
            }
        }
    }
}
