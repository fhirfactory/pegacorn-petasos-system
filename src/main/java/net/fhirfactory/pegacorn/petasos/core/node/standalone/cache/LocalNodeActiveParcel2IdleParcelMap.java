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

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fhirfactory.pegacorn.common.model.FDN;

/**
 * 
 * @author Mark A. Hunter
 *
 */
@ApplicationScoped
public class LocalNodeActiveParcel2IdleParcelMap {
	private static final Logger LOG = LoggerFactory.getLogger(LocalNodeActiveParcel2IdleParcelMap.class);

	private ConcurrentHashMap<FDN, FDN> activeParcel2TypeMap;

	public LocalNodeActiveParcel2IdleParcelMap() {
		activeParcel2TypeMap = new ConcurrentHashMap<FDN, FDN>();
	}

	public void setActivteParcelID(FDN parcelTypeID, FDN activeParcelInstanceID) {
		LOG.debug(".setActivteParcelID(): Entry, parcelTypeID --> {}, activeParcelInstanceID --> {}", parcelTypeID,
				activeParcelInstanceID);
		if ((parcelTypeID == null) || (activeParcelInstanceID == null)) {
			return;
		}
		if (activeParcel2TypeMap.containsKey(parcelTypeID)) {
			if (activeParcel2TypeMap.get(parcelTypeID).equals(activeParcelInstanceID)) {
				return;
			} else {
				activeParcel2TypeMap.remove(parcelTypeID);
			}
		}
		FDN tempParcelTypeID = new FDN(parcelTypeID);
		FDN tempParcelInstanceID = new FDN(activeParcelInstanceID);
		activeParcel2TypeMap.put(tempParcelTypeID, tempParcelInstanceID);
	}

	public FDN getActiveParcelID(FDN parcelTypeID) {
		LOG.debug(".getActiveParcelID(): Entry, parcelTypeID --> {}", parcelTypeID);
		if (parcelTypeID == null) {
			return (null);
		}
		Enumeration<FDN> parcelTypeEnumerator = activeParcel2TypeMap.keys();
		while (parcelTypeEnumerator.hasMoreElements()) {
			FDN currentParcelTypeID = parcelTypeEnumerator.nextElement();
			if (currentParcelTypeID.equals(parcelTypeID)) {
				FDN activeParcelID = new FDN(activeParcel2TypeMap.get(parcelTypeID));
				return (activeParcelID);
			}
		}
		return (null);
	}
}
