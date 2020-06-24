/*
 * Copyright (c) 2020 Mark A. Hunter (ACT Health)
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

package net.fhirfactory.pegacorn.petasos.core.node.common;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import net.fhirfactory.pegacorn.common.model.FDN;

/**
 * 
 * @author Mark A. Hunter
 *
 */

public class DownstreamParcelStatusSet {
	LinkedHashMap<FDN, DownstreamParcelStatus> parcelStatusSet;
	
	public DownstreamParcelStatusSet() {
		parcelStatusSet = new LinkedHashMap<FDN, DownstreamParcelStatus>();
	}
	
	public void addDownstreamWUPInstanceID(FDN downstreamWUPID) {
		if(downstreamWUPID==null) {
			return;
		}
		if(parcelStatusSet.containsKey(downstreamWUPID)) {
			return;
		}
		DownstreamParcelStatus newStatus = new DownstreamParcelStatus();
		parcelStatusSet.put(downstreamWUPID, newStatus);
	}
	
	public void updateParcelRegistrationStatus(FDN downstreamWUPID, FDN downstreamParcelID) {
		if((downstreamWUPID==null) || (downstreamParcelID==null)) {
			return;
		}
		if(parcelStatusSet.containsKey(downstreamWUPID)) {
			DownstreamParcelStatus registrationStatus = parcelStatusSet.get(downstreamWUPID);
			registrationStatus.registerSuccessorParcel(downstreamParcelID);
			parcelStatusSet.remove(downstreamWUPID);
			parcelStatusSet.put(downstreamWUPID,registrationStatus );
		} else {
			DownstreamParcelStatus registrationStatus = new DownstreamParcelStatus();
			registrationStatus.registerSuccessorParcel(downstreamParcelID);
			parcelStatusSet.put(downstreamWUPID,registrationStatus );
		}
	}
	
	public Set<FDN> getNonParcelRegisteredWUPInstance(){
		HashSet<FDN> wupSet = new HashSet<FDN>();
		if(parcelStatusSet.isEmpty()) {
			return(wupSet);
		}
		Set<FDN> fdnSet = parcelStatusSet.keySet();
		Iterator<FDN> fdnSetIterator = fdnSet.iterator();
		while(fdnSetIterator.hasNext()) {
			FDN currentFDN = fdnSetIterator.next();
			DownstreamParcelStatus registrationStatus = parcelStatusSet.get(currentFDN);
			if(registrationStatus.getSuccessorStatus() == DownstreamParcelStatusEnum.PARCEL_FINALISATION_STATUS_NOT_REGISTERED) {
				wupSet.add(currentFDN);
			}
		}
		return(wupSet);
	}

}
