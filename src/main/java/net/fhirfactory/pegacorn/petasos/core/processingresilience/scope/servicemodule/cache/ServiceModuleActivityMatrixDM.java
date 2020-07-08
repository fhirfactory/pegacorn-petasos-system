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

import net.fhirfactory.pegacorn.common.model.FDNToken;
import net.fhirfactory.pegacorn.common.model.FDNTokenSet;
import net.fhirfactory.pegacorn.petasos.core.servicemodule.manager.ServiceModuleIM;
import net.fhirfactory.pegacorn.petasos.model.pathway.ContinuityID;
import net.fhirfactory.pegacorn.petasos.model.resilience.activitymatrix.ParcelFinalisationStatusSet;
import net.fhirfactory.pegacorn.petasos.model.resilience.activitymatrix.ParcelStatusElement;
import net.fhirfactory.pegacorn.petasos.model.resilience.activitymatrix.ParcelStatusElementSet;
import net.fhirfactory.pegacorn.petasos.model.resilience.parcel.ResilienceParcelProcessingStatusEnum;
import net.fhirfactory.pegacorn.petasos.model.resilience.parcel.ResilienceParcel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Instant;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This is the re-factored Resilience framework ActivityMatrix for ResilienceParcels. It is a representational
 * Matrix of all the Resilience Parcel activity within the ServiceModule - and has hooks for supporting updates
 * from Clustered and Multi-Site equivalents.
 */
@ApplicationScoped
public class ServiceModuleActivityMatrixDM {
    private static final Logger LOG = LoggerFactory.getLogger(ServiceModuleActivityMatrixDM.class);

    private ConcurrentHashMap<FDNToken, ParcelStatusElementSet> occurrenceKey2Instance2WUPMatrix;
    private ConcurrentHashMap<FDNToken, ParcelFinalisationStatusSet> downstreamParcelRegistrationMap;

    @Inject
    ServiceModuleParcelCacheDM parcelCacheDM;

    @Inject
    ServiceModuleIM moduleIM;


    public ServiceModuleActivityMatrixDM() {
        occurrenceKey2Instance2WUPMatrix = new ConcurrentHashMap<FDNToken, ParcelStatusElementSet>();
        downstreamParcelRegistrationMap = new ConcurrentHashMap<FDNToken, ParcelFinalisationStatusSet>();
    }

    /**
     * Establish a relationship between a ResilienceParcel (its Type and Instance) and the associated
     * WUP on which it is executing (its Type and Instance).
     *
     * @param parcelInstanceID The unique Identifier that distinctly represents this Resilience Parcel
     * @param parcelEpisodeID     The Identifier that distinctly represents this Resilience Parcel Type
     * @param wupInstanceID    The unique Identifier that distinctly represents the WUP instance currently executing (or
     *                         possible waiting to execute) the Resilience Parcel
     * @param wupTypeID        The Identifier that distinctly represents the WUP Type. Note, the WUP Type should be have
     *                         equivalents within the Cluster and at Other Sites
     */
    public ParcelStatusElement registerWorkUnitActivity(FDNToken parcelInstanceID, FDNToken parcelEpisodeID, FDNToken wupInstanceID, FDNToken wupTypeID) {
        LOG.debug(".registerWorkUnitActivity(): Entry, parcelInstanceID --> {}, parcelEpisodeID --> {}, wupInstanceID --> {}, wupTypeID --> {} ", parcelInstanceID, parcelEpisodeID, wupInstanceID, wupTypeID);
        // We do an individual check so as to support useful error messages
        if (wupInstanceID == null) {
            throw (new IllegalArgumentException(".registerParcelExecution(): wupInstanceID is null"));
        }
        if (parcelEpisodeID == null) {
            throw (new IllegalArgumentException(".registerParcelExecution(): parcelTypeID is null"));
        }
        if (parcelInstanceID == null) {
            throw (new IllegalArgumentException(".registerParcelExecution(): parcelInstanceID is null"));
        }
        if (wupTypeID == null) {
            throw (new IllegalArgumentException(".registerParcelExecution(): wupTypeID is null"));
        }
        LOG.trace(".registerWorkUnitActivity(): Check to see if there is already a registered set of Parcels for the supplied parcelEpisodeID --> {}", parcelEpisodeID);
        ParcelStatusElementSet elementSet;
        if (!occurrenceKey2Instance2WUPMatrix.containsKey(parcelEpisodeID)) {
            LOG.trace(".registerWorkUnitActivity(): Could not find a registered parcel set for the supplied parcelEpisodeID, adding it");
            elementSet = new ParcelStatusElementSet();
            occurrenceKey2Instance2WUPMatrix.put(parcelEpisodeID, elementSet);
        } else {
            LOG.trace(".registerWorkUnitActivity(): Found registered set of Parcels for the supplied parcelEpisodeID, extracting it");
            elementSet = occurrenceKey2Instance2WUPMatrix.get(parcelEpisodeID);
        }
        LOG.trace(".registerWorkUnitActivity(): Now adding my (newly created) ParcelStatusElement for the given ParcelInstanceID");
        // The assumption is that, if a registration is already present, we should assume it's all OK. We will, for
        // debugging purposes, check its consistency however.
        if (elementSet.hasElement(parcelInstanceID)) {
            LOG.trace(".registerWorkUnitActivity(): Found existing registered element for this parcelInstanceID, just checking (for traceability)");
            ParcelStatusElement registeredElement = elementSet.getElement(parcelInstanceID);
            if (LOG.isTraceEnabled()) {
                LOG.trace(".registerWorkUnitActivity(): registeredElement.getParcelInstanceID() = {}, new ParcelInstanceID --> {}", registeredElement.getParcelInstanceID(), parcelInstanceID);
                LOG.trace(".registerWorkUnitActivity(): registeredElement.getWupInstanceID() = {}, new wupInstanceID --> {}", registeredElement.getWupInstanceID(), wupInstanceID);
                LOG.trace(".registerWorkUnitActivity(): registeredElement.getWupTypeID() = {}, new wupTypeID --> {}", registeredElement.getWupTypeID(), wupTypeID);
                LOG.trace(".registerWorkUnitActivity(): registeredElement.getEntryDate() = {}, registeredElement.isHasLocalFocus() = {}, registeredElement.getHasSystemWideFocus() = {}", registeredElement.getEntryDate(), registeredElement.getHasClusterFocus(), registeredElement.getHasSystemWideFocus());
            }
            return (registeredElement);
        } else {
            LOG.trace(".registerWorkUnitActivity(): Could not find a registered parcel for the supplied parcelInstanceID, adding it");
            ContinuityID newID = new ContinuityID();
            newID.setPresentParcelInstanceID(parcelInstanceID);
            newID.setPresentWUPInstanceID(wupInstanceID);
            newID.setPresentWUPTypeID(wupTypeID);
            newID.setPresentParcelEpisodeID(parcelEpisodeID);
            ParcelStatusElement newElement = elementSet.addElement(newID);
            LOG.trace(".registerWorkUnitActivity(): Check to see if someone has Focus (within Cluster and SystemWide)");
            ParcelStatusElement systemWideFocusedElement = elementSet.getSiteWideFocusElement();
            if (systemWideFocusedElement == null) {
                LOG.trace(".registerWorkUnitActivity(): Nobody has SystemWide focus, so assign to me");
                newElement.setHasSystemWideFocus(true);
                LOG.trace(".registerWorkUnitActivity(): And, by definition, if you have SystemWide focus, you should also have Cluster focus");
                newElement.setHasClusterFocus(true);
            } else {
                LOG.trace(".registerWorkUnitActivity(): Another WUP/Parcel Instance has SystemWide focus, so don't assign it to me");
                newElement.setHasSystemWideFocus(false);
                LOG.trace(".registerWorkUnitActivity(): But, now, let's check if we have Cluster focus");
                ParcelStatusElement clusterFocus = elementSet.getClusteredFocusElement();
                if (clusterFocus == null) {
                    LOG.trace(".registerWorkUnitActivity(): Nobody has Cluster wide focus, so assign it to me");
                    newElement.setHasClusterFocus(true);
                }
            }
            return (newElement);
        }
    }

    // TODO really only handles Clusterwide Focus issues - fix
    public ParcelStatusElement updateWorkUnitActivity(FDNToken parcelInstanceID, FDNToken parcelEpisodeID, ResilienceParcelProcessingStatusEnum status) {
        LOG.debug(".updateWorkUnitActivity(): Entry, parcelEpisodeID --> {}, parcelInstanceID --> {}, status --> {}", parcelEpisodeID, parcelInstanceID, status);
        if ((parcelEpisodeID == null) || (parcelInstanceID == null) || (status == null)) {
            throw (new IllegalArgumentException(".updateParcelActivity(): Parcel Type (parcelEpisodeID), Parcel Identifier (parcelInstanceID) or Processing Status (status) is null"));
        }
        LOG.trace(".updateWorkUnitActivity(): Extract who currently has the SystemWide focus for this parcelEpisodeID");
        FDNToken currentParcelWithFocus = checkForExistingClusterFocusedElement(parcelEpisodeID);
        if (parcelInstanceID.equals(currentParcelWithFocus)) {
            LOG.trace(".updateWorkUnitActivity(): This thread currently has CusterWide-Focus, so allow it to set the status");
            ResilienceParcel actualParcel = parcelCacheDM.getParcelInstance(parcelInstanceID);
            LOG.trace(".updateWorkUnitActivity(): Update my Parcel activity setting to --> {} ", status);
            actualParcel.setProcessingStatus(status);
            LOG.trace(".updateWorkUnitActivity(): Now, set the status of the returned ParcelStatusElement as required");
            LOG.trace(".updateWorkUnitActivity(): First, we need to extract the current ParcelStatusElement");
            ParcelStatusElementSet elementSet = occurrenceKey2Instance2WUPMatrix.get(parcelEpisodeID);
            ParcelStatusElement currentElement = elementSet.getElement(parcelInstanceID);
            return (currentElement);
        } else {
            LOG.trace(".updateWorkUnitActivity(): This thread doesn't have SystemWide Focus, therefore its status should be set to -waiting-");
            ResilienceParcel actualParcel = parcelCacheDM.getParcelInstance(parcelInstanceID);
            LOG.trace(".updateWorkUnitActivity(): Update my Parcel activity setting to --> {} ", status);
            actualParcel.setProcessingStatus(ResilienceParcelProcessingStatusEnum.PARCEL_STATUS_REGISTERED);
            ParcelStatusElementSet elementSet = occurrenceKey2Instance2WUPMatrix.get(parcelEpisodeID);
            ParcelStatusElement currentElement = elementSet.getElement(parcelInstanceID);
            currentElement.setHasClusterFocus(false);
            currentElement.setHasSystemWideFocus(false);
            currentElement.setEntryDate(Date.from(Instant.now()));
            return (currentElement);
        }
    }

    public void clearAgedContentFromUpActivityMatrix() {
        LOG.debug(".clearAgedContentFromUpActivityMatrix(): Entry");
        Enumeration<FDNToken> parcelEpisodeIDIterator = occurrenceKey2Instance2WUPMatrix.keys();
        LOG.trace(".clearAgedContentFromUpActivityMatrix(): Iterating through each ParcelType");
        Date currentDate = Date.from(Instant.now());
        Long cutOffAge = currentDate.getTime() - (moduleIM.getWorkUnitActivityCleanUpAgeLimit());
        Long timeOutAge = currentDate.getTime() - (moduleIM.getWorkUnitActivityTimeoutLimit());
        while (parcelEpisodeIDIterator.hasMoreElements()) {
            FDNToken parcelEpisodeID = parcelEpisodeIDIterator.nextElement();
            ParcelStatusElementSet statusSet = occurrenceKey2Instance2WUPMatrix.get(parcelEpisodeID);
            LOG.trace(".clearAgedContentFromUpActivityMatrix(): Iterating through ALL ParcelStatusElements for a particular ParcelEpisodeID --> {}, to see if one is FINISHED", parcelEpisodeID);
            Iterator<FDNToken> initialSearchStatusIterator = statusSet.getParcelInstanceSet().getElements().iterator();
            boolean foundFinished = false;
            FDNToken finishedParcelID = null;
            while (initialSearchStatusIterator.hasNext()) {
                FDNToken currentParcelInstanceID = initialSearchStatusIterator.next();
                ParcelStatusElement currentElement = statusSet.getElement(currentParcelInstanceID);
                switch (currentElement.getParcelStatus()) {
                    case PARCEL_STATUS_FINALISED:
                    case PARCEL_STATUS_FINALISED_ELSEWHERE:
                        if (currentElement.getEntryDate().getTime() > cutOffAge) {
                            statusSet.removeElement(currentParcelInstanceID);
                            if (statusSet.isEmpty()) {
                                occurrenceKey2Instance2WUPMatrix.remove(parcelEpisodeID);
                            }
                            parcelCacheDM.removeParcel(currentParcelInstanceID);
                        }
                        break;
                    case PARCEL_STATUS_REGISTERED:
                    case PARCEL_STATUS_INITIATED:
                    case PARCEL_STATUS_ACTIVE:
                    case PARCEL_STATUS_FINISHED:
                    case PARCEL_STATUS_FINISHED_ELSEWHERE:
                        if (currentElement.getEntryDate().getTime() < timeOutAge) {
                            break;
                        }
                    case PARCEL_STATUS_FAILED:
                        int currentRetryCount = currentElement.getRetryCount();
                        if (currentRetryCount < moduleIM.getWorkUnitActivityRetryLimit()) {
                            currentElement.setRetryCount((currentRetryCount + 1));
                            currentElement.setParcelStatus(ResilienceParcelProcessingStatusEnum.PARCEL_STATUS_REGISTERED);
                            currentElement.setRequiresRetry(true);
                            ResilienceParcel currentParcel = parcelCacheDM.getParcelInstance(currentParcelInstanceID);
                            currentParcel.setFinishedDate(null);
                            currentParcel.setStartDate(null);
                            currentParcel.setRegistrationDate(Date.from(Instant.now()));
                            currentParcel.setFinalisationDate(null);
                            currentParcel.setFinalisationStatus(ResilienceParcelFinalisationStatusEnum.PARCEL_FINALISATION_STATUS_NOT_FINALISED);
                            currentParcel.setProcessingStatus(ResilienceParcelProcessingStatusEnum.PARCEL_STATUS_REGISTERED);
                            break;
                        }
                    default:
                        statusSet.removeElement(currentParcelInstanceID);
                        if (statusSet.isEmpty()) {
                            occurrenceKey2Instance2WUPMatrix.remove(parcelEpisodeID);
                        }
                        parcelCacheDM.removeParcel(currentParcelInstanceID);
                }
            }
        }
    }

    public FDNToken checkForExistingSystemWideFocusedElement(FDNToken parcelEpisodeID) {
        LOG.debug(".checkForExistingSystemWideFocusedElement(): Entry, parcelEpisodeID --> {}", parcelEpisodeID);
        if (parcelEpisodeID == null) {
            return (null);
        }
        if (occurrenceKey2Instance2WUPMatrix.containsKey(parcelEpisodeID)) {
            LOG.trace(".checkForExistingSystemWideFocusedElement(): Found registered set of Parcels for the supplied parcelEpisodeID, extracting it");
            ParcelStatusElementSet elementSet = occurrenceKey2Instance2WUPMatrix.get(parcelEpisodeID);
            LOG.trace(".checkForExistingSystemWideFocusedElement(): Now checking to see if there is an Element that has SystemWide focus");
            ParcelStatusElement currentElement = elementSet.getSiteWideFocusElement();
            if (currentElement != null) {
                return (currentElement.getParcelInstanceID());
            }
        }
        return (null);
    }

    public FDNToken checkForExistingClusterFocusedElement(FDNToken parcelEpisodeID) {
        LOG.debug(".checkForExistingClusterFocusedElement(): Entry, parcelEpisodeID --> {}", parcelEpisodeID);
        if (parcelEpisodeID == null) {
            return (null);
        }
        if (occurrenceKey2Instance2WUPMatrix.containsKey(parcelEpisodeID)) {
            LOG.trace(".checkForExistingClusterFocusedElement(): Found registered set of Parcels for the supplied parcelEpisodeID, extracting it");
            ParcelStatusElementSet elementSet = occurrenceKey2Instance2WUPMatrix.get(parcelEpisodeID);
            LOG.trace(".checkForExistingClusterFocusedElement(): Now checking to see if there is an Element that has Cluster focus");
            ParcelStatusElement currentElement = elementSet.getClusteredFocusElement();
            if (currentElement != null) {
                return (currentElement.getParcelInstanceID());
            }
        }
        return (null);
    }

    public ParcelStatusElementSet getElementStatusSet(FDNToken parcelEpisodeID) {
        LOG.debug(".getElementStatusSet(): Entry, parcelEpisodeID --> {}", parcelEpisodeID);
        if (parcelEpisodeID == null) {
            throw (new IllegalArgumentException(".getElementStatusSet(): passed parameter (parcelEpisodeID) is null"));
        }
        if (occurrenceKey2Instance2WUPMatrix.containsKey(parcelEpisodeID)) {
            return (occurrenceKey2Instance2WUPMatrix.get(parcelEpisodeID));
        } else {
            return (null);
        }
    }

    public FDNTokenSet getLinkedWUPInstanceIDSet(FDNToken parcelEpisodeID) {
        LOG.debug(".getLinkedWUPInstanceIDSet(): Entry, parcelEpisodeID --> {}", parcelEpisodeID);
        if (parcelEpisodeID == null) {
            FDNTokenSet wupTokenSet = new FDNTokenSet();
            LOG.debug(".getLinkedWUPInstanceIDSet(): Exit, returning empty set as parcelEpisodeID was null");
            return (wupTokenSet);
        }
        if (occurrenceKey2Instance2WUPMatrix.containsKey(parcelEpisodeID)) {
            FDNTokenSet wupTokenSet = occurrenceKey2Instance2WUPMatrix.get(parcelEpisodeID).getWUPInstanceSet();
            LOG.debug(".getLinkedWUPInstanceIDSet(): Exit, returning FDNTokenSet --> {}", wupTokenSet);
            return (wupTokenSet);
        } else {
            FDNTokenSet wupTokenSet = new FDNTokenSet();
            LOG.debug(".getLinkedWUPInstanceIDSet(): Exit, returning empty set as no WUPInstances are linked to the parcelEpisodeID");
            return (wupTokenSet);
        }
    }

    public FDNTokenSet getLinkedParcelInstanceIDSet(FDNToken parcelEpisodeID) {
        LOG.debug(".getLinkedParcelInstanceIDSet(): Entry, parcelEpisodeID --> {}", parcelEpisodeID);
        if (parcelEpisodeID == null) {
            FDNTokenSet wupTokenSet = new FDNTokenSet();
            LOG.debug(".getLinkedParcelInstanceIDSet(): Exit, returning empty set as parcelEpisodeID was null");
            return (wupTokenSet);
        }
        if (occurrenceKey2Instance2WUPMatrix.containsKey(parcelEpisodeID)) {
            FDNTokenSet wupTokenSet = occurrenceKey2Instance2WUPMatrix.get(parcelEpisodeID).getParcelInstanceSet();
            LOG.debug(".getLinkedParcelInstanceIDSet(): Exit, returning FDNTokenSet --> {}", wupTokenSet);
            return (wupTokenSet);
        } else {
            FDNTokenSet wupTokenSet = new FDNTokenSet();
            LOG.debug(".getLinkedParcelInstanceIDSet(): Exit, returning empty set as no ParcelInstances are linked to the parcelEpisodeID");
            return (wupTokenSet);
        }
    }

    public void registerDownstreamWUPInterest(FDNToken parcelInstanceID, FDNToken downstreamWUPInstanceID) {
        LOG.debug(".registerDownstreamWUPInterest(): Entry, parcelInstanceID --> {}, downstreamWUPInstanceID --> {}", parcelInstanceID, downstreamWUPInstanceID);
        if ((parcelInstanceID == null) || (downstreamWUPInstanceID == null)) {
            return;
        }
        if (downstreamParcelRegistrationMap.containsKey(parcelInstanceID)) {
            ParcelFinalisationStatusSet downstreamWUPStatus = downstreamParcelRegistrationMap.get(parcelInstanceID);
            downstreamWUPStatus.addDownstreamWUPInstanceID(downstreamWUPInstanceID);
        } else {
            ParcelFinalisationStatusSet downstreamWUPStatus = new ParcelFinalisationStatusSet();
            downstreamWUPStatus.addDownstreamWUPInstanceID(downstreamWUPInstanceID);
            downstreamParcelRegistrationMap.put(parcelInstanceID, downstreamWUPStatus);
        }
    }

    public void registerDownstreamParcel(FDNToken parcelInstanceID, FDNToken downstreamWUPInstanceID, FDNToken downstreamParcelInstanceID) {
        LOG.debug(
                ".registerDownstreamParcel(): Entry, parcelInstanceID --> {}, downstreamWUPInstanceID --> {}, downstreamParcelInstanceID --> {} ",
                parcelInstanceID, downstreamWUPInstanceID, downstreamParcelInstanceID);
        if ((parcelInstanceID == null) || (downstreamWUPInstanceID == null) || (downstreamParcelInstanceID == null)) {
            return;
        }
        if (downstreamParcelRegistrationMap.containsKey(parcelInstanceID)) {
            ParcelFinalisationStatusSet downstreamWUPStatus = downstreamParcelRegistrationMap.get(parcelInstanceID);
            downstreamWUPStatus.updateParcelRegistrationStatus(downstreamWUPInstanceID, downstreamParcelInstanceID);
        } else {
            ParcelFinalisationStatusSet downstreamWUPStatus = new ParcelFinalisationStatusSet();
            downstreamWUPStatus.updateParcelRegistrationStatus(downstreamWUPInstanceID, downstreamParcelInstanceID);
            downstreamParcelRegistrationMap.put(parcelInstanceID, downstreamWUPStatus);
        }
    }

    /**
     * This function parses all the elements within the ActivityMatrix and, if any Parcel Processing has FINISHED,
     * for any ParcelEpisodeID --> then it re-parses the set of Parcels for that ParcelTypeID and sets the OTHER
     * Parcel's status to PARCEL_STATUS_FINISHED_ELSEWHERE.
     */
    public void checkForFinishedParcelForParcelEpisode() {
        LOG.debug(".checkForFinishedParcelForParcelOccurrenceKey(): Entry");
        Enumeration<FDNToken> parcelEpisodeIDIterator = occurrenceKey2Instance2WUPMatrix.keys();
        LOG.trace(".checkForFinishedParcelForParcelOccurrenceKey(): Iterating through each ParcelEpisodeID");
        while (parcelEpisodeIDIterator.hasMoreElements()) {
            FDNToken parcelEpisodeID = parcelEpisodeIDIterator.nextElement();
            ParcelStatusElementSet statusSet = occurrenceKey2Instance2WUPMatrix.get(parcelEpisodeID);
            LOG.trace(".checkForFinishedParcelForParcelOccurrenceKey(): Iterating through ALL ParcelStatusElements for a particular ParcelEpisodeID --> {}, to see if one is FINISHED", parcelEpisodeID);
            Iterator<FDNToken> initialSearchStatusIterator = statusSet.getParcelInstanceSet().getElements().iterator();
            boolean foundFinished = false;
            FDNToken finishedParcelID = null;
            while (initialSearchStatusIterator.hasNext()) {
                FDNToken currentParcelID = initialSearchStatusIterator.next();
                ParcelStatusElement currentElement = statusSet.getElement(currentParcelID);
                if (currentElement.getParcelStatus() == ResilienceParcelProcessingStatusEnum.PARCEL_STATUS_FINISHED) {
                    foundFinished = true;
                    finishedParcelID = currentParcelID;
                }
            }
            if (foundFinished) {
                LOG.trace(".checkForFinishedParcelForParcelType(): Iterating through ALL ParcelStatusElements and setting to PARCEL_STATUS_FINISHED_ELSEWHERE, except for 1st which is set to FINISHED");
                Iterator<FDNToken> updateStatusIterator = statusSet.getParcelInstanceSet().getElements().iterator();
                while (updateStatusIterator.hasNext()) {
                    FDNToken currentParcelID = updateStatusIterator.next();
                    ParcelStatusElement currentElement = statusSet.getElement(currentParcelID);
                    if (!currentElement.getParcelInstanceID().equals(finishedParcelID)) {
                        currentElement.setParcelStatus(ResilienceParcelProcessingStatusEnum.PARCEL_STATUS_FINISHED_ELSEWHERE);
                    }
                }
            }
        }
    }

    /**
     * This function parses all the elements within the ActivityMatrix and, if any Parcel Processing has FINALISED,
     * for any ParcelTypeID --> then it re-parses the set of Parcels for that ParcelTypeID and sets the OTHER
     * Parcels status to PARCEL_STATUS_FINALISED_ELSEWHERE.
     */
    public void checkForFinalisedParcelForParcelEpisode() {
        LOG.debug(".checkForFinalisedParcelForParcelEpisode(): Entry");
        Enumeration<FDNToken> parcelEpisodeIDIterator = occurrenceKey2Instance2WUPMatrix.keys();
        LOG.trace(".checkForFinalisedParcelForParcelEpisode(): Iterating through each ParselEpisodeID");
        while (parcelEpisodeIDIterator.hasMoreElements()) {
            FDNToken parselEpisodeID = parcelEpisodeIDIterator.nextElement();
            ParcelStatusElementSet statusSet = occurrenceKey2Instance2WUPMatrix.get(parselEpisodeID);
            LOG.trace(".checkForFinalisedParcelForParcelEpisode(): Iterating through ALL ParcelStatusElements for a particular ParselEpisodeID --> {}, to see if one is FINALISED", parselEpisodeID);
            Iterator<FDNToken> initialSearchStatusIterator = statusSet.getParcelInstanceSet().getElements().iterator();
            boolean foundFinalised = false;
            FDNToken finalisedParcelID = null;
            while (initialSearchStatusIterator.hasNext()) {
                FDNToken currentParcelID = initialSearchStatusIterator.next();
                ParcelStatusElement currentElement = statusSet.getElement(currentParcelID);
                if (currentElement.getParcelStatus() == ResilienceParcelProcessingStatusEnum.PARCEL_STATUS_FINALISED) {
                    foundFinalised = true;
                    finalisedParcelID = currentParcelID;
                }
            }
            if (foundFinalised) {
                LOG.trace(".checkForFinalisedParcelForParcelEpisode(): Iterating through ALL ParcelStatusElements and setting to PARCEL_STATUS_FINALISED_ELSEWHERE, except for 1st which is set to FINALISED");
                Iterator<FDNToken> updateStatusIterator = statusSet.getParcelInstanceSet().getElements().iterator();
                while (updateStatusIterator.hasNext()) {
                    FDNToken currentParcelID = updateStatusIterator.next();
                    ParcelStatusElement currentElement = statusSet.getElement(currentParcelID);
                    if (!currentElement.getParcelInstanceID().equals(finalisedParcelID)) {
                        currentElement.setParcelStatus(ResilienceParcelProcessingStatusEnum.PARCEL_STATUS_FINALISED_ELSEWHERE);
                    }
                }
            }
        }
    }

    /**
     * This function parses all the elements within the ActivityMatrix and, if any Parcel Processing has FAILED
     * for a particular ParcelEpisodeID --> then it attempts to re-try "n" times (loaded from the ModuleIM).
     */
    public void checkForFailedParcelForParcelEpisode() {
        LOG.debug(".checkForFailedParcelForParcelEpisode(): Entry");
        Enumeration<FDNToken> parcelEpisodeIDIterator = occurrenceKey2Instance2WUPMatrix.keys();
        LOG.trace(".checkForFailedParcelForParcelEpisode(): Iterating through each ParcelOccurrenceKey");
        while (parcelEpisodeIDIterator.hasMoreElements()) {
            FDNToken parcelEpisodeID = parcelEpisodeIDIterator.nextElement();
            ParcelStatusElementSet statusSet = occurrenceKey2Instance2WUPMatrix.get(parcelEpisodeID);
            LOG.trace(".checkForFailedParcelForParcelEpisode(): Iterating through ALL ParcelStatusElements for a particular ParcelEpisodeID --> {}, to see if one is FAILED", parcelEpisodeID);
            Iterator<FDNToken> initialSearchStatusIterator = statusSet.getParcelInstanceSet().getElements().iterator();
            boolean foundFailed = false;
            while (initialSearchStatusIterator.hasNext()) {
                FDNToken currentParcelID = initialSearchStatusIterator.next();
                ParcelStatusElement currentElement = statusSet.getElement(currentParcelID);
                if (currentElement.getParcelStatus() == ResilienceParcelProcessingStatusEnum.PARCEL_STATUS_FAILED) {
                    foundFailed = true;
                }
            }
            if (foundFailed) {
                LOG.trace(".checkForFailedParcelForParcelEpisode(): If any failed, then they all fail!");
                Iterator<FDNToken> updateStatusIterator = statusSet.getParcelInstanceSet().getElements().iterator();
                while (updateStatusIterator.hasNext()) {
                    FDNToken currentParcelID = updateStatusIterator.next();
                    ParcelStatusElement currentElement = statusSet.getElement(currentParcelID);
                    currentElement.setParcelStatus(ResilienceParcelProcessingStatusEnum.PARCEL_STATUS_FAILED);
                }
            }
        }
    }

    // TODO implement!
    public void checkForTimeoutsForParcelType(FDNToken parcelType) {

    }

}
