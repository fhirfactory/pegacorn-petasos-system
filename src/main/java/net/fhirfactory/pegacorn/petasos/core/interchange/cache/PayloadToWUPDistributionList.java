package net.fhirfactory.pegacorn.petasos.core.interchange.cache;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fhirfactory.pegacorn.common.model.FDNToken;
import net.fhirfactory.pegacorn.common.model.FDNTokenSet;
import net.fhirfactory.pegacorn.petasos.core.model.map.LinkingRoute;

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
