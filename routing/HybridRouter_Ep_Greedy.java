/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package routing;

import core.Settings;
import core.SimClock;

/**
 *
 * @author User
 */
public class HybridRouter_Ep_Greedy extends SprayAndWaitRouter {
    
    double WarmUpTime;
    
    public HybridRouter_Ep_Greedy(Settings s){
        super(s);
        
        deliveryRatioEpidemic = 0;
        deliveryRatioSprayAndWait = 0;
        timeDeterminer = true;
        numOfRunsEpidemic = 0;
        numOfRunsSprayAndWait = 0;
        flag = true;
    }
    
    public HybridRouter_Ep_Greedy(HybridRouter_Ep_Greedy r){
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
        double curTime = SimClock.getTime(); // Set current time
        double Time = curTime;
        int curTimeINT = (int)curTime;
        
        //if(!flag) System.out.println("S & W");
        //if(flag) System.out.println("EP");
        
        // Learning time (Exploration)
        double WarmUpTime = 50000;
                                                                                                                                                                                                                                                                                             WarmUpTime = 10000;
        double cycle = 5000;
        int cycleINT = 5000;
        double runTime = cycle/2; // One interval
        
        // This is equivalent to (curTimeINT % cycle) - finding the interval
        double d = (curTimeINT / cycleINT);
        curTime -= (d * cycle);
        
        // Flag --> True --> Epidemic
        // Flag --> False --> Spray And Wait
        if ( Time <= WarmUpTime ) { 
            if ( curTime >= 0 && curTime <= runTime ) { // 1st interval
                if ( !flag ) numOfRunsEpidemic++; // Previous - SNW, now - Epidemic
               
                selectRouter(); // Calculate ATO
                resetProperties(); // Reset all the variables
                
                flag = true;
            }
            else if ( curTime > runTime && curTime <= cycle ) {
                if ( flag ) numOfRunsSprayAndWait++; // Previous - Epidemic, now - SNW
                
                selectRouter(); // Calculate ATO
                resetProperties(); // Reset all the variables
                
                flag = false;
            }
        }
        else{
            if ( curTime >= 0 && curTime <= runTime ){
                // timeDeterminer is used to determine if simulation protocol is selected for this interval
                // timeDeterminer --> False --> First interval
                // timeDeterminer --> True --> Second interval
                if ( timeDeterminer ) {
                    flag = selectRouter();

                    if ( flag ) numOfRunsEpidemic++;
                    else numOfRunsSprayAndWait++;

                    resetProperties();
                    timeDeterminer = false;
                }
            }
            else if ( curTime > runTime && curTime <= cycle ) {
                if ( !timeDeterminer ) {
                    flag = selectRouter();

                    if ( flag ) numOfRunsEpidemic++;
                    else numOfRunsSprayAndWait++;
                    
                    resetProperties();                    
                    timeDeterminer = true;
                }
            }
        }
        
        super.update();
    }

    void resetProperties() {
        numOfMessageDeliveredSprayAndWait = 0;
        numOfMessageDeliveredEpidemic = 0;
        numOfMessageTransmittedSprayAndWait = 0;
        numOfMessageTransmittedEpidemic = 0;
    }
    
    boolean selectRouter() {
        double transmissionOverheadEpidemic = 0, transmissionOverheadSprayAndWait = 0;
        double curDeliveryRatioEpidemic = 0, curDeliveryRatioSprayAndWait = 0;
        
        if ( !flag ) { // Spray & Wait
            if ( numOfMessageTransmittedSprayAndWait > 0 ) {
                if ( numOfMessageTransmittedSprayAndWait > 0 ) {
                    curDeliveryRatioSprayAndWait = (numOfMessageDeliveredSprayAndWait / (double)numOfMessageTransmittedSprayAndWait);
                    deliveryRatioSprayAndWait += curDeliveryRatioSprayAndWait;
                }
            }
        }
        else{ // Epidemic
            if ( numOfMessageTransmittedEpidemic > 0 ) {
                curDeliveryRatioEpidemic = (numOfMessageDeliveredEpidemic / (double)numOfMessageTransmittedEpidemic);
                deliveryRatioEpidemic += curDeliveryRatioEpidemic;
            }
        }
        
        if ( numOfRunsEpidemic > 0 ) transmissionOverheadEpidemic = deliveryRatioEpidemic / numOfRunsEpidemic;
        if ( numOfRunsSprayAndWait > 0 ) transmissionOverheadSprayAndWait = deliveryRatioSprayAndWait / numOfRunsSprayAndWait;
        
        boolean protocol;
        
        // Greedy choice
        if ( transmissionOverheadEpidemic > transmissionOverheadSprayAndWait ) protocol = true; // Select Epidemic
        else protocol = false; // Select SnW
        
        // Epsilon-Greedy
        int upperValue = 10, lowerValue = 1;
        int p = (int) (Math.random() * (upperValue - lowerValue)) + lowerValue; // Generate random value from 1 to 10
        if( p <= 1 ){ // Exploration
            int upperValue1 = 2, lowerValue1 = 1;
            int p1 = (int) (Math.random() * (upperValue1 - lowerValue1)) + lowerValue1; // Generate random value from 1 to 2
            
            if ( p1 == 1 ) 
                protocol = true; // Randomly select Epidemic
            else
                protocol = false; // Randomly select SnW
        }
        
        // Exploitation
        return protocol;
    }
    
    @Override
    public HybridRouter_Ep_Greedy replicate(){
        return new HybridRouter_Ep_Greedy(this);
    }
    
    
                                                                                                                                                                                                                                                                                                                                                                                                                                                    void p()
                                                                                                                                                                                                                                                                                                                                                                                                                                                    {
                                                                                                                                                                                                                                                                                                                                                                                                                                                        if(SimClock.getTime() > 5000) 
                                                                                                                                                                                                                                                                                                                                                                                                                                                            flag = false;                                                                                                                                                                                                                                                                                                                                                                                                 }

}
