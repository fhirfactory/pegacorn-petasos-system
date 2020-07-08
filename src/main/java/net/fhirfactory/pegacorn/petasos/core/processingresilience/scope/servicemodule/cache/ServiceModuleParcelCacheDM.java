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
package net.fhirfactory.pegacorn.petasos.core.processingresilience.scope.servicemodule.cache;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Singleton;

import net.fhirfactory.pegacorn.petasos.model.resilience.parcel.ResilienceParcel;
import net.fhirfactory.pegacorn.petasos.model.resilience.parcel.ResilienceParcelProcessingStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fhirfactory.pegacorn.common.model.FDNToken;

@Singleton
public class ServiceModuleParcelCacheDM {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceModuleParcelCacheDM.class);

    private ConcurrentHashMap<FDNToken, ResilienceParcel> petasosParcelCache;

    public ServiceModuleParcelCacheDM() {
        petasosParcelCache = new ConcurrentHashMap<FDNToken, ResilienceParcel>();
    }

    public void addParcel(ResilienceParcel parcel) {
        LOG.debug(".addParcel(): Entry, parcel --> {}", parcel);
        if (parcel == null) {
            return;
        }
        if (!parcel.hasParcelInstanceID()) {
            return;
        }
        FDNToken parcelInstanceID = parcel.getInstanceID();
        petasosParcelCache.put(parcelInstanceID, parcel);
    }

    public ResilienceParcel getParcelInstance(FDNToken parcelInstanceID) {
        LOG.debug(".getParcelInstance(): Entry, parcelInstanceID --> {}", parcelInstanceID);
        if (petasosParcelCache.containsKey(parcelInstanceID)) {
            return (petasosParcelCache.get(parcelInstanceID));
        }
        return (null);
    }

    public void removeParcel(ResilienceParcel parcel) {
        LOG.debug(".removeParcel(): Entry, parcel --> {}", parcel);
        if (parcel == null) {
            return;
        }
        if (!parcel.hasParcelInstanceID()) {
            return;
        }
        petasosParcelCache.remove(parcel.getInstanceID());
    }

    public void removeParcel(FDNToken parcelInstanceID) {
        LOG.debug(".removeParcel(): Entry, parcelInstanceID --> {}", parcelInstanceID);
        if (parcelInstanceID == null) {
            return;
        }
        petasosParcelCache.remove(parcelInstanceID);
    }

    public List<ResilienceParcel> getParcelSet() {
        LOG.debug(".getParcelSet(): Entry");
        List<ResilienceParcel> parcelList = new LinkedList<ResilienceParcel>();
        petasosParcelCache.entrySet().forEach(entry -> parcelList.add(entry.getValue()));
        return (parcelList);
    }

    public List<ResilienceParcel> getParcelSetByState(ResilienceParcelProcessingStatusEnum status) {
        LOG.debug(".getParcelSet(): Entry, status --> {}", status);
        List<ResilienceParcel> parcelList = new LinkedList<ResilienceParcel>();
        Iterator<ResilienceParcel> parcelListIterator = getParcelSet().iterator();
        while (parcelListIterator.hasNext()) {
            ResilienceParcel currentParcel = parcelListIterator.next();
            if (currentParcel.hasProcessingStatus()) {
                if (currentParcel.getProcessingStatus() == status) {
                    parcelList.add(currentParcel);
                }
            }
        }
        return (parcelList);
    }

    public List<ResilienceParcel> getActiveParcelSet() {
        List<ResilienceParcel> parcelList = getParcelSetByState(ResilienceParcelProcessingStatusEnum.PARCEL_STATUS_ACTIVE);
        return (parcelList);
    }

    public List<ResilienceParcel> getFinishedParcelSet() {
        List<ResilienceParcel> parcelList = getParcelSetByState(ResilienceParcelProcessingStatusEnum.PARCEL_STATUS_FINISHED);
        return (parcelList);
    }

    public List<ResilienceParcel> getFinalisedParcelSet() {
        List<ResilienceParcel> parcelList = getParcelSetByState(ResilienceParcelProcessingStatusEnum.PARCEL_STATUS_FINALISED);
        return (parcelList);
    }

    public List<ResilienceParcel> getInProgressParcelSet() {
        LOG.debug(".getInProgressParcelSet(): Entry");
        List<ResilienceParcel> parcelList = new LinkedList<ResilienceParcel>();
        parcelList.addAll(getParcelSetByState(ResilienceParcelProcessingStatusEnum.PARCEL_STATUS_ACTIVE));
        parcelList.addAll(getParcelSetByState(ResilienceParcelProcessingStatusEnum.PARCEL_STATUS_INITIATED));
        parcelList.addAll(getParcelSetByState(ResilienceParcelProcessingStatusEnum.PARCEL_STATUS_REGISTERED));
        return (parcelList);
    }

    public List<ResilienceParcel> getParcelByParcelType(FDNToken parcelTypeID) {
        LOG.debug(".getInProgressParcelSet(): Entry, parcelTypeID --> {}" + parcelTypeID);
        List<ResilienceParcel> parcelList = new LinkedList<ResilienceParcel>();
        Iterator<ResilienceParcel> parcelListIterator = getParcelSet().iterator();
        while (parcelListIterator.hasNext()) {
            ResilienceParcel currentParcel = parcelListIterator.next();
            if (currentParcel.hasParcelTypeID()) {
                if (currentParcel.getTypeID().equals(parcelTypeID)) {
                    parcelList.add(currentParcel);
                }
            }
        }
        return (parcelList);
    }

    public void updateParcel(ResilienceParcel newParcel) {
        LOG.debug(".updateParcel() Entry, parcel --> {}", newParcel);
        if (newParcel == null) {
            throw (new IllegalArgumentException("newParcel is null"));
        }
        if (petasosParcelCache.containsKey(newParcel.getInstanceID())) {
            petasosParcelCache.remove(newParcel.getInstanceID());
        }
        petasosParcelCache.put(newParcel.getInstanceID(), newParcel);
    }

    public ResilienceParcel getCurrentParcelForWUP(FDNToken wupInstanceID, FDNToken uowInstanceID) {
        LOG.debug(".getCurrentParcel(): Entry, wupInstanceID --> {}" + wupInstanceID);
        List<ResilienceParcel> parcelList = new LinkedList<ResilienceParcel>();
        Iterator<ResilienceParcel> parcelListIterator = getParcelSet().iterator();
        while (parcelListIterator.hasNext()) {
            ResilienceParcel currentParcel = parcelListIterator.next();
            if (currentParcel.hasAssociatedWUPInstanceID()) {
                if (currentParcel.getAssociatedWUPInstanceID().equals(wupInstanceID)) {
                    if (currentParcel.hasActualUoW()) {
                        if (currentParcel.getActualUoW().getInstanceID().equals(uowInstanceID)) {
                            return (currentParcel);
                        }
                    }
                }
            }
        }
        return (null);
    }

}
