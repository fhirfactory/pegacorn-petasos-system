/*
 * Copyright (c) 2020 MAHun
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
package net.fhirfactory.pegacorn.petasos.model.parcel;

import net.fhirfactory.pegacorn.common.model.FDN;
import net.fhirfactory.pegacorn.common.model.FDNToken;
import net.fhirfactory.pegacorn.petasos.model.wup.WorkUnitProcessorActivityStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Date;

/**
 * @author ACT Health (Mark A. Hunter)
 */
public class PetasosParcelJobCard {
    private static final Logger LOG = LoggerFactory.getLogger(PetasosParcelJobCard.class);
    private FDNToken parcelID;
    private FDNToken associatedWUPInstanceID;
    private FDNToken activeParcelID;
    private PetasosParcelProcessingStatusEnum parcelStatus;
    private PetasosParcelFinalisationStatusEnum parcelFinalisationStatus;
    private WorkUnitProcessorActivityStatusEnum associatedWUPInstanceStatus;
    private Date statusUpdateInstant;

    private String toStringValue;

    /**
     * @param wupInstanceID
     * @param parcelInstanceID
     * @param wupStatus
     * @param parcelStatus
     * @param finalisationStatus
     * @param statusUpdateInstant
     */
    public PetasosParcelJobCard(FDN wupInstanceID, FDN parcelInstanceID, WorkUnitProcessorActivityStatusEnum wupStatus, PetasosParcelProcessingStatusEnum parcelStatus, PetasosParcelFinalisationStatusEnum finalisationStatus, Date statusUpdateInstant) {
        LOG.debug("FND(): Entry, wupInstanceID --> {}, parcelInstanceID --> {}, wupStatus --> {}, parcelStatus --> {}, finalisationStatus --> {}, statusUpdateInstant --> {}", wupInstanceID, parcelInstanceID, wupStatus, parcelStatus, finalisationStatus, statusUpdateInstant );
        if (parcelInstanceID == null) {
            throw (new IllegalArgumentException("parcelInstanceID (FDN) is null in Constructor"));
        }
        if (wupInstanceID == null) {
            throw (new IllegalArgumentException("wupInstanceID (FDN) is null in Constructor"));
        }
        this.parcelID = parcelInstanceID.getToken();
        this.associatedWUPInstanceID = wupInstanceID.getToken();
        this.parcelStatus = parcelStatus;
        this.statusUpdateInstant = statusUpdateInstant;
        this.associatedWUPInstanceStatus = wupStatus;
        this.parcelFinalisationStatus = finalisationStatus;
        this.activeParcelID = new FDNToken();
        generateToString();
    }

    /**
     * @param wupInstanceID
     * @param parcelInstanceID
     * @param wupStatus
     * @param parcelStatus
     * @param finalisationStatus
     */
    public PetasosParcelJobCard(FDN wupInstanceID, FDN parcelInstanceID, WorkUnitProcessorActivityStatusEnum wupStatus, PetasosParcelProcessingStatusEnum parcelStatus,PetasosParcelFinalisationStatusEnum finalisationStatus) {
        LOG.debug("FND(): Entry, wupInstanceID --> {}, parcelInstanceID --> {}, wupStatus --> {}, parcelStatus --> {}, finalisationStatus --> {}", wupInstanceID, parcelInstanceID, wupStatus, parcelStatus, finalisationStatus );
        if (parcelInstanceID == null) {
            throw (new IllegalArgumentException("parcelInstanceID (FDN) is null in Constructor"));
        }
        if (wupInstanceID == null) {
            throw (new IllegalArgumentException("wupInstanceID (FDN) is null in Constructor"));
        }
        this.parcelID = parcelInstanceID.getToken();
        this.associatedWUPInstanceID = wupInstanceID.getToken();
        this.parcelStatus = parcelStatus;
        this.statusUpdateInstant = Date.from(Instant.now());
        this.parcelFinalisationStatus = finalisationStatus;
        this.associatedWUPInstanceStatus = wupStatus;
        this.activeParcelID = new FDNToken();
        generateToString();
    }

    /**
     *
     * @param originalJobCard
     */
    public PetasosParcelJobCard( PetasosParcelJobCard originalJobCard){
        LOG.debug("FND(): Entry, originalJobCard --> {}", originalJobCard );

        if (originalJobCard == null) {
            throw (new IllegalArgumentException("originalJobCard (PetasosParcelJobCard) is null in Copy Constructor"));
        }
        this.parcelID = new FDNToken(originalJobCard.getParcelID());
        this.associatedWUPInstanceID = new FDNToken(originalJobCard.getAssociatedWUPInstanceID());
        this.associatedWUPInstanceStatus = originalJobCard.getAssociatedWUPInstanceStatus();
        this.parcelFinalisationStatus = originalJobCard.getParcelFinalisationStatus();
        this.parcelStatus = originalJobCard.getParcelStatus();
        this.statusUpdateInstant = originalJobCard.getStatusUpdateInstant();
        this.activeParcelID = new FDNToken(originalJobCard.getActiveParcelID());
        generateToString();
    }

    public PetasosParcelProcessingStatusEnum getParcelStatus() {
        return parcelStatus;
    }

    public void setParcelStatus(PetasosParcelProcessingStatusEnum parcelStatus) {
        this.parcelStatus = parcelStatus;
        generateToString();
    }

    public Date getStatusUpdateInstant() {
        return this.statusUpdateInstant;
    }

    public void setStatusUpdateInstant(Date statusUpdateInstant) {
        this.statusUpdateInstant = statusUpdateInstant;
        generateToString();
    }

    public PetasosParcelFinalisationStatusEnum getParcelFinalisationStatus() {
        return parcelFinalisationStatus;
    }

    public void setParcelFinalisationStatus(PetasosParcelFinalisationStatusEnum parcelFinalisationStatus) {
        this.parcelFinalisationStatus = parcelFinalisationStatus;
        generateToString();
    }

    public WorkUnitProcessorActivityStatusEnum getAssociatedWUPInstanceStatus() {
        return associatedWUPInstanceStatus;
    }

    public void setAssociatedWUPInstanceStatus(WorkUnitProcessorActivityStatusEnum associatedWUPStatus) {
        this.associatedWUPInstanceStatus = associatedWUPStatus;
        generateToString();
    }

    public FDNToken getParcelID() {
        return parcelID;
    }

    public void setParcelID(FDNToken parcelID) {
        this.parcelID = parcelID;
        generateToString();
    }

    public FDNToken getAssociatedWUPInstanceID() {
        return associatedWUPInstanceID;
    }

    public void setAssociatedWUPInstanceID(FDNToken associatedWUPInstanceID) {
        this.associatedWUPInstanceID = associatedWUPInstanceID;
        generateToString();
    }

    @Override
    public String toString(){
        return(this.toStringValue);
    }

    public void generateToString(){
        toStringValue = "PetasosParcelJobCard={"
                + "(parcelID=" + this.parcelID.toString() + "),"
                + "(associatedWUPInstanceID=" + this.associatedWUPInstanceID.toString() + "),"
                + "(parcelStatus=" + this.parcelStatus.toString() + "),"
                + "(parcelFinalisationStatus=" + this.parcelFinalisationStatus.toString() + "),"
                + "(associatedWUPInstanceStatus=" + this.associatedWUPInstanceStatus.toString() + "),"
                + "(statusUpdateInstant=" + this.statusUpdateInstant.toString() + "),"
                + "(activeParcelID=" + this.activeParcelID.toString() + ")}";
    }

    public FDNToken getActiveParcelID() {
        return activeParcelID;
    }

    public void setActiveParcelID(FDNToken activeParcelID) {
        this.activeParcelID = activeParcelID;
    }
}
