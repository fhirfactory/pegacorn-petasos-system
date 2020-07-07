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

package net.fhirfactory.pegacorn.petasos.core.processingresilience.scope.servicemodule.engine;

import net.fhirfactory.pegacorn.common.model.FDNToken;
import net.fhirfactory.pegacorn.petasos.core.processingresilience.scope.servicemodule.cache.ServiceModuleActivityMatrixDM;
import net.fhirfactory.pegacorn.petasos.model.resilience.activitymatrix.ParcelStatusElement;
import net.fhirfactory.pegacorn.petasos.model.resilience.activitymatrix.ParcelStatusElementSet;
import net.fhirfactory.pegacorn.petasos.model.wup.WUPActivityStatusEnum;
import net.fhirfactory.pegacorn.petasos.model.wup.WUPJobCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.time.Instant;
import java.util.Date;

/**
 * @author Mark A. Hunter
 */

public class ResilienceServicesArbitrator {
    private static final Logger LOG = LoggerFactory.getLogger(ResilienceServicesArbitrator.class);

    @Inject
    ServiceModuleActivityMatrixDM activityMatrixDM;

    public ParcelStatusElement synchroniseJobCard(WUPJobCard submittedJobCard) {
        LOG.debug(".synchroniseJobCard(): Entry, submittedJobCard --> {}", submittedJobCard);
        LOG.trace(".synchroniseJobCard(): Some housekeeping - to be done asynchronously in next release!"); // TODO This should be asynchronous
        this.updateFailedParcelStatus();
        this.updateFinalisedParcelStatus();
        this.updateFinishedParcelStatus();
        LOG.trace(".synchroniseJobCard(): First, let's see what is in the Matrix for the ParcelEpisodeID");
        FDNToken parcelEpisodeID = submittedJobCard.getCardID().getPresentResilienceParcelOccurrenceKey();
        ParcelStatusElementSet parcelSet = activityMatrixDM.getElementStatusSet(parcelEpisodeID);
        if(LOG.isTraceEnabled()) {
            LOG.trace(".synchroniseJobCard(): The ParcelSet associated with the ParcelEpisodeID --> {} contains {} elements", parcelEpisodeID, parcelSet.getParcelInstanceSet().getElements().size());
        }
        FDNToken systemWideFocusedParcelInstanceID = activityMatrixDM.checkForExistingSystemWideFocusedElement(parcelEpisodeID);
        LOG.trace(".synchroniseJobCard(): The Parcel with systemWideFocusedParcel --> {}", systemWideFocusedParcelInstanceID);
        FDNToken clusterFocusedParcelInstanceID = activityMatrixDM.checkForExistingClusterFocusedElement(parcelEpisodeID);
        LOG.trace(".synchroniseJobCard(): The Parcel with clusterFocusedParcel --> {}", clusterFocusedParcelInstanceID);
        if (parcelSet.isEmpty()) {
            throw (new IllegalArgumentException(".synchroniseJobCard(): There are no ResilienceParcels for the given ParcelEpisodeID --> something is very wrong!"));
        }
        LOG.trace(".synchroniseJobCard(): Now, again, for this release - there should only be a single thread per ParcelEpisodeID, so set it to have FOCUS"); // TODO this should be asynchronous
        parcelSet.setClusteredFocusElement(submittedJobCard.getCardID().getPresentResilienceParcelInstanceID()); // TODO This should not be here!
        parcelSet.setSystemWideElement(submittedJobCard.getCardID().getPresentResilienceParcelInstanceID()); // TODO This should not be here!
        LOG.trace(".synchroniseJobCard(): Now, lets update the JobCard based on the ActivityMatrix");
        if(clusterFocusedParcelInstanceID.equals(submittedJobCard.getCardID().getPresentResilienceParcelInstanceID())){
            ParcelStatusElement clusterFocusElement = parcelSet.getElement(clusterFocusedParcelInstanceID);
            LOG.trace(".synchroniseJobCard(): The WUP has Focus, so letting it do what ever it wants!");
            LOG.debug(".synchroniseJobCard(): Exit, associated ParcelStatusElement --> {}", clusterFocusElement);
            return(clusterFocusElement);
        } else {
            ParcelStatusElement parcelStatus = parcelSet.getElement(submittedJobCard.getCardID().getPresentResilienceParcelInstanceID());
            submittedJobCard.setSuggestedStatus(WUPActivityStatusEnum.WUP_ACTIVITY_STATUS_WAITING);
            submittedJobCard.setUpdateDate(Date.from(Instant.now()));
            LOG.debug(".synchroniseJobCard(): Exit, associated ParcelStatusElement --> {}", parcelStatus);
            return(parcelStatus);
        }
    }

    public ParcelStatusElement registerWorkUnitActivity(WUPJobCard submittedJobCard){
        LOG.trace(".registerParcel(): Now register the parcel with the ActivityMatrix");
        FDNToken parcelInstanceID = submittedJobCard.getCardID().getPresentResilienceParcelInstanceID();
        FDNToken parcelEpisodeID = submittedJobCard.getCardID().getPresentResilienceParcelOccurrenceKey();
        FDNToken wupInstanceID = submittedJobCard.getCardID().getPresentWUPInstanceID();
        FDNToken wupTypeID = submittedJobCard.getCardID().getPresentWUPTypeID();
        ParcelStatusElement statusElement = activityMatrixDM.registerWorkUnitActivity(parcelInstanceID,parcelEpisodeID,wupInstanceID,wupTypeID);
        return(statusElement);
    }

    public void purgeWorkUnitActivity(WUPJobCard submittedJobCard){

    }

    public void registerDownstreamWUP(FDNToken parcelInstanceID, FDNToken interestedWUPInstanceID) {
        activityMatrixDM.registerDownstreamWUPInterest(parcelInstanceID,interestedWUPInstanceID);
    }

    public void updateFinalisedParcelStatus(){
        LOG.debug(".updateFinalisedParcelStatus(): Entry");
        activityMatrixDM.checkForFinalisedParcelForParcelEpisode();
    }

    public void updateFinishedParcelStatus(){
        LOG.debug(".updateFinishedParcelStatus(): Entry");
        activityMatrixDM.checkForFinishedParcelForParcelEpisode();
    }

    public void updateFailedParcelStatus(){
        LOG.debug(".updateFailedParcelStatus(): Entry");
        activityMatrixDM.checkForFailedParcelForParcelEpisode();
    }

    public void updateTimeoutsForParcelStatus() {
        LOG.debug(".updateTimeoutsForParcelStatus(): Entry");
        // TODO: At some point...
    }
}
