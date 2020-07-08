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

package net.fhirfactory.pegacorn.petasos.wup;

import net.fhirfactory.pegacorn.petasos.model.pathway.ContinuityID;
import net.fhirfactory.pegacorn.petasos.model.resilience.parcel.ResilienceParcelProcessingStatusEnum;
import net.fhirfactory.pegacorn.petasos.model.resilience.parcel.ResilienceParcel;
import net.fhirfactory.pegacorn.petasos.model.uow.UoW;
import net.fhirfactory.pegacorn.petasos.model.wup.WUPJobCard;
import net.fhirfactory.pegacorn.petasos.model.wup.WUPActivityStatusEnum;
import net.fhirfactory.pegacorn.petasos.model.wup.WUPClusterModeEnum;
import net.fhirfactory.pegacorn.petasos.model.wup.WUPSystemModeEnum;
import net.fhirfactory.pegacorn.petasos.wup.common.GenericWUPTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Date;

public abstract class APIGatewayWUP extends GenericWUPTemplate {
    private static final Logger LOG = LoggerFactory.getLogger(APIGatewayWUP.class);

    public APIGatewayWUP(){
        super();
    }

     public void registerActivityStart(UoW unitOfWork, WUPClusterModeEnum clusterMode, WUPSystemModeEnum systemMode){
        LOG.debug(".registerActivityStart(): Entry, unitOfWork --> {}", unitOfWork);
        ContinuityID newContinuityID = new ContinuityID();
        newContinuityID.setPresentWUPTypeID(this.getWupTypeID());
        newContinuityID.setPresentWUPInstanceID(this.getWupInstanceID());
        WUPJobCard jobCard = new WUPJobCard(newContinuityID, WUPActivityStatusEnum.WUP_ACTIVITY_STATUS_WAITING, WUPActivityStatusEnum.WUP_ACTIVITY_STATUS_EXECUTING, WUPClusterModeEnum.CLUSTER_MUILTPLE_ONDEMAND, WUPSystemModeEnum.SYSTEMWIDE_MUILTPLE_ONDEMAND, Date.from(Instant.now()) );
        ResilienceParcel tempParcel = this.servicesBroker.registerWorkUnitActivity(jobCard, unitOfWork);
        tempParcel.setProcessingStatus(ResilienceParcelProcessingStatusEnum.PARCEL_STATUS_ACTIVE);

    }

    public void registerActivityFinish(UoW unitOfWork){
        LOG.debug(".registerActivityFinish(): Entry, unitOfWork --> {}", unitOfWork);
        ResilienceParcel tempParcel = new ResilienceParcel(this.getWupInstanceID(), this.getWupTypeID(), unitOfWork, null);
        tempParcel.setProcessingStatus(ResilienceParcelProcessingStatusEnum.PARCEL_STATUS_ACTIVE);
    }
}
