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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;

import net.fhirfactory.pegacorn.petasos.core.model.parcel.PetasosParcel;
import net.fhirfactory.pegacorn.petasos.core.model.parcel.PetasosParcelProcessingStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fhirfactory.pegacorn.common.model.FDN;

@ApplicationScoped
public class LocalNodeParcelCache {
	private static final Logger LOG = LoggerFactory.getLogger(LocalNodeParcelCache.class);
	
	private ConcurrentHashMap<FDN, PetasosParcel> petasosParcelCache;
	
	public LocalNodeParcelCache () {
		petasosParcelCache = new ConcurrentHashMap<FDN, PetasosParcel>();
	}
	
    public void addParcel(PetasosParcel parcel) {
    	LOG.debug(".addParcel(): Entry, parcel --> {}", parcel);
    	if(parcel == null) {
    		return; 
    	}
    	if(!parcel.hasParcelInstanceID()) {
    		return;
    	}
    	FDN parcelFDN = parcel.getParcelInstanceID();
    	petasosParcelCache.put(parcelFDN, parcel);
    }
    
    public PetasosParcel getParcelInstance(FDN parcelInstanceID) {
    	LOG.debug(".getParcelInstance(): Entry, parcelInstanceID --> {}", parcelInstanceID);
    	if(petasosParcelCache.containsKey(parcelInstanceID)) {
    		return(petasosParcelCache.get(parcelInstanceID));
    	}
    	return(null);
    }
    
    public void removeParcel(PetasosParcel parcel) {
    	LOG.debug(".removeParcel(): Entry, parcel --> {}", parcel);
    	if(parcel==null) {
    		return;
    	}
    	if(!parcel.hasParcelInstanceID()) {
    		return;
    	}
    	petasosParcelCache.remove(parcel.getParcelInstanceID());
    }
    
    public void removeParcel(FDN parcelInstanceID) {
    	LOG.debug(".removeParcel(): Entry, parcelInstanceID --> {}", parcelInstanceID);
    	if(parcelInstanceID==null) {
    		return;
    	}
    	petasosParcelCache.remove(parcelInstanceID);
    }
    
    public List<PetasosParcel> getParcelSet(){
    	LOG.debug(".getParcelSet(): Entry");
    	List<PetasosParcel> parcelList = new LinkedList<PetasosParcel>();
    	petasosParcelCache.entrySet().forEach(entry -> parcelList.add(entry.getValue()));
    	return(parcelList);
    }
    
    public List<PetasosParcel> getParcelSetByState(PetasosParcelProcessingStatusEnum status){
    	LOG.debug(".getParcelSet(): Entry, status --> {}", status);
    	List<PetasosParcel> parcelList = new LinkedList<PetasosParcel>();
    	Iterator<PetasosParcel> parcelListIterator = getParcelSet().iterator();
    	while(parcelListIterator.hasNext()) {
    		PetasosParcel currentParcel = parcelListIterator.next();
    		if(currentParcel.hasParcelJobCard()) {
    			if(currentParcel.getParcelJobCard().getParcelStatus()==status) {
    				parcelList.add(currentParcel);
    			}
    		}
    	}
    	return(parcelList);
    }    
    
    public List<PetasosParcel> getActiveParcelSet(){
    	List<PetasosParcel> parcelList = getParcelSetByState(PetasosParcelProcessingStatusEnum.PARCEL_STATUS_ACTIVE);
    	return(parcelList);
    }
    
    public List<PetasosParcel> getFinishedParcelSet(){
    	List<PetasosParcel> parcelList = getParcelSetByState(PetasosParcelProcessingStatusEnum.PARCEL_STATUS_FINISHED);
    	return(parcelList);
    }    
    
    public List<PetasosParcel> getFinalisedParcelSet(){
    	List<PetasosParcel> parcelList = getParcelSetByState(PetasosParcelProcessingStatusEnum.PARCEL_STATUS_FINALISED);
    	return(parcelList);
    }    
    
    public List<PetasosParcel> getInProgressParcelSet(){
    	LOG.debug(".getInProgressParcelSet(): Entry");
    	List<PetasosParcel> parcelList = new LinkedList<PetasosParcel>();
    	parcelList.addAll(getParcelSetByState(PetasosParcelProcessingStatusEnum.PARCEL_STATUS_ACTIVE));
    	parcelList.addAll(getParcelSetByState(PetasosParcelProcessingStatusEnum.PARCEL_STATUS_INITIATED));
    	parcelList.addAll(getParcelSetByState(PetasosParcelProcessingStatusEnum.PARCEL_STATUS_REGISTERED));
    	return(parcelList);
    }
    
    public List<PetasosParcel> getParcelByParcelType(FDN parcelTypeID){
    	LOG.debug(".getInProgressParcelSet(): Entry, parcelTypeID --> {}" + parcelTypeID);
    	List<PetasosParcel> parcelList = new LinkedList<PetasosParcel>();
    	Iterator<PetasosParcel> parcelListIterator = getParcelSet().iterator();
    	while(parcelListIterator.hasNext()) {
    		PetasosParcel currentParcel = parcelListIterator.next();
    		if(currentParcel.hasParcelTypeID()) {
    			if(currentParcel.getParcelTypeID().equals(parcelTypeID)) {
    				parcelList.add(currentParcel);
    			}
    		}
    	}    	
    	return(parcelList);
    }

    public void updateParcel(PetasosParcel newParcel){
		LOG.debug(".updateParcel() Entry, parcel --> {}", newParcel);
		if(newParcel == null) {
			throw (new IllegalArgumentException("newParcel is null"));
		}
		if(petasosParcelCache.containsKey(newParcel.getParcelInstanceID())) {
			petasosParcelCache.remove(newParcel.getParcelInstanceID());
		}
		petasosParcelCache.put(newParcel.getParcelInstanceID(), newParcel);
	}

	public PetasosParcel getCurrentParcelForWUP(FDN wupInstanceID, FDN uowInstanceID){
		LOG.debug(".getCurrentParcel(): Entry, wupInstanceID --> {}" + wupInstanceID);
		List<PetasosParcel> parcelList = new LinkedList<PetasosParcel>();
		Iterator<PetasosParcel> parcelListIterator = getParcelSet().iterator();
		while(parcelListIterator.hasNext()) {
			PetasosParcel currentParcel = parcelListIterator.next();
			if(currentParcel.hasAssociatedWUPInstanceID()) {
				if(currentParcel.getAssociatedWUPInstanceID().equals(wupInstanceID)) {
					if(currentParcel.hasActualUoW())
						if(currentParcel.getActualUoW().getUoWInstanceID().equals(uowInstanceID)){
							return(currentParcel);
					}
				}
			}
		}
		return(null);
	}

}
