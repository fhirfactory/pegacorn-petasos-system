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

package net.fhirfactory.pegacorn.petasos.model.resilience.activitymatrix;

import net.fhirfactory.pegacorn.common.model.FDNToken;
import net.fhirfactory.pegacorn.petasos.model.pathway.ContinuityID;

import java.time.Instant;
import java.util.Date;

public class ParcelStatusElement {

    private ContinuityID statusElementID;
    private ResilienceParcelProcessingStatusEnum parcelStatus;
    private Integer retryCount;
    private Date entryDate;
    private boolean hasClusterFocus;
    private boolean hasSystemWideFocus;
    private boolean requiresRetry;

    public ParcelStatusElement(ContinuityID newID) {
        this.statusElementID = new ContinuityID(newID);
        this.entryDate = Date.from(Instant.now());
        this.hasClusterFocus = false;
        this.hasSystemWideFocus = false;
        this.parcelStatus = null;
        this.requiresRetry = false;
    }

    public FDNToken getParcelInstanceID() {
        return (this.statusElementID.getPresentResilienceParcelInstanceID());
    }

    public FDNToken getWupInstanceID() {
        return (this.statusElementID.getPresentWUPInstanceID());
    }

    public FDNToken getWupTypeID() {
        return (this.statusElementID.getPresentWUPTypeID());
    }

    public ContinuityID getStatusElementID() {
        return statusElementID;
    }

    public void setStatusElementID(ContinuityID statusElementID) {
        this.statusElementID = statusElementID;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public boolean getHasClusterFocus() {
        return(this.hasClusterFocus);
    }

    public void setHasClusterFocus(boolean hasFocus) {
        this.hasClusterFocus = hasFocus;
    }

    public boolean getHasSystemWideFocus() {
        return this.hasSystemWideFocus;
    }

    public void setHasSystemWideFocus(boolean systemWideFocus) {
        this.hasSystemWideFocus = systemWideFocus;
    }

    public ResilienceParcelProcessingStatusEnum getParcelStatus() {
        return parcelStatus;
    }

    public void setParcelStatus(ResilienceParcelProcessingStatusEnum parcelStatus) {
        this.parcelStatus = parcelStatus;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public boolean isRequiresRetry() {
        return requiresRetry;
    }

    public void setRequiresRetry(boolean requiresRetry) {
        this.requiresRetry = requiresRetry;
    }
}
