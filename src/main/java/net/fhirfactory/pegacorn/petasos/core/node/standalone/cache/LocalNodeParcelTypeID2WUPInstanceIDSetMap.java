/*
 * Copyright (c) 2020 Mark A. Hunter
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

package net.fhirfactory.pegacorn.petasos.core.node.standalone.cache;

import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fhirfactory.pegacorn.common.model.FDN;
import net.fhirfactory.pegacorn.common.model.FDNTokenSet;

@ApplicationScoped
public class LocalNodeParcelTypeID2WUPInstanceIDSetMap {
	private static final Logger LOG = LoggerFactory.getLogger(LocalNodeParcelTypeID2WUPInstanceIDSetMap.class);
	
	private ConcurrentHashMap<FDN, FDNTokenSet> uowTypeID2WUPInstanceIDMap;
	
	public LocalNodeParcelTypeID2WUPInstanceIDSetMap() {
		uowTypeID2WUPInstanceIDMap = new ConcurrentHashMap<FDN, FDNTokenSet>();
	}
		
    public void linkParcelTypeID2WUPInstance(FDN parcelTypeID, FDN wupInstanceID) {
    	LOG.debug(".linkParcelTypeID2WUPInstance(): Entry, uowTypeID --> {}, wupInstanceID --> {}", parcelTypeID, wupInstanceID);
    	if(wupInstanceID==null) {
    		return;
    	}
    	if(parcelTypeID==null) {
    		return;
    	}
    	FDNTokenSet associatedParcelSet;
    	if(uowTypeID2WUPInstanceIDMap.containsKey(parcelTypeID)) {
    		associatedParcelSet = uowTypeID2WUPInstanceIDMap.get(parcelTypeID);
    		associatedParcelSet.addElement(wupInstanceID.getToken());
    	} else {
    		associatedParcelSet = new FDNTokenSet();
        	associatedParcelSet.addElement(wupInstanceID.getToken());
        	uowTypeID2WUPInstanceIDMap.put(parcelTypeID, associatedParcelSet);
    	}
    }
    
    public void unlinkParcelTypeIDFromWUPInstance(FDN parcelTypeID, FDN wupInstanceID) {
    	LOG.debug(".unlinkParcelTypeID2WUPInstance(): Entry, uowTypeID --> {}, wupInstanceID --> {}", parcelTypeID, wupInstanceID);
    	if( (wupInstanceID==null) || (parcelTypeID==null)) {
    		return;
    	}
    	if(uowTypeID2WUPInstanceIDMap.containsKey(parcelTypeID)) {
    		FDNTokenSet associatedParcelSet = uowTypeID2WUPInstanceIDMap.get(parcelTypeID);
    		associatedParcelSet.removeElement(wupInstanceID.getToken());
    		uowTypeID2WUPInstanceIDMap.remove(parcelTypeID);
    		uowTypeID2WUPInstanceIDMap.put(parcelTypeID, associatedParcelSet);
    	}
    }
    
    public FDNTokenSet getLinkedWUPInstanceIDSet(FDN parcelTypeID) {
    	LOG.debug(".getLinkedWUPInstanceIDSet(): Entry, uowFDN --> {}", parcelTypeID);
    	if(parcelTypeID==null) {
    		return(null);
    	}
    	if(uowTypeID2WUPInstanceIDMap.containsKey(parcelTypeID)) {
    		return(uowTypeID2WUPInstanceIDMap.get(parcelTypeID));
    	}
    	return(null);
    }

}
