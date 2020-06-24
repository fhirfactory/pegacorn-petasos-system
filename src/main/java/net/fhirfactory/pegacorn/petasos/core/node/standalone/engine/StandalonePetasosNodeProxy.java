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

package net.fhirfactory.pegacorn.petasos.core.node.standalone.engine;

import java.time.Instant;
import java.util.Date;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import net.fhirfactory.pegacorn.petasos.audit.api.PetasosAuditWriter;
import net.fhirfactory.pegacorn.petasos.core.model.parcel.PetasosParcelFinalisationStatusEnum;
import net.fhirfactory.pegacorn.petasos.core.model.parcel.PetasosParcelJobCard;
import net.fhirfactory.pegacorn.petasos.core.model.uow.UoW;
import net.fhirfactory.pegacorn.petasos.core.model.wup.WorkUnitProcessorJobCard;
import net.fhirfactory.pegacorn.petasos.core.node.common.PetasosNodeProxyInterface;
import net.fhirfactory.pegacorn.petasos.core.model.parcel.PetasosParcel;
import net.fhirfactory.pegacorn.petasos.core.model.parcel.PetasosParcelProcessingStatusEnum;
import net.fhirfactory.pegacorn.petasos.core.model.wup.WorkUnitProcessorActivityStatusEnum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fhirfactory.pegacorn.common.model.FDN;
import net.fhirfactory.pegacorn.deploymentproperties.PetasosProperties;
import net.fhirfactory.pegacorn.petasos.core.node.standalone.cache.LocalNodeActiveParcel2IdleParcelMap;
import net.fhirfactory.pegacorn.petasos.core.node.standalone.cache.LocalNodeDownstreamParcelRegistryMap;
import net.fhirfactory.pegacorn.petasos.core.node.standalone.cache.LocalNodeParcelCache;
import net.fhirfactory.pegacorn.petasos.core.node.standalone.cache.LocalNodeParcelTypeID2ActiveWUPInstanceIDMap;
import net.fhirfactory.pegacorn.petasos.core.node.standalone.cache.LocalNodeParcelTypeID2ParcelInstanceIDSetMap;
import net.fhirfactory.pegacorn.petasos.core.node.standalone.cache.LocalNodeParcelTypeID2WUPInstanceIDSetMap;

/**
 * @author Mark A. Hunter
 */
@ApplicationScoped
public class StandalonePetasosNodeProxy implements PetasosNodeProxyInterface {
    private static final Logger LOG = LoggerFactory.getLogger(StandalonePetasosNodeProxy.class);
    private FDN nodeInstanceFDN;

    @Inject
    LocalNodeParcelCache parcelSet;

    @Inject
    LocalNodeParcelTypeID2ParcelInstanceIDSetMap parcelInstance2ParcelSetMap;

    @Inject
    LocalNodeParcelTypeID2WUPInstanceIDSetMap parcelType2SupportingWUPMap;

    @Inject
    LocalNodeDownstreamParcelRegistryMap downstreamParcelRegistrationMap;

    @Inject
    LocalNodeActiveParcel2IdleParcelMap parcelTypeID2ActiveParcelInstanceID;

    @Inject
    LocalNodeParcelTypeID2ActiveWUPInstanceIDMap parcelTypeID2ActiveWUPInstanceID;

    @Inject
    PetasosProperties systemProperties;

    @Inject
    PetasosAuditWriter auditWriter;

    public StandalonePetasosNodeProxy() {
        this.nodeInstanceFDN = new FDN();
    }

    @Override
    public WorkUnitProcessorJobCard registerAndNotifyExecution(WorkUnitProcessorJobCard existingJobCard, UoW unitOfWork, FDN previousPetasosParcelInstanceID, boolean synchronousWriteToAudit) {
        LOG.debug(".registerAndNotifyExecution(): Entry, existingJobCard --> {}, unitOfWork --> {}, synchronousWriteToAudit -->{}", existingJobCard, unitOfWork, synchronousWriteToAudit);
        if ((unitOfWork == null) || (existingJobCard == null)) {
            throw (new IllegalArgumentException("unitOfWork or existingJobCard are null in method invocation"));
        }
        // 1st, let's see if an existing PetasosParcel exists --> first by seeing if there is a ParcelInstanceID in the Job Card
        LOG.trace(".registerAndNotifyExecution(): check for existing PetasosParcel instance for this WUP/UoW combination");
        PetasosParcel parcelInstance = null;
        FDN wupInstanceID = new FDN(existingJobCard.getWupInstanceID());
        if(existingJobCard.hasCurrentParcelID()){
            parcelInstance = parcelSet.getParcelInstance(wupInstanceID);
            if(parcelInstance == null){
                parcelInstance =  parcelSet.getCurrentParcelForWUP(wupInstanceID, unitOfWork.getUoWInstanceID());
            }
        }
        LOG.trace(".registerAndNotifyExecution(): Attempted to retrieve existing PetasosParcel, result --> {}", parcelInstance);
        // If not PetasosParcel was available, then let's create one!
        if( parcelInstance == null ) {
            parcelInstance = new PetasosParcel(wupInstanceID, unitOfWork.getUowTypeID(), unitOfWork, previousPetasosParcelInstanceID, WorkUnitProcessorActivityStatusEnum.WUP_ACTIVITY_STATUS_EXECUTING);
            LOG.trace(".registerAndNotifyExecution(): Created a new PetasosParcel, result --> {}", parcelInstance);
        }
        // We are running in Stand-Alone mode, so there should only ever be one WUP instance that can support
        // a particular Parcel Type. So, if requested, automatically grant it execution properties and
        // assign it as the main WUP Instance.
        LOG.trace(".registerAndNotifyExecution(): Updating the Parcel Instance to reflect operational state");
        parcelInstance.updatePetasosParcelProcessingStatus(PetasosParcelProcessingStatusEnum.PARCEL_STATUS_ACTIVE);
        LOG.trace(".registerAndNotifyExecution(): Updating the active WUP Instance within the Parcel Instance (this tells us which WUP is actually performing the Parcel task)");
        parcelInstance.setActiveWUPInstanceID(new FDN(existingJobCard.getWupInstanceID()));
        LOG.trace(".registerAndNotifyExecution(): Updating the StartDate for the Parcel (event though this is a bit late to do so)");
        parcelInstance.setParcelStartDate(Date.from(Instant.now()));
        LOG.trace("registerAndNotifyExecution(): Update the Alternate WUP Instance Set for the Parcel (i.e. other WUPs that could do the job)");
        parcelInstance.setAlternateWUPInstanceIDSet(parcelType2SupportingWUPMap.getLinkedWUPInstanceIDSet(wupInstanceID));
        // Now we update the Parcel Cache & other associated Cache's with the details
        LOG.trace("registerAndNotifyExecution(): Adding this parcel to the ParcelCache, Parcel --> {}", parcelInstance);
        this.parcelSet.addParcel(parcelInstance);
        // Now, let's create an Audit Entry
        LOG.trace(".registerAndNotifyExecution(): Writing the status/information to the Audit trail");
        boolean status = auditWriter.writeAuditEntry(parcelInstance, synchronousWriteToAudit);
        // Now let's update the other Cache's with the status/information
        LOG.trace("registerAndNotifyExecution(): Assigning the requesting WUP as the default implementation WUP for this Parcel (we are running in Standalone Mode)");
        this.parcelTypeID2ActiveWUPInstanceID.setActivteWUPInstancelID(parcelInstance.getParcelTypeID(), new FDN(wupInstanceID));
        LOG.trace("registerAndNotifyExecution(): Assigning the requesting WUP's Parcel as the default one for this Parcel Type (we are running in Standalone Mode)");
        this.parcelTypeID2ActiveParcelInstanceID.setActivteParcelID(parcelInstance.getParcelTypeID(), parcelInstance.getParcelInstanceID());
        LOG.trace("registerAndNotifyExecution(): Building map of all Potential Parcel's that have the same type - against the specific one actually being processed");
        this.parcelInstance2ParcelSetMap.linkParcelType2ParcelInstance(parcelInstance.getParcelTypeID(), parcelInstance.getParcelInstanceID());
        LOG.trace("registerAndNotifyExecution(): Adding this WUP Instance as one that supports the processing of the identified Parcel Type");
        this.parcelType2SupportingWUPMap.linkParcelTypeID2WUPInstance(parcelInstance.getParcelTypeID(), wupInstanceID);
        // OK, now we populate the WUP Job Card, letting it know the status and our expected behaviour from it.
        LOG.trace("registerAndNotifyExecution(): Creating a new JobCard for the WUP, with the key aim of informing the calling WUP that it should continue executing");
        WorkUnitProcessorJobCard newJobCard = new WorkUnitProcessorJobCard(wupInstanceID, parcelInstance.getParcelInstanceID(), existingJobCard.getCurrentStatus(), WorkUnitProcessorActivityStatusEnum.WUP_ACTIVITY_STATUS_EXECUTING, existingJobCard.getMode(), Date.from(Instant.now()));
        LOG.debug("registerAndNotifyExecution(): Exiting, newJobCard --> {}", newJobCard);
        return (newJobCard);
    }

    @Override
    public WorkUnitProcessorJobCard registerAndWait(WorkUnitProcessorJobCard existingJobCard, UoW unitOfWork, FDN previousPetasosParcelInstanceID,  boolean synchronousWriteToAudit) {
        LOG.debug(".registerAndWait(): Entry, existingJobCard --> {}, unitOfWork --> {}, synchronousWriteToAudit -->{}", existingJobCard, unitOfWork, synchronousWriteToAudit);
        if ((unitOfWork == null) || (existingJobCard == null)) {
            throw (new IllegalArgumentException("unitOfWork or existingJobCard are null in method invocation"));
        }
        // 1st, let's see if an existing PetasosParcel exists --> first by seeing if there is a ParcelInstanceID in the Job Card
        LOG.trace(".registerAndWait(): check for existing PetasosParcel instance for this WUP/UoW combination");
        PetasosParcel parcelInstance = null;
        FDN wupInstanceID = new FDN(existingJobCard.getWupInstanceID());
        if(existingJobCard.hasCurrentParcelID()){
            parcelInstance = parcelSet.getParcelInstance(wupInstanceID);
            if(parcelInstance == null){
                parcelInstance =  parcelSet.getCurrentParcelForWUP(wupInstanceID, unitOfWork.getUoWInstanceID());
            }
        }
        LOG.trace(".registerAndWait(): Attempted to retrieve existing PetasosParcel, result --> {}", parcelInstance);
        // If not PetasosParcel was available, then let's create one!
        if( parcelInstance == null ) {
            parcelInstance = new PetasosParcel(wupInstanceID, unitOfWork.getUowTypeID(), unitOfWork, previousPetasosParcelInstanceID, WorkUnitProcessorActivityStatusEnum.WUP_ACTIVITY_STATUS_EXECUTING);
            LOG.trace(".registerAndWait(): Created a new PetasosParcel, result --> {}", parcelInstance);
        }
        // We are running in Stand-Alone mode, so there should only ever be one WUP instance that can support
        // a particular Parcel Type. So, if requested, automatically grant it execution properties and
        // assign it as the main WUP Instance.
        LOG.trace(".registerAndWait(): Updating the Parcel Instance to reflect operational state");
        parcelInstance.updatePetasosParcelProcessingStatus(PetasosParcelProcessingStatusEnum.PARCEL_STATUS_ACTIVE);
        LOG.trace(".registerAndWait(): Updating the active WUP Instance within the Parcel Instance (this tells us which WUP is actually performing the Parcel task)");
        parcelInstance.setActiveWUPInstanceID(new FDN(existingJobCard.getWupInstanceID()));
        LOG.trace(".registerAndWait(): Updating the StartDate for the Parcel (event though this is a bit late to do so)");
        parcelInstance.setParcelStartDate(Date.from(Instant.now()));
        LOG.trace("registerAndWait(): Update the Alternate WUP Instance Set for the Parcel (i.e. other WUPs that could do the job)");
        parcelInstance.setAlternateWUPInstanceIDSet(parcelType2SupportingWUPMap.getLinkedWUPInstanceIDSet(wupInstanceID));
        // Now we update the Parcel Cache & other associated Cache's with the details
        LOG.trace("registerAndWait(): Adding this parcel to the ParcelCache, Parcel --> {}", parcelInstance);
        this.parcelSet.addParcel(parcelInstance);
        // Now, let's create an Audit Entry
        LOG.trace(".registerAndWait(): Writing the status/information to the Audit trail");
        boolean status = auditWriter.writeAuditEntry(parcelInstance, synchronousWriteToAudit);
        // And now, we'll update the other Cache's with the status information
        LOG.trace("registerAndWait(): Assigning the requesting WUP as the default implementation WUP for this Parcel (we are running in Standalone Mode)");
        this.parcelTypeID2ActiveWUPInstanceID.setActivteWUPInstancelID(parcelInstance.getParcelTypeID(), new FDN(wupInstanceID));
        LOG.trace("registerAndWait(): Assigning the requesting WUP's Parcel as the default one for this Parcel Type (we are running in Standalone Mode)");
        this.parcelTypeID2ActiveParcelInstanceID.setActivteParcelID(parcelInstance.getParcelTypeID(), parcelInstance.getParcelInstanceID());
        LOG.trace("registerAndWait(): Building map of all Potential Parcel's that have the same type - against the specific one actually being processed");
        this.parcelInstance2ParcelSetMap.linkParcelType2ParcelInstance(parcelInstance.getParcelTypeID(), parcelInstance.getParcelInstanceID());
        LOG.trace("registerAndWait(): Adding this WUP Instance as one that supports the processing of the identified Parcel Type");
        this.parcelType2SupportingWUPMap.linkParcelTypeID2WUPInstance(parcelInstance.getParcelTypeID(), wupInstanceID);
        // OK, now we populate the WUP Job Card, letting it know the status and our expected behaviour from it.
        LOG.trace("registerAndWait(): Creating a new JobCard for the WUP, with the key aim of informing the calling WUP that it should actual do the executing");
        WorkUnitProcessorJobCard newJobCard = new WorkUnitProcessorJobCard(wupInstanceID, parcelInstance.getParcelInstanceID(), existingJobCard.getCurrentStatus(), WorkUnitProcessorActivityStatusEnum.WUP_ACTIVITY_STATUS_EXECUTING, existingJobCard.getMode(), Date.from(Instant.now()));
        LOG.debug("registerAndWait(): Exiting, newJobCard --> {}", newJobCard);
        return (newJobCard);
    }

    /**
     * This method allows for a WUP to request execution privileges on their current Parcel. In Standalone mode, the request is always granted as
     * there should only be one instance of the appropriate ProcessingEngine active that is capable of actually processing the Parcel - therefore, we
     * merely grant privilege without any checking of states. This method is considerable more involved in the Clustered/Site-Resilient mode.
     *
     * @param existingJobCard The existing WUP Job Card
     * @return Returns a WorkUnitProcessorCard that, importantly, grants permission for the requesting WUP to take over executing privileges.
     */
    @Override
    public WorkUnitProcessorJobCard requestExecutionPrivilege(WorkUnitProcessorJobCard existingJobCard) {
        LOG.debug(".requestExecutionPrivilege(): Entry,  existingJobCard --> {}, parcelInstance --> {}", existingJobCard);
        if (existingJobCard == null) {
            throw (new IllegalArgumentException(".requestExecutionPrivilege(): existingJobCard is null in method invocation"));
        }
        // We are working in standalone mode, so there should only ever be a single ProcessorEngine capable of processing the Parcel
        // actually active - so if it requests execution privilege --> grant it!
        // But first, we need to get the actual PetasosParcel
        FDN parcelID = new FDN(existingJobCard.getCurrentParcelID());
        PetasosParcel actualParcel = parcelSet.getParcelInstance(parcelID);
        if( actualParcel == null ){
            WorkUnitProcessorJobCard failedJobCard = new WorkUnitProcessorJobCard(existingJobCard.getWupInstanceID(), existingJobCard.getCurrentParcelID(), existingJobCard.getCurrentStatus(), WorkUnitProcessorActivityStatusEnum.WUP_ACTIVITY_STATUS_FAILED, existingJobCard.getMode(), Date.from(Instant.now()));
            return(failedJobCard);
        }
        LOG.trace("requestExecutionPrivilege(): Assigning the requesting WUP's Parcel as the default one for this Parcel Type (we are running in Standalone Mode)");
        this.parcelTypeID2ActiveParcelInstanceID.setActivteParcelID(actualParcel.getParcelTypeID(), actualParcel.getParcelInstanceID());
        LOG.trace("requestExecutionPrivilege(): Assigning the requesting WUP's Parcel as the default one for this Parcel Type (we are running in Standalone Mode)");
        this.parcelTypeID2ActiveWUPInstanceID.setActivteWUPInstancelID(actualParcel.getParcelTypeID(), new FDN(existingJobCard.getWupInstanceID()));
        LOG.trace("requestExecutionPrivilege(): Creating a new JobCard for the WUP, with the key aim of informing the calling WUP that it should actual do the executing");
        WorkUnitProcessorJobCard newJobCard = new WorkUnitProcessorJobCard(existingJobCard.getWupInstanceID(), actualParcel.getParcelInstanceID().getToken(), existingJobCard.getCurrentStatus(), WorkUnitProcessorActivityStatusEnum.WUP_ACTIVITY_STATUS_EXECUTING, existingJobCard.getMode(), Date.from(Instant.now()));
        return (newJobCard);
    }

    /**
     * @param parcelInstanceID the FDN of the PetasosParcel of interest
     * @return Returns a PetasosParcelJobCard - detailing the status of the particular Parcel
     */
    @Override
    public PetasosParcelJobCard getParcelStatus(FDN parcelInstanceID) {
        LOG.debug(".getParcelStatus(): Entry, parcelInstanceID --> {}", parcelInstanceID);
        if (parcelInstanceID == null) {
            return (null);
        }
        PetasosParcel selectedParcelInstance = parcelSet.getParcelInstance(parcelInstanceID);
        if (selectedParcelInstance == null) {
            return (null);
        }
        return (selectedParcelInstance.getParcelJobCard());
    }

    /**
     * @param parcelInstanceID the FDN of the PetasosParcel of interest
     * @return Returns that PetasosParcel from the Node's cache. This should be considered definitive.
     */
    @Override
    public PetasosParcel getParcel(FDN parcelInstanceID) {
        LOG.debug(".getParcelStatus(): Entry, parcelInstanceID --> {}", parcelInstanceID);
        if ((parcelInstanceID == null)) {
            throw (new IllegalArgumentException("Either parcelInstance or wupInstanceID are null"));
        }
        PetasosParcel selectedParcelInstance = parcelSet.getParcelInstance(parcelInstanceID);
        if (selectedParcelInstance == null) {
            return (null);
        }
        return (selectedParcelInstance);
    }

    @Override
    public WorkUnitProcessorJobCard notifyFinish(WorkUnitProcessorJobCard existingJobCard, UoW finishedUoW) {
        LOG.debug(".registerFinished(): Entry, existingJobCard --> {}, finishedUoW --> {}", existingJobCard,finishedUoW);
        if ((existingJobCard == null) || (finishedUoW == null)) {
            throw (new IllegalArgumentException("Either existingJobCard or finishedUoW are null"));
        }
        // First, we need to get the actual PetasosParcel
        FDN parcelID = new FDN(existingJobCard.getCurrentParcelID());
        PetasosParcel actualParcel = parcelSet.getParcelInstance(parcelID);
        if( actualParcel == null ){
            WorkUnitProcessorJobCard failedJobCard = new WorkUnitProcessorJobCard(existingJobCard.getWupInstanceID(), existingJobCard.getCurrentParcelID(), existingJobCard.getCurrentStatus(), WorkUnitProcessorActivityStatusEnum.WUP_ACTIVITY_STATUS_FAILED, existingJobCard.getMode(), Date.from(Instant.now()));
            return(failedJobCard);
        }
        FDN wupInstanceID = new FDN(existingJobCard.getWupInstanceID());
        PetasosParcel selectedParcelInstance = parcelSet.getParcelInstance(actualParcel.getParcelInstanceID());
        selectedParcelInstance.setParcelFinishedDate(Date.from(Instant.now()));
        selectedParcelInstance.setActualUoW(actualParcel.getActualUoW());
        selectedParcelInstance.setActiveWUPInstanceID(wupInstanceID);
        selectedParcelInstance.setTaskProcessorActivityStatus(WorkUnitProcessorActivityStatusEnum.WUP_ACTIVITY_STATUS_FINISHED);
        LOG.trace("notifyFinish(): Creating a new JobCard for the WUP, with the key aim of informing the calling WUP that it is, indeed, finished");
        WorkUnitProcessorJobCard newJobCard = new WorkUnitProcessorJobCard(existingJobCard.getWupInstanceID(), actualParcel.getParcelInstanceID().getToken(), existingJobCard.getCurrentStatus(), WorkUnitProcessorActivityStatusEnum.WUP_ACTIVITY_STATUS_FINISHED, existingJobCard.getMode(), Date.from(Instant.now()));
        return (newJobCard);
    }

    /**
     * In theory, this should return Permission for the WUP to continue to process the identified PetasosParcel as
     * the active instance - once again note that the system is operating in Standalone mode and, thus, there would
     * only be a single WUP instance that could actually process the Parcel!
     *
     * @param existingJobCard
     * @return A WorkUnitProcessorJobCard that includes, as a minimum, the .suggestedStatus
     * set to WorkUnitProcessorActivityStatusEnum.WUP_ACTIVITY_STATUS_EXECUTING
     */
    @Override
    public WorkUnitProcessorJobCard notifyExecution(WorkUnitProcessorJobCard existingJobCard) {
        LOG.debug(".registerParcelAsActive(): Entry,  existingJobCard --> {}", existingJobCard);
        if ((existingJobCard == null)) {
            throw (new IllegalArgumentException("existingJobCard is null in method invocation"));
        }
        // We are working in standalone mode, so there should only ever be a single ProcessorEngine capable of processing the Parcel
        // actually active - so if it requests execution privilege --> grant it!
        // But first, we need to get the actual PetasosParcel
        FDN parcelID = new FDN(existingJobCard.getCurrentParcelID());
        PetasosParcel actualParcel = parcelSet.getParcelInstance(parcelID);
        if( actualParcel == null ){
            WorkUnitProcessorJobCard failedJobCard = new WorkUnitProcessorJobCard(existingJobCard.getWupInstanceID(), existingJobCard.getCurrentParcelID(), existingJobCard.getCurrentStatus(), WorkUnitProcessorActivityStatusEnum.WUP_ACTIVITY_STATUS_FAILED, existingJobCard.getMode(), Date.from(Instant.now()));
            return(failedJobCard);
        }
        FDN wupInstanceID = new FDN(existingJobCard.getWupInstanceID());
        LOG.trace("registerParcelAsActive(): Assigning the requesting WUP's Parcel as the default one for this Parcel Type (we are running in Standalone Mode)");
        this.parcelTypeID2ActiveParcelInstanceID.setActivteParcelID(actualParcel.getParcelTypeID(), actualParcel.getParcelInstanceID());
        LOG.trace("registerParcelAsActive(): Assigning the requesting WUP's Parcel as the default one for this Parcel Type (we are running in Standalone Mode)");
        this.parcelTypeID2ActiveWUPInstanceID.setActivteWUPInstancelID(actualParcel.getParcelTypeID(), wupInstanceID);
        LOG.trace("registerParcelAsActive(): Update the Parcel in the registry to ensure that it knows this WUP is the executing one & this Parcel is the Active one!");
        actualParcel.setActiveWUPInstanceID(wupInstanceID);
        LOG.trace("registerParcelAsActive(): Creating a new JobCard for the WUP, with the key aim of informing the calling WUP that it should actual do the executing");
        WorkUnitProcessorJobCard newJobCard = new WorkUnitProcessorJobCard(existingJobCard.getWupInstanceID(), actualParcel.getParcelInstanceID().getToken(), existingJobCard.getCurrentStatus(), WorkUnitProcessorActivityStatusEnum.WUP_ACTIVITY_STATUS_EXECUTING, existingJobCard.getMode(), Date.from(Instant.now()));
        return (newJobCard);
    }

    @Override
    public WorkUnitProcessorJobCard notifyFailed(WorkUnitProcessorJobCard existingJobCard, UoW unitOfWork) {
        return null;
    }

    @Override
    public void registerDownstreamWUPInterest(FDN parcelInstanceID, FDN downstreamWUPInstanceID) {
        LOG.debug(".registerDownstreamWUP(): Entry, parcelInstanceID --> {}, downstreamWUPInstanceID --> {}", parcelInstanceID, downstreamWUPInstanceID);
        if ((parcelInstanceID == null) || (downstreamWUPInstanceID == null)) {
            return;
        }
        downstreamParcelRegistrationMap.registerDownstreamWUPInterest(parcelInstanceID, downstreamWUPInstanceID);
    }

    @Override
    public void registerDownstreamParcel(FDN parcelInstanceID, FDN downstreamWUPInstanceID, FDN downstreamParcelInstanceID) {
        LOG.debug(".registerDownstreamParcel(): Entry, parcelInstanceID --> {}, downstreamWUPInstanceID --> {}, downstreamParcelInstanceID --> {} ",
                parcelInstanceID, downstreamWUPInstanceID, downstreamParcelInstanceID);
        if ((parcelInstanceID == null) || (downstreamWUPInstanceID == null) || (downstreamParcelInstanceID == null)) {
            return;
        }
        downstreamParcelRegistrationMap.registerDownstreamParcel(parcelInstanceID, downstreamWUPInstanceID, downstreamParcelInstanceID);
    }

    @Override
    public PetasosParcel getCurrentParcelForWUP(FDN wupInstanceID, FDN uowInstanceID) {
        LOG.debug(".getCurrentParcel(): Entry, wupInstanceID --> {}", wupInstanceID);
        if ((wupInstanceID == null)) {
            throw (new IllegalArgumentException("Either wupInstanceID is null"));
        }
        PetasosParcel selectedParcelInstance = parcelSet.getCurrentParcelForWUP(wupInstanceID, uowInstanceID);
        if (selectedParcelInstance == null) {
            return (null);
        }
        return (selectedParcelInstance);
    }
}
