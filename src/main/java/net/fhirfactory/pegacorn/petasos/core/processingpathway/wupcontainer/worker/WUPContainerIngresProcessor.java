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
import net.fhirfactory.pegacorn.petasos.core.PetasosServicesBroker;
import net.fhirfactory.pegacorn.petasos.model.pathway.ContinuityID;
import net.fhirfactory.pegacorn.petasos.model.pathway.WorkUnitTransportPacket;
import net.fhirfactory.pegacorn.petasos.model.uow.UoW;
import net.fhirfactory.pegacorn.petasos.model.wup.WUPActivityStatusEnum;
import net.fhirfactory.pegacorn.petasos.model.wup.WUPClusterModeEnum;
import net.fhirfactory.pegacorn.petasos.model.wup.WUPJobCard;
import net.fhirfactory.pegacorn.petasos.model.wup.WUPSystemModeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.time.Instant;
import java.util.Date;

public class WUPContainerIngresProcessor extends WUPContainerProcessorBase {
    private static final Logger LOG = LoggerFactory.getLogger(WUPContainerIngresProcessor.class);

    @Inject
    PetasosServicesBroker petasosServicesBroker;

    public UoW ingresContentProcessor(WorkUnitTransportPacket incomingPacket, FDNToken wupTypeID, FDNToken wupInstanceID) {
        LOG.debug(".ingresContentProcessor(): Enter, incomingPacket --> {}, wupTypeID --> {}, wupInstanceID -->", incomingPacket, wupTypeID, wupInstanceID);
        UoW uowOutput;
        LOG.trace(".ingresContentProcessor(): Now, check if this the 1st time the associated UoW has been (attempted to be) processed");
        if(incomingPacket.getIsARetry()){
            LOG.trace(".ingresContentProcessor(): This is a recovery or retry iteration of processing this UoW, so send to .alternativeIngresContentProcessor()");
            uowOutput = alternativeIngresContentProcessor(incomingPacket, wupTypeID, wupInstanceID);
        } else {
            LOG.trace(".ingresContentProcessor(): This is the 1st time this UoW is being processed, so send to .standardIngresContentProcessor()");
            uowOutput = standardIngresContentProcessor(incomingPacket, wupTypeID, wupInstanceID);
        }
        LOG.debug(".ingresContentProcessor(): Exit, uowOutput (UoW) -->", uowOutput);
        return (uowOutput);
    }

    public UoW standardIngresContentProcessor(WorkUnitTransportPacket incomingPacket, FDNToken wupTypeID, FDNToken wupInstanceID){
        LOG.debug(".ingresContentProcessor(): Enter, incomingPacket --> {}, wupTypeID --> {}, wupInstanceID -->", incomingPacket, wupTypeID, wupInstanceID);
        UoW theOutput = incomingPacket.getPayload();
        LOG.trace(".standardIngresContentProcessor(): Building the FDN from the FDNToken (for the WUP Instance ID");
        FDNToken localWUPInstanceID = new FDNToken(wupInstanceID);
        FDNToken localWUPTypeID = new FDNToken(wupTypeID);
        ContinuityID activityID = incomingPacket.getActivityID();
        FDNToken previousPresentParcelInstanceID = activityID.getPresentResilienceParcelInstanceID();
        FDNToken previousPresentWUPInstanceID = activityID.getPresentWUPInstanceID();
        FDNToken previousPresentWUPTypeID = activityID.getPresentWUPTypeID();
        activityID.setPreviousResilienceParcelInstanceID(previousPresentParcelInstanceID);
        activityID.setPreviousWUPInstanceID(previousPresentWUPInstanceID);
        activityID.setPreviousWUPTypeID(previousPresentWUPTypeID);
        activityID.setPresentWUPTypeID(localWUPTypeID);
        activityID.setPresentWUPInstanceID(localWUPInstanceID);
        FDNToken parcelOccurrenceKey = new FDNToken(theOutput.getTypeID());
        WUPJobCard activityJobCard = new WUPJobCard(activityID, WUPActivityStatusEnum.WUP_ACTIVITY_STATUS_WAITING,WUPActivityStatusEnum.WUP_ACTIVITY_STATUS_EXECUTING, WUPClusterModeEnum.CLUSTER_SINGLE_STANDALONE, WUPSystemModeEnum.SYSTEMWIDE_SINGLE_STANDALONE, Date.from(Instant.now()));
        petasosServicesBroker.registerStandardWorkUnitActivity(activityJobCard,theOutput);
        LOG.trace(".standardIngresContentProcessor(): Extracting the ContinuityID from the incoming Packet, if it has one");
        return(theOutput);
    }

    public UoW alternativeIngresContentProcessor(WorkUnitTransportPacket incomingPacket, FDNToken wupTypeID, FDNToken wupInstanceID){
        LOG.debug(".alternativeIngresContentProcessor(): Enter, incomingPacket --> {}, wupTypeID --> {}, wupInstanceID -->", incomingPacket, wupTypeID, wupInstanceID);
        UoW uowOutput = incomingPacket.getPayload();

        return(uowOutput);
    }
}
