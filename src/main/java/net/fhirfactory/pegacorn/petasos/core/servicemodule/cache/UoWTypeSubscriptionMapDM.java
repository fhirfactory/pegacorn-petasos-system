/*
 * Copyright (c) 2020 MAHun
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.fhirfactory.pegacorn.petasos.core.servicemodule.cache;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fhirfactory.pegacorn.common.model.FDNToken;
import net.fhirfactory.pegacorn.common.model.FDNTokenSet;

import javax.inject.Singleton;

@Singleton
public class UoWTypeSubscriptionMapDM {
	private static final Logger LOG = LoggerFactory.getLogger(UoWTypeSubscriptionMapDM.class);
	
	ConcurrentHashMap<FDNToken, FDNTokenSet> distributionList;
	
    public UoWTypeSubscriptionMapDM(){
        distributionList = new ConcurrentHashMap<FDNToken, FDNTokenSet>();
    }

    /**
     * This function retrieves the list (FDNTokenSet) of WUPs that are interested in 
     * receiving the identified uowTypeID (FDNToken).
     * 
     * @param uowTypeID The uowTypeID that we want to know which WUPs are interested in
     * @return The set of WUPs wanting to receive this payload type.
     */

    public FDNTokenSet getSubscriptionSetForUOWType(FDNToken uowTypeID){
    	LOG.debug(".getSubscriptionSetForUOWType(): Entry, uowTypeID --> {}", uowTypeID);
    	if(distributionList.isEmpty()) {
    		LOG.debug("getSubscriptionSetForUOWType(): Exit, empty list so can't match");
    		
    		return(null);
    	}
    	if(this.distributionList.containsKey(uowTypeID)) {
    		FDNTokenSet interestedWUPSet = this.distributionList.get(uowTypeID);
    		LOG.debug(".getSubscriptionSetForUOWType(): Exit, returning associated FDNSet of the WUPs interested --> {}", interestedWUPSet);
    		return(interestedWUPSet);
    	}
    	LOG.debug(".getSubscriptionSetForUOWType(): Couldn't find any associated FDNTokenSet elements (i.e. couldn't find any interested WUPs, returning null");
    	return(null);
    }
    
    /**
     * This function establishes a link between a Payload Type and a WUP that is interested in
     * processing/using it.
     * 
     * @param uowTypeID The uowTypeID (FDNToken) of the payload we have received from a WUP
     * @param interestedWUP The ID of the WUP that is interested in the payload type.
     */
    public void addSubscriberToUoWType(FDNToken uowTypeID, FDNToken interestedWUP) {
    	LOG.debug(".addInterestedWUPforPayload(): Entry, uowTypeID --> {}, interestedWUP --> {}", uowTypeID, interestedWUP);
    	if((uowTypeID==null) || (interestedWUP==null)) {
    		throw(new IllegalArgumentException(".setRouteForPayload(): uowTypeID or interestedWUP is null"));
    	}
    	if(this.distributionList.containsKey(uowTypeID)) {
    		LOG.trace(".addInterestedWUPforPayload(): Removing existing map for uowTypeID --> {}", uowTypeID);
    		FDNTokenSet payloadDistributionList = this.distributionList.get(uowTypeID);
    		payloadDistributionList.addElement(interestedWUP);
    	} else {
    		FDNTokenSet newPayloadDistributionList = new FDNTokenSet();
    		newPayloadDistributionList.addElement(interestedWUP);
    		this.distributionList.put(uowTypeID, newPayloadDistributionList);
    	}
    	LOG.debug(".addInterestedWUPforPayload(): Exit, assigned the interestedWUP to the uowTypeID");
    }

}
