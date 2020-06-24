package net.fhirfactory.pegacorn.petasos.core.node.standalone.cache;

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fhirfactory.pegacorn.common.model.FDN;

@ApplicationScoped
public class LocalNodeParcelTypeID2ActiveWUPInstanceIDMap {
	private static final Logger LOG = LoggerFactory.getLogger(LocalNodeParcelTypeID2ActiveWUPInstanceIDMap.class);

	private ConcurrentHashMap<FDN, FDN> parcelTypeID2WUPInstanceIDMap;

	public LocalNodeParcelTypeID2ActiveWUPInstanceIDMap() {
		parcelTypeID2WUPInstanceIDMap = new ConcurrentHashMap<FDN, FDN>();
	}

	public void setActivteWUPInstancelID(FDN parcelTypeID, FDN activeWUPInstanceID) {
		LOG.debug(".setActivteWUPInstancelID(): Entry, parcelTypeID --> {}, activeWUPInstanceID --> {}", parcelTypeID,
				activeWUPInstanceID);
		if ((parcelTypeID == null) || (activeWUPInstanceID == null)) {
			return;
		}
		if (parcelTypeID2WUPInstanceIDMap.containsKey(parcelTypeID)) {
			if (parcelTypeID2WUPInstanceIDMap.get(parcelTypeID).equals(activeWUPInstanceID)) {
				return;
			} else {
				parcelTypeID2WUPInstanceIDMap.remove(parcelTypeID);
			}
		}
		FDN tempParcelTypeID = new FDN(parcelTypeID);
		FDN tempWUPInstanceID = new FDN(activeWUPInstanceID);
		parcelTypeID2WUPInstanceIDMap.put(tempParcelTypeID, tempWUPInstanceID);
	}

	public FDN getActiveWUPInstanceID(FDN parcelTypeID) {
		LOG.debug(".getActiveWUPInstanceID(): Entry, parcelTypeID --> {}", parcelTypeID);
		if (parcelTypeID == null) {
			return (null);
		}
		Enumeration<FDN> parcelTypeEnumerator = parcelTypeID2WUPInstanceIDMap.keys();
		while (parcelTypeEnumerator.hasMoreElements()) {
			FDN currentParcelTypeID = parcelTypeEnumerator.nextElement();
			if (currentParcelTypeID.equals(parcelTypeID)) {
				FDN activeWUPInstancelID = new FDN(parcelTypeID2WUPInstanceIDMap.get(parcelTypeID));
				return (activeWUPInstancelID);
			}
		}
		return (null);
	}

}
