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

package net.fhirfactory.pegacorn.petasos.core.node.clustered.cache;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import net.fhirfactory.pegacorn.petasos.core.model.parcel.PetasosParcel;
import net.fhirfactory.pegacorn.petasos.core.model.parcel.PetasosParcelProcessingStatusEnum;
import org.apache.camel.CamelContext;
import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fhirfactory.pegacorn.common.model.FDN;
import net.fhirfactory.pegacorn.deploymentproperties.PetasosProperties;

/**
 * 
 * @author Mark A. Hunter
 * @author Scott Yeadon
 *
 */
@ApplicationScoped
public class ClusteredGeneralParcelMap {
	private static final Logger LOG = LoggerFactory.getLogger(ClusteredGeneralParcelMap.class);
	
	@Inject
	DefaultCacheManager petasosCacheManager;
	
    // Camel Context is required for creating a ProducerTemplate so we can send our
    // parcel and WUP state JSON to a camel consumer endpoint which will then forward
    // on to other sites.
    @Inject
    CamelContext camelContext;
    
    @Inject
    PetasosProperties petasosProperties;
    
    // The clustered cache for the PetasosParcels coordinated by this Petasos::Node (cluster) instance
    private Cache<FDN, PetasosParcel> petasosParcelCache;
    
    @PostConstruct
    public void start() {
        // get or create the clustered cache which will hold the transactions (aka Units of Work)
        petasosParcelCache = petasosCacheManager.getCache("petasos-parcel-cache", true);    	
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
    	Iterator<PetasosParcel> parcelListIterator = parcelList.iterator();
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
}
