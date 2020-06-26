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
import net.fhirfactory.pegacorn.petasos.core.node.common.DownstreamParcelStatusSet;

@ApplicationScoped
public class LocalNodeDownstreamParcelRegistryMap {
	private static final Logger LOG = LoggerFactory.getLogger(LocalNodeDownstreamParcelRegistryMap.class);
	
	private ConcurrentHashMap<FDN, DownstreamParcelStatusSet> downstreamParcelRegistrationMap;
	
	public LocalNodeDownstreamParcelRegistryMap() {
		downstreamParcelRegistrationMap = new ConcurrentHashMap<FDN, DownstreamParcelStatusSet>();
	}
	
	public void registerDownstreamWUPInterest(FDN parcelInstanceID, FDN downstreamWUPInstanceID) {
		LOG.debug(".registerDownstreamWUPInterest(): Entry, parcelInstanceID --> {}, downstreamWUPInstanceID --> {}",  parcelInstanceID, downstreamWUPInstanceID);
		if ((parcelInstanceID == null) || (downstreamWUPInstanceID == null)) {
			return;
		}
		if (downstreamParcelRegistrationMap.containsKey(parcelInstanceID)) {
			DownstreamParcelStatusSet downstreamWUPStatus = downstreamParcelRegistrationMap.get(parcelInstanceID);
			downstreamWUPStatus.addDownstreamWUPInstanceID(downstreamWUPInstanceID);
		} else {
			DownstreamParcelStatusSet downstreamWUPStatus = new DownstreamParcelStatusSet();
			downstreamWUPStatus.addDownstreamWUPInstanceID(downstreamWUPInstanceID);
			downstreamParcelRegistrationMap.put(parcelInstanceID, downstreamWUPStatus);
		}		
	}
	
	public void registerDownstreamParcel(FDN parcelInstanceID, FDN downstreamWUPInstanceID, FDN downstreamParcelInstanceID) {
		LOG.debug(
				".registerDownstreamParcel(): Entry, parcelInstanceID --> {}, downstreamWUPInstanceID --> {}, downstreamParcelInstanceID --> {} ",
				parcelInstanceID, downstreamWUPInstanceID, downstreamParcelInstanceID);
		if ((parcelInstanceID == null) || (downstreamWUPInstanceID == null) || (downstreamParcelInstanceID == null)) {
			return;
		}
		if (downstreamParcelRegistrationMap.containsKey(parcelInstanceID)) {
			DownstreamParcelStatusSet downstreamWUPStatus = downstreamParcelRegistrationMap.get(parcelInstanceID);
			downstreamWUPStatus.updateParcelRegistrationStatus(downstreamWUPInstanceID, downstreamParcelInstanceID);
		} else {
			DownstreamParcelStatusSet downstreamWUPStatus = new DownstreamParcelStatusSet();
			downstreamWUPStatus.updateParcelRegistrationStatus(downstreamWUPInstanceID, downstreamParcelInstanceID);
			downstreamParcelRegistrationMap.put(parcelInstanceID, downstreamWUPStatus);
		}
	}	
}
