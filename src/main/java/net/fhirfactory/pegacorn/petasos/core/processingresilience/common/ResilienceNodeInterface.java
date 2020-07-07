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

package net.fhirfactory.pegacorn.petasos.core.processingresilience.common;

import net.fhirfactory.pegacorn.common.model.FDNToken;
import net.fhirfactory.pegacorn.petasos.model.resilience.parcel.ResilienceParcel;
import net.fhirfactory.pegacorn.petasos.model.uow.UoW;
import net.fhirfactory.pegacorn.petasos.model.wup.WUPJobCard;

public interface ResilienceNodeInterface {
    public WUPJobCard registerAndNotifyExecution(WUPJobCard existingJobCard, UoW unitOfWork, boolean synchronousWriteToAudit );
    public WUPJobCard registerAndWait(WUPJobCard existingJobCard, UoW unitOfWork, boolean synchronousWriteToAudit );
    public WUPJobCard requestExecutionPrivilege(WUPJobCard existingJobCard);
    public WUPJobCard notifyExecution(WUPJobCard existingJobCard);
    public WUPJobCard notifyFinish(WUPJobCard existingJobCard, UoW unitOfWork);
    public WUPJobCard notifyFailed(WUPJobCard existingJobCard, UoW unitOfWork);

    public ResilienceParcelJobCard getParcelStatus(FDNToken parcelInstanceID);
    public ResilienceParcel getParcel(FDNToken parcelInstanceID);

    public ResilienceParcel getCurrentParcelForWUP(FDNToken wupInstanceID, FDNToken uowInstanceID);

    public void registerDownstreamWUPInterest( FDNToken parcelInstanceID, FDNToken downstreamWUPInstanceID);

    public void registerDownstreamParcel( FDNToken parcelInstanceID, FDNToken downstreamWUPInstanceID, FDNToken downstreamParcelInstanceID);
}
