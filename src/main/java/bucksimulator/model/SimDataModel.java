/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bucksimulator.model;

import java.time.LocalDate;
import java.sql.Time;

/**
 * Model used to pull/push data to the Derby database.
 * Holds the past/current simulation data.
 * @author balaram
 */
public class SimDataModel
{
    String simName;
    LocalDate simRunDate;
    float rcr;
    float ct;
    float t1tp;
    float t2tp;
    float t3tp;
    float bhtp;
    float fhtp;
    float mhtp;
    float nftp;
    Time st;
    Time et;

    public String getSimName()
    {
        return simName;
    }

    public void setSimName(String simName)
    {
        this.simName = simName;
    }

    public LocalDate getSimRunDate()
    {
        return simRunDate;
    }

    public void setSimRunDate(LocalDate simRunDate)
    {
        this.simRunDate = simRunDate;
    }

    public float getRcr()
    {
        return rcr;
    }

    public void setRcr(float rcr)
    {
        this.rcr = rcr;
    }

    public float getCt()
    {
        return ct;
    }

    public void setCt(float ct)
    {
        this.ct = ct;
    }

    public float getT1tp()
    {
        return t1tp;
    }

    public void setT1tp(float t1tp)
    {
        this.t1tp = t1tp;
    }

    public float getT2tp()
    {
        return t2tp;
    }

    public void setT2tp(float t2tp)
    {
        this.t2tp = t2tp;
    }

    public float getT3tp()
    {
        return t3tp;
    }

    public void setT3tp(float t3tp)
    {
        this.t3tp = t3tp;
    }

    public float getBhtp()
    {
        return bhtp;
    }

    public void setBhtp(float bhtp)
    {
        this.bhtp = bhtp;
    }

    public float getFhtp()
    {
        return fhtp;
    }

    public void setFhtp(float fhtp)
    {
        this.fhtp = fhtp;
    }

    public float getMhtp()
    {
        return mhtp;
    }

    public void setMhtp(float mhtp)
    {
        this.mhtp = mhtp;
    }

    public float getNftp()
    {
        return nftp;
    }

    public void setNftp(float nftp)
    {
        this.nftp = nftp;
    }
    
    public Time getSt()
    {
        return st;
    }
    
    public void setSt(Time time)
    {
        st = time;
    }
    
    public Time getEt()
    {
        return et;
    }
    
    public void setEt(Time time)
    {
        et = time;
    }
}
