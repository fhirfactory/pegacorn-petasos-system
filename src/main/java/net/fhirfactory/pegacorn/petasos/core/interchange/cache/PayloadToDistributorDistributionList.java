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

package net.fhirfactory.pegacorn.petasos.core.interchange.cache;

import net.fhirfactory.pegacorn.common.model.FDNToken;
import net.fhirfactory.pegacorn.petasos.core.model.map.LinkingRoute;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PayloadToDistributorDistributionList {
	private static final Logger LOG = LoggerFactory.getLogger(PayloadToDistributorDistributionList.class);
	
    private ConcurrentHashMap<FDNToken, LinkingRoute> distributorList;

    public PayloadToDistributorDistributionList(){
        distributorList = new ConcurrentHashMap<FDNToken, LinkingRoute>();
    }

    /**
     * This function retrieves the associated LinkingRoute for a given Payload type. Within a ServiceModule, we want
     * to know the distribution node for the traffic from the WUP of type "payloadType". At the end of the route should
     * be another bean who's job it is is to distribute the payload to any WUP that is listed into the targetWUP set 
     * of the payload itself.
     * 
     * @param payloadType The payloadType that we want to know the associated Route to forward messages onto
     * @return The LinkingRoute associated to the payload type.
     */
    public LinkingRoute getRouteForPayload(FDNToken payloadType){
    	LOG.debug(".getRouteForPayload(): Entry, payloadType --> {}", payloadType);
    	if(distributorList.isEmpty()) {
    		LOG.debug("getRouteForPayload(): Exit, empty list so can't match");
    		return(null);
    	}
    	if(this.distributorList.containsKey(payloadType)) {
    		LinkingRoute associatedRoute = this.distributorList.get(payloadType);
    		LOG.debug(".getRouteForPayload(): Exit, returning associated LinkingRoute --> {}", associatedRoute);
    		return(associatedRoute);
    	}
    	LOG.debug(".getRouteForPayload(): Couldn't find any associated LinkingRoute, returning null");
    	return(null);
    }
    
    /**
     * This function establishes a link between a Payload Type and an associated Distribution module (via a LinkingRoute
     * instance). The Distribution module will forward this payload to ALL the downstream WUPs interested in this type
     * of Payload.
     * 
     * @param payloadType The payloadType (FDNToken) of the payload we have received from a WUP
     * @param associatedRoute A route that will lead to a distributor for this payload type.
     */
    public void setRouteForPayload(FDNToken payloadType, LinkingRoute associatedRoute) {
    	LOG.debug(".setRouteForPayload(): Entry, payloadType --> {}, associatedRoute --> {}", payloadType, associatedRoute);
    	if((payloadType==null) || (associatedRoute==null)) {
    		throw(new IllegalArgumentException(".setRouteForPayload(): payloadType or associatedRoute is null"));
    	}
    	if(this.distributorList.containsKey(payloadType)) {
    		LOG.trace(".setRouteForPayload(): Removing existing map for payloadType --> {}", payloadType);
    		this.distributorList.remove(payloadType);
    	}
    	this.distributorList.put(payloadType,associatedRoute);
    	LOG.debug(".setRouteForPayload(): Exit, assigned the associatedRoute to the payloadType");
    }
}
