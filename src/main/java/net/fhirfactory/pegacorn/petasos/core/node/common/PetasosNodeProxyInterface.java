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

package net.fhirfactory.pegacorn.petasos.core.node.common;

import net.fhirfactory.pegacorn.common.model.FDN;
import net.fhirfactory.pegacorn.petasos.core.model.parcel.PetasosParcel;
import net.fhirfactory.pegacorn.petasos.core.model.parcel.PetasosParcelJobCard;
import net.fhirfactory.pegacorn.petasos.core.model.uow.UoW;
import net.fhirfactory.pegacorn.petasos.core.model.wup.WorkUnitProcessorJobCard;

public interface PetasosNodeProxyInterface {
    public WorkUnitProcessorJobCard registerAndNotifyExecution(WorkUnitProcessorJobCard existingJobCard, UoW unitOfWork, FDN previousPetasosParcelInstanceID, boolean synchronousWriteToAudit );
    public WorkUnitProcessorJobCard registerAndWait(WorkUnitProcessorJobCard existingJobCard, UoW unitOfWork,  FDN previousPetasosParcelInstanceID, boolean synchronousWriteToAudit );
    public WorkUnitProcessorJobCard requestExecutionPrivilege(WorkUnitProcessorJobCard existingJobCard);
    public WorkUnitProcessorJobCard notifyExecution(WorkUnitProcessorJobCard existingJobCard);
    public WorkUnitProcessorJobCard notifyFinish(WorkUnitProcessorJobCard existingJobCard, UoW unitOfWork);
    public WorkUnitProcessorJobCard notifyFailed(WorkUnitProcessorJobCard existingJobCard, UoW unitOfWork);

    public PetasosParcelJobCard getParcelStatus(FDN parcelInstanceID);
    public PetasosParcel getParcel(FDN parcelInstanceID);

    public PetasosParcel getCurrentParcelForWUP(FDN wupInstanceID, FDN uowInstanceID);

    public void registerDownstreamWUPInterest( FDN parcelInstanceID, FDN downstreamWUPInstanceID);

    public void registerDownstreamParcel( FDN parcelInstanceID, FDN downstreamWUPInstanceID, FDN downstreamParcelInstanceID);
}
