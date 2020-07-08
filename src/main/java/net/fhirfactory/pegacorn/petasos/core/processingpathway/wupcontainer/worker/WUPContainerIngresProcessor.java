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

package net.fhirfactory.pegacorn.petasos.core.processingpathway.wupcontainer.worker;

import net.fhirfactory.pegacorn.common.model.FDNToken;
import net.fhirfactory.pegacorn.deploymentproperties.PetasosProperties;
import net.fhirfactory.pegacorn.petasos.core.PetasosServicesBroker;
import net.fhirfactory.pegacorn.petasos.core.processingpathway.naming.RouteElementNames;
import net.fhirfactory.pegacorn.petasos.core.servicemodule.manager.ServiceModuleIM;
import net.fhirfactory.pegacorn.petasos.model.pathway.ContinuityID;
import net.fhirfactory.pegacorn.petasos.model.pathway.WorkUnitTransportPacket;
import net.fhirfactory.pegacorn.petasos.model.resilience.activitymatrix.ParcelStatusElement;
import net.fhirfactory.pegacorn.petasos.model.resilience.parcel.ResilienceParcelProcessingStatusEnum;
import net.fhirfactory.pegacorn.petasos.model.uow.UoW;
import net.fhirfactory.pegacorn.petasos.model.wup.WUPActivityStatusEnum;
import net.fhirfactory.pegacorn.petasos.model.wup.WUPClusterModeEnum;
import net.fhirfactory.pegacorn.petasos.model.wup.WUPJobCard;
import net.fhirfactory.pegacorn.petasos.model.wup.WUPSystemModeEnum;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.time.Instant;
import java.util.Date;

public class WUPContainerIngresProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(WUPContainerIngresProcessor.class);
    private RouteElementNames elementNames;

    @Inject
    PetasosServicesBroker petasosServicesBroker;

    @Inject
    ServiceModuleIM moduleIM;

    public WorkUnitTransportPacket ingresContentProcessor(WorkUnitTransportPacket incomingPacket, Exchange camelExchange, FDNToken wupTypeID, FDNToken wupInstanceID) {
        LOG.debug(".ingresContentProcessor(): Enter, incomingPacket --> {}, wupTypeID --> {}, wupInstanceID -->{}", incomingPacket, wupTypeID, wupInstanceID);
        elementNames = new RouteElementNames(wupTypeID);
        LOG.trace(".ingresContentProcessor(): Now, check if this the 1st time the associated UoW has been (attempted to be) processed");
        WorkUnitTransportPacket transportPacket;
        if (incomingPacket.getIsARetry()) {
            LOG.trace(".ingresContentProcessor(): This is a recovery or retry iteration of processing this UoW, so send to .alternativeIngresContentProcessor()");
            transportPacket = alternativeIngresContentProcessor(incomingPacket, camelExchange, wupTypeID, wupInstanceID);
        } else {
            LOG.trace(".ingresContentProcessor(): This is the 1st time this UoW is being processed, so send to .standardIngresContentProcessor()");
            transportPacket = standardIngresContentProcessor(incomingPacket, camelExchange, wupTypeID, wupInstanceID);
        }
        int waitTime = moduleIM.getWorkUnitActivitySleepInterval();
        boolean waitState = true;
        WUPJobCard jobCard = transportPacket.getCurrentJobCard();
        while (waitState) {
            switch (jobCard.getCurrentStatus()) {
                case WUP_ACTIVITY_STATUS_WAITING:
                    jobCard.setRequestedStatus(WUPActivityStatusEnum.WUP_ACTIVITY_STATUS_EXECUTING);
                    ParcelStatusElement parcelStatus = petasosServicesBroker.synchroniseJobCard(jobCard);
                    if (!parcelStatus.getHasClusterFocus()) {
                        jobCard.setCurrentStatus(WUPActivityStatusEnum.WUP_ACTIVITY_STATUS_WAITING);
                        waitState = true;
                        break;
                    }
                    if (jobCard.getGrantedStatus() == WUPActivityStatusEnum.WUP_ACTIVITY_STATUS_EXECUTING) {
                        jobCard.setCurrentStatus(WUPActivityStatusEnum.WUP_ACTIVITY_STATUS_EXECUTING);
                        waitState = false;
                        break;
                    }
                    break;
                case WUP_ACTIVITY_STATUS_EXECUTING:
                case WUP_ACTIVITY_STATUS_FINISHED:
                case WUP_ACTIVITY_STATUS_FAILED:
                case WUP_ACTIVITY_STATUS_CANCELED:
                default:
                    jobCard.setIsToBeDiscarded(true);
                    waitState = false;
                    jobCard.setCurrentStatus(WUPActivityStatusEnum.WUP_ACTIVITY_STATUS_CANCELED);
                    jobCard.setRequestedStatus(WUPActivityStatusEnum.WUP_ACTIVITY_STATUS_CANCELED);
            }
            if (waitState) {
                try {
                    Thread.sleep(waitTime);
                } catch (InterruptedException e) {
                    LOG.trace(".ingresContentProcessor(): Something interrupted my nap! reason --> {}", e.getStackTrace());
                }
            }
        }
        if (jobCard.getIsToBeDiscarded()) {
            ParcelStatusElement currentParcelStatus = transportPacket.getCurrentParcelStatus();
            currentParcelStatus.setRequiresRetry(true);
            currentParcelStatus.setParcelStatus(ResilienceParcelProcessingStatusEnum.PARCEL_STATUS_FAILED);
        }
        LOG.debug(".ingresContentProcessor(): Exit, WUP Job Card --> {}", jobCard);
        return (transportPacket);
    }

    public WorkUnitTransportPacket standardIngresContentProcessor(WorkUnitTransportPacket incomingPacket, Exchange camelExchange, FDNToken wupTypeID, FDNToken wupInstanceID) {
        LOG.debug(".ingresContentProcessor(): Enter, incomingPacket --> {}, wupTypeID --> {}, wupInstanceID --> {}", incomingPacket, wupTypeID, wupInstanceID);
        UoW theUoW = incomingPacket.getPayload();
        LOG.trace(".standardIngresContentProcessor(): Creating a new ContinuityID/ActivityID");
        FDNToken localWUPInstanceID = new FDNToken(wupInstanceID);
        FDNToken localWUPTypeID = new FDNToken(wupTypeID);
        ContinuityID oldActivityID = incomingPacket.getCurrentJobCard().getCardID();
        ContinuityID newActivityID = new ContinuityID();
        FDNToken previousPresentParcelInstanceID = oldActivityID.getPresentParcelInstanceID();
        FDNToken previousPresentEpisodeID = oldActivityID.getPresentParcelInstanceID();
        FDNToken previousPresentWUPInstanceID = oldActivityID.getPresentWUPInstanceID();
        FDNToken previousPresentWUPTypeID = oldActivityID.getPresentWUPTypeID();
        newActivityID.setPreviousParcelInstanceID(previousPresentParcelInstanceID);
        newActivityID.setPreviousParcelEpisodeID(previousPresentEpisodeID);
        newActivityID.setPreviousWUPInstanceID(previousPresentWUPInstanceID);
        newActivityID.setPreviousWUPTypeID(previousPresentWUPTypeID);
        newActivityID.setPresentWUPTypeID(localWUPTypeID);
        newActivityID.setPresentWUPInstanceID(localWUPInstanceID);
        LOG.trace(".standardIngresContentProcessor(): Creating new JobCard");
        WUPJobCard activityJobCard = new WUPJobCard(newActivityID, WUPActivityStatusEnum.WUP_ACTIVITY_STATUS_WAITING, WUPActivityStatusEnum.WUP_ACTIVITY_STATUS_EXECUTING, WUPClusterModeEnum.CLUSTER_SINGLE_STANDALONE, WUPSystemModeEnum.SYSTEMWIDE_SINGLE_STANDALONE, Date.from(Instant.now()));
        LOG.trace(".standardIngresContentProcessor(): Registering the Work Unit Activity using the ContinuityID --> {} and UoW --> {}", newActivityID, theUoW);
        ParcelStatusElement statusElement = petasosServicesBroker.registerStandardWorkUnitActivity(activityJobCard, theUoW);
        LOG.trace(".standardIngresContentProcessor(): Let's check the status of everything");
        switch (statusElement.getParcelStatus()) {
            case PARCEL_STATUS_REGISTERED:
            case PARCEL_STATUS_ACTIVE_ELSEWHERE:
                LOG.trace(".standardIngresContentProcessor(): The Parcel is either Registered or Active_Elsewhere - both are acceptable at this point");
                break;
            case PARCEL_STATUS_FAILED:
            case PARCEL_STATUS_ACTIVE:
            case PARCEL_STATUS_FINALISED_ELSEWHERE:
            case PARCEL_STATUS_FINALISED:
            case PARCEL_STATUS_FINISHED_ELSEWHERE:
            case PARCEL_STATUS_FINISHED:
            case PARCEL_STATUS_INITIATED:
            default:
                LOG.trace(".standardIngresContentProcessor(): The Parcel is doing something odd, none of the above states should be in-play, so cancel");
                statusElement.setParcelStatus(ResilienceParcelProcessingStatusEnum.PARCEL_STATUS_FAILED);
                statusElement.setRequiresRetry(true);
                activityJobCard.setRequestedStatus(WUPActivityStatusEnum.WUP_ACTIVITY_STATUS_CANCELED);
                activityJobCard.setIsToBeDiscarded(true);
        }
        WorkUnitTransportPacket transportPacket = new WorkUnitTransportPacket(elementNames.getEndPointWUPContainerIngresGatekeeperIngres(),Date.from(Instant.now()),incomingPacket.getPayload());
        return (transportPacket);
    }

    public WorkUnitTransportPacket alternativeIngresContentProcessor(WorkUnitTransportPacket incomingPacket, Exchange camelExchange, FDNToken wupTypeID, FDNToken wupInstanceID) {
        LOG.debug(".alternativeIngresContentProcessor(): Enter, incomingPacket --> {}, wupTypeID --> {}, wupInstanceID --> {}", incomingPacket, wupTypeID, wupInstanceID);
        return (incomingPacket);
    }
}
