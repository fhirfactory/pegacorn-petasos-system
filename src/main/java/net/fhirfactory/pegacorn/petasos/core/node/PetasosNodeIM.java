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

package net.fhirfactory.pegacorn.petasos.core.node;

import net.fhirfactory.pegacorn.common.model.FDN;
import net.fhirfactory.pegacorn.petasos.ServiceModuleProperties;
import net.fhirfactory.pegacorn.petasos.core.model.parcel.PetasosParcel;
import net.fhirfactory.pegacorn.petasos.core.model.parcel.PetasosParcelJobCard;
import net.fhirfactory.pegacorn.petasos.core.model.uow.UoW;
import net.fhirfactory.pegacorn.petasos.core.model.wup.WorkUnitProcessorJobCard;
import net.fhirfactory.pegacorn.petasos.core.node.common.PetasosNodeModeEnum;
import net.fhirfactory.pegacorn.petasos.core.node.common.PetasosNodeProxyInterface;
import net.fhirfactory.pegacorn.petasos.core.node.standalone.engine.StandalonePetasosNodeProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class PetasosNodeIM implements PetasosNodeProxyInterface{
    private static final Logger LOG = LoggerFactory.getLogger(PetasosNodeIM.class);
    private PetasosNodeModeEnum availabilityMode;
    private FDN nodeID;
    private PetasosNodeProxyInterface actualPetasosNode;

    @Inject
    ServiceModuleProperties instanceProperties;

    @Inject
    StandalonePetasosNodeProxy standalonePetasosNode;

    PetasosNodeIM(){
        this.availabilityMode = instanceProperties.getReliabilityMode();
        this.nodeID = instanceProperties.getServiceModuleInstanceContext();
        if( this.availabilityMode == PetasosNodeModeEnum.PETASOS_NODE_MODE_STANDALONE ){
            actualPetasosNode = standalonePetasosNode;
        }
    }

    public WorkUnitProcessorJobCard registerAndNotifyExecution(WorkUnitProcessorJobCard existingJobCard, UoW unitOfWork, FDN previousPetasosParcelInstanceID, boolean synchronousWriteToAudit ){
        return(actualPetasosNode.registerAndNotifyExecution(existingJobCard, unitOfWork, previousPetasosParcelInstanceID, synchronousWriteToAudit));
    }
    public WorkUnitProcessorJobCard registerAndWait(WorkUnitProcessorJobCard existingJobCard, UoW unitOfWork,  FDN previousPetasosParcelInstanceID, boolean synchronousWriteToAudit ){
        return(actualPetasosNode.registerAndWait(existingJobCard, unitOfWork, previousPetasosParcelInstanceID, synchronousWriteToAudit));
    }
    public WorkUnitProcessorJobCard requestExecutionPrivilege(WorkUnitProcessorJobCard existingJobCard ){
        return(actualPetasosNode.requestExecutionPrivilege(existingJobCard));
    }

    public PetasosParcelJobCard getParcelStatus(FDN parcelInstanceID){
        return(actualPetasosNode.getParcelStatus(parcelInstanceID));
    }

    @Override
    public PetasosParcel getCurrentParcelForWUP(FDN wupInstanceID, FDN uowInstanceID) {
        return(actualPetasosNode.getCurrentParcelForWUP(wupInstanceID,uowInstanceID ));
    }

    public PetasosParcel getParcel(FDN parcelInstanceID){
        return(actualPetasosNode.getParcel(parcelInstanceID));
    }

    public WorkUnitProcessorJobCard notifyFinish(WorkUnitProcessorJobCard existingJobCard, UoW unitOfWork){
        return(actualPetasosNode.notifyFinish(existingJobCard, unitOfWork));
    }
    public WorkUnitProcessorJobCard notifyFailed(WorkUnitProcessorJobCard existingJobCard, UoW unitOfWork){
        return(actualPetasosNode.notifyFailed(existingJobCard, unitOfWork));
    }
    public WorkUnitProcessorJobCard notifyExecution(WorkUnitProcessorJobCard existingJobCard){
        return(actualPetasosNode.notifyExecution(existingJobCard));
    }

    public void registerDownstreamWUPInterest( FDN parcelInstanceID, FDN downstreamWUPInstanceID){
        actualPetasosNode.registerDownstreamWUPInterest(parcelInstanceID,downstreamWUPInstanceID);
    }

    public void registerDownstreamParcel( FDN parcelInstanceID, FDN downstreamWUPInstanceID, FDN downstreamParcelInstanceID){
        actualPetasosNode.registerDownstreamParcel(parcelInstanceID, downstreamWUPInstanceID, downstreamParcelInstanceID);
    }

}
