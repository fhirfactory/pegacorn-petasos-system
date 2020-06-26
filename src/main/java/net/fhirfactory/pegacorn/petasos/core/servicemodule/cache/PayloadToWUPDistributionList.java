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

public class PayloadToWUPDistributionList {
	private static final Logger LOG = LoggerFactory.getLogger(PayloadToWUPDistributionList.class);
	
	ConcurrentHashMap<FDNToken, FDNTokenSet> distributionList;
	
    public PayloadToWUPDistributionList(){
        distributionList = new ConcurrentHashMap<FDNToken, FDNTokenSet>();
    }

    /**
     * This function retrieves the list (FDNTokenSet) of WUPs that are interested in 
     * receiving the identified payloadType (FDNToken).
     * 
     * @param payloadType The payloadType that we want to know which WUPs are interested in
     * @return The set of WUPs wanting to receive this payload type.
     */

    public FDNTokenSet getDistrubtionForPayload(FDNToken payloadType){
    	LOG.debug(".getDistrubtionForPayload(): Entry, payloadType --> {}", payloadType);
    	if(distributionList.isEmpty()) {
    		LOG.debug("getDistrubtionForPayload(): Exit, empty list so can't match");
    		
    		return(null);
    	}
    	if(this.distributionList.containsKey(payloadType)) {
    		FDNTokenSet interestedWUPSet = this.distributionList.get(payloadType);
    		LOG.debug(".getRouteForPayload(): Exit, returning associated FDNSet of the WUPs interested --> {}", interestedWUPSet);
    		return(interestedWUPSet);
    	}
    	LOG.debug(".getRouteForPayload(): Couldn't find any associated FDNTokenSet elements (i.e. couldn't find any interested WUPs, returning null");
    	return(null);
    }
    
    /**
     * This function establishes a link between a Payload Type and a WUP that is interested in
     * processing/using it.
     * 
     * @param payloadType The payloadType (FDNToken) of the payload we have received from a WUP
     * @param interestedWUP The ID of the WUP that is interested in the payload type.
     */
    public void addInterestedWUPforPayload(FDNToken payloadType, FDNToken interestedWUP) {
    	LOG.debug(".addInterestedWUPforPayload(): Entry, payloadType --> {}, interestedWUP --> {}", payloadType, interestedWUP);
    	if((payloadType==null) || (interestedWUP==null)) {
    		throw(new IllegalArgumentException(".setRouteForPayload(): payloadType or interestedWUP is null"));
    	}
    	if(this.distributionList.containsKey(payloadType)) {
    		LOG.trace(".addInterestedWUPforPayload(): Removing existing map for payloadType --> {}", payloadType);
    		FDNTokenSet payloadDistributionList = this.distributionList.get(payloadType);
    		payloadDistributionList.addElement(interestedWUP);
    	} else {
    		FDNTokenSet newPayloadDistributionList = new FDNTokenSet();
    		newPayloadDistributionList.addElement(interestedWUP);
    		this.distributionList.put(payloadType, newPayloadDistributionList);
    	}
    	LOG.debug(".addInterestedWUPforPayload(): Exit, assigned the interestedWUP to the payloadType");
    }

}
