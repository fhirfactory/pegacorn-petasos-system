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

/**
 * 
 * @author Mark A. Hunter
 *
 */

@ApplicationScoped
public class LocalNodeParcelTypeID2ParcelInstanceIDSetMap {
	private static final Logger LOG = LoggerFactory.getLogger(LocalNodeParcelTypeID2ParcelInstanceIDSetMap.class);
			
	private ConcurrentHashMap<FDN, FDNTokenSet> parcelType2InstanceSet;
	
	public LocalNodeParcelTypeID2ParcelInstanceIDSetMap() {
		parcelType2InstanceSet = new ConcurrentHashMap<FDN, FDNTokenSet>();
	}
		
    public void linkParcelType2ParcelInstance(FDN parcelTypeID, FDN parcelInstanceID) {
    	LOG.debug(".linkParcelType2ParcelInstance(): Entry, parcelTypeID --> {}, parcelInstanceID --> {}", parcelTypeID, parcelInstanceID);
    	if(parcelInstanceID==null) {
    		return;
    	}
    	if(parcelTypeID==null) {
    		return;
    	}
    	FDNTokenSet associatedParcelSet;
    	if(parcelType2InstanceSet.containsKey(parcelTypeID)) {
    		associatedParcelSet = parcelType2InstanceSet.get(parcelTypeID);
    		associatedParcelSet.addElement(parcelInstanceID.getToken());
    	} else {
    		associatedParcelSet = new FDNTokenSet();
        	associatedParcelSet.addElement(parcelInstanceID.getToken());
        	parcelType2InstanceSet.put(parcelTypeID, associatedParcelSet);
    	}
    }
    
    public void unlinkParcelTypeFromParcelInstance(FDN parcelTypeID, FDN parcelInstanceID) {
    	LOG.debug(".unlinkParcelTypeFromParcelInstance(): Entry, parcelTypeID --> {}, parcelInstanceID --> {}", parcelTypeID, parcelInstanceID);
    	if( (parcelInstanceID==null) || (parcelTypeID==null)) {
    		return;
    	}
    	if(parcelType2InstanceSet.containsKey(parcelTypeID)) {
    		FDNTokenSet associatedParcelSet = parcelType2InstanceSet.get(parcelTypeID);
    		associatedParcelSet.removeElement(parcelInstanceID.getToken());
    		parcelType2InstanceSet.remove(parcelTypeID);
    		parcelType2InstanceSet.put(parcelTypeID, associatedParcelSet);
    	}
    }
    
    public FDNTokenSet getLinkedParcelInstanceIDSet(FDN parcelTypeID) {
    	LOG.debug(".getLinkedParcelInstanceIDSet(): Entry, parcelTypeID --> {}", parcelTypeID);
    	if(parcelTypeID==null) {
    		return(null);
    	}
    	if(parcelType2InstanceSet.containsKey(parcelTypeID)) {
    		return(parcelType2InstanceSet.get(parcelTypeID));
    	}
    	return(null);
    }
	
}
