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

package net.fhirfactory.pegacorn.petasos.core;

import net.fhirfactory.pegacorn.common.model.FDNToken;
import net.fhirfactory.pegacorn.petasos.core.processingresilience.scope.servicemodule.engine.ResilienceServicesArbitrator;
import net.fhirfactory.pegacorn.petasos.model.resilience.activitymatrix.ParcelStatusElement;
import net.fhirfactory.pegacorn.petasos.model.resilience.parcel.ResilienceParcel;
import net.fhirfactory.pegacorn.petasos.model.uow.UoW;
import net.fhirfactory.pegacorn.petasos.model.wup.WUPJobCard;
import net.fhirfactory.pegacorn.petasos.core.processingresilience.scope.servicemodule.engine.ResilienceServicesIM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class PetasosServicesBroker {
    private static final Logger LOG = LoggerFactory.getLogger(PetasosServicesBroker.class);

    @Inject
    ResilienceServicesIM nodeIM;

    @Inject
    ResilienceServicesArbitrator resilienceArbitrator;

    public ParcelStatusElement registerStandardWorkUnitActivity(WUPJobCard jobCard, UoW initialUoW){
        if((jobCard == null) || (initialUoW == null)){
            throw( new IllegalArgumentException(".registerWorkUnitActivity(): jobCard or initialUoW are null"));
        }
        FDNToken wupTypeID = jobCard.getCardID().getPresentWUPTypeID();
        FDNToken wupInstanceID = jobCard.getCardID().getPresentWUPInstanceID();
        FDNToken previousParcelInstanceID = jobCard.getCardID().getPreviousParcelInstanceID();
        ResilienceParcel newParcel = nodeIM.registerParcel(wupTypeID,wupInstanceID,previousParcelInstanceID, initialUoW, false );
        jobCard.getCardID().setPresentParcelInstanceID(newParcel.getInstanceID());
        ParcelStatusElement statusElement = resilienceArbitrator.synchroniseJobCard(jobCard);
        return(statusElement);
    }

    public ParcelStatusElement registerSystemEdgeWUA(WUPJobCard jobCard, UoW initialUoW){
        if((jobCard == null) || (initialUoW == null)){
            throw( new IllegalArgumentException(".registerWorkUnitActivity(): jobCard or initialUoW are null"));
        }
        FDNToken wupTypeID = jobCard.getCardID().getPresentWUPTypeID();
        FDNToken wupInstanceID = jobCard.getCardID().getPresentWUPInstanceID();
        FDNToken previousParcelInstanceID = jobCard.getCardID().getPreviousParcelInstanceID();
        ResilienceParcel newParcel = nodeIM.registerParcel(wupTypeID,wupInstanceID,previousParcelInstanceID, initialUoW, true );
        jobCard.getCardID().setPresentParcelInstanceID(newParcel.getInstanceID());
        ParcelStatusElement statusElement = resilienceArbitrator.synchroniseJobCard(jobCard);
        return(statusElement);
    }

    public ParcelStatusElement notifyStartOfWorkUnitActivity(WUPJobCard jobCard, UoW startedUoW){
        if((jobCard == null) || (startedUoW == null)){
            throw( new IllegalArgumentException(".registerWorkUnitActivity(): jobCard or startedUoW are null"));
        }
        ResilienceParcel finishedParcel = nodeIM.notifyParcelProcessingStart(jobCard.getCardID().getPresentParcelInstanceID());
        ParcelStatusElement statusElement = resilienceArbitrator.synchroniseJobCard(jobCard);
        return(statusElement);
    }

    public ParcelStatusElement notifyFinishOfWorkUnitActivity(WUPJobCard jobCard, UoW finishedUoW){
        if((jobCard == null) || (finishedUoW == null)){
            throw( new IllegalArgumentException(".registerWorkUnitActivity(): jobCard or finishedUoW are null"));
        }
        ResilienceParcel finishedParcel = nodeIM.notifyParcelProcessingFinish(jobCard.getCardID().getPresentParcelInstanceID(), finishedUoW);
        ParcelStatusElement statusElement = resilienceArbitrator.synchroniseJobCard(jobCard);
        return(statusElement);
    }

    public ParcelStatusElement notifyFinalisationOfWorkUnitActivity(WUPJobCard jobCard){
        if((jobCard == null)){
            throw( new IllegalArgumentException(".registerWorkUnitActivity(): jobCard is null"));
        }
        ResilienceParcel finishedParcel = nodeIM.notifyParcelProcessingFinalisation(jobCard.getCardID().getPresentParcelInstanceID());
        ParcelStatusElement statusElement = resilienceArbitrator.synchroniseJobCard(jobCard);
        return(statusElement);
    }

    public ParcelStatusElement notifyFailureOfWorkUnitActivity(WUPJobCard jobCard, UoW failedUoW){
        if((jobCard == null) || (failedUoW == null)){
            throw( new IllegalArgumentException(".notifyFailureOfWorkUnitActivity(): jobCard or finishedUoW are null"));
        }
        ResilienceParcel failedParcel = nodeIM.notifyParcelProcessingFinish(jobCard.getCardID().getPresentParcelInstanceID(), failedUoW);
        ParcelStatusElement statusElement = resilienceArbitrator.synchroniseJobCard(jobCard);
        return(statusElement);
    }

    public void notifyPurgeOfWorkUnitActivity(WUPJobCard jobCard){
        if((jobCard == null)){
            throw( new IllegalArgumentException(".registerWorkUnitActivity(): jobCard is null"));
        }
        if(!jobCard.hasCardID()) {
            return;
        }
        if(!jobCard.getCardID().hasPresentResilienceParcelInstanceID()){
            return;
        }
        nodeIM.notifyParcelProcessingPurge(jobCard.getCardID().getPresentParcelInstanceID());
    }

    public ParcelStatusElement synchroniseJobCard(WUPJobCard existingJobCard){
        ParcelStatusElement statusElement = resilienceArbitrator.synchroniseJobCard(existingJobCard);
        return(statusElement);
    }

    public void registerDownstreamWUP(FDNToken parcelID, FDNToken interestedWUP){
        resilienceArbitrator.registerDownstreamWUP(parcelID, interestedWUP);
    }

    public ResilienceParcel getUnprocessedParcel(FDNToken wupTypeID){
        // TODO - this is the mechanism to re-start on failure, not currently implemented.
        return(null);
    }

}
