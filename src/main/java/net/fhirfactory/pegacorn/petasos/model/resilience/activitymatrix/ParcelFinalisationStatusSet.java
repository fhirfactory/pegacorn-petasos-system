/*
 * Copyright (c) 2020 mhunter
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

package net.fhirfactory.pegacorn.petasos.model.resilience.activitymatrix;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.fhirfactory.pegacorn.common.model.FDNToken;

/**
 * 
 * @author Mark A. Hunter
 *
 */

public class ParcelFinalisationStatusSet {
	ConcurrentHashMap<FDNToken, ParcelFinalisationRegistrationStatus> parcelStatusSet;
	
	public ParcelFinalisationStatusSet() {
		parcelStatusSet = new ConcurrentHashMap<FDNToken, ParcelFinalisationRegistrationStatus>();
	}
	
	public void addDownstreamWUPInstanceID(FDNToken downstreamWUPID) {
		if(downstreamWUPID==null) {
			return;
		}
		if(parcelStatusSet.containsKey(downstreamWUPID)) {
			return;
		}
		ParcelFinalisationRegistrationStatus newStatus = new ParcelFinalisationRegistrationStatus();
		parcelStatusSet.put(downstreamWUPID, newStatus);
	}
	
	public void updateParcelRegistrationStatus(FDNToken downstreamWUPID, FDNToken downstreamParcelID) {
		if((downstreamWUPID==null) || (downstreamParcelID==null)) {
			return;
		}
		if(parcelStatusSet.containsKey(downstreamWUPID)) {
			ParcelFinalisationRegistrationStatus registrationStatus = parcelStatusSet.get(downstreamWUPID);
			registrationStatus.registerSuccessorParcel(downstreamParcelID);
			parcelStatusSet.remove(downstreamWUPID);
			parcelStatusSet.put(downstreamWUPID,registrationStatus );
		} else {
			ParcelFinalisationRegistrationStatus registrationStatus = new ParcelFinalisationRegistrationStatus();
			registrationStatus.registerSuccessorParcel(downstreamParcelID);
			parcelStatusSet.put(downstreamWUPID,registrationStatus );
		}
	}
	
	public Set<FDNToken> getNonParcelRegisteredWUPInstance(){
		HashSet<FDNToken> wupSet = new HashSet<FDNToken>();
		if(parcelStatusSet.isEmpty()) {
			return(wupSet);
		}
		Set<FDNToken> fdnSet = parcelStatusSet.keySet();
		Iterator<FDNToken> fdnSetIterator = fdnSet.iterator();
		while(fdnSetIterator.hasNext()) {
			FDNToken currentFDN = fdnSetIterator.next();
			ParcelFinalisationRegistrationStatus registrationStatus = parcelStatusSet.get(currentFDN);
			if(registrationStatus.getSuccessorStatus() == ParcelFinalisationRegistrationStatusEnum.PARCEL_FINALISATION_STATUS_NOT_REGISTERED) {
				wupSet.add(currentFDN);
			}
		}
		return(wupSet);
	}

}
