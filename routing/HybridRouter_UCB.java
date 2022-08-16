/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package routing;

import java.util.ArrayList;
import core.Message;
import core.Settings;
import core.SimClock;
import java.util.Collection;
import java.lang.*;

/**
 *
 * @author User
 */
public class HybridRouter_UCB extends SprayAndWaitRouter {
    
    // Delivery Ratio
    double WarmUpTime;
    double cycle;
    int cycleINT;
    
    public HybridRouter_UCB(Settings s){
        super(s);
        
        deliveryRatioEpidemic = 0;
        deliveryRatioSprayAndWait = 0;
        timeDeterminer = true;
        numOfRunsEpidemic = 0;
        numOfRunsSprayAndWait = 0;
        flag = true;
    }
    
    public HybridRouter_UCB(HybridRouter_UCB r){
        super(r);
         
        deliveryRatioEpidemic = 0;
        deliveryRatioSprayAndWait = 0;
        timeDeterminer = true;
        numOfRunsEpidemic = 0;
        numOfRunsSprayAndWait = 0;
        flag = true;
    }
    
    @Override
    public void update(){
        //if(numOfRunsEpidemic > 0) System.out.println(numOfRunsEpidemic + " " + numOfRunsSprayAndWait);
        double curTime = SimClock.getTime();
        double Time = curTime;
        int curTimeINT = (int)curTime;
        
        //if(!flag) System.out.println("S & W");
        //if(flag) System.out.println("EP");
        
        // 10000
        //double WarmUpTime = 10000;
        double WarmUpTime = 50000;
                                                                                                                                                                                    WarmUpTime = 5000;
        // 5000
        cycle = 5000;
        cycleINT = 5000;
        
                                                                                                                                                   //  p();
        double runTime = cycle/2;
        
        // This is equivalent to (curTimeINT%cycle)
        double d = (curTimeINT/cycleINT);
        curTime -= (d*cycle);
        
        // System.out.println(curTime + " " + curTimeINT + " " + cycle + " " + runTime);
                                                                                                                                                                                    p();
        // resetProperties();
        //  flag = true;
        // System.out.println(numOfRunsEpidemic + " " + numOfRunsSprayAndWait);
        // Flag --> True --> Epidemic
        // Flag --> False --> Spray And Wait
        if(Time <= WarmUpTime){
            
            if(curTime >= 0 && curTime <= runTime){
                // First Interval
                 if(!flag) numOfRunsEpidemic++;
               
                selectRouter();
                resetProperties();
                flag = true;
            }
            else if(curTime > runTime && curTime <= cycle){
               // if(numOfMessageTransmittedEpidemic > 0) System.out.println(numOfMessageTransmittedEpidemic + " " + numOfMessageTransmittedSprayAndWait);
               // Second Interval
                if(flag) numOfRunsSprayAndWait++; 
                selectRouter();
                resetProperties();
                flag = false;
            }
            //p();
        }
        else{
            if(curTime >= 0 && curTime <= runTime){
            // timeDeterminer is used to determine if simulation protocol is selected for this interval
                // timeDeterminer --> False --> First interval
                // timeDeterminer --> True --> Second interval
                if(timeDeterminer){                
                    flag = selectRouter();

                    if(flag) numOfRunsEpidemic++;
                    else numOfRunsSprayAndWait++;

                    resetProperties();                
                    timeDeterminer = false;
                }
            }
            else if(curTime > runTime && curTime <= cycle){
                if(!timeDeterminer){
                    flag = selectRouter();

                    if(flag) numOfRunsEpidemic++;
                    else numOfRunsSprayAndWait++;
                    
                    resetProperties();                    
                    timeDeterminer = true;
                }
            }
            //p();
            //if(!flag) System.out.println("S & W");
            //if(flag) System.out.println("EP");
        }
        // flag = false;
        
        
        
//        // System.out.println("Going Inside from here");
////        flag = true;
//        super.update();
//         //System.out.println("Got Outside from here " + flag);
//        if(flag && messageDeliveredEpidemic > 0){
//            e += messageDeliveredEpidemic;
//            System.out.println("EP " + e + " " + Time+ " " + messageDeliveredEpidemic + " " + messageTransmittedEpidemic + " " + numOfRunsEpidemic);
//        }
//        else if(messageDeliveredSprayAndWait > 0){
//            s += messageDeliveredEpidemic;
//            //System.out.println("S & W " + s + " " + messageDeliveredSprayAndWait + " " + messageTransmittedSprayAndWait+ " " +numOfRunsSprayAndWait);
//        }
          super.update();
    }

    
    void resetProperties()
    {
       // System.out.println("HERE " + messageDeliveredSprayAndWait);
        numOfMessageDeliveredSprayAndWait = 0;
        numOfMessageDeliveredEpidemic = 0;
        numOfMessageTransmittedSprayAndWait = 0;
        numOfMessageTransmittedEpidemic = 0;
        // System.out.println(messageTransmittedSprayAndWait + " " + messageTransmittedEpidemic);
    }
    
    boolean selectRouter()
    {
        //System.out.println("CAME ");
        double transmissionOverheadEpidemic = 0, transmissionOverheadSprayAndWait = 0;
        double curDeliveryRatioEpidemic = 0, curDeliveryRatioSprayAndWait = 0;
        double UB_Epidemic = 0, UB_SprayAndWait = 0;
        
        if(!flag){
//            System.out.println("S & W " + numOfRunsSprayAndWait + " " + numOfMessageTransmittedSprayAndWait + " " + numOfMessageDeliveredSprayAndWait + " " + curDeliveryRatioSprayAndWait + " " + deliveryRatioSprayAndWait);

            if(numOfMessageTransmittedSprayAndWait > 0){
                // S & W
                if(numOfMessageTransmittedSprayAndWait > 0){
                    curDeliveryRatioSprayAndWait = (numOfMessageDeliveredSprayAndWait/(double)numOfMessageTransmittedSprayAndWait);   
                    //System.out.println("PREV " + deliveryRatioSprayAndWait);
                    deliveryRatioSprayAndWait += curDeliveryRatioSprayAndWait;
                }
            }    
        }
        else{
            // EPIDEMIC
            if(numOfMessageTransmittedEpidemic > 0){
                curDeliveryRatioEpidemic = (numOfMessageDeliveredEpidemic/(double)numOfMessageTransmittedEpidemic);
                deliveryRatioEpidemic += curDeliveryRatioEpidemic;
            }
        }
//        
        if(numOfRunsEpidemic > 0){
            double L = 1;
            
            transmissionOverheadEpidemic = deliveryRatioEpidemic/numOfRunsEpidemic;
            double times = SimClock.getTime();
            
            UB_Epidemic = transmissionOverheadEpidemic*100 + Math.sqrt((L* Math.log(times))/numOfRunsEpidemic);
        }
        if(numOfRunsSprayAndWait > 0){
            double L = 1;
            
            transmissionOverheadSprayAndWait = deliveryRatioSprayAndWait/numOfRunsSprayAndWait;
            double times = SimClock.getTime();
            
            UB_SprayAndWait = transmissionOverheadSprayAndWait*100 + Math.sqrt((L* Math.log(times))/numOfRunsSprayAndWait);
            
        }
        
        // if(transmissionOverheadSprayAndWait > 0) System.out.println(transmissionOverheadEpidemic + " " + transmissionOverheadSprayAndWait);
//        return false;
        
        // System.out.println("S & W " + deliveryRatioSprayAndWait + " " + numOfRunsSprayAndWait + " " + transmissionOverheadSprayAndWait);
        // System.out.println("EP " +  deliveryRatioEpidemic + " " + numOfRunsEpidemic +  " " + transmissionOverheadEpidemic);
        // Epidemic is chosen
        
        boolean protocol;
        
        if(UB_Epidemic > UB_SprayAndWait) protocol = true;
        else protocol = false;
                    
        return protocol;
    }
    
    @Override
    public HybridRouter_UCB replicate(){
        return new HybridRouter_UCB(this);
    }
    
    
                                                                                                                                                                                                                                                                                                                                                                                                                                                    void p()
                                                                                                                                                                                                                                                                                                                                                                                                                                                    {
                                                                                                                                                                                                                                                                                                                                                                                                                                                        //if(SimClock.getTime() > 2000)
                                                                                                                                                                                                                                                                                                                                                                                                                                                        flag = false;
                                                                                                                                                                                                                                                                                                                                                                                                                                                    }
        
}
