/* 
 * Copyright 2010 Aalto University, ComNet
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package routing;

import core.Settings;
import core.SimClock;

/**
 * Epidemic message router with drop-oldest buffer and only single transferring
 * connections at a time.
 */
public class EpidemicRouter extends ActiveRouter {
	
	/**
	 * Constructor. Creates a new message router based on the settings in
	 * the given Settings object.
	 * @param s The settings object
	 */
	public EpidemicRouter(Settings s) {
		super(s);
		//System.out.println("=============TAPOJIT=================");
		
		// rSystem.out.println("TIME " + SimClock.getTime());
		//TODO: read&use epidemic router specific settings (if any)
	}
	
	/**
	 * Copy constructor.
	 * @param r The router prototype where setting values are copied from
	 */
	protected EpidemicRouter(EpidemicRouter r) {
		super(r);
		//System.out.println("=============TAPOJIT=================");
		//TODO: copy epidemic settings here (if any)
	}
			
	@Override
	public void update() {
		super.update();
                // System.out.println("TIME " + SimClock.getTime());
                 //System.out.println(messageDeliveredEpidemic + " " + messageTransmittedEpidemic);
		if (isTransferring() || !canStartTransfer()) {
			return; // transferring, don't try other connections yet
		}
                
		numOfRunsEpidemic++;
		// Try first the messages that can be delivered to final recipient
		if (exchangeDeliverableMessages() != null) {
                        
                        // System.out.println(numOfMessageDeliveredEpidemic + " " + numOfRunsEpidemic);
                        numOfMessageDeliveredEpidemic++;
			return; // started a transfer, don't try others (yet)
		}
		
		// then try any/all message to any/all connection
		this.tryAllMessagesToAllConnections();
	}
	
	
	@Override
	public EpidemicRouter replicate() {
		// System.out.println("=============TAPOJIT=================");
		// System.out.println(SimClock.getTime());
                return new EpidemicRouter(this);
	}

}